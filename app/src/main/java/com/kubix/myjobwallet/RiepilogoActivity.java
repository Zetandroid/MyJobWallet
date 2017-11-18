package com.kubix.myjobwallet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class RiepilogoActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riepilogo);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRiepilogo);
        setTitle(R.string.toolbarRiepilogo);
        toolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoBianco));
        setSupportActionBar(toolbar);
    }

}
