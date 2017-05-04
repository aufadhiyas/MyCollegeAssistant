package me.citrafa.asistenkuliahku.ModelClass;

import android.app.Activity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

/**
 * Created by SENSODYNE on 08/04/2017.
 */

public class TugasModel extends RealmObject{
    @PrimaryKey
    private int no_t;
    private String deskripsi_t;
    private String attlink_t;
    private Date waktu_t;
    private int status_t;
    private Date created_at;
    private Date updated_at;
    private String author;
    private String noonline_t;


    public TugasModel() {
    }

    public TugasModel(int no_t, String deskripsi_t, String attlink_t, Date waktu_t, int status_t, Date created_at, Date updated_at, String author, String noonline_t) {
        this.no_t = no_t;
        this.deskripsi_t = deskripsi_t;
        this.attlink_t = attlink_t;
        this.waktu_t = waktu_t;
        this.status_t = status_t;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.author = author;
        this.noonline_t = noonline_t;
    }

    public int getNo_t() {
        return no_t;
    }

    public void setNo_t(int no_t) {
        this.no_t = no_t;
    }

    public String getDeskripsi_t() {
        return deskripsi_t;
    }

    public void setDeskripsi_t(String deskripsi_t) {
        this.deskripsi_t = deskripsi_t;
    }

    public String getAttlink_t() {
        return attlink_t;
    }

    public void setAttlink_t(String attlink_t) {
        this.attlink_t = attlink_t;
    }

    public Date getWaktu_t() {
        return waktu_t;
    }

    public void setWaktu_t(Date waktu_t) {
        this.waktu_t = waktu_t;
    }


    public int getStatus_t() {
        return status_t;
    }

    public void setStatus_t(int status_t) {
        this.status_t = status_t;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNoonline_t() {
        return noonline_t;
    }

    public void setNoonline_t(String noonline_t) {
        this.noonline_t = noonline_t;
    }
}
