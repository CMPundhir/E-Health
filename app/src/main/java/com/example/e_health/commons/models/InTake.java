package com.example.e_health.commons.models;

import android.os.Parcel;
import android.os.Parcelable;

public class InTake implements Parcelable {
    private String name;
    private int quantity;

    public InTake() {
    }

    protected InTake(Parcel in) {
        name = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<InTake> CREATOR = new Creator<InTake>() {
        @Override
        public InTake createFromParcel(Parcel in) {
            return new InTake(in);
        }

        @Override
        public InTake[] newArray(int size) {
            return new InTake[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(quantity);
    }

    @Override
    public String toString() {
        return "InTake{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}