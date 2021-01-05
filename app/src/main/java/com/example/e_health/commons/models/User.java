package com.example.e_health.commons.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.e_health.commons.enums.UserType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("specialisations")
    @Expose
    private List<String> specialisations;
    @SerializedName("yoe")
    @Expose
    private String yoe;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;
    @SerializedName("licence_pic")
    @Expose
    private String licence_pic;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private UserType type;


    public User() {

    }


    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        specialisations = in.createStringArrayList();
        yoe = in.readString();
        description = in.readString();
        profile_pic = in.readString();
        licence_pic = in.readString();
        status = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public List<String> getSpecialisations() {
        return specialisations;
    }

    public void setSpecialisations(List<String> specialisations) {
        this.specialisations = specialisations;
    }

    public String getYoe() {
        return yoe;
    }

    public void setYoe(String yoe) {
        this.yoe = yoe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getLicence_pic() {
        return licence_pic;
    }

    public void setLicence_pic(String licence_pic) {
        this.licence_pic = licence_pic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeStringList(specialisations);
        dest.writeString(yoe);
        dest.writeString(description);
        dest.writeString(profile_pic);
        dest.writeString(licence_pic);
        dest.writeString(status);
    }
}
