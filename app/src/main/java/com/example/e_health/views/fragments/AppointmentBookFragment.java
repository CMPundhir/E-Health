package com.example.e_health.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.e_health.R;
import com.example.e_health.commons.enums.AppointmentStatus;
import com.example.e_health.commons.enums.UserStatus;
import com.example.e_health.commons.listeners.DoctorSelectedListener;
import com.example.e_health.commons.models.Appointment;
import com.example.e_health.commons.models.User;
import com.example.e_health.commons.pickers.DatePickerFragment;
import com.example.e_health.commons.pickers.TimePickerFragment;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Constants;
import com.example.e_health.databinding.FragmentAppointmentListBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class AppointmentBookFragment extends Fragment {

    FragmentAppointmentListBinding binding;
    private Context context;
    private List<User> doctorsList = new ArrayList<>();
    private User selectedDoctor;

    public AppointmentBookFragment() {
        // Required empty public constructor
    }

    public static AppointmentBookFragment newInstance() {
        AppointmentBookFragment fragment = new AppointmentBookFragment();
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
        binding = FragmentAppointmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ArrayAdapter<String> adapterTOD = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, context.getResources().getStringArray(R.array.type_of_doctors));
        //binding.sTypeOfDoctor.setAdapter(adapterTOD);
        //binding.sTypeOfDoctor.setThreshold(1);

        SearchableSpinner searchableSpinner = new SearchableSpinner(context) {
            @Override
            public void onSearchableItemClicked(Object item, int position) {
                super.onSearchableItemClicked(item, position);
                //Toast.makeText(context, "Clicked "+position, Toast.LENGTH_SHORT).show();
                binding.sTypeOfDoctor.setText(item.toString());
                binding.progressBarRl.setVisibility(VISIBLE);
                FirebaseFirestore.getInstance().collection(Constants.COLLECTIONS.USERS).whereEqualTo(Constants.DocumentKeys.STATUS, UserStatus.ACTIVE.name()).whereArrayContains(Constants.DocumentKeys.SPECIALISATIONS, item.toString()).get().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        CommonDialogs.myDialog(context, "Error!", e.getMessage());
                        binding.progressBarRl.setVisibility(GONE);
                    }
                }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d("doctors", queryDocumentSnapshots.toString());
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            doctorsList.add(documentSnapshot.toObject(User.class));
                        }
                        Toast.makeText(context, "size : " + doctorsList.size(), Toast.LENGTH_SHORT).show();
                        binding.progressBarRl.setVisibility(GONE);
                    }
                });
            }
        };
        searchableSpinner.setAdapter(adapterTOD);
        binding.sTypeOfDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchableSpinner.onTouch(v, MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0, 0, 0));

            }
        });
        binding.sDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialogs.showDoctorsList(context, doctorsList, new DoctorSelectedListener() {
                    @Override
                    public void onDoctorSelected(User doctor) {
                        if(doctor.getName().toUpperCase().contains("DR.")){
                            binding.sDoctor.setText(String.format("%s", doctor.getName()));
                        }else {
                            binding.sDoctor.setText(String.format("Dr. %s", doctor.getName()));
                        }
                        selectedDoctor = doctor;
                    }

                    @Override
                    public void onDoctorStatus(User doctor) {

                    }
                });
            }
        });

        binding.eAppTime.setOnClickListener(v -> {
            DialogFragment newFragment = new TimePickerFragment(time -> {
                binding.eAppTime.setText(time);
            });
            newFragment.show(getChildFragmentManager(), "timePicker");
        });
        binding.eAppDate.setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment(new DatePickerFragment.OnDateSelectedListener() {
                @Override
                public void onDateSelected(String date) {
                    binding.eAppDate.setText(date);
                }
            });
            newFragment.show(getChildFragmentManager(), "datePicker");
        });
        binding.bBook.setOnClickListener(v -> {
            String pName, pAge, docId, desc, date, time;
            date = binding.eAppDate.getText().toString();
            time = binding.eAppTime.getText().toString();
            pName = binding.ePatientName.getText().toString();
            pAge = binding.ePatientAge.getText().toString();
            docId = selectedDoctor.getId();
            desc = binding.eRemark.getText().toString();
            if (TextUtils.isEmpty(pName)) {
                Toast.makeText(context, "Enter patient Name", Toast.LENGTH_SHORT).show();
                binding.ePatientName.setError("Enter Patient Name");
                return;
            }
            if (TextUtils.isEmpty(date)) {
                Toast.makeText(context, "Enter Patient date", Toast.LENGTH_SHORT).show();
                binding.eAppDate.setError("Enter Patient date");
                return;
            }
            if (TextUtils.isEmpty(time)) {
                Toast.makeText(context, "Enter Patient time", Toast.LENGTH_SHORT).show();
                binding.eAppTime.setError("Enter Patient time");
                return;
            }
            if (TextUtils.isEmpty(pAge)) {
                Toast.makeText(context, "Enter Patient Age", Toast.LENGTH_SHORT).show();
                binding.ePatientAge.setError("Enter Patient Age");
                return;
            }
            if (selectedDoctor == null || TextUtils.isEmpty(binding.sDoctor.getText().toString())) {
                Toast.makeText(context, "Please select doctor", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(desc)) {
                Toast.makeText(context, "Enter patient problem description", Toast.LENGTH_SHORT).show();
                binding.eRemark.setError("Enter Patient problem description");
                return;
            }
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.bBook.setEnabled(false);
            Appointment appointment = new Appointment();
            appointment.setStatus(AppointmentStatus.APPROVAL_PENDING.name());
            appointment.setPatientName(pName);
            appointment.setDate(date);
            appointment.setTime(time);
            appointment.setPatientAge(pAge);
            appointment.setDoctor(selectedDoctor);
            appointment.setDoctorId(docId);
            appointment.setProblemDesc(desc);
            appointment.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
            FirebaseFirestore.getInstance()
                    .collection(Constants.COLLECTIONS.USERS)
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection(Constants.COLLECTIONS.APPOINTMENTS)
                    .add(appointment)
                    .addOnSuccessListener(documentReference ->
                            {
                                FirebaseFirestore.getInstance()
                                        .collection(Constants.COLLECTIONS.APPOINTMENTS)
                                        .document(documentReference.getId())
                                        .set(appointment)
                                        .addOnSuccessListener(v1->{

                                        }).addOnFailureListener(e->{

                                        });
                                FirebaseFirestore.getInstance()
                                        .collection(Constants.COLLECTIONS.USERS)
                                        .document(docId)
                                        .collection(Constants.COLLECTIONS.APPOINTMENTS)
                                        .document(documentReference.getId())
                                        .set(appointment)
                                        .addOnSuccessListener(unused -> {
                                            CommonDialogs.myDialog(context, "Success", "Appointment Booked Successfully", new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    startActivity(((Activity) context).getIntent());
                                                    ((Activity) context).finish();
                                                    ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                    binding.progressBar.setVisibility(View.GONE);
                                                    binding.bBook.setEnabled(true);
                                                }
                                            });
                                        }).addOnFailureListener(e -> {
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.bBook.setEnabled(true);
                                    CommonDialogs.myDialog(context, "Error!", e.getMessage());
                                });
                            }
                    ).addOnFailureListener(e -> {
                binding.progressBar.setVisibility(View.GONE);
                binding.bBook.setEnabled(true);
                CommonDialogs.myDialog(context, "Error!", e.getMessage());
            });
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}