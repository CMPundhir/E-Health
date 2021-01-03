package com.example.e_health.commons.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AuthUtil {
    public static final int RC_SIGN_IN = 1;

    public static boolean isAuthValid(Context context){
        FirebaseApp.initializeApp(context);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            signIn(context);
            return false;
        }
        return true;
    }

    public static void signIn(Context context){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());
//                new AuthUI.IdpConfig.PhoneBuilder().build(),
//                new AuthUI.IdpConfig.GoogleBuilder().build(),
//                new AuthUI.IdpConfig.FacebookBuilder().build(),
//                new AuthUI.IdpConfig.TwitterBuilder().build());

        ((Activity)context).startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public static boolean validateAuthResult(Context context, int requestCode, int resultCode, Intent data){
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                return true;
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                CommonDialogs.notCancelableDialog(context, "Error!", "Authentication failed, Please login to use APP", "Login", "Close", (dialog, which) -> {
                    signIn(context);
                }, (dialog, which) -> {
                    System.exit(0);
                }, dialog -> {

                });
            }
        }
        return false;
    }

    public static void fetchUser(){

    }
}
