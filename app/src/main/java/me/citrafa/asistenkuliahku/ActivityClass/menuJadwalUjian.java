package me.citrafa.asistenkuliahku.ActivityClass;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import me.citrafa.asistenkuliahku.AdapterRecycleView.AdapterJadwalUjianRV;
import me.citrafa.asistenkuliahku.ModelClass.JadwalKuliahModel;
import me.citrafa.asistenkuliahku.ModelClass.JadwalUjianModel;
import me.citrafa.asistenkuliahku.R;
import me.citrafa.asistenkuliahku.ActivityClass.Fragment.fragment_form_ujian;

public class menuJadwalUjian extends AppCompatActivity{
    FloatingActionButton fab;
    Realm realm;
    RecyclerView recyclerView;
    RealmResults<JadwalUjianModel> jadwalUjianModels;
    AdapterJadwalUjianRV adapter;
    private Context mContex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_jadwal_ujian);
        realm = Realm.getDefaultInstance();
        RealmResults<JadwalUjianModel> jau = realm.where(JadwalUjianModel.class).findAllSorted("waktu", Sort.ASCENDING);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerJL);
        adapter = new AdapterJadwalUjianRV(realm.where(JadwalUjianModel.class).findAll(),jau);
        final LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        fab = (FloatingActionButton) findViewById(R.id.fabAddJadwalUjian);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment();
                fab.hide();
            }
        });
    }

    private void changeFragment(){
        getFragmentManager().beginTransaction().replace(R.id.activity_menu_ujian, new fragment_form_ujian()).addToBackStack(null).commit();
    }
    public void onStart(){
        super.onStart();

    }
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fab.show();
        adapter.notifyDataSetChanged();
    }

    public void onClick(View v){

    }
}
