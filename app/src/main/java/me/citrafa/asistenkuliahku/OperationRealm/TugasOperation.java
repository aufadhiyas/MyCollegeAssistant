package me.citrafa.asistenkuliahku.OperationRealm;

import android.util.Log;

import io.realm.Realm;
import me.citrafa.asistenkuliahku.ModelClass.DateStorageModel;
import me.citrafa.asistenkuliahku.ModelClass.JadwalKuliahModel;
import me.citrafa.asistenkuliahku.ModelClass.TugasModel;

/**
 * Created by SENSODYNE on 27/04/2017.
 */

public class TugasOperation {
    private static String TAG = TugasOperation.class.getSimpleName();
    Realm realm;

    public void TambahData(final TugasModel obj){
        realm = Realm.getDefaultInstance();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(obj);
                if (obj.getWaktu_t()!=null){
                    DateStorageModel dso =new DateStorageModel(getIdDate(),obj.getNo_t(),"TugasModel",obj.getWaktu_t(),null);
                    realm.copyToRealm(dso);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            public void onSuccess() {
                Log.d(TAG, "Berhasil Menyimpan Data Di Realm");
                Log.d(TAG, "Path : " + realm.getPath());
            }
        });
    }
    public void updatedata(final TugasModel obj){

    }
    public int getNextId() {
        realm = Realm.getDefaultInstance();
        Number currentID = realm.where(TugasModel.class).max("no_t");
        int nextID;
        if (currentID == null){
            nextID = 1;
        }else{
            nextID = currentID.intValue() + 1;
        }
        return nextID;
    }
    public int getIdDate(){
        realm = Realm.getDefaultInstance();
        Number number = realm.where(DateStorageModel.class).max("id");
        int nextid;
        if (number ==null){
            nextid = 1;
        }else{
            nextid = number.intValue()+1;
        }
        return nextid;
    }
}
