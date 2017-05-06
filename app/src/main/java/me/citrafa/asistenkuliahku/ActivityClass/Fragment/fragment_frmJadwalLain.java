package me.citrafa.asistenkuliahku.ActivityClass.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import me.citrafa.asistenkuliahku.ModelClass.DateStorageModel;
import me.citrafa.asistenkuliahku.ModelClass.JadwalLainModel;
import me.citrafa.asistenkuliahku.OperationRealm.DateStorageOperation;
import me.citrafa.asistenkuliahku.OperationRealm.JadwalLainOperation;
import me.citrafa.asistenkuliahku.R;
import me.citrafa.asistenkuliahku.SessionManager.SessionManager;

import static java.lang.String.valueOf;

public class fragment_frmJadwalLain extends Fragment {
    private EditText txtNama,txtwaktuS,txtwaktuF,txttempat,txtdeskripsi;
    private static String modelName = "JadwalLainModel";
    private Button btnSimpan;
    private int id;
    private DateStorageModel dso;
    private DateStorageOperation DSO;
    private JadwalLainOperation JUO;
    private JadwalLainModel jml;
    int no_jl;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Context mContext;
    Date DateS,DateF;
    SessionManager session;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_frm_jadwal_lain, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        JUO = new JadwalLainOperation();
        DSO = new DateStorageOperation();
        //no_jl=getArguments().getInt("ID");
        initView(view);
        session = new SessionManager(getActivity());

        txtwaktuS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickerS();
            }
        });
        txtwaktuF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickerF();
            }
        });
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpanData();
            }
        });
    }
    private void initView(View view){
        txtNama = (EditText)view.findViewById(R.id.txtNamaJl);
        txtwaktuS = (EditText)view.findViewById(R.id.txtWaktuJlS);
        txtwaktuF = (EditText)view.findViewById(R.id.txtWaktuJlF);
        txttempat = (EditText)view.findViewById(R.id.txtTempatJl);
        txtdeskripsi = (EditText)view.findViewById(R.id.txtDeskripsiJl);
        btnSimpan = (Button)view.findViewById(R.id.btnSimpanJl);
    }


    public void SimpanData() {
        int ids = id(10000);
        final int dateID = DSO.getNextId();
        String nama = txtNama.getText().toString().trim();
        String uid = uuid();
        Date waktuS = DateS;
        Date waktuF = DateF;
        String tempat = txttempat.getText().toString().trim();
        String deskripsi = txtdeskripsi.getText().toString();
        Boolean status = true;
        Date created_at=getCurrentTimeStamp();
        Date updated_at=getCurrentTimeStamp();
        String Author="User";
        String noOnline="test";
        jml = new JadwalLainModel(ids,uid,nama,waktuS,waktuF,tempat,deskripsi,status,Author,created_at,updated_at);
        dso = new DateStorageModel(dateID,ids,modelName,waktuS,waktuF);
        DSO.insertDatetoStorage(dso);
        JUO.tambahJadwalLain(jml);

    }

    //UNTUK MENENTUKAN ID JADWAL LAIN, JIKA MAU TAMBAH GUNAKAN 10000, JIKA MAU UPDATE TINGGAL PAKE ID JADWAL LAIN DARI RECYCLERVIEW
    public int id(int status){
        //10000 untuk Menyimpan Data Di Realm
        //ELSE /Lainnya untuk mengupdate
        if (status==10000){
            id = JUO.getNextId();
        }else{
            id = status;
        }
        return id;
    }
    public static Date getCurrentTimeStamp(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        try {
            return sdfDate.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return now;
    }

    public void DateTimePickerS(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, final int year,
                                          final int monthOfYear, final int dayOfMonth) {

                        //.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        //DateS = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                        final Calendar c = Calendar.getInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm");
                                        String formatedDate = sdf.format(new Date(year,monthOfYear,dayOfMonth,hourOfDay,minute));
                                        try {
                                            Date dateS = sdf.parse(formatedDate);
                                            SimpleDateFormat Dates = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                                            String Jadi = Dates.format(dateS);
                                            DateS = dateS;
                                            txtwaktuS.setText(Jadi);
                                        } catch (ParseException e) {

                                        }
                                    }
                                }, mHour, mMinute, true);
                        timePickerDialog.show();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    public void DateTimePickerF(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, final int year,
                                          final int monthOfYear, final int dayOfMonth) {

                        //.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        //DateS = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                        final Calendar c = Calendar.getInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm");
                                        String formatedDate = sdf.format(new Date(year,monthOfYear,dayOfMonth,hourOfDay,minute));
                                        try {
                                            Date dateF = sdf.parse(formatedDate);
                                            SimpleDateFormat Dates = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                                            String Jadi = Dates.format(dateF);
                                            DateF = dateF;
                                            txtwaktuF.setText(Jadi);
                                        } catch (ParseException e) {

                                        }
                                    }
                                }, mHour, mMinute, true);
                        timePickerDialog.show();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    private void insert(JadwalLainModel  jadwalLainModel){
        JUO.tambahJadwalLain(jadwalLainModel);

    }
    private void update(){

    }
    public String uuid(){
        return UUID.randomUUID().toString().toString();
    }

}
