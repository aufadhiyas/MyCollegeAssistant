package me.citrafa.asistenkuliahku.ActivityClass.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;

import me.citrafa.asistenkuliahku.ActivityClass.menuJadwalUjian;
import me.citrafa.asistenkuliahku.CustomWidget.LibraryDateCustom;
import me.citrafa.asistenkuliahku.ModelClass.DateStorageModel;
import me.citrafa.asistenkuliahku.ModelClass.JadwalUjianModel;
import me.citrafa.asistenkuliahku.OperationRealm.DateStorageOperation;
import me.citrafa.asistenkuliahku.OperationRealm.JadwalUjianOperation;
import me.citrafa.asistenkuliahku.R;

import static java.lang.String.valueOf;


public class fragment_form_ujian extends Fragment {
    menuJadwalUjian mju;
    private Spinner spJenis;
    private EditText txtMakul, txtWaktu, txtDeskripsi, txtRuangan;
    private Button simpan,selesai;
    private JadwalUjianModel jum;
    private DateStorageModel datesSave;
    JadwalUjianOperation JUO;
    DateStorageModel dso;
    DateStorageOperation DSO;
    private static String modelName = "JadwalUjianModel";
    int id;
    Date dates;
    LibraryDateCustom CW;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jadwal_ujian_form, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DSO = new DateStorageOperation();
        JUO = new JadwalUjianOperation();
        CW = new LibraryDateCustom();
        initView(view);
        txtWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CW.DateTimePickerSingle(getActivity(),txtWaktu);
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
        int ids = id(10000);
        int dateID= DSO.getNextId();
        String Jenis = spJenis.getSelectedItem().toString();
        String Makul = txtMakul.getText().toString().trim();
        Date Waktu = CW.getFINALDATE();
        Date Waktuf= Waktu;
        String Deskripsi = txtDeskripsi.getText().toString();
        String Ruangan = txtRuangan.getText().toString().trim();
        String Author = "User";
        Date created_at = CW.getCurrentTimeStamp();
        Date updated_at = CW.getCurrentTimeStamp();
        int Status = 1;
        jum = new JadwalUjianModel(ids,Jenis,Makul,Waktu,Deskripsi,Ruangan,Status,Author,created_at,updated_at);
        datesSave = new DateStorageModel(dateID,ids,modelName,Waktu,Waktuf);
        JUO.tambahJadwalUjian(jum);
        DSO.insertDatetoStorage(datesSave);
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


}
