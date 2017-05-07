package me.citrafa.asistenkuliahku.ActivityClass.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


import io.realm.Realm;
import me.citrafa.asistenkuliahku.CustomWidget.LibraryDateCustom;
import me.citrafa.asistenkuliahku.ModelClass.CatatanModel;
import me.citrafa.asistenkuliahku.OperationRealm.CatatanOperation;
import me.citrafa.asistenkuliahku.R;
import me.citrafa.asistenkuliahku.SessionManager.SessionManager;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;


/**
 * Created by SENSODYNE on 17/04/2017.
 */

public class fragment_frm_catatan extends Fragment {
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private  static final String TAG = fragment_frm_catatan.class.getSimpleName();
    EditText txtNamaCatatan, txtDeskripsi, txtWaktu;
    Button btnAttach, btnSimpan;
    Realm realm;
    TextView lblFile;
    CatatanOperation co;
    LibraryDateCustom LDC;
    Date Dates = null;
    File source, destination;
    ContentResolver contentResolver;
    int PICKFILE_RESULT_CODE,noC;
    String filePaths;
    CatatanModel cm;
    SessionManager session;

    private static final int MY_READ_EXTERNAL_STORAGE=0;

    private int mYear, mMonth, mDay, mHour, mMinute, id;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noC = getArguments().getInt("noCatatan",10000);
        return inflater.inflate(R.layout.fragment_frm_catatan, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        realm = Realm.getDefaultInstance();
        txtNamaCatatan = (EditText) view.findViewById(R.id.txtCatatanNama);
        btnAttach = (Button) view.findViewById(R.id.btnCatatanAttach);
        txtWaktu = (EditText) view.findViewById(R.id.txtWaktuCatatan);
        btnSimpan = (Button) view.findViewById(R.id.btnCatatanSimpan);
        txtDeskripsi = (EditText) view.findViewById(R.id.txtDeskripsiCatatan);
        lblFile = (TextView) view.findViewById(R.id.lblCatatanFileName);
        session = new SessionManager(getActivity());
        LDC = new LibraryDateCustom();
        co = new CatatanOperation();
        if (noC !=10000){
            CatatanModel catatan = realm.where(CatatanModel.class).equalTo("no_c",noC).findFirst();
            if (catatan!=null){
                txtNamaCatatan.setText(catatan.getNama_c());
                txtDeskripsi.setText(catatan.getDeskripsi_c());
                if (catatan.getWaktu_c()!=null){
                    txtWaktu.setText(LDC.getHariDariWaktu(catatan.getWaktu_c())+" - "+LDC.getWaktuTanggalBiasa(catatan.getWaktu_c()));
                    Dates = catatan.getWaktu_c();
                }
                if (catatan.getAttlink_c()!=null){
                    lblFile.setText(catatan.getAttlink_c());
                }
            }
        }


        btnAttach.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                boolean statusPermission = false;
                statusPermission = isStoragePermissionGranted();
                if (statusPermission !=false){
                    openFilePicker();

                }else{
                    Toast.makeText(getActivity(), "MyCollege Assistant tidak mendapat perizinan untuk membaca file kamu", Toast.LENGTH_SHORT).show();
                    btnAttach.setClickable(false);
                }
            }
        });
        txtWaktu.setOnClickListener(new View.OnClickListener() {
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



    public void SimpanData() {
        int ids = id(noC);
        String nama = txtNamaCatatan.getText().toString().trim();
        String uid = uuid();
        Date dateC = Dates;
        String paths = lblFile.getText().toString();
        String deskripsi = txtDeskripsi.getText().toString();
        Date created_at=getCurrentTimeStamp();
        Date updated_at=getCurrentTimeStamp();
        String Author=session.getEmaiUser();
        if (ids ==10000){
            cm = new CatatanModel(ids,uid,nama,deskripsi,dateC,paths,Author,true,created_at,updated_at);
            co.tambahCatatan(cm);
        }else {
            cm = new CatatanModel(ids,nama,deskripsi,dateC,paths,Author,true,updated_at);
            co.updateCatatan(cm);
        }

    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    public int id(int status) {
        //10000 untuk Menyimpan Data Di Realm
        //ELSE /Lainnya untuk mengupdate
        if (status == 10000) {
            id = co.getNextId();
        } else {
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


    private void openFilePicker() {
        new MaterialFilePicker()
                .withSupportFragment(fragment_frm_catatan.this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(false)
                .start();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            if (path != null) {
                lblFile.setText(path);
                filePaths = path;

            }
        }
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
                                            Dates = dateF;
                                            txtWaktu.setText(LDC.getHariDariWaktu(dateF)+""+LDC.getWaktuTanggalBiasa(dateF));
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

