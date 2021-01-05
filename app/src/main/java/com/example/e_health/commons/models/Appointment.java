package com.example.e_health.commons.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;

public class Appointment implements Parcelable {
    @DocumentId
    private String id;
    private String userId;
    private String patientName;
    private String patientAge;
    private String date;
    private String time;
    private String doctorId;
    private User doctor;
    private String problemDesc;
    private String status;
    private Treatment treatment;
    @ServerTimestamp
    private Timestamp timestamp;

    public Appointment(){

    }


    protected Appointment(Parcel in) {
        id = in.readString();
        userId = in.readString();
        patientName = in.readString();
        patientAge = in.readString();
        date = in.readString();
        time = in.readString();
        doctorId = in.readString();
        problemDesc = in.readString();
        status = in.readString();
        treatment = in.readParcelable(Treatment.class.getClassLoader());
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public String getProblemDesc() {
        return problemDesc;
    }

    public void setProblemDesc(String problemDesc) {
        this.problemDesc = problemDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(patientName);
        dest.writeString(patientAge);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(doctorId);
        dest.writeString(problemDesc);
        dest.writeString(status);
        dest.writeParcelable(treatment, flags);
        dest.writeParcelable(timestamp, flags);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", patientName='" + patientName + '\'' +
                ", patientAge='" + patientAge + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", doctor=" + doctor +
                ", problemDesc='" + problemDesc + '\'' +
                ", status='" + status + '\'' +
                ", treatment=" + treatment +
                ", timestamp=" + timestamp +
                '}';
    }
}
