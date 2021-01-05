package com.example.e_health.commons.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;

public class Treatment implements Parcelable {
    private String symptoms;
    private String advice;
    private ArrayList<Medicine> medicines;
    private String fileUrl;

    public Treatment() {
    }

    protected Treatment(Parcel in) {
        symptoms = in.readString();
        advice = in.readString();
        fileUrl = in.readString();
    }

    public static final Creator<Treatment> CREATOR = new Creator<Treatment>() {
        @Override
        public Treatment createFromParcel(Parcel in) {
            return new Treatment(in);
        }

        @Override
        public Treatment[] newArray(int size) {
            return new Treatment[size];
        }
    };

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public ArrayList<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(ArrayList<Medicine> medicines) {
        this.medicines = medicines;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symptoms);
        dest.writeString(advice);
        dest.writeString(fileUrl);
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "symptoms='" + symptoms + '\'' +
                ", advice='" + advice + '\'' +
                ", medicines=" + medicines +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}
