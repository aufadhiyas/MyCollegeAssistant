package me.citrafa.asistenkuliahku.ActivityClass;

import android.app.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import io.realm.Realm;
import me.citrafa.asistenkuliahku.CustomWidget.LibraryDateCustom;
import me.citrafa.asistenkuliahku.ModelClass.DateStorageModel;
import me.citrafa.asistenkuliahku.ModelClass.JadwalKuliahModel;
import me.citrafa.asistenkuliahku.OperationRealm.DateStorageOperation;
import me.citrafa.asistenkuliahku.R;
import me.citrafa.asistenkuliahku.OperationRealm.JadwalKuliahOperation;

import static java.lang.String.valueOf;

public class frmJadwalKuliah extends AppCompatActivity {

    Spinner sp;
    EditText Ruang, makul, dosen, kelas, Jam;
    Button simpan, batal;
    private int mHour, mMinute;
    private JadwalKuliahModel jk;
    private String currentDateTime, hari;
    private Date waktus,waktuf;
    private Realm realm;
    JadwalKuliahOperation opJK;
    LibraryDateCustom LDC;
    int no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.frm_jadwalkuliah);
        Intent intent = getIntent();
        final int id = intent.getIntExtra("id", 19999);

        sp = (Spinner) findViewById(R.id.spinnerHari);
        Ruang = (EditText) findViewById(R.id.txtRuangan);
        makul = (EditText) findViewById(R.id.txtMakul);
        dosen = (EditText) findViewById(R.id.txtDosen);
        kelas = (EditText) findViewById(R.id.txtKelas);
        Jam = (EditText) findViewById(R.id.txtJam);
        disableSoftInputFromAppearing(Jam);
        simpan = (Button) findViewById(R.id.btnSimpan);
        batal = (Button) findViewById(R.id.btnBatal);
        LDC = new LibraryDateCustom();
        if (id<10000){
            JadwalKuliahModel jk = realm.where(JadwalKuliahModel.class).equalTo("no_jk",id).findFirst();
            sp.setSelection(jk.getNohari());
            Ruang.setText(jk.getRuangan_jk());
            makul.setText(jk.getMakul_jk());
            dosen.setText(jk.getDosen_jk());
            kelas.setText(jk.getKelas_jk());
            final Date date1 = jk.getWaktu_jk();
            final Date date2 = jk.getWaktu_jkf();
            SimpleDateFormat sdf = new SimpleDateFormat("HH;mm");
            Jam.setText(sdf.format(date1)+" - "+sdf.format(date2));
            waktuf = jk.getWaktu_jk();
            waktus = jk.getWaktu_jkf();

        }
        opJK = new JadwalKuliahOperation();


        Jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                final int year0 = 2011;
                final int month0 = 1;
                final int day0 = 1;

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(frmJadwalKuliah.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                String formatedDate = sdf.format(new Date(year0,month0,day0,hourOfDay,minute));
                                try {
                                    final Date date = sdf.parse(formatedDate);
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(frmJadwalKuliah.this,
                                            new TimePickerDialog.OnTimeSetListener() {

                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay1,
                                                                      int minute1) {
                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                                    String formatedDate = sdf.format(new Date(year0,month0,day0,hourOfDay1+1,minute1));
                                                    try {
                                                        Date date1 = sdf.parse(formatedDate);
                                                        SimpleDateFormat sdp = new SimpleDateFormat("HH:mm");
                                                        waktus = date;
                                                        waktuf = date1;
                                                        String prints = sdp.format(date);
                                                        String printf = sdp.format(date1);
                                                        Jam.setText(prints+" - "+printf);
                                                        Toast.makeText(frmJadwalKuliah.this, ""+valueOf(date), Toast.LENGTH_SHORT).show();


                                                    } catch (ParseException e) {

                                                    }

                                                }
                                            }, mHour, mMinute, true);
                                    timePickerDialog.setTitle("Jam Kuliah Selesai");
                                    timePickerDialog.show();

                                } catch (ParseException e) {

                                }

                            }
                        }, mHour, mMinute, true);
                timePickerDialog.setTitle("Jam Mulai Kuliah");
                timePickerDialog.show();
            }
        });


        simpan.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                final int no = ids(id);
                hari = sp.getSelectedItem().toString();
                final Date jams = waktus;
                final Date jamf = waktuf;
                final String Makul= makul.getText().toString().trim();
                final String Ruangan = Ruang.getText().toString().trim();
                final String Dosen = dosen.getText().toString().trim();
                final String Kelas = kelas.getText().toString().trim();
                final Date updated_at = getCurrentTimeStamp();
                final Date created_at = getCurrentTimeStamp();
                final String Author = "User";
                final int No_Online = 0;
                final String uid = "";
                final boolean status = true;
                final String Type = "Jadwal";
                final int nohari= noHariMen();
                //Toast.makeText(frmJadwalKuliah.this, "SIMPAN : "+hari+", " +Makul, Toast.LENGTH_SHORT).show();

                //memasukkan variable ke constructor
                jk = new JadwalKuliahModel(no, uid, hari, nohari, jams, jamf, Makul, Ruangan, Dosen, Kelas, created_at, updated_at, status, Author,Type,No_Online);
                kosongField();
                Toast.makeText(frmJadwalKuliah.this, "Jadwal Kuliahmu Tersimpan !", Toast.LENGTH_SHORT).show();
                if (hari.equals("Pilih Hari")){
                    Toast.makeText(frmJadwalKuliah.this, "Pilih Hari Dengan Benar", Toast.LENGTH_SHORT).show();
                }else if(hari.equals("Minggu")){
                    AlertHariMinggu();
                }else{
                    insert();
                }
            }
        });
        }


    private void insert(){
        opJK.tambahJadwalKuliah(jk);
    }

    private void AlertHariMinggu(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(frmJadwalKuliah.this);
        alertDialog.setMessage("Apakah Anda Yakin Ada Kuliah Pada Hari Minggu?");
        alertDialog.setPositiveButton("YA", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                insert();
            }
        });
        alertDialog.setNegativeButton("TIDAK",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                Toast.makeText(frmJadwalKuliah.this, "Silahkan Pilih Hari Anda!", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        alertDialog.show();
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
    public int noHariMen(){
        int no = 0;
        if (hari.equals("Senin")){
            no = 1;
        }else if (hari.equals("Selasa")){
           no = 2;
        }else if (hari.equals("Rabu")){
            no = 3;
        }else if (hari.equals("Kamis")){
            no = 4;
        }else if (hari.equals("Jumat")){
            no = 5;
        }else if (hari.equals("Sabtu")){
            no = 6;
        }else if (hari.equals("Minggu")){
            no = 7;
        }else{
            no = 0;
        }
       return no;
    }
    public void kosongField(){
        sp.setSelection(0);
        Ruang.setText("");
        makul.setText("");
        dosen.setText("");
        kelas.setText("");
        Jam.setText("");
    }
    public int ids(int id) {
        int idn;
        if (id == 19999) {
            idn = opJK.getNextId();
        } else {
            idn = id;
        }
        return idn;
    }

    public static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }

}
