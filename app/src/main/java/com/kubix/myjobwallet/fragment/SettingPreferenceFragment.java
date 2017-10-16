package com.kubix.myjobwallet.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.kubix.myjobwallet.R;

/**
 * Created by mowmo on 15/10/17.
 */

public class SettingPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.impostazioni);

    }
}
