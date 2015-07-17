package com.teiki.cafea1e.business;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class Fields implements Parcelable {

    private String nom;

    @SerializedName("dist")
    private double distance;

    private double prix_comptoir;

    private double prix_salle;

    private int arrondissement;

    private String adresse;

    private String terasse;

    private String maj;

    private double[] geo_latitude = new double[2];

    private LatLng pos_gps;

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public double getPrix_comptoir() {
        return prix_comptoir;
    }
    public void setPrix_comptoir(double prix_comptoir) {
        this.prix_comptoir = prix_comptoir;
    }
    public double getPrix_salle() {
        return prix_salle;
    }
    public void setPrix_salle(double prix_salle) {
        this.prix_salle = prix_salle;
    }
    public int getArrondissement() {
        return arrondissement;
    }
    public void setArrondissement(int arrondissement) {
        this.arrondissement = arrondissement;
    }
    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String getTerasse() {
        return terasse;
    }
    public void setTerasse(String terasse) {
        this.terasse = terasse;
    }
    public String getMaj() {
        return maj;
    }
    public void setMaj(String maj) {
        this.maj = maj;
    }
    public double[] getGeo_Latitude() {
        return geo_latitude;
    }
    public void setLat_long(double[] lat_long) {
        this.geo_latitude = lat_long;
    }
    public LatLng getPos_gps() {
        if (pos_gps == null)
            if (geo_latitude != null)
                pos_gps = new LatLng(geo_latitude[0], geo_latitude[1]);
        return pos_gps;
    }
    public void setPos_gps(LatLng pos_gps) {
        this.pos_gps = pos_gps;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeDouble(prix_comptoir);
        dest.writeInt(arrondissement);
        dest.writeString(adresse);
        dest.writeString(terasse);
        dest.writeString(maj);
        dest.writeDoubleArray(geo_latitude);
    }

    public static final Parcelable.Creator<Fields> CREATOR = new Parcelable.Creator<Fields>() {
        public Fields createFromParcel(Parcel in) {
            return new Fields(in);
        }

        public Fields[] newArray(int size) {
            return new Fields[size];
        }
    };

    private Fields(Parcel in) {
        nom=in.readString();
        prix_comptoir = in.readDouble();
        arrondissement = in.readInt();
        adresse=in.readString();
        terasse=in.readString();
        maj=in.readString();
        geo_latitude = in.createDoubleArray();
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

}
