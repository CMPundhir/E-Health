package com.example.e_health.commons.listeners;

import com.example.e_health.commons.models.Medicine;

public interface MedicineAdapterListener {
    void onDelete(int pos, Medicine medicine);
    void onEdit(int pos, Medicine medicine);
}
