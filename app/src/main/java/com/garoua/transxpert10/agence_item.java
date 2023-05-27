package com.garoua.transxpert10;

import android.os.Parcel;
import android.os.Parcelable;

public class agence_item implements Parcelable {

    private String nom;
    private String nom_chef;



    public agence_item(Parcel in) {
        nom = in.readString();
        nom_chef = in.readString();


    }

    public static final Creator<profil_item> CREATOR = new Creator<profil_item>() {
        @Override
        public profil_item createFromParcel(Parcel parcel) {
            return new profil_item(parcel);
        }

        @Override
        public profil_item[] newArray(int size) {
            return new profil_item[size];
        }
    };

    public agence_item(String nom, String nom_chef) {
        this.nom = nom;
        this.nom_chef = nom_chef;
    }


    public String getNom() {
        return nom;
    }

    public String getNom_chef() {
        return nom_chef;
    }





    @Override
    public int describeContents (){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(nom);
        parcel.writeString(nom_chef);
    }
}
