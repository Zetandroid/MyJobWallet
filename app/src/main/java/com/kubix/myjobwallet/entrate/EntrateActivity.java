package com.kubix.myjobwallet.entrate;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mowmo on 10/10/17.
 */

public class EntrateActivity extends AppCompatActivity {

    //TODO INDICIZZA OGGETTI
    GridView listaEntrate;

    //TODO ADMOB NATIVA
    private static String LOG_TAG = "ENTRATE";
    NativeExpressAdView mAdView;
    VideoController mVideoController;

    FloatingActionButton fab;

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


        //TODO FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEntrata);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity (new Intent(EntrateActivity.this,EntrateAggiungiActivity.class));

            }
        });

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

