package com.kubix.myjobwallet;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by mowmo on 21/09/17.
 */

public class SettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.impostazioni);
    }

}

