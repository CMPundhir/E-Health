package com.example.e_health.views.fragments.ui.gallery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.e_health.commons.models.Appointment;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Constants;
import com.example.e_health.databinding.FragmentGalleryBinding;
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
import java.util.List;

public class GalleryFragment extends Fragment {

    private Context context;
    private FragmentGalleryBinding binding;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointment> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        appointmentAdapter = new AppointmentAdapter(context, list);
        binding.recyclerView.setAdapter(appointmentAdapter);
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

    private void getData(){
        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection(Constants.COLLECTIONS.USERS).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(Constants.COLLECTIONS.APPOINTMENTS).orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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