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

        return result > 0 && result < 200;
    }

    public static double parseData(EditText et) {
        return Double.parseDouble(et.getText().toString().trim());
    }
}
