package me.citrafa.asistenkuliahku.ActivityClass;

import android.app.FragmentTransaction;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.View;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import me.citrafa.asistenkuliahku.ActivityClass.Fragment.fragment_frmJadwalLain;
import me.citrafa.asistenkuliahku.ActivityClass.InterfaceFragment.FragmentCommunication;
import me.citrafa.asistenkuliahku.AdapterRecycleView.AdapterJadwalLainRV;
import me.citrafa.asistenkuliahku.ModelClass.JadwalLainModel;
import me.citrafa.asistenkuliahku.R;

public class menuJadwalLain extends AppCompatActivity {
    private FloatingActionButton fab;
    private Realm realm;
    private RecyclerView recyclerView;
    private Menu menu;
    RealmResults<JadwalLainModel> data;
    AdapterJadwalLainRV adapter;
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_jadwallain);


        realm = Realm.getDefaultInstance();
        RealmResults<JadwalLainModel> jlm = realm.where(JadwalLainModel.class).equalTo("status_jl",true).findAllSorted("waktus_jl", Sort.ASCENDING);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerJL);
        adapter = new AdapterJadwalLainRV(menuJadwalLain.this,realm.where(JadwalLainModel.class).equalTo("status_jl",true).findAll(),jlm);
        final LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);


        fab = (FloatingActionButton)findViewById(R.id.fabAddJadwalLain);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("noJL",0);
                fragment_frmJadwalLain frg = new fragment_frmJadwalLain();
                frg.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.activity_menu_jadwal_lain, frg).addToBackStack(null).commit();

                fab.hide();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fab.show();
        adapter.notifyDataSetChanged();
    }
    public void showFloatingActionButton() {
        fab.show();
    }

    public void hideFloatingActionButton() {
        fab.hide();
    }
}
