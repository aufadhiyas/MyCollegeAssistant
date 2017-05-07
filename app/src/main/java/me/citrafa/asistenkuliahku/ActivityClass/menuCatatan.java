package me.citrafa.asistenkuliahku.ActivityClass;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.realm.Realm;
import io.realm.RealmResults;
import me.citrafa.asistenkuliahku.ActivityClass.Fragment.fragment_frm_catatan;
import me.citrafa.asistenkuliahku.AdapterRecycleView.AdapterCatatanRV;
import me.citrafa.asistenkuliahku.ModelClass.CatatanModel;
import me.citrafa.asistenkuliahku.R;

public class menuCatatan extends AppCompatActivity {
    private FloatingActionButton fab;
    private Realm realm;
    private RecyclerView recyclerView;
    RealmResults<CatatanModel> data;
    AdapterCatatanRV adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_catatan);
        realm = Realm.getDefaultInstance();
        data = realm.where(CatatanModel.class).equalTo("status",true).findAll();
        recyclerView = (RecyclerView)findViewById(R.id.recyclerC);
        adapter = new AdapterCatatanRV(menuCatatan.this,realm.where(CatatanModel.class).equalTo("status",true).findAll(),data);
        final LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        fab = (FloatingActionButton)findViewById(R.id.fabAddCatatan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment();
                fab.hide();
            }
        });


    }
    private void changeFragment(){
        Bundle bundle = new Bundle();
        bundle.putInt("noCatatan",10000);
        fragment_frm_catatan frm = new fragment_frm_catatan();
        frm.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_menu_catatan, frm).addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fab.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
