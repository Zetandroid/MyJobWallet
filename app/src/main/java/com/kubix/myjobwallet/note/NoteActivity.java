package com.kubix.myjobwallet.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.kubix.myjobwallet.R;

import java.util.ArrayList;
import java.util.Arrays;

public class NoteActivity extends AppCompatActivity {

    // LISTA RECYCLER VIEW
    ArrayList<String> titoloNote = new ArrayList<>(Arrays.asList("Titolo1", "Titolo2", "Titolo3", "Titolo4", "Titolo5", "Titolo6", "Titolo7"));

    //TODO ADS INTERSTITIAL
    private String TAG = NoteActivity.class.getSimpleName();
    InterstitialAd mInterstitialAd;

    //TODO ADMOB NATIVA
    private static String LOG_TAG = "EXAMPLE";
    NativeExpressAdView mAdView;
    VideoController mVideoController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //ID DEL RECYCLER
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //LAYOUT LINEARE VERTICALE
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //CHIAMATA ADAPTER PER INVIARE RIFERIMENTI
        CustomAdapter customAdapter = new CustomAdapter(NoteActivity.this, titoloNote);
        recyclerView.setAdapter(customAdapter);

        mInterstitialAd = new InterstitialAd(this);

        //TODO ID
        mInterstitialAd.setAdUnitId(getString(R.string.adsInterstitial));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        //TODO CARICAMENTO ADS
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }


        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNote);
        setTitle(R.string.toolbarNote);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        setSupportActionBar(toolbar);

        //TODO ADMOB NATIVA
        mAdView = (NativeExpressAdView) findViewById(R.id.adViewNote);
        mAdView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());
        mVideoController = mAdView.getVideoController();
        mVideoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            @Override
            public void onVideoEnd() {
                Log.d(LOG_TAG, "Video playback is finished.");
                super.onVideoEnd();
            }
        });
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mVideoController.hasVideoContent()) {
                    Log.d(LOG_TAG, "Received an ad that contains a video asset.");
                } else {
                    Log.d(LOG_TAG, "Received an ad that does not contain a video asset.");
                }
            }
        });

        mAdView.loadAd(new AdRequest.Builder().build());


        //TODO FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingAddNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity (new Intent(NoteActivity.this,NoteAggiungiActivity.class));

            }
        });
    }

}
