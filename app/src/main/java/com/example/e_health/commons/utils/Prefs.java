package com.example.e_health.commons.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.e_health.commons.enums.UserType;
import com.example.e_health.commons.models.User;
import com.google.gson.Gson;


public class Prefs {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    public static final String USER_TYPE = "user_type";
    public static final String USER = "user";

    public static void init(Context context) {
        preferences = context.getSharedPreferences("auth_preferences", Context.MODE_PRIVATE);
    }

    public static String getUserType() {
        return preferences.getString(USER_TYPE, UserType.NONE.name());
    }

    public static void setUser(User user){
        editor = preferences.edit();
        editor.putString(USER, new Gson().toJson(user));
        editor.putString(USER_TYPE, user.getType().name());
        editor.apply();
    }

    public static User getUser(){
        String str = preferences.getString(USER, "");
        if(str.equals("")){
            return new User();
        }
        User user = new Gson().fromJson(str, User.class);
        if(user==null){
            return new User();
        }
        return user;
    }

    public static void clear(){
        editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
