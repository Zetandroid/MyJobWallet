package com.kubix.myjobwallet.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    //INDICIZZAZIONE
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // PER LE SHARED PREFERENCES DELLA INTRO
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "MyJobWAllet";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
