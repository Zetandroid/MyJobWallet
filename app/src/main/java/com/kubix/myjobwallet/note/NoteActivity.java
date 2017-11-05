package com.kubix.myjobwallet.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.kubix.myjobwallet.MainActivity;
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

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final Note movie = noteList.get(position);
                Toast.makeText(getApplicationContext(), movie.getTitolo() + " is selected!", Toast.LENGTH_SHORT).show();

                //ELIMINA NOTE
                try{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setCancelable(true);
                    builder.setTitle("ELIMINA NOTA");
                    builder.setMessage("Questa nota sarà eliminata dal database, procedere?");
                    builder.setPositiveButton(R.string.si_elimina,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.db.execSQL("DELETE FROM Note WHERE Titolo = '"+movie.getTitolo()+"' AND Nota = '"+movie.getNote()+"'");
                                    dialog.dismiss();
                                    finish();
                                }
                            });

                    builder.setNegativeButton(R.string.non_eliminare,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        notaData();
    }

    private void notaData() {

        try {
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Note",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{

                        String campoTitolo=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCorpo=cr.getString(cr.getColumnIndex("Nota"));
                        Note note = new Note (campoTitolo, campoCorpo);
                        noteList.add(note);


                    }while (cr.moveToNext());
                }else{
                    Toast.makeText(getApplicationContext(), R.string.noEntrateAggiunte, Toast.LENGTH_LONG).show();

                }
            }
            cr.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

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
        toolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoBianco));
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
