package com.example.e_health.views.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.e_health.MainActivity;
import com.example.e_health.commons.enums.UserType;
import com.example.e_health.commons.models.User;
import com.example.e_health.commons.utils.AuthUtil;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Constants;
import com.example.e_health.commons.utils.Prefs;
import com.example.e_health.databinding.ActivitySplashBinding;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "splash_";
    ActivitySplashBinding binding;
    private static GoogleSignInClient mSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
        Prefs.init(this);
        //FirebaseAuth.getInstance().signOut();
        if(AuthUtil.isAuthValid(this)){

            User user = Prefs.getUser();
            if(user.getType()!=null) {
                if (user.getType().equals(UserType.PATIENT)) {
                    Prefs.init(SplashActivity.this);
                    Prefs.setUser(user);
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else if (user.getType().equals(UserType.DOCTOR)) {
                    Intent intent = new Intent(SplashActivity.this, DoctorDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else if (user.getType().equals(UserType.ADMIN)) {
                    Intent intent = new Intent(SplashActivity.this, AdminNewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }else{
                Log.d(TAG, "user not found");
                FirebaseAuth.getInstance().signOut();
                Prefs.clear();
                AuthUtil.signIn(this);
                //CommonDialogs.myDialog(SplashActivity.this, "Error!",new Gson().toJson(user));
            }
        }
        //CommonDialogs.preRegistrationDialog(SplashActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(AuthUtil.validateAuthResult(SplashActivity.this, requestCode, resultCode, data)){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore.getInstance().collection(Constants.COLLECTIONS.USERS).document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);
                    if(user!=null) {
                        Prefs.setUser(user);
                        if (user.getType().equals(UserType.DOCTOR)) {
                            Intent intent = new Intent(SplashActivity.this, DoctorDashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }else if (user.getType().equals(UserType.ADMIN)) {
                            Intent intent = new Intent(SplashActivity.this, AdminNewActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }else {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }else{
                        Log.d(TAG, "user not found");
                        CommonDialogs.preRegistrationDialog(SplashActivity.this);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.d(TAG, "error : "+e.getMessage());
                }
            });
        }
    }
}