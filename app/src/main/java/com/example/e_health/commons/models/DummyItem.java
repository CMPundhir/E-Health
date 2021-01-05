package com.example.e_health.commons.models;

import android.widget.CheckBox;
import android.widget.EditText;

public class DummyItem {
    private String name;
    private CheckBox checkBox;
    private EditText quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public EditText getQuantity() {
        return quantity;
    }

    public void setQuantity(EditText quantity) {
        this.quantity = quantity;
    }
}
