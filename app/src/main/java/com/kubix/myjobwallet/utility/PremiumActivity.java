package com.kubix.myjobwallet.utility;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.kubix.myjobwallet.R;

public class PremiumActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPremium);
        setTitle(R.string.toolbarPremium);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        setSupportActionBar(toolbar);

    }
}
