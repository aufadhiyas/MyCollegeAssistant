package me.citrafa.asistenkuliahku.OperationRealm;

import android.util.Log;

import io.realm.Realm;
import me.citrafa.asistenkuliahku.ModelClass.JadwalLainModel;

/**
 * Created by SENSODYNE on 18/04/2017.
 */

public class JadwalLainOperation {
    private static String TAG = JadwalLainOperation.class.getSimpleName();
    RealmBaseActivity rba;
    Realm realm;
    JadwalLainModel jum;

    public void tambahJadwalLain(final JadwalLainModel obj){
        realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(obj);
            }
        }, new Realm.Transaction.OnSuccess() {
            public void onSuccess() {
                Log.d(TAG, "Berhasil Menyimpan Data Di Realm");
                Log.d(TAG, "Path : " + realm.getPath());
            }
        });
        realm.beginTransaction();
        realm.commitTransaction();
    }
    public int getNextId() {
        realm = Realm.getDefaultInstance();
        Number currentID = realm.where(JadwalLainModel.class).max("no_jl");
        int nextID;
        if (currentID == null){
            nextID = 1;
        }else{
            nextID = currentID.intValue() + 1;
        }
        return nextID;
    }

    public int getLastId(){
        realm.getDefaultInstance();
        int lastID;
        Number number = realm.where(JadwalLainModel.class).max("no_jk");
        lastID = number.intValue();
        return lastID;

    }
}
