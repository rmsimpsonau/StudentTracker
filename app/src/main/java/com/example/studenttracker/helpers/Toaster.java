package com.example.studenttracker.helpers;

import android.content.Context;
import android.widget.Toast;

public class Toaster {

    public static void makeToast(String message, Context context)
    {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
