package com.example.e_health.views.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_health.commons.listeners.MedicineAdapterListener;
import com.example.e_health.commons.models.InTake;
import com.example.e_health.commons.models.Medicine;
import com.example.e_health.databinding.ItemMedicineBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    private Context context;
    private List<Medicine> list;
    private MedicineAdapterListener listener;
    private boolean isEditable;
    public MedicineAdapter(Context context, List<Medicine> list, MedicineAdapterListener listener, boolean isEditable) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.isEditable = isEditable;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemMedicineBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        final Medicine medicine = list.get(position);
        holder.clear();
        holder.binding.tSno.setText(String.format(Locale.ENGLISH, "%d. ", position + 1));
        holder.binding.eName.setText(medicine.getName());
        StringBuilder builder = new StringBuilder("");
        for (InTake inTake : medicine.getInTakes()) {
            builder.append(inTake.getName()).append("  x ").append(inTake.getQuantity()).append(" Tablets").append("\n");
        }
        builder.replace(builder.lastIndexOf("\n"), builder.length(), "");
        holder.binding.tTiming.setText(builder.toString());
        if(medicine.getPrecaution()!=null && !TextUtils.isEmpty(medicine.getPrecaution())) {
            holder.binding.tAdvice.setVisibility(View.VISIBLE);
            holder.binding.tAdvice.setText("Advice/Precaution: "+medicine.getPrecaution());
        }else{
            holder.binding.tAdvice.setVisibility(View.GONE);
        }
        holder.binding.bEdit.setOnClickListener(v->{
            listener.onEdit(position, medicine);
        });
        holder.binding.bDelete.setOnClickListener(v->{
            listener.onDelete(position, medicine);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMedicineBinding binding;
        public ViewHolder(@NonNull @NotNull ItemMedicineBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            if(!isEditable){
                binding.bDelete.setVisibility(View.GONE);
                binding.bEdit.setVisibility(View.GONE);
            }
        }
        public void clear(){
            binding.eName.setText("");
            binding.tSno.setText("");
            binding.tTiming.setText("");
            binding.tAdvice.setText("");
        }
    }
}
