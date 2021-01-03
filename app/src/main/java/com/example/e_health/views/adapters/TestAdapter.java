package com.example.e_health.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_health.commons.models.Appointment;
import com.example.e_health.commons.models.Test;
import com.example.e_health.commons.utils.DateTimePicker;
import com.example.e_health.databinding.ItemAppointmentBinding;
import com.example.e_health.databinding.ItemTestBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {
    private Context context;
    private List<Test> list;

    public TestAdapter(Context context, List<Test> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemTestBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Test appointment = list.get(position);
        holder.binding.tPName.setText(String.format("%s (%s year)", appointment.getPatientName(), appointment.getPatientAge()));
        holder.binding.tPDescription.setText(appointment.getProblemDesc());
        holder.binding.tDName.setText(appointment.getTestName());
        holder.binding.tAppTime.setText(String.format("%s @ %s", appointment.getDate(), appointment.getTime()));
        if(appointment.getTimestamp()!=null) {
            holder.binding.tTimestamp.setText(DateTimePicker.format1(appointment.getTimestamp().toDate()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemTestBinding binding;
        public ViewHolder(@NonNull @NotNull ItemTestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
