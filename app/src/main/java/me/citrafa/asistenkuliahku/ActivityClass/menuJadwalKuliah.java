package me.citrafa.asistenkuliahku.ActivityClass;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import io.realm.Realm;
import me.citrafa.asistenkuliahku.OperationRealm.RealmController;
import me.citrafa.asistenkuliahku.R;
import me.citrafa.asistenkuliahku.AdapterRecycleView.AdapterJadwalKuliahRV;
import me.citrafa.asistenkuliahku.OperationRealm.RealmBaseActivity;
import me.citrafa.asistenkuliahku.OperationRealm.JadwalKuliahOperation;

public class menuJadwalKuliah extends AppCompatActivity {

    private FloatingActionButton Tambah;
    private Realm realm;
    private Button Edit;
    private AlertDialog alertDialog;
    private ListView listJK;
    private String EmailView;
    private RecyclerView recyclerView;
    private AdapterJadwalKuliahRV adapter;
    private RealmBaseActivity rba;
    private JadwalKuliahOperation jkk;
    private Context context;
    //private List<JadwalKuliahModel> jadwalKuliah = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_jadwalkuliah_yang_lama);
        Tambah = (FloatingActionButton) findViewById(R.id.addjk);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerjk);
        this.realm = RealmController.with(this).getRealm();

        realm.getDefaultInstance();
        setupRecycler();

        Tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(menuJadwalKuliah.this, frmJadwalKuliah.class));
            }
        });

        /*Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });*/

    }


    public void setupRecycler() {
        //adapter = new AdapterJadwalKuliahRV(realm.where(JadwalKuliahModel.class).findAll());
        final LinearLayoutManager layout = new LinearLayoutManager(context);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

    }

    protected void onDestroy() {
        super.onDestroy();
        recyclerView.setAdapter(null);
        realm.close();
    }

}
