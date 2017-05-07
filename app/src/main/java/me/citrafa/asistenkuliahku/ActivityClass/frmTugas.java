package me.citrafa.asistenkuliahku.ActivityClass;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    LibraryDateCustom LDS;
    private EditText txt1,txt2,txt3;
    private TextView lbl1,lbl2, lblSwitch,lbl3;
    private Button btn1,btn2,btn3;
    private Switch switchWaktuKumpulTugas;
    private static final String switchOff = "Kumpul tugas saat jam kuliah";
    private static final String switchOn = "Kumpul tugas diluar jam kuliah";
    Date dateTugas = null;
    Context mContext;
    private String namaMakul, defaultDate;
    private String hari_makul;
    private String jam_makul;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_tugas_auto);

        JadwalKuliahOperation JKO = new JadwalKuliahOperation();
        session = new SessionManager(getApplicationContext());
        TO = new TugasOperation();
        LDS = new LibraryDateCustom();
        realm = Realm.getDefaultInstance();
        Intent intent = getIntent();
        final int idjk = intent.getIntExtra("idJK", 10000);// ID JADWAL KULIAH
        final int idTugas = intent.getIntExtra("idTugas",10000);//ID TUGAS
        final int statusForm = intent.getIntExtra("statusFormTugas",0);//STATUS TAMBAH / EDIT
        txt1 = (EditText) findViewById(R.id.txtDeskripsiTugas);
        txt2 = (EditText) findViewById(R.id.txtWaktuKumpulTugas);
        lbl1 = (me.citrafa.asistenkuliahku.CustomWidget.TVLatoFontMedium) findViewById(R.id.lblMakulTugas);
        lbl2 = (me.citrafa.asistenkuliahku.CustomWidget.TVLatoFontRegular) findViewById(R.id.lblWaktuMakulTugas);
        lbl3 = (TextView) findViewById(R.id.lblFileTugas);
        lblSwitch = (TextView) findViewById(R.id.lblSwitchTugas);
        switchWaktuKumpulTugas = (Switch)findViewById(R.id.switchWaktuKumpulTugas);
        btn1 = (Button) findViewById(R.id.btnBrowseFileTugas);
        btn2 = (Button) findViewById(R.id.btnSimpanTugas);

        jadwalKuliahModel = realm.where(JadwalKuliahModel.class).equalTo("no_jk", idjk).findFirst();


        if (id !=10000 && jadwalKuliahModel!=null){
            namaMakul = jadwalKuliahModel.getMakul_jk();
            hari_makul = jadwalKuliahModel.getHari_jk();
            SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
            jam_makul = jam.format(jadwalKuliahModel.getWaktu_jk());
            defaultDate = jadwalKuliahModel.getHari_jk()+" - "+jam_makul;
            lbl1.setText("" + namaMakul);
            lbl2.setText(hari_makul+" - " + jam_makul);
        }
        if (statusForm == 1) {//EDIT TUGAS
            final TugasModel to = realm.where(TugasModel.class).equalTo("no_t",idTugas).findFirst();
            txt1.setText(to.getDeskripsi_t());
            if (to.getWaktu_t() != null) {
                switchWaktuKumpulTugas.setChecked(true);
                txt2.setText(LDS.getHariDariWaktu(to.getWaktu_t()) + ", " + LDS.getWaktuTanggalBiasa(to.getWaktu_t()));
                switchWaktuKumpulTugas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            txt2.setClickable(true);
                            txt2.setEnabled(true);
                            txt2.setFocusable(true);
                            dateTugas = to.getWaktu_t();
                            txt2.setText(LDS.getHariDariWaktu(to.getWaktu_t()) + ", " + LDS.getWaktuTanggalBiasa(to.getWaktu_t()));
                        }else{
                            lblSwitch.setText(switchOff);
                            txt2.setText(defaultDate);
                            txt2.setClickable(false);
                            txt2.setEnabled(false);
                            txt2.setFocusable(false);
                        }
                    }
                });
            } else {
                switchTugasAction(switchWaktuKumpulTugas,lblSwitch,txt2,defaultDate);
            }
            if (to.getAttlink_t()!=null){
                lbl3.setText(to.getAttlink_t());
            }
        }else{
            switchTugasAction(switchWaktuKumpulTugas,lblSwitch,txt2,defaultDate);
        }
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePicker();
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchWaktuKumpulTugas.isChecked()&&txt2.getText()!=null) {
                    Date Tugas = dateTugas;
                    if (idTugas == 10000) {
                        insert(idjk,Tugas);
                    } else if (idTugas != 10000) {
                        update(idTugas,Tugas);
                    } else {
                        Toast.makeText(frmTugas.this, "Pastikan Data Anda Benar!!", Toast.LENGTH_SHORT).show();
                    }
                }else if (switchStatus==1&&txt2.getText()==null){
                    Toast.makeText(frmTugas.this, "Atur waktu pengumpulan terlebih dahulu !", Toast.LENGTH_SHORT).show();
                }else{
                    Date Tugas = null;
                    if (idTugas == 10000) {
                        insert(idjk,Tugas);
                    } else if (idTugas != 10000) {
                        update(idTugas,Tugas);
                    } else {
                        Toast.makeText(frmTugas.this, "Pastikan Data Anda Benar!!", Toast.LENGTH_SHORT).show();
                    }
                }

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
                    txt2.setFocusable(true);

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
    public void insert(int status,Date Tugas){
        int no = id(10000);
        String Deskripsi = txt1.getText().toString();
        String attLink = lbl3.getText().toString();
        Date dateTugas= Tugas;
        String uid = uuid();
        Boolean statusT = true;
        Date created_at = getCurrentTimeStamp();
        Date updated_at = getCurrentTimeStamp();
        String author = session.getEmaiUser();
        tugasModel =new TugasModel(no,uid,Deskripsi,attLink,dateTugas,statusT,author,created_at,updated_at);
        realm.beginTransaction();
        JadwalKuliahModel jk = realm.where(JadwalKuliahModel.class).equalTo("no_jk",status).findFirst();
        jk.Tugas.add(tugasModel);
        Toast.makeText(this, ""+dateTugas, Toast.LENGTH_SHORT).show();
        realm.commitTransaction();
        TO.TambahData(tugasModel);
    }
    public void update(final int idTugas,Date Tugas){
        int no = idTugas;
        String Deskripsi = txt1.getText().toString();
        String attLink = lbl3.getText().toString();
        Date dateTugas= Tugas;
        Boolean statusT = true;
        Date updated_at = getCurrentTimeStamp();
        String author = session.getEmaiUser();
        tugasModel =new TugasModel(no,Deskripsi,attLink,dateTugas,statusT,author,updated_at);
        TO.updatedata(tugasModel);
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
    private void DateTimePicker(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);



        DatePickerDialog datePickerDialog = new DatePickerDialog(frmTugas.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, final int year,
                                          final int monthOfYear, final int dayOfMonth) {

                        //.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        //DateS = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;


                        final Calendar c = Calendar.getInstance();
                        int mHour = c.get(Calendar.HOUR_OF_DAY);
                        int mMinute = c.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(frmTugas.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        String formatedDate = sdf.format(new Date(year,monthOfYear,dayOfMonth,hourOfDay,minute));
                                        try {
                                            Date date = sdf.parse(formatedDate);
                                            dateTugas = date;
                                            txt2.setText(LDS.getHariDariWaktu(date)+", "+LDS.getWaktuTanggalBiasa(date));
                                        } catch (ParseException e) {

                                        }

                                    }
                                }, mHour, mMinute, true);
                        timePickerDialog.show();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


}
