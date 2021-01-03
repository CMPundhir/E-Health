package com.example.e_health.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_health.commons.listeners.ItemRemoveListener;
import com.example.e_health.databinding.ItemAreaOfSpecialityBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class DoctorSpecialityRecyclerAdapter extends RecyclerView.Adapter<DoctorSpecialityRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<String> list;
    private ItemRemoveListener listener;

    public DoctorSpecialityRecyclerAdapter(Context context, List<String> list, ItemRemoveListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemAreaOfSpecialityBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.binding.tSno.setText(String.format(Locale.ENGLISH, "%d. ", position + 1));
        holder.binding.tTitle.setText(list.get(position));
        holder.binding.ibDelete.setOnClickListener(v->{
            listener.onRemove(list.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemAreaOfSpecialityBinding binding;
        public ViewHolder(@NonNull @NotNull ItemAreaOfSpecialityBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
