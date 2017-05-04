package me.citrafa.asistenkuliahku.Service;

import io.realm.Realm;

/**
 * Created by SENSODYNE on 26/04/2017.
 */

public class CountDate {
    Realm realm;


    public void CountDates() {
        realm = Realm.getDefaultInstance();

    }
}
