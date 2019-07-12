package com.example.apktienda1.Utils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.io.Serializable;

public class Producto implements Parcelable, Serializable {
    private String name, description,idUser,id,picture;
    private double price;

    Bitmap img;

    protected Producto(Parcel in) {
        name = in.readString();
        description = in.readString();
        idUser = in.readString();
        id = in.readString();
        picture = in.readString();
        price = in.readDouble();
        img = in.readParcelable(Bitmap.class.getClassLoader());
        cant = in.readInt();
    }

    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel in) {
            return new Producto(in);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };

    public Bitmap getImg() {
        return img;
    }
    public void setImg(Bitmap img) {
        this.img = img;
    }
    public Producto(String name, String description, String idUser, String id, String picture, double price, int cant) {
        this.name = name;
        this.description = description;
        this.idUser = idUser;
        this.id = id;
        this.picture = picture;
        this.price = price;
        this.cant = cant;
        img=null;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    private int cant;

    public Producto(String name, String description, String idUser, String id, double price, int cant) {
        this.name = name;
        this.description = description;
        this.idUser = idUser;
        this.id = id;
        this.price = price;
        this.cant = cant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Producto(String name, String description, String idUser, double price, int cant) {
        this.name = name;
        this.description = description;
        this.idUser = idUser;
        this.price = price;
        this.cant = cant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }


    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(idUser);
        parcel.writeString(id);
        parcel.writeString(picture);

        parcel.writeDouble(price);

        parcel.writeParcelable(img,i);
        parcel.writeString(name);

    }
    private void readFromParcel(Parcel in) {
        name = in.readString();
        description = in.readString();
        idUser = in.readString();
        id = in.readString();
        picture = in.readString();
        price = in.readDouble();
        img = in.readParcelable(Bitmap.class.getClassLoader());
        cant = in.readInt();
    }
}
