package com.anime.reminderoli;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Mobil implements Parcelable{
    String id_user;
    String id_oli;

    public String getTanggalService() {
        return tanggalService;
    }

    public void setTanggalService(String tanggalService) {
        this.tanggalService = tanggalService;
    }

    public static Creator<Mobil> getCREATOR() {
        return CREATOR;
    }

    String tanggalService;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    String Status;

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_oli() {
        return id_oli;
    }

    public void setId_oli(String id_oli) {
        this.id_oli = id_oli;
    }

    public String getNamaMobil() {
        return namaMobil;
    }

    public void setNamaMobil(String namaMobil) {
        this.namaMobil = namaMobil;
    }

    public String getJenisMobil() {
        return jenisMobil;
    }

    public void setJenisMobil(String jenisMobil) {
        this.jenisMobil = jenisMobil;
    }

    public String getNoPol() {
        return noPol;
    }

    public void setNoPol(String noPol) {
        this.noPol = noPol;
    }

    public String getKmSekarang() {
        return kmSekarang;
    }

    public void setKmSekarang(String kmSekarang) {
        this.kmSekarang = kmSekarang;
    }

    public String getKmService() {
        return kmService;
    }

    public void setKmService(String kmService) {
        this.kmService = kmService;
    }

    String namaMobil;
    String jenisMobil;
    String noPol;
    String kmSekarang;
    String kmService;
    public  Mobil(){}
    public Mobil(JSONObject object) {
        try {


            String id_user = object.getString("Id_User");
            String id_oli = object.getString("Id_Oli");
            String namaMobil = object.getString("Nama_Mobil");
            String jenisMobil = object.getString("Jenis_Mobil");
            String noPol = object.getString("noPol");
            String kmSekarang = object.getString("kmAwal");
            String kmService = object.getString("kmService");
            String tglSer = object.getString("tgl_service");

            this.id_user = id_user;
            this.id_oli = id_oli ;
            this.namaMobil = namaMobil ;
            this.jenisMobil = jenisMobil ;
            this.noPol = noPol;
            this.kmSekarang = kmSekarang;
            this.kmService = kmService ;
            this.tanggalService = tglSer;


        }catch (JSONException e){
            e.printStackTrace();
        }
    }



    protected Mobil(Parcel in) {
        id_user = in.readString();
        id_oli = in.readString();
        namaMobil = in.readString();
        jenisMobil = in.readString();
        noPol = in.readString();
        kmSekarang = in.readString();
        kmService = in.readString();
        Status = in.readString();
        tanggalService = in.readString();
    }

    public static final Creator<Mobil> CREATOR = new Creator<Mobil>() {
        @Override
        public Mobil createFromParcel(Parcel in) {
            return new Mobil(in);
        }

        @Override
        public Mobil[] newArray(int size) {
            return new Mobil[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_user);
        dest.writeString(id_oli);
        dest.writeString(namaMobil);
        dest.writeString(jenisMobil);
        dest.writeString(noPol);
        dest.writeString(kmSekarang);
        dest.writeString(kmService);
        dest.writeString(Status);
        dest.writeString(tanggalService);
    }
}
