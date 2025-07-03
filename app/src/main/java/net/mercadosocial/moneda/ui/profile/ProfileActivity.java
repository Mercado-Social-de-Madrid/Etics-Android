package net.mercadosocial.moneda.ui.profile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.App;
import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.api.common.ApiClient;
import net.mercadosocial.moneda.api.response.Data;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Node;
import net.mercadosocial.moneda.model.Person;
import net.mercadosocial.moneda.util.DateUtils;
import net.mercadosocial.moneda.util.WebUtils;
import net.mercadosocial.moneda.views.CircleTransform;

import org.jetbrains.annotations.NotNull;

import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;

public class ProfileActivity extends BaseActivity implements View.OnClickListener, ProfileView {

    private AppCompatImageView imgProfile;
    private AppCompatButton btnChangeImage;
    private ProfilePresenter presenter;
    private TextView tvProfileName, tvProfileType, tvProfileMarket;
    private AppCompatButton btnLogout;
    private EasyImage easyImage;
    private View btnDeleteAccount;

    private void findViews() {
        imgProfile = findViewById(R.id.img_profile);
        tvProfileType = findViewById(R.id.tv_profile_type);
        tvProfileMarket = findViewById(R.id.tv_profile_market);
        btnLogout = findViewById(R.id.btn_logout);
        btnDeleteAccount = findViewById(R.id.btn_delete_account);

        tvProfileName = findViewById(R.id.tv_profile_name);
        btnChangeImage = findViewById(R.id.btn_change_image);

        btnLogout.setOnClickListener(this);
        btnChangeImage.setOnClickListener(this);
        btnDeleteAccount.setOnClickListener(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = ProfilePresenter.newInstance(this, this);
        setBasePresenter(presenter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        configureSecondLevelActivity();
        setToolbarTitle(R.string.account);
        findViews();

        easyImage = new EasyImage.Builder(this)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .build();

        presenter.onCreate();

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_profile, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menuItem_logout:
//                setResult(RESULT_OK);
//                finish();
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    ActivityResultLauncher<Intent> launcher =
//            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
//                if (result.getResultCode() == RESULT_OK) {
//                    Uri uri = result.getData().getData();
//                    // Use the uri to load the image
//                } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
//                    // Use ImagePicker.Companion.getError(result.getData()) to show an error
//                }
//            });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_image:

//                showImageChangeDisabledDialog();
                checkPermissionAndOpenChooser();
                break;

            case R.id.btn_logout:
                setResult(RESULT_OK);
                finish();
                break;

            case R.id.btn_delete_account:
                showDeleteAccountDialog();
                break;

        }
    }

    private void showImageChangeDisabledDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.change_image_temporarily_disabled)
                .setPositiveButton(R.string.go_to_web, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WebUtils.openLink(ProfileActivity.this, ApiClient.BASE_URL);
                    }
                })
                .setNegativeButton(R.string.back, null)
                .show();
    }

    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.delete_account_info)
                .setPositiveButton(R.string.open_email, (dialog, which) -> openDeleteAccountEmail())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void openDeleteAccountEmail() {
        Node node = getApp().getCurrentNode();

        String text = "";
        Data userData = App.getUserData(this);
        if (userData != null) {
            text = getString(R.string.member_id_x, userData.getAccount().getMemberId());
        }

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{node.getContactEmail()});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.leave_request));
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_leave_request)));

    }

    private void checkPermissionAndOpenChooser() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        easyImage.openChooser(ProfileActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        easyImage.openGallery(ProfileActivity.this);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        new AlertDialog.Builder(ProfileActivity.this)
                                .setMessage(R.string.need_access_camera_profile_photo)
                                .setPositiveButton(R.string.go_ahead, (dialog, which) -> token.continuePermissionRequest())
                                .setNegativeButton(R.string.cancel, (dialog, which) -> token.cancelPermissionRequest())
                                .show();
                    }
                })
                .check();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {

            @Override
            public void onImagePickerError(@NotNull Throwable throwable, @NotNull MediaSource mediaSource) {
                toast(throwable.getMessage());
            }

            @Override
            public void onMediaFilesPicked(@NotNull MediaFile[] mediaFiles, @NotNull MediaSource mediaSource) {
                presenter.onImagePicked(mediaFiles[0].getFile());
            }

            @Override
            public void onCanceled(@NotNull MediaSource mediaSource) {
            }
        });
    }


    @Override
    public void showPersonProfile(Person person) {

        tvProfileName.setText(String.format("%s %s", person.getName(), person.getSurname()));
        tvProfileMarket.setText(getApp().getCurrentNode().getName());

        if (person.is_guest_account()) {
            String dateFormatted = DateUtils.convertDateApiToUserFormat(person.getExpiration_date());
            tvProfileType.setText(String.format(getString(R.string.guest_account_info_format), dateFormatted));
        } else {
            tvProfileType.setText(R.string.consumer);
        }

        Picasso.get()
                .load(person.getProfileImage())
                .placeholder(R.mipmap.ic_avatar_2)
                .error(R.mipmap.ic_avatar_2)
                .transform(new CircleTransform())
                .into(imgProfile);

    }

    @Override
    public void showEntityProfile(Entity entity) {


        tvProfileName.setText(entity.getName());
        tvProfileMarket.setText(getApp().getCurrentNode().getName());
        tvProfileType.setText(R.string.entity);

        Picasso.get()
                .load(entity.getProfileImage())
                .placeholder(R.mipmap.ic_avatar_2)
                .error(R.mipmap.ic_avatar_2)
                .transform(new CircleTransform())
                .into(imgProfile);
    }
}
