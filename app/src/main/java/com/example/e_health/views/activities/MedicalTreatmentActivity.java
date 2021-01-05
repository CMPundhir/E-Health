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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.e_health.commons.enums.AppointmentStatus;
import com.example.e_health.commons.listeners.MedicineAdapterListener;
import com.example.e_health.commons.listeners.OnMedicineCreatedListener;
import com.example.e_health.commons.models.Appointment;
import com.example.e_health.commons.models.Medicine;
import com.example.e_health.commons.models.Treatment;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Constants;
import com.example.e_health.commons.utils.Prefs;
import com.example.e_health.databinding.ActivityMedicalTreamentBinding;
import com.example.e_health.views.adapters.MedicineAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MedicalTreatmentActivity extends AppCompatActivity implements MedicineAdapterListener {
    private StorageReference mStorageRef;
    private StorageReference imgRef;
    private String imgRefId;
    private Uri imageUri;
    private static final int PICK_IMAGE = 11;
    private ActivityMedicalTreamentBinding binding;
    private MedicineAdapter adapter;
    private ArrayList<Medicine> list = new ArrayList<>();
    private Appointment appointment;
    private Treatment treatment;
    private ArrayList<Medicine> medicines;
    private boolean isMedicineRequired = true;
    private DocumentReference aminDocument;
    private DocumentReference doctorDocument;
    private DocumentReference patientDocument;
    private boolean isEditable = true;
    private boolean isImageLoaded = false;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMedicalTreamentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Prefs.init(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        appointment = getIntent().getParcelableExtra(Constants.INTENT.APPOINTMENT);
        treatment = getIntent().getParcelableExtra(Constants.INTENT.TREATMENT);
        medicines = getIntent().getParcelableArrayListExtra(Constants.INTENT.MEDICINES);

        if (appointment == null) {
            CommonDialogs.myDialog(this, "Error!", "Try again after sometime.", new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
            return;
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(appointment.getPatientName() + "'s Treatment");
        isEditable = getIntent().getBooleanExtra(Constants.INTENT.EDITABLE, false);
        if(!isEditable){
            binding.eAdvice.setFocusable(false);
            binding.eAdvice.setClickable(false);
            binding.eSymptoms.setFocusable(false);
            binding.eSymptoms.setClickable(false);
            binding.bAdd.setVisibility(View.GONE);
            binding.bDeleteFile.setVisibility(View.GONE);
            binding.bUpload.setVisibility(View.GONE);
            binding.bFile.setVisibility(View.GONE);

            if(treatment!=null) {
                treatment.setMedicines(medicines);
                appointment.setTreatment(treatment);
                binding.eSymptoms.setText(treatment.getSymptoms());
                binding.eAdvice.setText(treatment.getAdvice());
                Log.d("appointment", appointment.toString());
                Log.d("appointment", appointment.getTreatment().toString());
                if(treatment.getMedicines()!=null) {
                    list.addAll(treatment.getMedicines());
                }
                imgRefId = treatment.getFileUrl();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(imgRefId));
                    if(bitmap==null){
                        throw new IOException("No Image found");
                    }
                    binding.img.setImageBitmap(bitmap);
                    isImageLoaded = true;
                    Log.d("imggg","imag found");
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Log.d("imggg","image Not found");
                    binding.progressBar.setVisibility(View.VISIBLE);
                    imgRef = mStorageRef.child("appointment/" + imgRefId);
                    try {
                        File localFile = File.createTempFile("images", "jpg");
                        Log.d("imggg","fetching image from "+imgRefId);
                        imgRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                            Drawable drawable = BitmapDrawable.createFromPath(localFile.getAbsolutePath());
                            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                            binding.img.setImageBitmap(bitmap);
                            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap , imgRefId , "treatment_"+appointment.getPatientName());
                            binding.progressBar.setVisibility(View.GONE);
                            Log.d("imggg","fetching success image from "+imgRefId);
                            isImageLoaded = true;
                        }).addOnFailureListener(e -> {
                            Log.d("imggg","fetching failed image from "+imgRefId);
                            Log.d("imggg",e.getMessage());
                            binding.progressBar.setVisibility(View.GONE);
                            CommonDialogs.myDialog(MedicalTreatmentActivity.this, "Error!", e.getMessage());
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("imggg",e.getMessage());
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }else {
            if (appointment.getId() == null) {
                imgRefId = UUID.randomUUID().toString();
            } else {
                imgRefId = appointment.getId();
            }
            imgRef = mStorageRef.child("appointment/" + imgRefId);

            doctorDocument = FirebaseFirestore.getInstance()
                    .collection(Constants.COLLECTIONS.USERS)
                    .document(appointment.getDoctorId())
                    .collection(Constants.COLLECTIONS.APPOINTMENTS)
                    .document(appointment.getId());
            patientDocument = FirebaseFirestore.getInstance()
                    .collection(Constants.COLLECTIONS.USERS)
                    .document(appointment.getUserId())
                    .collection(Constants.COLLECTIONS.APPOINTMENTS)
                    .document(appointment.getId());
            aminDocument = FirebaseFirestore.getInstance()
                    .collection(Constants.COLLECTIONS.APPOINTMENTS)
                    .document(appointment.getId());
            binding.bAdd.setOnClickListener(v -> {
                CommonDialogs.medicineDialog(MedicalTreatmentActivity.this, null, new OnMedicineCreatedListener() {
                    @Override
                    public void onMedicineCreated(Medicine medicine) {
                        list.add(medicine);
                        adapter.notifyDataSetChanged();

                    }
                });
            });
            binding.bFile.setOnClickListener(v -> {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, PICK_IMAGE);
            });
            binding.bDeleteFile.setOnClickListener(v->{
                imageUri = null;
                binding.img.setImageBitmap(null);
            });
            binding.bUpload.setOnClickListener(v->{
                String symptoms, advice;
                symptoms = binding.eSymptoms.getText().toString();
                advice = binding.eAdvice.getText().toString();
                if(TextUtils.isEmpty(symptoms)){
                    Toast.makeText(this, "Please mention symptoms/findings of problem.", Toast.LENGTH_SHORT).show();
                    binding.eSymptoms.setError("Please mention symptoms/findings of problem.");
                    return;
                }
                if(TextUtils.isEmpty(advice)){
                    Toast.makeText(this, "Please provide some advice.", Toast.LENGTH_SHORT).show();
                    binding.eAdvice.setError("Please provide some advice.");
                    return;
                }
                if(list.isEmpty() && isMedicineRequired){
                    CommonDialogs.myDialog(MedicalTreatmentActivity.this, "No Medicine Mentioned!","Is patient don't need medicine?","No Need","Medicine Required", (a,b)->{
                        isMedicineRequired = false;
                    }, (a, b)->{
                        binding.bAdd.performClick();
                    });
                }else{
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.bUpload.setEnabled(false);
                    Treatment treatment = new Treatment();
                    treatment.setSymptoms(symptoms);
                    treatment.setAdvice(advice);
                    treatment.setMedicines(list);
                    treatment.setFileUrl(imgRefId);
                    appointment.setDoctor(Prefs.getUser());
                    appointment.setTreatment(treatment);
                    appointment.setStatus(AppointmentStatus.DONE.name());
                    doctorDocument.set(appointment).addOnSuccessListener(a->{
                        aminDocument.set(appointment).addOnSuccessListener(v1->{

                        }).addOnFailureListener(e->{

                        });
                        patientDocument.set(appointment).addOnSuccessListener(a1->{
                            binding.progressBar.setVisibility(View.GONE);
                            binding.bUpload.setEnabled(true);
                            uploadImg();
                        }).addOnFailureListener(e->{
                            binding.progressBar.setVisibility(View.GONE);
                            binding.bUpload.setEnabled(true);
                            CommonDialogs.myDialog(MedicalTreatmentActivity.this, "Error!", e.getMessage());
                        });
                    }).addOnFailureListener(e->{
                        binding.progressBar.setVisibility(View.GONE);
                        binding.bUpload.setEnabled(true);
                        CommonDialogs.myDialog(MedicalTreatmentActivity.this, "Error!", e.getMessage());
                    });
                }
            });
        }
        adapter = new MedicineAdapter(this, list, this, isEditable);
        binding.recyclerView.setAdapter(adapter);
        binding.img.setOnClickListener(v->{
            if(imageUri!=null || isImageLoaded) {
                CommonDialogs.showImage(MedicalTreatmentActivity.this, binding.img);
            }
        });
    }

    @Override
    public void onDelete(int pos, Medicine medicine) {

    }

    @Override
    public void onEdit(int pos, Medicine medicine) {

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
                Toast.makeText(MedicalTreatmentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                CommonDialogs.myDialog(MedicalTreatmentActivity.this, "Error!", e.getMessage());
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
            Log.d("imggg", taskSnapshot.toString());
            Log.d("imggg", taskSnapshot.getUploadSessionUri().toString());
            binding.progressBar.setVisibility(View.GONE);
            CommonDialogs.myDialog(MedicalTreatmentActivity.this, "Success", "Treatment shared successfully.", new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
        }).addOnFailureListener(e -> {
            binding.progressBar.setVisibility(View.GONE);
            //CommonDialogs.myDialog(MedicalTreatmentActivity.this, "Error!", e.getMessage());
            CommonDialogs.myDialog(MedicalTreatmentActivity.this, "Success", "Treatment shared successfully.", new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
        });


    }
}