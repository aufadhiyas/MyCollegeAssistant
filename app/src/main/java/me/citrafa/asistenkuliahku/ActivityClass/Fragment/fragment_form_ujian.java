package me.citrafa.asistenkuliahku.ActivityClass.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import me.citrafa.asistenkuliahku.ActivityClass.menuJadwalUjian;
import me.citrafa.asistenkuliahku.CustomWidget.LibraryDateCustom;
import me.citrafa.asistenkuliahku.ModelClass.DateStorageModel;
import me.citrafa.asistenkuliahku.ModelClass.JadwalKuliahModel;
import me.citrafa.asistenkuliahku.ModelClass.JadwalUjianModel;
import me.citrafa.asistenkuliahku.OperationRealm.DateStorageOperation;
import me.citrafa.asistenkuliahku.OperationRealm.JadwalUjianOperation;
import me.citrafa.asistenkuliahku.R;

import static java.lang.String.valueOf;


public class fragment_form_ujian extends Fragment {
    menuJadwalUjian mju;
    private Spinner spJenis;Realm realm;
    private EditText txtMakul, txtWaktu, txtDeskripsi, txtRuangan;
    private Button simpan,selesai;
    private JadwalUjianModel jum;
    private DateStorageModel datesSave;
    JadwalUjianOperation JUO;
    DateStorageModel dso;
    DateStorageOperation DSO;
    private static String modelName = "JadwalUjianModel";
    int id;
    Date dates,Waktu;
    int extra;
    LibraryDateCustom CW;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        extra = getArguments().getInt("noJU",10000);
        return inflater.inflate(R.layout.fragment_jadwal_ujian_form, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        realm = Realm.getDefaultInstance();
        DSO = new DateStorageOperation();
        JUO = new JadwalUjianOperation();
        CW = new LibraryDateCustom();
        initView(view);
        if (extra !=10000){
           JadwalUjianModel ju = realm.where(JadwalUjianModel.class).equalTo("no_ju",extra).findFirst();
            if (ju !=null){
                spJenis.setSelection(positionSpinner(ju.getJenis()));
                txtMakul.setText(ju.getNama_makul());
                txtDeskripsi.setText(ju.getDeskripsi());
                txtRuangan.setText(ju.getRuangan());
                Waktu = ju.getWaktu();
                txtWaktu.setText(CW.getHariDariWaktu(ju.getWaktu())+""+CW.getWaktuTanggalBiasa(ju.getWaktu()));
            }
        }
        txtWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickerF();
            }
        });
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpanData();
                buttonMagic();
            }
        });

    }


    private void initView(View view) {
        spJenis = (Spinner) view.findViewById(R.id.spJenisUjian);
        txtMakul = (EditText) view.findViewById(R.id.txtNamaJU);
        txtWaktu = (EditText) view.findViewById(R.id.txtWaktuJU);
        txtDeskripsi = (EditText) view.findViewById(R.id.txtDeskripsiJU);
        txtRuangan = (EditText) view.findViewById(R.id.txtRuanganJU);
        simpan = (Button) view.findViewById(R.id.btnSimpanJu);
        selesai = (Button)view.findViewById(R.id.btnSelesaiJu);

    }

    public void SimpanData() {
        int ids = id(extra);
        int dateID= DSO.getNextId();
        String Jenis = spJenis.getSelectedItem().toString();
        String uid = uuid();
        String Makul = txtMakul.getText().toString().trim();
        Date Waktuf= Waktu;
        String Deskripsi = txtDeskripsi.getText().toString();
        String Ruangan = txtRuangan.getText().toString().trim();
        String Author = "User";
        Date created_at = CW.getCurrentTimeStamp();
        Date updated_at = CW.getCurrentTimeStamp();
        if (extra==10000){
            jum = new JadwalUjianModel(ids,uid,Makul,Waktuf,Jenis,Deskripsi,Ruangan,true,Author,created_at,updated_at);
            JUO.tambahJadwalUjian(jum);
        }else {
            jum = new JadwalUjianModel(ids,Makul,Waktuf,Jenis,Deskripsi,Ruangan,true,Author,updated_at);
            JUO.updateJadwalUjian(jum);
        }
    }

    public int id(int status) {
        if (status == 10000) {
            id = JUO.getNextId();
        } else {
            id = status;
        }
        return id;
    }

    private void buttonMagic(){
        selesai.setText("Selesai");
    }

    private int positionSpinner(String nama){
        if (nama.equals("PRETEST")){
            return 0;
        }else if (nama.equals("PASTEST")){
            return 1;
        }else if (nama.equals("UTS")){
            return 2;
        }else if (nama.equals("UAS")){
            return 3;
        }
        return 0;
    }
    public String uuid(){
        return UUID.randomUUID().toString().toString();
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
                                            SimpleDateFormat Datess = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                                            String Jadi = Datess.format(dateF);
                                            Waktu = dateF;
                                            txtWaktu.setText(CW.getHariDariWaktu(dateF)+""+CW.getWaktuTanggalBiasa(dateF));
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
