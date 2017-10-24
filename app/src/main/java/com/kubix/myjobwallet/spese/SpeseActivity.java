package com.kubix.myjobwallet.spese;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;

import java.util.ArrayList;
import java.util.List;

public class SpeseActivity extends AppCompatActivity{

    //TODO INDICIZZA OGGETTI
    GridView listaSpese;

    //TODO ADMOB NATIVA
    private static String LOG_TAG = "EXAMPLE";
    NativeExpressAdView mAdView;
    VideoController mVideoController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spese);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSpese);
        setTitle(R.string.toolbarSpese);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        setSupportActionBar(toolbar);


        //TODO ADMOB NATIVA
        mAdView = (NativeExpressAdView) findViewById(R.id.adViewSpese);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSpese);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity (new Intent(SpeseActivity.this,SpeseAggiungiActivity.class));

            }
        });

        leggiSpese();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        leggiSpese();
    }

    public void leggiSpese(){
        listaSpese=(GridView)findViewById(R.id.listaSpese);
        List<String> li=new ArrayList<>();
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,li);
        dataAdapter.setDropDownViewResource(R.layout.activity_entrate);

        try {
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Uscite ORDER BY Data",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{

                        String campoData=cr.getString(cr.getColumnIndex("Data"));
                        String campoTitolo=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifra=cr.getString(cr.getColumnIndex("Cifra"));

                        li.add(campoData + "          " + campoTitolo + "          " + campoCifra);
                        listaSpese.setAdapter(dataAdapter);

                    }while (cr.moveToNext());
                }else{
                    Toast.makeText(getApplicationContext(), R.string.noSpeseAggiunte, Toast.LENGTH_LONG).show();
                }
            }
            cr.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
