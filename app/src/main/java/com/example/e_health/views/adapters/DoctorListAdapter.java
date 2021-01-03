package com.example.e_health.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_health.commons.listeners.DoctorSelectedListener;
import com.example.e_health.commons.models.User;
import com.example.e_health.databinding.ItemDoctorBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ViewHolder> {
    private Context context;
    private List<User> list;
    private DoctorSelectedListener listener;

    public DoctorListAdapter(Context context, List<User> list, DoctorSelectedListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemDoctorBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        User doctor = list.get(position);
        holder.binding.tTitle.setText(doctor.getName());
        holder.binding.tYOE.setText(String.format("Experience : %s years", doctor.getYoe()));
    StringBuilder builder = new StringBuilder();
    for(String s : doctor.getSpecialisations()){
        builder.append(s).append(",");
    }
        builder.replace(builder.lastIndexOf(","), builder.length(), "");
        holder.binding.tSpeciality.setText(builder.toString());
        holder.binding.tTitle.setText(doctor.getName());
        holder.binding.tDescription.setText(doctor.getDescription());
        holder.binding.cardview.setOnClickListener(v->{
            listener.onDoctorSelected(doctor);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemDoctorBinding binding;
        public ViewHolder(@NonNull @NotNull ItemDoctorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
