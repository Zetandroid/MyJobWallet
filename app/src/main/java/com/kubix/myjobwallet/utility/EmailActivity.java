package com.kubix.myjobwallet.utility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.kubix.myjobwallet.R;

public class EmailActivity extends AppCompatActivity{

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    //AdMob
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        // AdMob
        MobileAds.initialize(this, "ca-app-pub-9460579775308491~5760945149");
        mAdView = findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        //CONTROLLO PER LA VISUALIZZAZIONE DELL'ADD VIEW
        if (VariabiliGlobali.statoPremium.equals("SI")){
            mAdView.setVisibility(View.GONE);
        }
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
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }

        //CONTROLLO PER LA VISUALIZZAZIONE DELL'ADD VIEW
        if (VariabiliGlobali.statoPremium.equals("SI")){
            mAdView.setVisibility(View.GONE);
        }
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


        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEmail);
        setTitle(R.string.toolbarEmail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        findViewById(R.id.fabInviaEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });
    }

    //AVVIA IL PANNELLO DI SCELTA PER EMAIL
    private void sendFeedback() {
        final Intent _Intent = new Intent(android.content.Intent.ACTION_SEND);
        _Intent.setType("text/html");
        _Intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ "studio.kubix@gmail.com" });
        _Intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MyJobWallet");
        _Intent.putExtra(android.content.Intent.EXTRA_TEXT, "Ciao Kubix Studio...");
        startActivity(Intent.createChooser(_Intent, "Invia email a Kubix Studio"));
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
