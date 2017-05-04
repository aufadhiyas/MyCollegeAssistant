package me.citrafa.asistenkuliahku.AdapterRecycleView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.RealmViewHolder;
import me.citrafa.asistenkuliahku.AdapterList.CustomListAdapter;
import me.citrafa.asistenkuliahku.Jadwal;
import me.citrafa.asistenkuliahku.ModelClass.JadwalKuliahModel;
import me.citrafa.asistenkuliahku.R;

/**
 * Created by SENSODYNE on 12/04/2017.
 */

public class AdapterJadwalKuliahRV extends RecyclerView.Adapter<AdapterJadwalKuliahRV.MyViewHolder> {

    String TAG = "";

    private ArrayList<Jadwal> data;
    int index;
    private Context context;

    public AdapterJadwalKuliahRV(ArrayList<Jadwal> data, Context context){
        setHasStableIds(true);
        index = data.size();
        this.context = context;
        this.data = data;
        Log.d(TAG, " TAG : ADAPTER JADWAL KULIAH RV");

    }
    private void inputData(JadwalKuliahModel jadwalKuliahModel){
        for (int i = 0; i<this.data.size(); i++){
            if(this.data.get(i).getTittle().equals(jadwalKuliahModel.getHari_jk())){
                Jadwal jd = data.get(i);
                jd.addData(jadwalKuliahModel);
                jd.setNoJK(jadwalKuliahModel.getNo_jk());
                removeData(jadwalKuliahModel.getHari_jk());
                this.data.add(jd);
                Log.d(TAG,"Input Data");
                break;
            }
        }
        Jadwal jk = new Jadwal();
        jk.setTittle(jadwalKuliahModel.getHari_jk());
        jk.setNoJK(jadwalKuliahModel.getNo_jk());
        this.data.add(jk);
        Log.d(TAG, "TAG : Input Data");
    }

    private void removeData(String hari){
        for(int i = 0;i<this.data.size();i++){
            if(this.data.get(i).getTittle().equals(hari)){
                this.data.remove(i);
            }
        }
    }

    @Nullable
    public Jadwal getItem(int index) {
        return this.data.get(index);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemjks, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Jadwal jk = getItem(position);
        holder.txtHari.setText(jk.getTittle());
        CustomListAdapter customListAdapter = new CustomListAdapter(jk.getJk(),context);
        ViewGroup.LayoutParams params = holder.listView.getLayoutParams();
        final float scale = context.getResources().getDisplayMetrics().density;
        int z = (80*(jk.getJk().size()));
        int pixels = (int) (z * scale + 0.5f);
        params.height = pixels;
        holder.listView.setLayoutParams(params);
        holder.listView.requestLayout();
        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int positions, long arg3)
            {
                positions = jk.getNoJK();
                Toast.makeText(context, "position :" + positions, Toast.LENGTH_SHORT).show();
            }
        });
        holder.listView.setAdapter(customListAdapter);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    //@Override
   // public int getItemCount(){ return data.size(); }


    class MyViewHolder extends RealmViewHolder {

        private TextView txtHari;
        private ListView listView;

        private MyViewHolder(View container){
            super(container);
            txtHari = (TextView) container.findViewById(R.id.harijklist);
            listView = (ListView) container.findViewById(R.id.listJk);


        }

    }
}