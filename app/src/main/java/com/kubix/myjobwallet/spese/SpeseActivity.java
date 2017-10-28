package com.kubix.myjobwallet.spese;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.kubix.myjobwallet.fragment.BtnSheetSpeseFragment;

import java.util.ArrayList;
import java.util.List;

public class SpeseActivity extends AppCompatActivity implements View.OnClickListener {

    //TODO FAB
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_apri, fab_chiudi, fab_ruota_avanti, fab_ruota_indietro;

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

        //TODO FAB MENU
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab_apri = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_apri);
        fab_chiudi = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_chiudi);
        fab_ruota_avanti = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_ruota_avanti);
        fab_ruota_indietro = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_ruota_indietro);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
                BottomSheetDialogFragment bottomSheetDialogFragment = new BtnSheetSpeseFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                break;
            case R.id.fab2:
                startActivity(new Intent(this, SpeseAggiungiActivity.class));
                break;
        }
    }

    public void animateFAB() {

        if (isFabOpen) {

            fab.startAnimation(fab_ruota_indietro);
            fab1.startAnimation(fab_chiudi);
            fab2.startAnimation(fab_chiudi);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(fab_ruota_avanti);
            fab1.startAnimation(fab_apri);
            fab2.startAnimation(fab_apri);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            Log.d("Raj", "open");

        }

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
