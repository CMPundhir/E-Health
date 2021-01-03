package com.example.e_health.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_health.commons.enums.UserType;
import com.example.e_health.commons.listeners.OnAppointmentStatusChangedListener;
import com.example.e_health.commons.models.Appointment;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Constants;
import com.example.e_health.commons.utils.DateTimePicker;
import com.example.e_health.commons.utils.Prefs;
import com.example.e_health.databinding.ItemAppointmentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private Context context;
    private List<Appointment> list;

    public AppointmentAdapter(Context context, List<Appointment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemAppointmentBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Appointment appointment = list.get(position);
        holder.binding.tPName.setText(String.format("%s (%s year)", appointment.getPatientName(), appointment.getPatientAge()));
        holder.binding.tPDescription.setText(appointment.getProblemDesc());
        holder.binding.tDName.setText(appointment.getDoctor().getName());
        holder.binding.tYOE.setText(String.format("Experience : %s year", appointment.getDoctor().getYoe()));
        StringBuilder builder = new StringBuilder();
        for(String s : appointment.getDoctor().getSpecialisations()){
            builder.append(s).append(",");
        }
        holder.binding.tStatus.setText(String.format("Appointment Status: %s", appointment.getStatus()));
        holder.binding.tSpeciality.setText(builder.toString());
        holder.binding.tAppTime.setText(String.format("%s @ %s", appointment.getDate(), appointment.getTime()));
        if(appointment.getTimestamp()!=null) {
            holder.binding.tTimestamp.setText(DateTimePicker.format1(appointment.getTimestamp().toDate()));
        }
        if(Prefs.getUserType().equals(UserType.DOCTOR.name())){
            holder.binding.cardview.setOnClickListener(v->{
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                CommonDialogs.appointmentStatus(context, new OnAppointmentStatusChangedListener() {
                    @Override
                    public void onStatusChange(String status) {
                        holder.binding.progressBar.setVisibility(View.VISIBLE);
                        holder.binding.cardview.setEnabled(false);
                        appointment.setStatus(status);
                        FirebaseFirestore.getInstance().collection(Constants.COLLECTIONS.USERS).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(Constants.COLLECTIONS.APPOINTMENTS).document(appointment.getId()).
                                set(appointment).addOnSuccessListener(voi->{
                                FirebaseFirestore.getInstance().collection(Constants.COLLECTIONS.USERS).document(appointment.getUserId()).collection(Constants.COLLECTIONS.APPOINTMENTS).
                                        document(appointment.getId()).set(appointment).addOnSuccessListener(voi2->{
                                        CommonDialogs.myDialog(context, "Success", "Appointment Booked Successfully", new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                holder.binding.progressBar.setVisibility(View.GONE);
                                                holder.binding.cardview.setEnabled(true);
                                                notifyDataSetChanged();
                                            }
                                        });


                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        holder.binding.cardview.setEnabled(true);
                                        holder.binding.progressBar.setVisibility(View.GONE);
                                        CommonDialogs.myDialog(context,"Error!",e.getMessage());
                                    }
                                });

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                holder.binding.cardview.setEnabled(true);
                                holder.binding.progressBar.setVisibility(View.GONE);
                                CommonDialogs.myDialog(context,"Error!",e.getMessage());
                            }
                        });
                    }
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemAppointmentBinding binding;
        public ViewHolder(@NonNull @NotNull ItemAppointmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
