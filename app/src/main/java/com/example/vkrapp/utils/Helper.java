package com.example.vkrapp.utils;

import android.widget.EditText;

public class Helper {

    public static boolean isValidDataElement(EditText et) {
        if(et.getText().toString().isEmpty()) {
            return false;
        }

        String line = et.getText().toString().trim();
        double result;

        try {
            result = Double.parseDouble(line);
        }catch (NumberFormatException e) {
            return false;
        }

        return result > 0 && result < 7000;
    }

    public static double parseData(EditText et) {
        String value = et.getText().toString().trim();
        return (value.isEmpty())? 0 : Double.valueOf(value);
    }
}
