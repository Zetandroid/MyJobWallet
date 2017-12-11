package com.kubix.myjobwallet.calendario;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.kubix.myjobwallet.R;
import com.kubix.myjobwallet.setting.ClsSettings;

public class CalendarioActivityDue extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        ClsSettings settings = new ClsSettings(getBaseContext());
        if (settings.get_temadark()) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_due);
    }
}
