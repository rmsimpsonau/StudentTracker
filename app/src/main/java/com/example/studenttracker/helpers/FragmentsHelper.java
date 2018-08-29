package com.example.studenttracker.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentsHelper {

    public static void replaceFragment(FragmentManager fragmentManager, int fragmentId, Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentId, fragment, fragment.getClass().getName());
        fragmentTransaction.commit();
    }

}
