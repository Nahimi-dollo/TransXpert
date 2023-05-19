package com.garoua.transxpert10;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

public class trans_item implements Parcelable {

    private String designation;
    private String description;
    private String region;
    private double longitude;
    private double latitude;
    private String image;
    private String ligne;
    private int puissance;



    public trans_item(Parcel in) {
        designation = in.readString();
        description = in.readString();
        region = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        image = in.readString();
        ligne = in.readString();
        puissance = in.readInt();


    }

    public static final Creator<trans_item> CREATOR = new Creator<trans_item>() {
        @Override
        public trans_item createFromParcel(Parcel parcel) {
            return new trans_item(parcel);
        }

        @Override
        public trans_item[] newArray(int size) {
            return new trans_item[size];
        }
    };

    public trans_item(String Designation, String Description, String Region, Double latitude, Double Longitude, String image, String Ligne, int Puissance) {
        this.designation = Designation;
        this.description = Description;
        this.region = Region;
        this.latitude = latitude;
        this.longitude = Longitude;
        this.image = image;
        this.ligne = Ligne;
        this.puissance = Puissance;
    }


    public String getDesignation() {
        return designation;
    }

    public String getDescription() {
        return description;
    }

    public String getRegion() {
        return region;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getlatitude() {
        return latitude;
    }

    public String getImage(){
        return image;
    }

    public String getLigne() {
        return ligne;
    }

    public int getPuissance() {
        return puissance;
    }



    @Override
    public int describeContents (){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(designation);
        parcel.writeString(description);
        parcel.writeString(region);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(image);
        parcel.writeString(ligne);
        parcel.writeInt(puissance);
    }
}