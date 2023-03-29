package net.mercadosocial.moneda.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.squareup.picasso.Picasso;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;
import net.mercadosocial.moneda.model.Entity;
import net.mercadosocial.moneda.model.Person;
import net.mercadosocial.moneda.ui.invitations.InvitationsPresenter;
import net.mercadosocial.moneda.ui.profile.pincode_change.PincodeChangePresenter;
import net.mercadosocial.moneda.util.DateUtils;
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
    private LinearLayout btnChangePincode;
    private LinearLayout btnInvitations;
    private AppCompatButton btnLogout;
    private EasyImage easyImage;

    private void findViews() {
        imgProfile = findViewById(R.id.img_profile);
        tvProfileType = findViewById(R.id.tv_profile_type);
        tvProfileMarket = findViewById(R.id.tv_profile_market);
        btnChangePincode = findViewById(R.id.btn_change_pincode);
        btnInvitations = findViewById(R.id.btn_invitations);
        btnLogout = findViewById(R.id.btn_logout);

        tvProfileName = findViewById(R.id.tv_profile_name);
        btnChangeImage = findViewById(R.id.btn_change_image);

        btnLogout.setOnClickListener(this);
        btnChangeImage.setOnClickListener(this);
        btnChangePincode.setOnClickListener(this);
        btnInvitations.setOnClickListener(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = ProfilePresenter.newInstance(this, this);
        setBasePresenter(presenter);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        configureSecondLevelActivity();
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

                easyImage.openChooser(this);

                break;

            case R.id.btn_logout:
                setResult(RESULT_OK);
                finish();
                break;

            case R.id.btn_invitations:
                InvitationsPresenter.launchInvitationsActivity(this);
                break;

            case R.id.btn_change_pincode:
                PincodeChangePresenter.launchPincodeChangeActivity(this);
                break;


        }
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
        tvProfileMarket.setText(person.getCityName());

        if (person.is_guest_account()) {
            String dateFormatted = DateUtils.convertDateApiToUserFormat(person.getExpiration_date());
            tvProfileType.setText(String.format(getString(R.string.guest_account_info_format), dateFormatted));
        } else {
            tvProfileType.setText(R.string.consumer);
        }

        Picasso.get()
                .load(person.getProfile_image())
                .placeholder(R.mipmap.ic_avatar_2)
                .error(R.mipmap.ic_avatar_2)
                .transform(new CircleTransform())
                .into(imgProfile);

    }

    @Override
    public void showEntityProfile(Entity entity) {

        tvProfileName.setText(entity.getName());
        tvProfileMarket.setText(entity.getCityName());
        tvProfileType.setText(R.string.entity);

        Picasso.get()
                .load(entity.getLogoThumbnailOrCover())
                .placeholder(R.mipmap.ic_avatar_2)
                .error(R.mipmap.ic_avatar_2)
                .transform(new CircleTransform())
                .into(imgProfile);
    }
}
