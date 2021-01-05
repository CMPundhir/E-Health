package com.example.e_health.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_health.commons.enums.AppointmentStatus;
import com.example.e_health.commons.enums.UserType;
import com.example.e_health.commons.listeners.OnAppointmentStatusChangedListener;
import com.example.e_health.commons.models.Appointment;
import com.example.e_health.commons.utils.CommonDialogs;
import com.example.e_health.commons.utils.Constants;
import com.example.e_health.commons.utils.DateTimePicker;
import com.example.e_health.commons.utils.Prefs;
import com.example.e_health.databinding.ItemAppointmentBinding;
import com.example.e_health.views.activities.MedicalTreatmentActivity;
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
        Log.d("appointment_",appointment.toString());
        holder.binding.tPName.setText(String.format("%s (%s year)", appointment.getPatientName(), appointment.getPatientAge()));
        holder.binding.tPDescription.setText(appointment.getProblemDesc());
        if(appointment.getDoctor()!=null) {
            holder.binding.tDName.setText(appointment.getDoctor().getName());
            holder.binding.tYOE.setText(String.format("Experience : %s year", appointment.getDoctor().getYoe()));
            StringBuilder builder = new StringBuilder();
            for (String s : appointment.getDoctor().getSpecialisations()) {
                builder.append(s).append(",");
            }
            holder.binding.tSpeciality.setText(builder.toString());
        }
        holder.binding.tStatus.setText(String.format("Appointment Status: %s", appointment.getStatus()));
        holder.binding.tAppTime.setText(String.format("Appointment Time: %s @ %s", appointment.getDate(), appointment.getTime()));
        if (appointment.getTimestamp() != null) {
            holder.binding.tTimestamp.setText(DateTimePicker.format1(appointment.getTimestamp().toDate()));
        }
        if(appointment.getTreatment()!=null){
            holder.binding.bCheckReport.setVisibility(View.VISIBLE);
            holder.binding.bCheckReport.setOnClickListener(v->{
                Intent intent = new Intent(context, MedicalTreatmentActivity.class);
                intent.putExtra(Constants.INTENT.APPOINTMENT, appointment);
                intent.putExtra(Constants.INTENT.TREATMENT, appointment.getTreatment());
                intent.putParcelableArrayListExtra(Constants.INTENT.MEDICINES, appointment.getTreatment().getMedicines());
                intent.putExtra(Constants.INTENT.EDITABLE, false);
                //Toast.makeText(context, appointment.getTreatment().toString(), Toast.LENGTH_SHORT).show();
                ((Activity) context).startActivity(intent);
            });
        }else{
            holder.binding.bCheckReport.setVisibility(View.GONE);
        }
        if (Prefs.getUserType().equals(UserType.DOCTOR.name())) {
            holder.binding.cardview.setOnClickListener(v -> {
                //Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                CommonDialogs.appointmentStatus(context, new OnAppointmentStatusChangedListener() {
                    @Override
                    public void onStatusChange(String status) {
                        if (status.equals(AppointmentStatus.DONE.name())) {
                            Intent intent = new Intent(context, MedicalTreatmentActivity.class);
                            intent.putExtra(Constants.INTENT.APPOINTMENT, appointment);
                            intent.putExtra(Constants.INTENT.EDITABLE, true);
                            ((Activity) context).startActivity(intent);
                        } else {
                            appointment.setStatus(status);
                            holder.binding.progressBar.setVisibility(View.VISIBLE);
                            holder.binding.cardview.setEnabled(false);
                            FirebaseFirestore.getInstance()
                                    .collection(Constants.COLLECTIONS.USERS)
                                    .document(appointment.getUserId())
                                    .collection(Constants.COLLECTIONS.APPOINTMENTS)
                                    .document(appointment.getId())
                                    .set(appointment).addOnSuccessListener(voi -> {
                                            FirebaseFirestore.getInstance()
                                                    .collection(Constants.COLLECTIONS.USERS)
                                                    .document(appointment.getDoctorId())
                                                    .collection(Constants.COLLECTIONS.APPOINTMENTS)
                                                    .document(appointment.getId())
                                                    .set(appointment).addOnSuccessListener(voi2 -> {
                                                CommonDialogs.myDialog(context, "Success", "Status updated Successfully", new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {
                                                        appointment.setStatus(status);

                                                    }
                                                });
                                                holder.binding.progressBar.setVisibility(View.GONE);
                                                holder.binding.cardview.setEnabled(true);
                                                notifyDataSetChanged();
                                            }).addOnFailureListener(e -> {
                                                holder.binding.cardview.setEnabled(true);
                                                holder.binding.progressBar.setVisibility(View.GONE);
                                                CommonDialogs.myDialog(context, "Error!", e.getMessage());
                                            });

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    holder.binding.cardview.setEnabled(true);
                                    holder.binding.progressBar.setVisibility(View.GONE);
                                    CommonDialogs.myDialog(context, "Error!", e.getMessage());
                                }
                            });
                        }
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
