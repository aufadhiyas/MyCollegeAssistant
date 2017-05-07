package me.citrafa.asistenkuliahku.AdapterRecycleView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import me.citrafa.asistenkuliahku.ActivityClass.frmTugas;
import me.citrafa.asistenkuliahku.CustomWidget.LibraryDateCustom;
import me.citrafa.asistenkuliahku.ModelClass.JadwalKuliahModel;
import me.citrafa.asistenkuliahku.ModelClass.JadwalLainModel;
import me.citrafa.asistenkuliahku.ModelClass.TugasModel;
import me.citrafa.asistenkuliahku.OperationRealm.TugasOperation;
import me.citrafa.asistenkuliahku.R;

import static java.lang.String.valueOf;

/**
 * Created by SENSODYNE on 27/04/2017.
 */

public class AdapterTugasRV extends RealmRecyclerViewAdapter<TugasModel, AdapterTugasRV.MyViewHolder> {

    private static final String TAG = "AdapterTugasRV";
    OrderedRealmCollection<TugasModel> data;
    RealmResults<TugasModel> tugasModels;
    Realm realm;
    RealmList<JadwalKuliahModel> jadwalKuliahModels;
    Context mContext;
    TugasOperation yo;

    LibraryDateCustom ldc;

    public AdapterTugasRV(@Nullable OrderedRealmCollection<TugasModel> data
            , RealmResults<TugasModel> tugasModels) {

        super(data, true);
        setHasStableIds(true);
        realm = Realm.getDefaultInstance();
        ldc = new LibraryDateCustom();
        this.tugasModels = tugasModels;
        yo = new TugasOperation();

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
    public int getItemCount() {
        return tugasModels.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final TugasModel tm = tugasModels.get(position);
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        realm = Realm.getDefaultInstance();
        final JadwalKuliahModel jk = realm.where(JadwalKuliahModel.class).equalTo("Tugas.no_t",tm.getNo_t()).findFirst();
        holder.txt1.setText(jk.getMakul_jk());
        SimpleDateFormat dateTime = new SimpleDateFormat(" dd/MM - HH:mm");

        if (tm.getWaktu_t() !=null){

            holder.txt2.setText(ldc.getHaridanTanggalUntukListSingle(tm.getWaktu_t())+""+dateTime.format(tm.getWaktu_t()));
        }else{
            holder.txt2.setText(jk.getHari_jk()+" - "+timeFormatter.format(jk.getWaktu_jk()));
        }

        if (tm.getAttlink_t()!=null){
            holder.imgFile.setVisibility(View.VISIBLE);
        }
        holder.txt4.setText(tm.getDeskripsi_t());
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v,position,tm.getNo_t(),jk.getMakul_jk(),jk.getNo_jk());
            }
        });
        holder.imgFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt1,txt2,txt4;
        ImageButton imgFile,imgMore;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt1 = (TextView)itemView.findViewById(R.id.rowTugasMakul);
            txt2 = (TextView)itemView.findViewById(R.id.rowTugasWaktu);
            txt4 = (TextView)itemView.findViewById(R.id.rowTugasDeskripsi);
            imgFile = (ImageButton)itemView.findViewById(R.id.rowTugasFilebtn);
            imgMore = (ImageButton)itemView.findViewById(R.id.rowTugasMoreBtn);
        }
    }
    private void showPopupMenu(View view,int position,int no,String Nama,int noJK){

        PopupMenu popup = new PopupMenu(view.getContext(),view);
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.popupmenutugas, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position,no,Nama,noJK));
        popup.show();
    }
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener{

        private int position;
        private int no,noJK;
        private String nama;
        public MyMenuItemClickListener(int position,int no,String nama, int noJK){
            this.position = position;
            this.no = no;
            this.nama = nama;
            this.noJK = noJK;
        }
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.menuTugasUbah:
                    Intent intent = new Intent(mContext,frmTugas.class);
                    intent.putExtra("idTugas",no);
                    intent.putExtra("idJK",noJK);
                    intent.putExtra("statusFormTugas",1);
                    mContext.startActivity(intent);
                    return true;
                case R.id.menuTugasHapus:
                    new AlertDialog.Builder(mContext)
                            .setTitle("Hapus Tugas ?")
                            .setMessage("Yakin Ingin Hapus Tugas :"+nama)
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    yo.hapusData(no);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Batal",null)
                            .show();
                    return true;
            }
            return false;
        }
    }
}
