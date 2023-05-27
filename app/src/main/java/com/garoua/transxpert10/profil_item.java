package com.garoua.transxpert10;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

public class profil_item implements Parcelable {

    private String nom;
    private String email;
    private String fonction;
    private String password;



    public profil_item(Parcel in) {
        nom = in.readString();
        email = in.readString();
        fonction = in.readString();
        password = in.readString();


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

    public profil_item(String nom, String email, String fonction, String password) {
        this.nom = nom;
        this.email = email;
        this.fonction = fonction;
        this.password = password;
    }


    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getFonction() {
        return fonction;
    }

    public String getPassword() {
        return password;
    }




    @Override
    public int describeContents (){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(nom);
        parcel.writeString(email);
        parcel.writeString(fonction);
        parcel.writeString(password);
    }
}