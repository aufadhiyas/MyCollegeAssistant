package me.citrafa.asistenkuliahku.OperationRealm;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;
import me.citrafa.asistenkuliahku.ModelClass.CatatanModel;
import me.citrafa.asistenkuliahku.ModelClass.CatatanModel;

/**
 * Created by SENSODYNE on 22/04/2017.
 */

public class CatatanOperation {
    private static String TAG = CatatanModel.class.getSimpleName();
    RealmBaseActivity rba;
    Realm realm;
    CatatanModel cm;

    public void tambahCatatan(final CatatanModel obj){
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
        Number currentID = realm.where(CatatanModel.class).max("no_c");
        int nextID;
        if (currentID == null){
            nextID = 1;
        }else{
            nextID = currentID.intValue() + 1;
        }
        return nextID;
    }
    public RealmResults<CatatanModel> getAllJadwalKuliah(){
        return realm.where(CatatanModel.class).findAll();
    }

    public int getLastId(){
        realm.getDefaultInstance();
        int lastID;
        Number number = realm.where(CatatanModel.class).max("no_c");
        lastID = number.intValue();
        return lastID;

    }
}
