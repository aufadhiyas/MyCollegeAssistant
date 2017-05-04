package me.citrafa.asistenkuliahku;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmModel;
import me.citrafa.asistenkuliahku.ModelClass.JadwalKuliahModel;

/**
 * Created by SENSODYNE on 14/04/2017.
 */

public class Jadwal implements Serializable,RealmModel{
    ArrayList<JadwalKuliahModel> jk;
    String Tittle;
    int noJK;

    public Jadwal(){
        jk = new ArrayList<>();
    }

    public int getNoJK() {
        return noJK;
    }

    public void setNoJK(int noJK) {
        this.noJK = noJK;
    }

    public ArrayList<JadwalKuliahModel> getJk() {
        return jk;
    }

    public void setJk(ArrayList<JadwalKuliahModel> jk) {
        this.jk = jk;
    }

    public String getTittle() {
        return Tittle;
    }

    public void setTittle(String tittle) {
        Tittle = tittle;
    }

    public void addData(JadwalKuliahModel data){
        jk.add(data);
    }
}
