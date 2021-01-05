package com.example.e_health.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e_health.R;
import com.example.e_health.commons.enums.UserStatus;
import com.example.e_health.commons.models.User;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Prefs;
import com.example.e_health.databinding.FragmentProfileBinding;
import com.example.e_health.views.activities.DoctorRegistrationActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class ProfileFragment extends Fragment {

    private Context context;
    private FragmentProfileBinding binding;
    private StorageReference mStorageRef;
    private static final int PICK_IMAGE = 11;
    private StorageReference imgRef;
    private User user;
    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        imgRef = mStorageRef.child("profile_pic/"+FirebaseAuth.getInstance().getUid());
        updateUI();
        binding.bChangePic.setOnClickListener(v->{
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE);
        });
        if(user.getProfile_pic()==null){
            try {
                //Toast.makeText(context, "Downloading Profile Pic...", Toast.LENGTH_SHORT).show();
                File localFile = File.createTempFile("images", "jpg");
                imgRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Drawable drawable = BitmapDrawable.createFromPath(localFile.getAbsolutePath());
                        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                        binding.img.setImageBitmap(bitmap);
                        user.setProfile_pic(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap , FirebaseAuth.getInstance().getUid() , "profile_pic"));
                        Prefs.setUser(user);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        //CommonDialogs.myDialog(context, "Error!", e.getMessage());
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                CommonDialogs.myDialog(context, "Error!", e.getMessage());
            }
        }else{
            try {
                //Toast.makeText(context, "Setting Profile Pic...", Toast.LENGTH_SHORT).show();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(user.getProfile_pic()));
                binding.img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                CommonDialogs.myDialog(context, "Error!", e.getMessage());
            }
        }
        if(user.getStatus()==null || user.getStatus().equals(UserStatus.APPROVAL_PENDING.name())){
            binding.cardviewApprovalPending.setVisibility(View.VISIBLE);
        }else{
            binding.cardviewApprovalPending.setVisibility(View.GONE);
        }
        binding.bShowLicence.setOnClickListener(v->{
            try {
                binding.bShowLicence.setEnabled(false);
                binding.progressBar.setVisibility(View.VISIBLE);
                File localFile = File.createTempFile("licence_image", "jpg");
                imgRef = mStorageRef.child("doctor_licence/"+FirebaseAuth.getInstance().getUid());
                imgRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Drawable drawable = BitmapDrawable.createFromPath(localFile.getAbsolutePath());
                    Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                    CommonDialogs.showImage(context, bitmap);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.bShowLicence.setEnabled(true);
                }).addOnFailureListener(e -> {
                    CommonDialogs.myDialog(context, "Error!", e.getMessage());
                    binding.progressBar.setVisibility(View.GONE);
                    binding.bShowLicence.setEnabled(true);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        binding.bUpdate.setOnClickListener(v->{
            Intent intent = new Intent(context, DoctorRegistrationActivity.class);
            intent.putExtra("Profile_update", true);
            startActivity(intent);
        });
        return root;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            Log.d("imgg", data.toString());
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                binding.img.setImageBitmap(bitmap);

                binding.progressBar.setVisibility(View.VISIBLE);
                imgRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        user.setProfile_pic(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, FirebaseAuth.getInstance().getUid() , "profile_pic"));
                        Prefs.setUser(user);
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        binding.progressBar.setVisibility(View.GONE);
                        CommonDialogs.myDialog(context, "Error!", e.getMessage());
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                CommonDialogs.myDialog(context, "Error!", e.getMessage());
            }

            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //binding.img.setImageBitmap(imageBitmap);
            //Toast.makeText(context, "Image", Toast.LENGTH_SHORT).show();
        }
    }

    // MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, imageFileName , description);


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        user = Prefs.getUser();
        binding.name.setText(user.getName());
        binding.yoe.setText(user.getYoe()+" year");
        StringBuilder builder = new StringBuilder();
        for(String s : user.getSpecialisations()){
            builder.append(s).append(",");
        }
        binding.speciality.setText(builder.toString());
        binding.desc.setText(user.getDescription());
    }
}