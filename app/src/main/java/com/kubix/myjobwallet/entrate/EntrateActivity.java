package com.kubix.myjobwallet.entrate;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
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
import com.kubix.myjobwallet.fragment.BtnSheetEntrateFragment;

import java.util.ArrayList;
import java.util.List;

public class EntrateActivity extends AppCompatActivity implements View.OnClickListener {

    //TODO FAB
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_apri, fab_chiudi, fab_ruota_avanti, fab_ruota_indietro;

    //TODO INDICIZZA OGGETTI
    GridView listaEntrate;

    //TODO ADMOB NATIVA
    private static String LOG_TAG = "ENTRATE";
    NativeExpressAdView mAdView;
    VideoController mVideoController;


    String dataEntrata;
    String titoloEntrata;
    String cifraEntrata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrate);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEntrate);
        setTitle(R.string.toolbarEntrate);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        setSupportActionBar(toolbar);

        //TODO ADMOB NATIVA
        mAdView = (NativeExpressAdView) findViewById(R.id.adViewEntrate);
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
                BottomSheetDialogFragment bottomSheetDialogFragment = new BtnSheetEntrateFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                break;
            case R.id.fab2:
                startActivity(new Intent(this, EntrateAggiungiActivity.class));
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

        leggiEntrate();

        listaEntrate.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                //PRELEVA DATI ENTRATA
                dataEntrata = arg0.getItemAtPosition(position).toString().substring(0,10);
                titoloEntrata = arg0.getItemAtPosition(position).toString().substring(20, 25).replace(" ","");
                cifraEntrata = arg0.getItemAtPosition(position).toString().substring(30).replace(" ", "");
                eliminaEntrate();
            }
        });
    }

    @Override
    public void onRestart(){
        super.onRestart();
        leggiEntrate();
    }

    public void leggiEntrate(){
        listaEntrate=(GridView)findViewById(R.id.listaEntrate);
        List<String> li=new ArrayList<>();
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,li);
        dataAdapter.setDropDownViewResource(R.layout.activity_entrate);

        try {
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Entrate ORDER BY Data",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{

                        String campoData=cr.getString(cr.getColumnIndex("Data"));
                        String campoTitolo=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifra=cr.getString(cr.getColumnIndex("Cifra"));

                        li.add(campoData + "          " + campoTitolo + "          " + campoCifra);
                        listaEntrate.setAdapter(dataAdapter);

                    }while (cr.moveToNext());
                }else{
                    Toast.makeText(getApplicationContext(), R.string.noEntrateAggiunte, Toast.LENGTH_LONG).show();

                }
            }
            cr.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminaEntrate(){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("ELIMINA ENTRATA");
            builder.setMessage("Questa entrata sarà eliminata dal database, procedere?");
            builder.setPositiveButton(R.string.si_elimina,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                MainActivity.db.execSQL("DELETE FROM Entrate WHERE Data = '"+dataEntrata+"' AND Titolo = '"+titoloEntrata+"' AND Cifra = '"+cifraEntrata+"'");
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
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

