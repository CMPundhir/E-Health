package com.example.e_health.commons.models;

import com.example.e_health.commons.enums.UserType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
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
    @SerializedName("type")
    @Expose
    private UserType type;
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

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }
}
