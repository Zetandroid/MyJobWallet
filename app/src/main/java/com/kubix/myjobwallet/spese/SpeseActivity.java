package com.kubix.myjobwallet.spese;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;
import com.kubix.myjobwallet.fragment.BtnSheetSpeseFragment;
import com.kubix.myjobwallet.utility.VariabiliGlobali;

import java.util.ArrayList;
import java.util.List;

public class SpeseActivity extends AppCompatActivity implements View.OnClickListener {

    //FAB
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_apri, fab_chiudi, fab_ruota_avanti, fab_ruota_indietro;

    // LISTA RECYCLER VIEW
    private List<Uscite> usciteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private com.kubix.myjobwallet.spese.CustomAdapter mAdapter;

    //OGGETTI PER MODIFICA
    EditText testoModifica;
    Button buttonModifica;

    //ADMOB NATIVA
    private static String LOG_TAG = "EXAMPLE";
    NativeExpressAdView mAdView;
    VideoController mVideoController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spese);

        //INDICIZZA
        recyclerView = (RecyclerView) findViewById(R.id.listaSpese);

        //SETTAGGI RECYCLER VIEW
        mAdapter = new com.kubix.myjobwallet.spese.CustomAdapter(usciteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new com.kubix.myjobwallet.spese.RecyclerTouchListener(getApplicationContext(), recyclerView, new com.kubix.myjobwallet.spese.RecyclerTouchListener.ClickListener() {

            //EVENTI DI CLICK DEL RECYCLER
            @Override
            public void onClick(View view, int position) {

                //MODIFICA ENTRATA
                final Uscite movie = usciteList.get(position);
                vecchiaData = movie.getDataUscita();
                vecchioTitolo = movie.getTitolo();
                vecchiaCifra = movie.getUscita();
                vecchioTag = movie.getCategoriaUscita();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SpeseActivity.this);
                LayoutInflater inflater = SpeseActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_modifica_uscite, null);
                dialogBuilder.setView(dialogView);
                testoModifica = (EditText) dialogView.findViewById(R.id.testoModificaCifraUscita);
                buttonModifica = (Button) dialogView.findViewById(R.id.bottoneModificaCifraUscita);
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }

            @Override
            public void onLongClick(View view, final int position) {

                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(SpeseActivity.this);
                builder.setTitle(R.string.elimina)
                        .setMessage(R.string.elimina_veramente)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // ELIMINAZIONE
                                final Uscite movie = usciteList.get(position);
                                MainActivity.db.execSQL("DELETE FROM Uscite WHERE Titolo = '"+movie.getTitolo()+"' AND Cifra = '"+movie.getUscita()+"' AND Ora = '"+movie.getPromemoria()+"' AND Data = '"+movie.getDataUscita()+"' AND Categoria = '"+movie.getCategoriaUscita()+"'");
                                usciteList.remove(position);
                                mAdapter.notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // NOTHING
                            }
                        })
                        .setIcon(R.drawable.ic_dialog_alert)
                        .show();
            }
        }));

        //TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSpese);
        setTitle(R.string.toolbarSpese);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //ADMOB NATIVA
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

        //FAB MENU
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

        //CONTROLLO PER LA VISUALIZZAZIONE DELL'ADD VIEW
        if (VariabiliGlobali.statoPremium.equals("SI")){
            mAdView.setVisibility(View.GONE);
        }

        caricaEntrate();

    }

    //ON CLICK DEL FAB
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
                animateFAB();
                BottomSheetDialogFragment bottomSheetDialogFragment = new BtnSheetSpeseFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                break;
            case R.id.fab2:
                animateFAB();
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

        return;
}

    @Override
    public void onRestart(){
        super.onRestart();
        caricaEntrate();
    }

    public void caricaEntrate(){
        //CARICA NOTE IN LISTA
        try {
            usciteList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Uscite ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloSpesa=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraSpesa=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaSpesa=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataSpesa= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Uscite uscite = new Uscite (campoTitoloSpesa, campoCifraSpesa, campoPromemoria,campoDataSpesa, campoCategoriaSpesa, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        usciteList.add(uscite);
                        mAdapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }else
                    Snackbar.make(fab, R.string.dati_non_inseriti, Snackbar.LENGTH_LONG).show();
            }
            cr.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //EVENTI BOTTOMSHEET ENTRATE
    public void clickBottomSheetSpeseCasa(View v){
        //CARICA SPESE CASA IN LISTA
        try {
            usciteList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Uscite WHERE Categoria = 'Casa' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloSpesa=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraSpesa=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaSpesa=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataSpesa= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Uscite uscite = new Uscite (campoTitoloSpesa, campoCifraSpesa, campoPromemoria,campoDataSpesa, campoCategoriaSpesa, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        usciteList.add(uscite);
                        mAdapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }else
                    Snackbar.make(fab, R.string.dati_non_inseriti, Snackbar.LENGTH_LONG).show();
            }
            cr.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clickBottomSheetSpeseTrasporti(View v){
        //CARICA SPESE TRASPORTI IN LISTA
        try {
            usciteList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Uscite WHERE Categoria = 'Trasporti' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloSpesa=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraSpesa=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaSpesa=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataSpesa= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Uscite uscite = new Uscite (campoTitoloSpesa, campoCifraSpesa, campoPromemoria,campoDataSpesa, campoCategoriaSpesa, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        usciteList.add(uscite);
                        mAdapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }else
                    Snackbar.make(fab, R.string.dati_non_inseriti, Snackbar.LENGTH_LONG).show();
            }
            cr.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clickBottomSheetSpeseAuto(View v){
        //CARICA SPESE AUTO IN LISTA
        try {
            usciteList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Uscite WHERE Categoria = 'Auto' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloSpesa=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraSpesa=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaSpesa=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataSpesa= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Uscite uscite = new Uscite (campoTitoloSpesa, campoCifraSpesa, campoPromemoria,campoDataSpesa, campoCategoriaSpesa, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        usciteList.add(uscite);
                        mAdapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }else
                    Snackbar.make(fab, R.string.dati_non_inseriti, Snackbar.LENGTH_LONG).show();
            }
            cr.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clickBottomSheetSpeseCarburante(View v){
        //CARICA SPESE CARBURANTE IN LISTA
        try {
            usciteList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Uscite WHERE Categoria = 'Carburante' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloSpesa=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraSpesa=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaSpesa=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataSpesa= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Uscite uscite = new Uscite (campoTitoloSpesa, campoCifraSpesa, campoPromemoria,campoDataSpesa, campoCategoriaSpesa, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        usciteList.add(uscite);
                        mAdapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }else
                    Snackbar.make(fab, R.string.dati_non_inseriti, Snackbar.LENGTH_LONG).show();
            }
            cr.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clickBottomSheetSpeseBollette(View v){
        //CARICA SPESE BOLLETTE IN LISTA
        try {
            usciteList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Uscite WHERE Categoria = 'Bollette' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloSpesa=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraSpesa=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaSpesa=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataSpesa= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Uscite uscite = new Uscite (campoTitoloSpesa, campoCifraSpesa, campoPromemoria,campoDataSpesa, campoCategoriaSpesa, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        usciteList.add(uscite);
                        mAdapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }else
                    Snackbar.make(fab, R.string.dati_non_inseriti, Snackbar.LENGTH_LONG).show();
            }
            cr.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clickBottomSheetSpeseShopping(View v){
        //CARICA SPESE SHOPPING IN LISTA
        try {
            usciteList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Uscite WHERE Categoria = 'Shopping' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloSpesa=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraSpesa=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaSpesa=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataSpesa= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Uscite uscite = new Uscite (campoTitoloSpesa, campoCifraSpesa, campoPromemoria,campoDataSpesa, campoCategoriaSpesa, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        usciteList.add(uscite);
                        mAdapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }else
                    Snackbar.make(fab, R.string.dati_non_inseriti, Snackbar.LENGTH_LONG).show();
            }
            cr.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clickBottomSheetSpeseCibo(View v){
        //CARICA SPESE CIBO E BEVANDE IN LISTA
        try {
            usciteList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Uscite WHERE Categoria = 'Cibo & Bevande' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloSpesa=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraSpesa=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaSpesa=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataSpesa= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Uscite uscite = new Uscite (campoTitoloSpesa, campoCifraSpesa, campoPromemoria,campoDataSpesa, campoCategoriaSpesa, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        usciteList.add(uscite);
                        mAdapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }else
                    Snackbar.make(fab, R.string.dati_non_inseriti, Snackbar.LENGTH_LONG).show();
            }
            cr.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clickBottomSheetSpeseSvago(View v){
        //CARICA SPESE SVAGO IN LISTA
        try {
            usciteList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Uscite WHERE Categoria = 'Svago' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloSpesa=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraSpesa=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaSpesa=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataSpesa= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Uscite uscite = new Uscite (campoTitoloSpesa, campoCifraSpesa, campoPromemoria,campoDataSpesa, campoCategoriaSpesa, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        usciteList.add(uscite);
                        mAdapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }else
                    Snackbar.make(fab, R.string.dati_non_inseriti, Snackbar.LENGTH_LONG).show();
            }
            cr.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clickBottomSheetSpeseViaggi(View v){
        //CARICA SPESE VIAGGI IN LISTA
        try {
            usciteList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Uscite WHERE Categoria = 'Viaggi' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloSpesa=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraSpesa=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaSpesa=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataSpesa= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Uscite uscite = new Uscite (campoTitoloSpesa, campoCifraSpesa, campoPromemoria,campoDataSpesa, campoCategoriaSpesa, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        usciteList.add(uscite);
                        mAdapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }else
                    Snackbar.make(fab,R.string.dati_non_inseriti, Snackbar.LENGTH_LONG).show();
            }
            cr.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clickBottomSheetSpeseAltro(View v){
        //CARICA SPESE ALTRO IN LISTA
        try {
            usciteList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Uscite WHERE Categoria = 'Altro' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloSpesa=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraSpesa=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaSpesa=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataSpesa= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Uscite uscite = new Uscite (campoTitoloSpesa, campoCifraSpesa, campoPromemoria,campoDataSpesa, campoCategoriaSpesa, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        usciteList.add(uscite);
                        mAdapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }else
                    Snackbar.make(fab, R.string.dati_non_inseriti, Snackbar.LENGTH_LONG).show();
            }
            cr.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void clickBottomSheetSpeseTutte(View v){
        //CARICA TUTTE LE SPESE IN LISTA
        try {
            usciteList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Uscite ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloSpesa=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraSpesa=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaSpesa=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataSpesa= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Uscite uscite = new Uscite (campoTitoloSpesa, campoCifraSpesa, campoPromemoria,campoDataSpesa, campoCategoriaSpesa, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        usciteList.add(uscite);
                        mAdapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }else
                    Snackbar.make(fab, R.string.dati_non_inseriti, Snackbar.LENGTH_LONG).show();
            }
            cr.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    String vecchiaData;
    String vecchioTitolo;
    String vecchiaCifra;
    String vecchioTag;

    public void modificaSpesa(View v){
       if (! testoModifica.getText().toString().equals("")){
           MainActivity.db.execSQL("UPDATE Uscite SET Cifra = '"+testoModifica.getText().toString()+"' WHERE Data = '"+vecchiaData+"' AND Titolo = '"+vecchioTitolo+"' AND Cifra = '"+vecchiaCifra+"' AND Categoria = '"+vecchioTag+"'");
           Toast.makeText(this, R.string.dati_inseriti_successo, Toast.LENGTH_SHORT).show();
           finish();
       }else{
           Toast.makeText(this, R.string.nessuna_modifica, Toast.LENGTH_SHORT).show();
       }
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
