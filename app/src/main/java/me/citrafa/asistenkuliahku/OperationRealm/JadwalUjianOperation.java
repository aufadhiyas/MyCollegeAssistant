package me.citrafa.asistenkuliahku.OperationRealm;

import android.util.Log;

import io.realm.Realm;
import me.citrafa.asistenkuliahku.ModelClass.JadwalUjianModel;

/**
 * Created by SENSODYNE on 18/04/2017.
 */

public class JadwalUjianOperation {
    private static String TAG = JadwalKuliahOperation.class.getSimpleName();
    RealmBaseActivity rba;
    Realm realm;
    JadwalUjianModel jum;

    public void tambahJadwalUjian(final JadwalUjianModel obj){
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
        Number currentID = realm.where(JadwalUjianModel.class).max("no_ju");
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
        Number number = realm.where(JadwalUjianModel.class).max("no_jk");
        lastID = number.intValue();
        return lastID;

    }
}
