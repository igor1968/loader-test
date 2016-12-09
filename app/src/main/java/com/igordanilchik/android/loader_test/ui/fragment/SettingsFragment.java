package com.igordanilchik.android.loader_test.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.igordanilchik.android.loader_test.R;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String LOG_TAG = SettingsFragment.class.getSimpleName();

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

}
