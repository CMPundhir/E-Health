package com.example.e_health.commons.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_health.MainActivity;
import com.example.e_health.R;
import com.example.e_health.commons.enums.UserType;
import com.example.e_health.commons.listeners.DoctorSelectedListener;
import com.example.e_health.commons.listeners.OnAppointmentStatusChangedListener;
import com.example.e_health.commons.models.User;
import com.example.e_health.views.activities.DoctorRegistrationActivity;
import com.example.e_health.views.activities.SplashActivity;
import com.example.e_health.views.adapters.DoctorListAdapter;
import com.example.e_health.views.adapters.DoctorSpecialityRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommonDialogs {
    public static AlertDialog myDialog(final Context context, String title, String msg, String pB, String nB){
        if(context==null || ((Activity)context).isFinishing() || ((Activity)context).isDestroyed()){
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(msg);
        if(pB!=null) {
            builder.setPositiveButton(pB, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        if(nB!=null){
            builder.setNegativeButton(nB, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public static AlertDialog myDialog(final Context context, String title, String msg){
        return myDialog(context,title,msg,"Ok",null);
    }

    public static AlertDialog myDialog(final Context context, String title, String msg, DialogInterface.OnDismissListener dismissListener){
        return myDialog(context,title,msg,null,null, null , null , dismissListener);
    }

    public static AlertDialog myDialog(final Context context, String title, String msg, String pB, String nB, DialogInterface.OnDismissListener dismissListener){
        return myDialog(context,title,msg,pB,nB, null , null , dismissListener);
    }


    public static AlertDialog myDialog(final Context context, String title, String msg, String pB, String nB, DialogInterface.OnClickListener pListener, DialogInterface.OnClickListener nListener){
        if(context==null || ((Activity)context).isFinishing() || ((Activity)context).isDestroyed()){
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(msg);
        if(pB!=null) {
            builder.setPositiveButton(pB,pListener);
        }
        if(nB!=null){
            builder.setNegativeButton(nB,nListener);
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static AlertDialog myDialog(final Context context, String title, String msg, String pB, String nB, String nuB, DialogInterface.OnClickListener pListener, DialogInterface.OnClickListener nListener, DialogInterface.OnClickListener nuListener){
        if(context==null || ((Activity)context).isFinishing() || ((Activity)context).isDestroyed()){
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(msg);
        if(pB!=null) {
            builder.setPositiveButton(pB,pListener);
        }
        if(nB!=null){
            builder.setNegativeButton(nB,nListener);
        }
        if(nuB!=null){
            builder.setNeutralButton(nuB,nuListener);
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static AlertDialog myDialog(final Context context, String title, String msg, String pB, String nB, DialogInterface.OnClickListener pListener, DialogInterface.OnClickListener nListener, DialogInterface.OnDismissListener dismissListener){
        if(context==null || ((Activity)context).isFinishing() || ((Activity)context).isDestroyed()){
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(msg);
        if(pB!=null) {
            builder.setPositiveButton(pB,pListener);
        }
        if(nB!=null){
            builder.setNegativeButton(nB,nListener);
        }
        if(dismissListener!=null) {
            builder.setOnDismissListener(dismissListener);
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static AlertDialog notCancelableDialog(final Context context, String title, String msg, String pB, String nB, DialogInterface.OnClickListener pListener, DialogInterface.OnClickListener nListener, DialogInterface.OnDismissListener dismissListener){
        if(context==null || ((Activity)context).isFinishing() || ((Activity)context).isDestroyed()){
            return null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false)
                .setTitle(title)
                .setMessage(msg);
        if(pB!=null) {
            builder.setPositiveButton(pB,pListener);
        }
        if(nB!=null){
            builder.setNegativeButton(nB,nListener);
        }
        if(dismissListener!=null) {
            builder.setOnDismissListener(dismissListener);
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static Dialog getCustomDialog(final Context context, int layout){
        if(context==null || ((Activity)context).isFinishing() || ((Activity)context).isDestroyed()){
            return null;
        }
        Dialog dialog;
        dialog = new Dialog(context,android.R.style.Theme_Material_Light_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        return dialog;
    }

    public static Dialog preRegistrationDialog(Context context){
        Dialog dialog = getCustomDialog(context, R.layout.dialog_pre_registration);
        dialog.setCancelable(false);
        Button bContinue = dialog.findViewById(R.id.button);
        Button bDocReg = dialog.findViewById(R.id.button2);
        ProgressBar progressBar = dialog.findViewById(R.id.progress_bar);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser==null){
            AuthUtil.signIn(context);
            dialog.dismiss();
            return null;
        }
        bContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bContinue.setEnabled(false);
                bDocReg.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                User user = new User();
                user.setType(UserType.PATIENT);
                user.setEmail(firebaseUser.getEmail());
                user.setId(firebaseUser.getUid());
                FirebaseFirestore.getInstance().collection(Constants.COLLECTIONS.USERS).document(firebaseUser.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Prefs.init(context);
                        Prefs.setUser(user);
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                        ((Activity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        bContinue.setEnabled(true);
                        bDocReg.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        CommonDialogs.myDialog(context, "Error!", e.getMessage());
                        bContinue.setEnabled(true);
                        bDocReg.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });
        bDocReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorRegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
                ((Activity)context).finish();
                ((Activity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        return dialog;
    }

    public static Dialog showDoctorsList(Context context, List<User> doctors, DoctorSelectedListener listener){
        Dialog dialog = getCustomDialog(context, R.layout.dialog_doctors_list);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        Button bClose = dialog.findViewById(R.id.bClose);
        DoctorListAdapter adapter = new DoctorListAdapter(context, doctors, new DoctorSelectedListener() {
            @Override
            public void onDoctorSelected(User doctor) {
                listener.onDoctorSelected(doctor);
                dialog.dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
        bClose.setOnClickListener(v->{
            dialog.dismiss();
        });
        return dialog;
    }

    public static Dialog appointmentStatus(Context context, OnAppointmentStatusChangedListener listener){
        Dialog dialog = getCustomDialog(context, R.layout.dialog_appointment_status);
        Button bApprove = dialog.findViewById(R.id.bApprove);
        Button bCancel = dialog.findViewById(R.id.bCancel);
        Button bChange = dialog.findViewById(R.id.bChange);

        bApprove.setOnClickListener(v->{
            listener.onStatusChange("APPROVE");
            dialog.dismiss();
        });
        bCancel.setOnClickListener(v->{
            listener.onStatusChange("CANCEL");
            dialog.dismiss();
        });
        bChange.setOnClickListener(v->{
            listener.onStatusChange("CHANGE");
            dialog.dismiss();
        });
        return dialog;
    }

}
