package me.citrafa.asistenkuliahku.ModelClass;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by SENSODYNE on 08/04/2017.
 */

public class CatatanModel extends RealmObject {

    @PrimaryKey
    private int no_c;
    private String nama_c;
    private String deskripsi_c;
    private Date waktu_c;
    private String attlink_c;
    private String noonline_c;
    private String Author;
    private Date created_at;
    private Date updated_at;


    public CatatanModel() {
    }

    public CatatanModel(int no_c, String nama_c, String deskripsi_c, Date waktu_c, String attlink_c, String noonline_c, String author, Date created_at, Date updated_at) {
        this.no_c = no_c;
        this.nama_c = nama_c;
        this.deskripsi_c = deskripsi_c;
        this.waktu_c = waktu_c;
        this.attlink_c = attlink_c;
        this.noonline_c = noonline_c;
        Author = author;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getNo_c() {
        return no_c;
    }

    public void setNo_c(int no_c) {
        this.no_c = no_c;
    }

    public String getNama_c() {
        return nama_c;
    }

    public void setNama_c(String nama_c) {
        this.nama_c = nama_c;
    }

    public String getDeskripsi_c() {
        return deskripsi_c;
    }

    public void setDeskripsi_c(String deskripsi_c) {
        this.deskripsi_c = deskripsi_c;
    }

    public Date getWaktu_c() {
        return waktu_c;
    }

    public void setWaktu_c(Date waktu_c) {
        this.waktu_c = waktu_c;
    }

    public String getAttlink_c() {
        return attlink_c;
    }

    public void setAttlink_c(String attlink_c) {
        this.attlink_c = attlink_c;
    }

    public String getNoonline_c() {
        return noonline_c;
    }

    public void setNoonline_c(String noonline_c) {
        this.noonline_c = noonline_c;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
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
}