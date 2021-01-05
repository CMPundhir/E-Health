package com.example.e_health.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.e_health.R;
import com.example.e_health.commons.enums.UserStatus;
import com.example.e_health.commons.enums.UserType;
import com.example.e_health.commons.listeners.ItemRemoveListener;
import com.example.e_health.commons.models.User;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Constants;
import com.example.e_health.commons.utils.Prefs;
import com.example.e_health.databinding.ActivityDoctorRegistrationBinding;
import com.example.e_health.views.adapters.DoctorSpecialityRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoctorRegistrationActivity extends AppCompatActivity implements ItemRemoveListener {

    private StorageReference mStorageRef;
    private StorageReference imgRef;
    private ActivityDoctorRegistrationBinding binding;
    private DoctorSpecialityRecyclerAdapter adapter;
    private List<String> list = new ArrayList<>();
    private List<String> specialisationList = new ArrayList<>();
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE = 11;
    private String imgRefId;
    private Uri imageUri;
    private boolean isProfileUpdate = false;
    private boolean isHideRegButton = false;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Prefs.init(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        imgRef = mStorageRef.child("doctor_licence/" + firebaseUser.getUid());
        isProfileUpdate = getIntent().getBooleanExtra("Profile_update", false);
        user = getIntent().getParcelableExtra("doctor");
        if(user == null){
            user = Prefs.getUser();
        }
        isHideRegButton = getIntent().getBooleanExtra("hide_r_button", false);

        binding.eName.setText(user.getName());
        if(user.getStatus()!=null && isProfileUpdate){
            binding.eName.setText(user.getName());
            binding.eAge.setText(user.getYoe());
            binding.eRemark.setText(user.getDescription());
            binding.bRegister.setText("Update Profile");
            if(user.getSpecialisations()!=null) {
                list.addAll(user.getSpecialisations());
            }
            try {
                binding.progressBar.setVisibility(View.VISIBLE);
                File localFile = File.createTempFile("licence_image", "jpg");
                imgRef = mStorageRef.child("doctor_licence/"+user.getId());
                imgRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Drawable drawable = BitmapDrawable.createFromPath(localFile.getAbsolutePath());
                    Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                    binding.img.setImageBitmap(bitmap);
                    binding.progressBar.setVisibility(View.GONE);
                    imageUri = Uri.parse(localFile.getAbsolutePath());
                }).addOnFailureListener(e -> {
                   // CommonDialogs.myDialog(context, "Error!", e.getMessage());
                    binding.progressBar.setVisibility(View.GONE);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        adapter = new DoctorSpecialityRecyclerAdapter(this, list, this::onRemove);
        binding.recyclerView.setAdapter(adapter);
        specialisationList.addAll(Arrays.asList(getResources().getStringArray(R.array.type_of_doctors).clone()));
        ArrayAdapter<String> adapterTOD = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, specialisationList);

        SearchableSpinner searchableSpinner = new SearchableSpinner(this){
            @Override
            public void onSearchableItemClicked(Object item, int position) {
                super.onSearchableItemClicked(item, position);
                list.add(item.toString());
                specialisationList.remove(item.toString());
                adapter.notifyDataSetChanged();
            }
        };
        searchableSpinner.setAdapter(adapterTOD);
        searchableSpinner.setTitle("Choose Specialisation");
        binding.bAdd.setOnClickListener(v->{
            searchableSpinner.onTouch(v, MotionEvent.obtain(0,0,MotionEvent.ACTION_UP,0,0,0));
        });
        binding.bFile.setOnClickListener(v -> {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Upload Medical Licence");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE);
        });
        binding.bDeleteFile.setOnClickListener(v->{
            imageUri = null;
            binding.img.setImageBitmap(null);
        });
        binding.bRegister.setOnClickListener(v->{
            String name, yoe, remark;
            name = binding.eName.getText().toString();
            yoe = binding.eAge.getText().toString();
            remark = binding.eRemark.getText().toString();
            if(TextUtils.isEmpty(name)){
                Toast.makeText(this, "Please enter Name", Toast.LENGTH_SHORT).show();
                binding.eName.setError("Please enter name");
                return;
            }
            if(TextUtils.isEmpty(yoe)){
                Toast.makeText(this, "Please enter age", Toast.LENGTH_SHORT).show();
                binding.eAge.setError("Please enter age");
                return;
            }
            if(list.size()==0){
                Toast.makeText(this, "Please add your specialisation", Toast.LENGTH_SHORT).show();
                binding.bAdd.performClick();
                return;
            }
            if(TextUtils.isEmpty(remark)){
                Toast.makeText(this, "Please enter Description", Toast.LENGTH_SHORT).show();
                binding.eRemark.setError("Please enter Description");
                return;
            }
            if(!isProfileUpdate && imageUri==null){
                CommonDialogs.myDialog(DoctorRegistrationActivity.this, "Medical Licence is Rquired!", "Please upload your medical licence for registration", new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        binding.bFile.performClick();
                    }
                });
            }
            User doctor = new User();
            if(isProfileUpdate && user.getStatus().toUpperCase().equals(UserStatus.ACTIVE.name())){
                doctor.setStatus(UserStatus.ACTIVE.name());
            }else {
                doctor.setStatus(UserStatus.APPROVAL_PENDING.name());
            }
            doctor.setLicence_pic("doctor_licence/" + firebaseUser.getUid());
            doctor.setName(name);
            doctor.setDescription(remark);
            doctor.setType(UserType.DOCTOR);
            doctor.setYoe(yoe);
            doctor.setSpecialisations(list);
            doctor.setEmail(firebaseUser.getEmail());
            doctor.setId(firebaseUser.getUid());
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.bRegister.setEnabled(false);
            FirebaseFirestore.getInstance().collection(Constants.COLLECTIONS.USERS).document(firebaseUser.getUid()).set(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Prefs.init(DoctorRegistrationActivity.this);
                    Prefs.setUser(doctor);
                    if(isProfileUpdate){
                        CommonDialogs.myDialog(DoctorRegistrationActivity.this, "Success", " Profile updated successfully.", new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        });
                    }else {
                        uploadImg();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    CommonDialogs.myDialog(DoctorRegistrationActivity.this, "Error!", e.getMessage());
                }
            });
        });
        if(isHideRegButton){
            binding.bRegister.setVisibility(View.GONE);
            binding.bAdd.setVisibility(View.GONE);
            binding.bDeleteFile.setVisibility(View.GONE);
            binding.bFile.setVisibility(View.GONE);
            binding.eName.setFocusable(false);
            binding.eRemark.setFocusable(false);
            binding.eAge.setFocusable(false);

            binding.eName.setClickable(false);
            binding.eRemark.setClickable(false);
            binding.eAge.setClickable(false);

        }
        binding.img.setOnClickListener(v->{
            CommonDialogs.showImage(DoctorRegistrationActivity.this, binding.img);
        });
    }

    @Override
    public void onRemove(String item) {
        list.remove(item);
        specialisationList.add(item);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            Log.d("imgg", data.toString());
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                binding.img.setImageBitmap(bitmap);
                //uploadImg();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(DoctorRegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                CommonDialogs.myDialog(DoctorRegistrationActivity.this, "Error!", e.getMessage());
            }
        }
    }

    private void uploadImg() {
        if (imageUri == null) {
            Toast.makeText(this, "No Image Found", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        imgRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.bRegister.setEnabled(true);

            Intent intent = new Intent(DoctorRegistrationActivity.this, DoctorDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            binding.progressBar.setVisibility(View.GONE);
            //CommonDialogs.myDialog(MedicalTreatmentActivity.this, "Error!", e.getMessage());
            CommonDialogs.myDialog(DoctorRegistrationActivity.this, "Success", " Profile Uploaded successfully.", new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
        });


    }
}