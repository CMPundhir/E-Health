package com.example.e_health.views.fragments.ui.slideshow;

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
import com.example.e_health.commons.models.Test;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Constants;
import com.example.e_health.databinding.FragmentSlideshowBinding;
import com.example.e_health.views.adapters.TestAdapter;
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

public class SlideshowFragment extends Fragment {

    private Context context;
    private FragmentSlideshowBinding binding;
    private TestAdapter adapter;
    private List<Test> list = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        adapter = new TestAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);
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
        FirebaseFirestore.getInstance().collection(Constants.COLLECTIONS.USERS).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(Constants.COLLECTIONS.TEST).orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                    list.add(documentSnapshot.toObject(Test.class));
                }
                adapter.notifyDataSetChanged();
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