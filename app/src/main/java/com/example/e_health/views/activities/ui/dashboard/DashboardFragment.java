package com.example.e_health.views.activities.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_health.commons.models.Appointment;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Constants;
import com.example.e_health.databinding.FragmentDashboardBinding;
import com.example.e_health.views.adapters.AppointmentAdapter;
import com.example.e_health.views.fragments.AppointmentBookFragment;
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


public class DashboardFragment extends Fragment {


    private Context context;
    private FragmentDashboardBinding binding;
    private AppointmentAdapter appointmentAdapter;
    private static final String PARAM = "param";
    private List<Appointment> list = new ArrayList<>();
    private String i;
    public static DashboardFragment newInstance(String i) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(PARAM, i);
        fragment.setArguments(args);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        appointmentAdapter = new AppointmentAdapter(context, list);
        binding.recyclerView.setAdapter(appointmentAdapter);
        getData();
        return root;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            i = getArguments().getString(PARAM);
        }
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

    private void getData(){
        binding.progressBar.setVisibility(View.VISIBLE);
        Query query = FirebaseFirestore.getInstance().collection(Constants.COLLECTIONS.USERS).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(Constants.COLLECTIONS.APPOINTMENTS);
        Date date = Calendar.getInstance().getTime();
        if(i!=null) {
            if (i.equals("0")) {
                query.whereGreaterThan("timestamp", date);
            } else {
                query.whereLessThan("timestamp", date);
            }
        }
        query.whereGreaterThan("timestamp", date);
        query.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    list.add(documentSnapshot.toObject(Appointment.class));
                }
                appointmentAdapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                CommonDialogs.myDialog(context, "Error!", e.getMessage());
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

}