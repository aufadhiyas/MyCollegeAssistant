package me.citrafa.asistenkuliahku.AdapterRecycleView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import me.citrafa.asistenkuliahku.ModelClass.JadwalKuliahModel;
import me.citrafa.asistenkuliahku.ModelClass.JadwalLainModel;
import me.citrafa.asistenkuliahku.ModelClass.TugasModel;
import me.citrafa.asistenkuliahku.R;

import static java.lang.String.valueOf;

/**
 * Created by SENSODYNE on 27/04/2017.
 */

public class AdapterTugasRV extends RealmRecyclerViewAdapter<TugasModel, AdapterTugasRV.MyViewHolder> {

    private static final String TAG = "AdapterTugasRV";
    OrderedRealmCollection<TugasModel> data;
    RealmResults<TugasModel> tugasModels;
    RealmList<JadwalKuliahModel> jadwalKuliahModels;
    Context mContext;
    private Realm realm;

    public AdapterTugasRV(@Nullable OrderedRealmCollection<TugasModel> data
            , RealmResults<TugasModel> tugasModels) {

        super(data, true);
        this.tugasModels = tugasModels;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rowtugas, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TugasModel tm = tugasModels.get(position);
//        realm = Realm.getDefaultInstance();
//        JadwalKuliahModel jk = realm.where(JadwalKuliahModel.class).equalTo("Tugas.no_t",tm.getNo_t()).findFirst();
//        holder.txt1.setText(jk.getMakul_jk());
        holder.txt2.setText(valueOf(tm.getWaktu_t()));
        holder.txt4.setText(tm.getDeskripsi_t());
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt1,txt2,txt3,txt4;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt1 = (TextView)itemView.findViewById(R.id.rowTugasMakul);
            txt2 = (TextView)itemView.findViewById(R.id.rowTugasWaktu);
            txt4 = (TextView)itemView.findViewById(R.id.rowTugasDeskripsi);
        }
    }
}
