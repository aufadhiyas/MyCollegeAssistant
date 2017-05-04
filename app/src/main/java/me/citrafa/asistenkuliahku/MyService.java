package me.citrafa.asistenkuliahku;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import me.citrafa.asistenkuliahku.ModelClass.DateStorageModel;
import me.citrafa.asistenkuliahku.ModelClass.JadwalKuliahModel;
import me.citrafa.asistenkuliahku.Service.MyBroadcastReceiver;

public class MyService extends Service {

    public final IBinder iBinder = new MyBinder();
    LocalBroadcastManager broadcastManager;

    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private final String TimerCode= "MyTimers";
    private final String SendTimerAction="SENDTIMER";
    private Chronometer mChronometer;


    Date date1;
    Realm realm;
    DateStorageModel dsm;
    getCurrentDateTime gtm;
    private static String TAG = "LOG";
    long millisJamJK = 0;
    String NamaMakul = "NAMA MAKUL";
    CountDownTimer cdt;



    public MyService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandlerThread = new HandlerThread("LocalServiceThread");
        mHandlerThread.start();
        getJadwalKuliahEarly();
        mHandler = new Handler(mHandlerThread.getLooper());
        Log.d(TAG, "TAG : SERVICE STARTED");
        broadcastManager = LocalBroadcastManager.getInstance(this);
        mChronometer = new Chronometer(this);
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String getTimestamp() {
        long elapsedMillis = SystemClock.elapsedRealtime()
                - mChronometer.getBase();
        int hours = (int) (elapsedMillis / 3600000);
        int minutes = (int) (elapsedMillis - hours * 3600000) / 60000;
        int seconds = (int) (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
        int millis = (int) (elapsedMillis - hours * 3600000 - minutes * 60000 - seconds * 1000);
        return hours + ":" + minutes + ":" + seconds + ":" + millis;
    }

    public void getJadwalKuliahEarly(){
        realm = Realm.getDefaultInstance();
        gtm = new getCurrentDateTime();
        int noHari = getNoHariHariIni(gtm.getEEE());
        final int year0 = 2011;
        final int month0 = 1;
        final int day0 = 1;
        Calendar c = Calendar.getInstance();
        int jam = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        long jadis = 10000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formatedDate = sdf.format(new Date(year0,month0,day0,jam,minutes));
        try {
            Date dateMowForJK = sdf.parse(formatedDate);
            JadwalKuliahModel jkm = realm.where(JadwalKuliahModel.class).equalTo("nohari",noHari).greaterThan("waktu_jk",dateMowForJK).findFirst();
            Log.d(TAG,"waktu : CEK ");
            if (jkm != null) {
                SimpleDateFormat formatjam = new SimpleDateFormat("HH:mm:ss:SSS");
                Date now = new Date();
                String Nows = formatjam.format(now);
                final String JK = formatjam.format(jkm.getWaktu_jk());
                final long jadi = (formatjam.parse(JK).getTime() - formatjam.parse(Nows).getTime());
                millisJamJK = jadi;
                scheduleNotification(getNotification(jkm.getMakul_jk()),jadi);

            }else{
                scheduleNotification(getNotification("COBA AJAJ"),10000);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG,"waktu : error");
        }
    }

    public String CountDownTime(){

        String k = "THIS IS FROM SERVICE";
        Log.d(TAG,"Service Eksekusi");
        return k;

    }

    private void scheduleNotification(Notification notification, long menit) {

        Intent notificationIntent = new Intent(this, MyBroadcastReceiver.class);
        notificationIntent.putExtra(MyBroadcastReceiver.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(MyBroadcastReceiver.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = menit;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis,pendingIntent);
    }

    private Notification getNotification(String content) {
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("JUDUL NOTIFIKASI");
        builder.setContentText(content);
        builder.setAutoCancel(true);
        builder.setSound(uri);
        builder.setSmallIcon(R.drawable.ic_alarm_on_green_48px);
        return builder.build();
    }

    public void getDateEarly(Context contex, TextView txt1, TextView txt2){
        int status = 1;
        Calendar calendar = Calendar.getInstance();


        realm = Realm.getDefaultInstance();
    }

    public int getNoHariHariIni(String EEE){

        if (EEE.equals("Mon")){
            return 1;
        }else if (EEE.equals("Tue")) {
            return 2;
        }else if (EEE.equals("Wed")) {
            return 3;
        }else if (EEE.equals("Thu")) {
            return 4;
        }else if (EEE.equals("Fri")) {
            return 5;
        }else if (EEE.equals("Sat")) {
            return 6;
        }else if (EEE.equals("Sun")) {
            return 7;
        }
        return 0;
    }

    public class getCurrentDateTime{
        Date date;
        int year,month,day,hour,minute;
        String EEE;
        public getCurrentDateTime(){
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.getMinimum(Calendar.MINUTE);
            date = new Date();
            SimpleDateFormat eee = new  SimpleDateFormat("EEE");
            EEE = eee.format(date);
        }

        public Date getDate() {
            return date;
        }

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getDay() {
            return day;
        }

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }

        public String getEEE() {
            return EEE;
        }
    }
    public class MyBinder extends Binder{
        public MyService getService(){
            return MyService.this;
        }
    }



}
