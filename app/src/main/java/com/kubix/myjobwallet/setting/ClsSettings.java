package com.kubix.myjobwallet.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kubix.myjobwallet.R;

public class ClsSettings {

    SharedPreferences settings;
    Context context;

    public ClsSettings (Context context) {
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public boolean get_temadark() {
        boolean temadark = false;
        if (settings != null && context != null) {
            temadark = settings.getBoolean(context.getResources().getString(R.string.SETTINGSKEY_TEMADARK), temadark);
        }
        return temadark;
    }
}
