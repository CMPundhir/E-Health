package com.example.e_health.commons.utils;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.e_health.MainActivity;
import com.example.e_health.R;
import com.example.e_health.commons.enums.AppointmentStatus;
import com.example.e_health.commons.enums.UserStatus;
import com.example.e_health.commons.enums.UserType;
import com.example.e_health.commons.listeners.DoctorSelectedListener;
import com.example.e_health.commons.listeners.OnAppointmentStatusChangedListener;
import com.example.e_health.commons.listeners.OnMedicineCreatedListener;
import com.example.e_health.commons.models.DummyItem;
import com.example.e_health.commons.models.InTake;
import com.example.e_health.commons.models.Medicine;
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

import java.util.ArrayList;
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

            @Override
            public void onDoctorStatus(User doctor) {

            }
        }, false);
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
            listener.onStatusChange(AppointmentStatus.APPROVE.name());
            dialog.dismiss();
        });
        bCancel.setOnClickListener(v->{
            listener.onStatusChange(AppointmentStatus.CANCEL.name());
            dialog.dismiss();
        });
        bChange.setOnClickListener(v->{
            listener.onStatusChange(AppointmentStatus.DONE.name());
            dialog.dismiss();
        });
        return dialog;
    }

    public static Dialog medicineDialog(Context context, Medicine medicine, OnMedicineCreatedListener listener){
        Dialog dialog = getCustomDialog(context, R.layout.dialog_medicine);
        Button bDone = dialog.findViewById(R.id.bDone);
        EditText eName = dialog.findViewById(R.id.eName);
        EditText ePrecautions = dialog.findViewById(R.id.ePrecautions);

        EditText eQuantity = dialog.findViewById(R.id.eQuantity);
        EditText eQuantity2 = dialog.findViewById(R.id.eQuantity2);
        EditText eQuantity3 = dialog.findViewById(R.id.eQuantity3);
        EditText eQuantity4 = dialog.findViewById(R.id.eQuantity4);
        EditText eQuantity5 = dialog.findViewById(R.id.eQuantity5);
        List<EditText> quantityList = new ArrayList<>();
        quantityList.add(eQuantity);
        quantityList.add(eQuantity2);
        quantityList.add(eQuantity3);
        quantityList.add(eQuantity4);
        quantityList.add(eQuantity5);

        CheckBox checkBox = dialog.findViewById(R.id.checkbox);
        CheckBox checkBox2 = dialog.findViewById(R.id.checkbox2);
        CheckBox checkBox3 = dialog.findViewById(R.id.checkbox3);
        CheckBox checkBox4 = dialog.findViewById(R.id.checkbox4);
        CheckBox checkBox5 = dialog.findViewById(R.id.checkbox5);
        List<CheckBox> checkBoxList = new ArrayList<>();
        checkBoxList.add(checkBox);
        checkBoxList.add(checkBox2);
        checkBoxList.add(checkBox3);
        checkBoxList.add(checkBox4);
        checkBoxList.add(checkBox5);

        TextView t1 = dialog.findViewById(R.id.t1);
        TextView t2 = dialog.findViewById(R.id.t2);
        TextView t3 = dialog.findViewById(R.id.t3);
        TextView t4 = dialog.findViewById(R.id.t4);
        TextView t5 = dialog.findViewById(R.id.t5);
        List<TextView> tList = new ArrayList<>();
        tList.add(t1);
        tList.add(t2);
        tList.add(t3);
        tList.add(t4);
        tList.add(t5);

        List<DummyItem> dummyItemList = new ArrayList<>();
        for(int i=0; i<5; i++){
            DummyItem dummyItem = new DummyItem();
            dummyItem.setName(tList.get(i).getText().toString());
            dummyItem.setCheckBox(checkBoxList.get(i));
            dummyItem.setQuantity(quantityList.get(i));
            dummyItemList.add(dummyItem);
            dummyItem.getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        dummyItem.getQuantity().setText("1");
                    }else{
                        dummyItem.getQuantity().setText("");
                    }
                }
            });
        }

        bDone.setOnClickListener(v->{
            String name = eName.getText().toString();
            String precaution = ePrecautions.getText().toString();
            if(TextUtils.isEmpty(name)){
                Toast.makeText(context, "Please enter Medicine Name", Toast.LENGTH_SHORT).show();
                eName.setError("Please enter Medicine Name");
                return;
            }
            List<InTake> inTakeList = new ArrayList<>();
            for(DummyItem dummyItem : dummyItemList){
                if(dummyItem.getCheckBox().isChecked()){
                    InTake inTake = new InTake();
                    inTake.setName(dummyItem.getName());
                    String q = dummyItem.getQuantity().getText().toString();
                    if(TextUtils.isEmpty(q)){
                        Toast.makeText(context, "Please enter Quantity", Toast.LENGTH_SHORT).show();
                        dummyItem.getQuantity().setError("Mention Quantity");
                        return;
                    }else{
                        int qInt = Integer.parseInt(q);
                        if(qInt==0){
                            Toast.makeText(context, "Please enter valid Quantity", Toast.LENGTH_SHORT).show();
                            dummyItem.getQuantity().setError("Mention Quantity");
                            return;
                        }
                        inTake.setQuantity(qInt);
                    }
                    inTakeList.add(inTake);
                }
            }
            if(inTakeList.size()==0){
                Toast.makeText(context, "Please choose any of time given above.", Toast.LENGTH_SHORT).show();
                CommonDialogs.myDialog(context, "No Time For Medicine", "Please choose time of Medicine Intake from given options");
                return;
            }
            Medicine medicine1 = new Medicine();
            medicine1.setName(name);
            medicine1.setPrecaution(precaution);
            medicine1.setInTakes(inTakeList);

            listener.onMedicineCreated(medicine1);
            dialog.dismiss();
        });
        return dialog;
    }

    public static void showImage(Context context, ImageView imageView){
        imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = drawable.getBitmap();
            Dialog dialog = getCustomDialog(context, R.layout.dialog_image);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            SubsamplingScaleImageView zoomableImageView = dialog.findViewById(R.id.imageView);
            zoomableImageView.setImage(ImageSource.bitmap(bitmap));
        }
    }

    public static void showImage(Context context, Bitmap bitmap){
        if (bitmap != null) {
            Dialog dialog = getCustomDialog(context, R.layout.dialog_image);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            SubsamplingScaleImageView zoomableImageView = dialog.findViewById(R.id.imageView);
            zoomableImageView.setImage(ImageSource.bitmap(bitmap));
        }
    }


    public static Dialog doctorStatus(Context context, OnAppointmentStatusChangedListener listener){
        Dialog dialog = getCustomDialog(context, R.layout.dialog_doctor_status);
        Button bApprove = dialog.findViewById(R.id.bApprove);
        Button bCancel = dialog.findViewById(R.id.bCancel);
        Button bPending = dialog.findViewById(R.id.bPending);

        bApprove.setOnClickListener(v->{
            listener.onStatusChange(UserStatus.ACTIVE.name());
            dialog.dismiss();
        });
        bCancel.setOnClickListener(v->{
            listener.onStatusChange(UserStatus.INACTIVE.name());
            dialog.dismiss();
        });
        bPending.setOnClickListener(v->{
            listener.onStatusChange(UserStatus.APPROVAL_PENDING.name());
            dialog.dismiss();
        });
        return dialog;
    }


}
