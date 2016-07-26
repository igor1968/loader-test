package com.igordanilchik.android.loader_test.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtils {

    public static void replaceFragment(FragmentActivity activity, int containerId, Fragment fragment, boolean addToBackStack) {
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(containerId, fragment, fragment.getClass().getName());
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }
}
