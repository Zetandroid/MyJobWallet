package com.kubix.myjobwallet;

import android.os.Bundle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

import com.kubix.myjobwallet.fragment.SettingPreferenceFragment;

import static com.kubix.myjobwallet.R.id.toolbarSetting;

/**
 * Created by mowmo on 11/10/17.
 */

public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //TODO TOOLBAR
        toolbar = (Toolbar) findViewById(toolbarSetting);
        toolbar.setTitle(R.string.toolbarImpostazioni);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_freccia_indietro);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new SettingPreferenceFragment()).commit();


    }

}
