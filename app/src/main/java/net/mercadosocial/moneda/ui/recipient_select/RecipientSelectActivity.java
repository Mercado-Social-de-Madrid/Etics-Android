package net.mercadosocial.moneda.ui.recipient_select;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import net.mercadosocial.moneda.R;
import net.mercadosocial.moneda.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class RecipientSelectActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabsRecipients;
    private GridView recyclerRecipients;
    private List<String> names = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private void findViews() {
        tabsRecipients = (TabLayout)findViewById( R.id.tabs_recipients );
        recyclerRecipients = (GridView)findViewById( R.id.recycler_recipients );
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient_select);

        configureSecondLevelActivity();
        findViews();

        tabsRecipients.addOnTabSelectedListener(this);

//        recyclerRecipients.setLayoutManager(new LinearLayoutManager(this));
//        recyclerRecipients.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new ArrayAdapter<>(this, R.layout.grid_item_mock, names);
        recyclerRecipients.setAdapter(adapter);

        recyclerRecipients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.putExtra("select", names.get(position));

                setResult(RESULT_OK, intent);
                finish();
            }
        });

        fillListNames("Entidad ");

    }

    private void fillListNames(String name) {
        names.clear();

        for (int i = 1; i < 31; i++) {
            names.add(name + i);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {

            case 0:
                fillListNames("Entidad ");
                break;


            case 1:
                fillListNames("Persona ");
                break;
        }
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


}
