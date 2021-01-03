package com.example.e_health.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.e_health.R;
import com.example.e_health.commons.listeners.DoctorSelectedListener;
import com.example.e_health.commons.models.Appointment;
import com.example.e_health.commons.models.Test;
import com.example.e_health.commons.models.User;
import com.example.e_health.commons.pickers.DatePickerFragment;
import com.example.e_health.commons.pickers.TimePickerFragment;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Constants;
import com.example.e_health.databinding.FragmentAppointmentListBinding;
import com.example.e_health.databinding.FragmentTestBookBinding;
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
import java.util.Arrays;
import java.util.List;


public class TestBookFragment extends Fragment {

    FragmentTestBookBinding binding;
    private Context context;
    private List<String> testList = new ArrayList<>();
    private String selectedTest;

    public TestBookFragment() {
        // Required empty public constructor
    }

    public static TestBookFragment newInstance() {
        TestBookFragment fragment = new TestBookFragment();
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
        binding = FragmentTestBookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        testList.addAll(Arrays.asList(context.getResources().getStringArray(R.array.type_of_test)));
        ArrayAdapter<String> adapterTOD = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, testList);
        //binding.sTypeOfDoctor.setAdapter(adapterTOD);
        //binding.sTypeOfDoctor.setThreshold(1);

        SearchableSpinner searchableSpinner = new SearchableSpinner(context){
            @Override
            public void onSearchableItemClicked(Object item, int position) {
                super.onSearchableItemClicked(item, position);
                //Toast.makeText(context, "Clicked "+position, Toast.LENGTH_SHORT).show();
                binding.sTypeOfDoctor.setText(item.toString());
                selectedTest = item.toString();
            }
        };
        searchableSpinner.setAdapter(adapterTOD);
        binding.sTypeOfDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchableSpinner.onTouch(v, MotionEvent.obtain(0,0,MotionEvent.ACTION_UP,0,0,0));

            }
        });

        binding.eAppTime.setOnClickListener(v -> {
            DialogFragment newFragment = new TimePickerFragment(time->{
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
        binding.bBook.setOnClickListener(v->{
            String pName, pAge, desc, date, time;
            date = binding.eAppDate.getText().toString();
            time = binding.eAppTime.getText().toString();
            pName = binding.ePatientName.getText().toString();
            pAge = binding.ePatientAge.getText().toString();
            desc = binding.eRemark.getText().toString();
            if(TextUtils.isEmpty(pName)){
                Toast.makeText(context, "Enter patient Name", Toast.LENGTH_SHORT).show();
                binding.ePatientName.setError("Enter Patient Name");
                return;
            }
            if(TextUtils.isEmpty(date)){
                Toast.makeText(context, "Enter Patient date", Toast.LENGTH_SHORT).show();
                binding.eAppDate.setError("Enter Patient date");
                return;
            }
            if(TextUtils.isEmpty(time)){
                Toast.makeText(context, "Enter Patient time", Toast.LENGTH_SHORT).show();
                binding.eAppTime.setError("Enter Patient time");
                return;
            }
            if(TextUtils.isEmpty(pAge)){
                Toast.makeText(context, "Enter Patient Age", Toast.LENGTH_SHORT).show();
                binding.ePatientAge.setError("Enter Patient Age");
                return;
            }
            if(TextUtils.isEmpty(selectedTest)){
                Toast.makeText(context, "Enter Select Test", Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(desc)){
                Toast.makeText(context, "Enter patient problem description", Toast.LENGTH_SHORT).show();
                binding.eRemark.setError("Enter Patient problem description");
                return;
            }
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.bBook.setEnabled(false);
            Test appointment = new Test();
            appointment.setPatientName(pName);
            appointment.setDate(date);
            appointment.setTime(time);
            appointment.setPatientAge(pAge);
            appointment.setTestName(selectedTest);
            appointment.setProblemDesc(desc);
            FirebaseFirestore.getInstance().collection(Constants.COLLECTIONS.USERS).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(Constants.COLLECTIONS.TEST).add(appointment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    FirebaseFirestore.getInstance().collection(Constants.COLLECTIONS.TEST).add(appointment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            CommonDialogs.myDialog(context, "Success", "Test Booked Successfully", new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    startActivity(((Activity)context).getIntent());
                                    ((Activity)context).finish();
                                    ((Activity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.bBook.setEnabled(true);
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.bBook.setEnabled(true);
                            CommonDialogs.myDialog(context,"Error!",e.getMessage());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.bBook.setEnabled(true);
                    CommonDialogs.myDialog(context,"Error!",e.getMessage());
                }
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