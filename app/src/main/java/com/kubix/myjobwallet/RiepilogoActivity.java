package com.kubix.myjobwallet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.kubix.myjobwallet.utility.VariabiliGlobali;

public class RiepilogoActivity extends AppCompatActivity{

    //AdMob
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riepilogo);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRiepilogo);
        setTitle(R.string.toolbarRiepilogo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // AdMob
        MobileAds.initialize(this, "ca-app-pub-9460579775308491~5760945149");
        mAdView = findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }

        //CONTROLLO PER LA VISUALIZZAZIONE DELL'ADD VIEW
        if (VariabiliGlobali.statoPremium.equals("SI")){
            mAdView.setVisibility(View.GONE);
        }

        super.onPause();
    }


    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }

        //CONTROLLO PER LA VISUALIZZAZIONE DELL'ADD VIEW
        if (VariabiliGlobali.statoPremium.equals("SI")){
            mAdView.setVisibility(View.GONE);
        }
        super.onDestroy();
    }

    // Freccia Indietro
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}



