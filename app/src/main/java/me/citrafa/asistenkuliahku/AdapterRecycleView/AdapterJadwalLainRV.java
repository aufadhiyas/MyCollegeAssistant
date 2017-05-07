package me.citrafa.asistenkuliahku.AdapterRecycleView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import me.citrafa.asistenkuliahku.ActivityClass.Fragment.fragment_frmJadwalLain;
import me.citrafa.asistenkuliahku.ActivityClass.Fragment.fragment_frm_catatan;
import me.citrafa.asistenkuliahku.ActivityClass.InterfaceFragment.FragmentCommunication;
import me.citrafa.asistenkuliahku.ActivityClass.menuJadwalLain;
import me.citrafa.asistenkuliahku.ModelClass.JadwalLainModel;
import me.citrafa.asistenkuliahku.OperationRealm.JadwalLainOperation;
import me.citrafa.asistenkuliahku.R;

/**
 * Created by SENSODYNE on 19/04/2017.
 */

public class AdapterJadwalLainRV extends RealmRecyclerViewAdapter<JadwalLainModel, AdapterJadwalLainRV.MyViewHolder> {

    private Context mContext;
    Realm realm;
    private RealmResults<JadwalLainModel> jlm;
    menuJadwalLain mjl;
    FragmentManager manager;
    JadwalLainOperation jlo;


    public AdapterJadwalLainRV(Context context,OrderedRealmCollection<JadwalLainModel> data, RealmResults<JadwalLainModel> jlm) {
        super(data, true);
        this.jlm = jlm;
        mjl = (menuJadwalLain)context;
        setHasStableIds(true);
        realm = Realm.getDefaultInstance();
        jlo=new JadwalLainOperation();

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_jl, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final JadwalLainModel jl = jlm.get(position);

        holder.data = jl;

        Date dateS = jl.getWaktus_jl();
        SimpleDateFormat Dates = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        String JadiS = Dates.format(dateS);
        Date dateF = jl.getWaktuf_jl();
        SimpleDateFormat DateF = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        String JadiF = DateF.format(dateF);

        holder.txt1.setText(JadiS+" - "+JadiF);

        holder.txt2.setText(jl.getNama_jl());
        holder.txt3.setText(jl.getTempat_jl());
        holder.txt4.setText(jl.getDeskripsi_jl());
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                RealmResults<JadwalLainModel> results = realm.where(JadwalLainModel.class).findAll();
//                JadwalLainModel jl = results.get(position);
                int No = jl.getNo_jl();
                Toast.makeText(mContext, ""+No, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        holder.ibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int No = jl.getNo_jl();
                showPopupMenu(holder.ibutton,position,No,jl.getNama_jl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public JadwalLainModel data;
        CardView cardView;
        TextView txt1,txt2,txt3,txt4;
        ImageButton ibutton;

        public MyViewHolder(View view){
            super(view);
            ibutton = (ImageButton)view.findViewById(R.id.btnOptionJL);
            cardView = (CardView)view.findViewById(R.id.cardviewJL);
            txt1 = (TextView)view.findViewById(R.id.lbl_jl_tanggal);
            txt2 = (TextView)view.findViewById(R.id.lbl_jl_nama);
            txt3 = (TextView)view.findViewById(R.id.lbl_jl_lokasi);
            txt4 = (TextView)view.findViewById(R.id.lbl_jl_deskripsi);
        }
    }
    private void showPopupMenu(View view,int position,int no,String Nama){

        PopupMenu popup = new PopupMenu(view.getContext(),view);
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.popupmenu_jl, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position,no,Nama));
        popup.show();
    }
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener{

        private int position;
        private int no;
        private String nama;
        public MyMenuItemClickListener(int position,int no,String Nama){
            this.position = position;
            this.no = no;
            this.nama=Nama;
        }
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.MenuJLUbah:
                    Bundle bundle = new Bundle();
                    bundle.putInt("noJL",no);
                    fragment_frmJadwalLain fragment_frmJadwalLain = new fragment_frmJadwalLain();
                    fragment_frmJadwalLain.setArguments(bundle);
                    mjl.getFragmentManager().beginTransaction().replace(R.id.activity_menu_jadwal_lain, fragment_frmJadwalLain).addToBackStack(null).commit();
                    break;
                case R.id.MenuJLHapus:
                    new AlertDialog.Builder(mContext)
                            .setTitle("Hapus jadwal lain?")
                            .setMessage("Apa kamu yakin ingin menghapus jadwal : "+nama)
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    JadwalLainModel jl = realm.where(JadwalLainModel.class).equalTo("no_jl",no).findFirst();
                                    jlo.delete(jl);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Batal",null)
                            .show();
                    break;
            }
            return false;
        }
    }
}
