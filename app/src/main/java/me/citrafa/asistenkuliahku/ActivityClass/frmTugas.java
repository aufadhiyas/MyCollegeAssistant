package me.citrafa.asistenkuliahku.ActivityClass;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import me.citrafa.asistenkuliahku.CustomWidget.LibraryDateCustom;
import me.citrafa.asistenkuliahku.ModelClass.DateStorageModel;
import me.citrafa.asistenkuliahku.ModelClass.JadwalKuliahModel;
import me.citrafa.asistenkuliahku.ModelClass.TugasModel;
import me.citrafa.asistenkuliahku.OperationRealm.DateStorageOperation;
import me.citrafa.asistenkuliahku.OperationRealm.JadwalKuliahOperation;
import me.citrafa.asistenkuliahku.OperationRealm.TugasOperation;
import me.citrafa.asistenkuliahku.R;
import me.citrafa.asistenkuliahku.SessionManager.SessionManager;

public class frmTugas extends AppCompatActivity {
    private  static final String TAG = frmDaftar.class.getSimpleName();
    TugasModel tugasModel;
    JadwalKuliahModel jadwalKuliahModel;
    Realm realm;
    int id,switchStatus;
    TugasOperation TO;
    JadwalKuliahOperation JKO;
    DateStorageModel dso;
    DateStorageOperation DSO;
    LibraryDateCustom LDS;
    private EditText txt1,txt2,txt3;
    private TextView lbl1,lbl2, lblSwitch,lbl3;
    private Button btn1,btn2,btn3;
    private Switch switchWaktuKumpulTugas;
    private String switchOn = "Waktu pengumpulan diluar jam kuliah";
    private String switchOff = "Pengumpulan tugas saat jam kuliah";
    Context mContext;
    private String namaMakul, defaultDate;
    private String hari_makul;
    private String jam_makul;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JadwalKuliahOperation JKO = new JadwalKuliahOperation();
        session = new SessionManager(getApplicationContext());
        TO = new TugasOperation();
        LDS = new LibraryDateCustom();
        realm = Realm.getDefaultInstance();
        Intent intent = getIntent();
        final int id = intent.getIntExtra("id", 10000);
        txt1 = (EditText) findViewById(R.id.txtDeskripsiTugas);
        txt2 = (EditText) findViewById(R.id.txtWaktuKumpulTugas);
        lbl1 = (me.citrafa.asistenkuliahku.CustomWidget.TVLatoFontMedium) findViewById(R.id.lblMakulTugas);
        lbl2 = (me.citrafa.asistenkuliahku.CustomWidget.TVLatoFontRegular) findViewById(R.id.lblWaktuMakulTugas);
        lbl3 = (TextView) findViewById(R.id.lblFileTugas);
        lblSwitch = (TextView) findViewById(R.id.lblSwitchTugas);
        switchWaktuKumpulTugas = (Switch)findViewById(R.id.switchWaktuKumpulTugas);
        btn1 = (Button) findViewById(R.id.btnBrowseFileTugas);
        btn2 = (Button) findViewById(R.id.btnSimpanTugas);
        jadwalKuliahModel = realm.where(JadwalKuliahModel.class).equalTo("no_jk", id).findFirst();
        if (id !=10000){
            namaMakul = jadwalKuliahModel.getMakul_jk();
            hari_makul = jadwalKuliahModel.getHari_jk();
            SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
            jam_makul = jam.format(jadwalKuliahModel.getWaktu_jk());
        }

        lbl1.setText("" + namaMakul);
        lbl2.setText(hari_makul+" - " + jam_makul);
        lbl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LDS.DateTimePickerSingle(mContext,txt2);
            }
        });
        defaultDate = jadwalKuliahModel.getHari_jk()+" - "+jam_makul;
        switchTugasAction(switchWaktuKumpulTugas,lblSwitch,txt2,defaultDate);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert(id);
            }
        });
    }



    private void switchTugasAction(Switch s, TextView lbl, final EditText editText, final String defaultDates){
        editText.setText(defaultDates);
        editText.setClickable(false);
        editText.setEnabled(false);
        editText.setFocusable(false);
        lbl.setText(switchOff);

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    lblSwitch.setText(switchOn);
                    editText.setText("");
                    txt2.setClickable(true);
                    txt2.setEnabled(true);
                    txt2.setFocusable(false);
                    switchStatus = 1;

                }else {
                    lblSwitch.setText(switchOff);
                    editText.setText(defaultDates);
                    txt2.setClickable(false);
                    txt2.setEnabled(false);
                    txt2.setFocusable(false);
                    switchStatus=0;
                }
            }
        });
    }
    public void insert(int status){
        int no = id(status);
        String Deskripsi = txt1.getText().toString();
        String attLink = lbl3.getText().toString();
        Date dateTugas= null;
        String uid = uuid();
        Boolean statusT = true;
        Date created_at = getCurrentTimeStamp();
        Date updated_at = getCurrentTimeStamp();
        String author = session.getEmaiUser();
        String noonline="hh";
        tugasModel =new TugasModel(no,uid,Deskripsi,attLink,dateTugas,statusT,author,created_at,updated_at);
        jadwalKuliahModel.Tugas.add(status,tugasModel);
        TO.TambahData(tugasModel);
    }
    public void update(){

    }

    public int id(int status) {
        if (status == 10000) {
            id = TO.getNextId();
        } else {
            id = status;
        }
        return id;
    }
    public static Date getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        try {
            Date noes = sdfDate.parse(strDate);
            return noes;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String uuid(){
        return UUID.randomUUID().toString().toString();
    }


}
