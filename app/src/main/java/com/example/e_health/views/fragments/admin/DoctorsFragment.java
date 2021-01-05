package com.example.e_health.views.fragments.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_health.R;
import com.example.e_health.commons.enums.UserStatus;
import com.example.e_health.commons.enums.UserType;
import com.example.e_health.commons.listeners.DoctorSelectedListener;
import com.example.e_health.commons.listeners.OnAppointmentStatusChangedListener;
import com.example.e_health.commons.models.Appointment;
import com.example.e_health.commons.models.User;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Constants;
import com.example.e_health.commons.utils.Prefs;
import com.example.e_health.databinding.FragmentDoctorsBinding;
import com.example.e_health.views.activities.DoctorRegistrationActivity;
import com.example.e_health.views.adapters.DoctorListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class DoctorsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Context context;
    private List<User> doctorList = new ArrayList<>();
    private FragmentDoctorsBinding binding;
    private DoctorListAdapter adapter;

    public DoctorsFragment() {
        // Required empty public constructor
    }

    public static DoctorsFragment newInstance() {
        DoctorsFragment fragment = new DoctorsFragment();
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
        binding = FragmentDoctorsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        adapter = new DoctorListAdapter(context, doctorList, new DoctorSelectedListener() {
            @Override
            public void onDoctorSelected(User doctor) {
                Intent intent = new Intent(context, DoctorRegistrationActivity.class);
                intent.putExtra("doctor", doctor);
                intent.putExtra("hide_r_button", true);
                intent.putExtra("Profile_update", true);
                startActivity(intent);
            }

            @Override
            public void onDoctorStatus(User doctor) {
                CommonDialogs.doctorStatus(context, new OnAppointmentStatusChangedListener() {
                    @Override
                    public void onStatusChange(String status) {
                        FirebaseFirestore.getInstance().collection(Constants.COLLECTIONS.USERS).document(doctor.getId()).update(Constants.DocumentKeys.STATUS, status).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                CommonDialogs.myDialog(context, "Success", " Status changed successfully.", new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        onRefresh();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                CommonDialogs.myDialog(context, "Error!", e.getMessage());
                            }
                        });
                    }
                });
            }
        }, true);
        binding.recyclerView.setAdapter(adapter);
        binding.pullToRefresh.setOnRefreshListener(this::onRefresh);
        getData();
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

    @Override
    public void onRefresh() {
        binding.pullToRefresh.setRefreshing(false);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        binding.pullToRefresh.setRefreshing(true);
        FirebaseFirestore.getInstance().collection(Constants.COLLECTIONS.USERS)
                .whereEqualTo(Constants.DocumentKeys.TYPE, UserType.DOCTOR.name())
                //.whereNotEqualTo(Constants.DocumentKeys.STATUS, UserStatus.ACTIVE.name())
                .get()
                .addOnSuccessListener(v -> {
                    doctorList.clear();
                    for (DocumentSnapshot documentSnapshot : v.getDocuments()) {
                        doctorList.add(documentSnapshot.toObject(User.class));
                    }
                    adapter.notifyDataSetChanged();
                    binding.pullToRefresh.setRefreshing(false);
                }).addOnFailureListener(e -> {
            binding.pullToRefresh.setRefreshing(false);
        });
    }
}