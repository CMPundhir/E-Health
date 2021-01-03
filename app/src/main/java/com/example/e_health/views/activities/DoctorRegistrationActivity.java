package com.example.e_health.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.e_health.R;
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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoctorRegistrationActivity extends AppCompatActivity implements ItemRemoveListener {

    private ActivityDoctorRegistrationBinding binding;
    private DoctorSpecialityRecyclerAdapter adapter;
    private List<String> list = new ArrayList<>();
    private List<String> specialisationList = new ArrayList<>();
    private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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
        binding.eName.setText(firebaseUser.getDisplayName());
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
            User doctor = new User();
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
                    binding.progressBar.setVisibility(View.GONE);
                    binding.bRegister.setEnabled(true);
                    Prefs.init(DoctorRegistrationActivity.this);
                    Prefs.setUser(doctor);
                    Intent intent = new Intent(DoctorRegistrationActivity.this, DoctorDashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    CommonDialogs.myDialog(DoctorRegistrationActivity.this, "Error!", e.getMessage());
                }
            });
        });
    }

    @Override
    public void onRemove(String item) {
        list.remove(item);
        specialisationList.add(item);
        adapter.notifyDataSetChanged();
    }
}