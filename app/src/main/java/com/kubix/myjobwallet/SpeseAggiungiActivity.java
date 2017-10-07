package com.kubix.myjobwallet;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class SpeseAggiungiActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spese_aggiungi);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSpeseAggiungi);
        setTitle(R.string.toolbarSpeseAggiungi);
        toolbar.setTitleTextColor(getResources().getColor(R.color.lightText));
        setSupportActionBar(toolbar);
    }
}
