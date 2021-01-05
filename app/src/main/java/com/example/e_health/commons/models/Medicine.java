package com.example.e_health.commons.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Medicine implements Parcelable {
    private String name;
    private String precaution;
    private List<InTake> inTakes;

    public Medicine() {
    }

    protected Medicine(Parcel in) {
        name = in.readString();
        precaution = in.readString();
        inTakes = in.createTypedArrayList(InTake.CREATOR);
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrecaution() {
        return precaution;
    }

    public void setPrecaution(String precaution) {
        this.precaution = precaution;
    }

    public List<InTake> getInTakes() {
        return inTakes;
    }

    public void setInTakes(List<InTake> inTakes) {
        this.inTakes = inTakes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(precaution);
        dest.writeTypedList(inTakes);
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "name='" + name + '\'' +
                ", precaution='" + precaution + '\'' +
                ", inTakes=" + inTakes +
                '}';
    }

}
