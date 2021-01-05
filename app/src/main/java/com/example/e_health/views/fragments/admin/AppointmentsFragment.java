package com.example.e_health.views.fragments.admin;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.e_health.R;
import com.example.e_health.commons.enums.UserType;
import com.example.e_health.commons.models.Appointment;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Constants;
import com.example.e_health.databinding.FragmentAppointmentsBinding;
import com.example.e_health.views.adapters.AppointmentAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AppointmentsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private Context context;
    private List<Appointment> list = new ArrayList<>();
    private FragmentAppointmentsBinding binding;
    private AppointmentAdapter appointmentAdapter;
    public AppointmentsFragment() {
        // Required empty public constructor
    }

    public static AppointmentsFragment newInstance() {
        AppointmentsFragment fragment = new AppointmentsFragment();
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
        binding = FragmentAppointmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        appointmentAdapter = new AppointmentAdapter(context, list);
        binding.recyclerView.setAdapter(appointmentAdapter);
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
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        binding.pullToRefresh.setRefreshing(true);
        Query query = FirebaseFirestore.getInstance()
                .collection(Constants.COLLECTIONS.APPOINTMENTS)
                .limit(50);
        query.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            list.clear();
            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                list.add(documentSnapshot.toObject(Appointment.class));
            }
            appointmentAdapter.notifyDataSetChanged();
            binding.pullToRefresh.setRefreshing(false);
            //Toast.makeText(context, list.size()+" 0 ", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            CommonDialogs.myDialog(context, "Error!", e.getMessage());
            binding.pullToRefresh.setRefreshing(false);
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onRefresh() {
        getData();
    }
}