package com.kubix.myjobwallet.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
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
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    // LISTA RECYCLER VIEW
    private List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapter mAdapter;

    //INTERSTITIAL
    private String TAG = NoteActivity.class.getSimpleName();
    InterstitialAd mInterstitialAd;

    //ADMOB NATIVA
    private static String LOG_TAG = "EXAMPLE";
    NativeExpressAdView mAdView;
    VideoController mVideoController;

    //FAB DISSOLVENZA
    private FloatingActionButton mFloatingActionButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //RECYCLER VIEW
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //FAB DISSOLVENZA
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.floatingAddNote);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mFloatingActionButton.getVisibility() == View.VISIBLE) {
                    mFloatingActionButton.hide();
                } else if (dy < 0 && mFloatingActionButton.getVisibility() != View.VISIBLE) {
                    mFloatingActionButton.show();
                }
            }
        });

        mAdapter = new CustomAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        notaData();
    }

    private void notaData() {
        Note note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        note = new Note ("Titolo", "Nota");
        noteList.add(note);

        mAdapter.notifyDataSetChanged();

        mInterstitialAd = new InterstitialAd(this);



        //ID INTERSTITIAL
        mInterstitialAd.setAdUnitId(getString(R.string.adsInterstitial));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        //CARICAMENTO INTERSTITIAL
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


        //TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNote);
        setTitle(R.string.toolbarNote);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        setSupportActionBar(toolbar);

        //ADMOB NATIVA
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


        //FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingAddNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity (new Intent(NoteActivity.this,NoteAggiungiActivity.class));

            }
        });
    }

}
