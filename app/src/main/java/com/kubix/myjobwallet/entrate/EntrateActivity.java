package com.kubix.myjobwallet.entrate;

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
import com.kubix.myjobwallet.fragment.BtnSheetEntrateFragment;
import com.kubix.myjobwallet.utility.VariabiliGlobali;

import java.util.ArrayList;
import java.util.List;

public class EntrateActivity extends AppCompatActivity implements View.OnClickListener {

    //FAB
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;
    private Animation fab_apri, fab_chiudi, fab_ruota_avanti, fab_ruota_indietro;

    //ADMOB NATIVA
    private static String LOG_TAG = "ENTRATE";
    NativeExpressAdView mAdView;
    VideoController mVideoController;

    // LISTA RECYCLER VIEW
    private List<Entrate> entrateList = new ArrayList<>();
    private RecyclerView recyclerView;
    private com.kubix.myjobwallet.entrate.CustomAdapter mAdapter;

    //OGGETTI PER MODIFICA
    EditText testoModifica;
    Button buttonModifica;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrate);

        //INDICIZZA
        recyclerView = (RecyclerView) findViewById(R.id.listaEntrate);

        //SETTAGGI RECYCLER VIEW
        mAdapter = new com.kubix.myjobwallet.entrate.CustomAdapter(entrateList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {

            //EVENTI DI CLICK DEL RECYCLER
            @Override
            public void onClick(View view, int position) {
                //MODIFICA ENTRATA
                final Entrate movie = entrateList.get(position);
                vecchiaData = movie.getDataEntrata();
                vecchioTitolo = movie.getTitolo();
                vecchiaCifra = movie.getEntrata();
                vecchioTag = movie.getCategoria();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EntrateActivity.this);
                LayoutInflater inflater = EntrateActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_modifica_entrate, null);
                dialogBuilder.setView(dialogView);
                testoModifica = (EditText) dialogView.findViewById(R.id.testoModificaCifraEntrata);
                buttonModifica = (Button) dialogView.findViewById(R.id.bottoneModificaCifraEntrata);
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }

            @Override
            public void onLongClick(View view, final int position) {
                //ELIMINA ENTRATA
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(EntrateActivity.this);
                builder.setTitle(R.string.elimina)
                        .setMessage(R.string.elimina_veramente)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // ELIMINA
                                final Entrate movie = entrateList.get(position);
                                MainActivity.db.execSQL("DELETE FROM Entrate WHERE Titolo = '"+movie.getTitolo()+"' AND Cifra = '"+movie.getEntrata()+"' AND Ora = '"+movie.getPromemoria()+"' AND Data = '"+movie.getDataEntrata()+"' AND Categoria = '"+movie.getCategoria()+"'");
                                entrateList.remove(position);
                                mAdapter.notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // NOTHING
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }));

        //TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEntrate);
        setTitle(R.string.toolbarEntrate);
        toolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoBianco));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //ADMOB NATIVA
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
                BottomSheetDialogFragment bottomSheetDialogFragment = new BtnSheetEntrateFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                break;
            case R.id.fab2:
                animateFAB();
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

    }

    @Override
    public void onRestart(){
        super.onRestart();
        caricaEntrate();
    }

    public void caricaEntrate(){
        //CARICA ENTRATE IN RECYCLER
        try {
            entrateList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Entrate ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloEntrata=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraEntrata=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaEntrata=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataEntrata= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Entrate entrate = new Entrate (campoTitoloEntrata, campoCifraEntrata, campoPromemoria,campoDataEntrata, campoCategoriaEntrata, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        entrateList.add(entrate);
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
    public void clickBottomSheetEntrateBonifico(View v){
        //CARICA ENTRATE BONIFICO IN RECYCLER
        try {
            entrateList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Entrate WHERE Categoria = 'Bonifico' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloEntrata=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraEntrata=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaEntrata=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataEntrata= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Entrate entrate = new Entrate (campoTitoloEntrata, campoCifraEntrata, campoPromemoria,campoDataEntrata, campoCategoriaEntrata, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        entrateList.add(entrate);
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

    public void clickBottomSheetEntrateAssegno(View v){
        //CARICA ENTRATE ASSEGNO IN RECYCLER
        try {
            entrateList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Entrate WHERE Categoria = 'Assegno' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloEntrata=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraEntrata=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaEntrata=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataEntrata= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Entrate entrate = new Entrate (campoTitoloEntrata, campoCifraEntrata, campoPromemoria,campoDataEntrata, campoCategoriaEntrata, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        entrateList.add(entrate);
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

    public void clickBottomSheetEntrateVincite(View v){
        //CARICA ENTRATE VINCITA IN RECYCLER
        try {
            entrateList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Entrate WHERE Categoria = 'Vincita' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloEntrata=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraEntrata=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaEntrata=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataEntrata= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Entrate entrate = new Entrate (campoTitoloEntrata, campoCifraEntrata, campoPromemoria,campoDataEntrata, campoCategoriaEntrata, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        entrateList.add(entrate);
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

    public void clickBottomSheetEntrateRegalo(View v){
        //CARICA ENTRATE REGALO IN RECYCLER
        try {
            entrateList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Entrate WHERE Categoria = 'Regalo' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloEntrata=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraEntrata=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaEntrata=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataEntrata= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Entrate entrate = new Entrate (campoTitoloEntrata, campoCifraEntrata, campoPromemoria,campoDataEntrata, campoCategoriaEntrata, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        entrateList.add(entrate);
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

    public void clickBottomSheetEntrateBeni(View v){
        //CARICA ENTRATE BENI IN RECYCLER
        try {
            entrateList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Entrate WHERE Categoria = 'Beni' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloEntrata=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraEntrata=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaEntrata=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataEntrata= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Entrate entrate = new Entrate (campoTitoloEntrata, campoCifraEntrata, campoPromemoria,campoDataEntrata, campoCategoriaEntrata, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        entrateList.add(entrate);
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

    public void clickBottomSheetEntrateRimborsi(View v){
        //CARICA ENTRATE RIMBORSI IN RECYCLER
        try {
            entrateList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Entrate WHERE Categoria = 'Rimborsi' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloEntrata=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraEntrata=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaEntrata=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataEntrata= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Entrate entrate = new Entrate (campoTitoloEntrata, campoCifraEntrata, campoPromemoria,campoDataEntrata, campoCategoriaEntrata, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        entrateList.add(entrate);
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

    public void clickBottomSheetEntrateVendita(View v){
        //CARICA ENTRATE VENDITA IN RECYCLER
        try {
            entrateList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Entrate WHERE Categoria = 'Vendite' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloEntrata=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraEntrata=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaEntrata=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataEntrata= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Entrate entrate = new Entrate (campoTitoloEntrata, campoCifraEntrata, campoPromemoria,campoDataEntrata, campoCategoriaEntrata, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        entrateList.add(entrate);
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

    public void clickBottomSheetEntrateAltro(View v){
        //CARICA ENTRATE ALTRO IN RECYCLER
        try {
            entrateList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Entrate WHERE Categoria = 'Altro' ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloEntrata=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraEntrata=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaEntrata=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataEntrata= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Entrate entrate = new Entrate (campoTitoloEntrata, campoCifraEntrata, campoPromemoria,campoDataEntrata, campoCategoriaEntrata, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        entrateList.add(entrate);
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

    public void clickBottomSheetEntrateTutteLeCategorie(View v){
        //CARICA TUTTE LE ENTRATE IN RECYCLER
        try {
            entrateList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Entrate ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitoloEntrata=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCifraEntrata=cr.getString(cr.getColumnIndex("Cifra"));
                        String campoCategoriaEntrata=cr.getString(cr.getColumnIndex("Categoria"));
                        String campoPromemoria = cr.getString(cr.getColumnIndex("Ora"));
                        String campoDataEntrata= cr.getString(cr.getColumnIndex("Data"));
                        String campoTestoGiorno = cr.getString(cr.getColumnIndex("GiornoTesto"));
                        String campoNumeroGiorno = cr.getString(cr.getColumnIndex("GiornoNumero"));
                        String campoNumeroMese = cr.getString(cr.getColumnIndex("MeseNumero"));
                        String campoNumeroAnno = cr.getString(cr.getColumnIndex("AnnoNumero"));
                        Entrate entrate = new Entrate (campoTitoloEntrata, campoCifraEntrata, campoPromemoria,campoDataEntrata, campoCategoriaEntrata, campoTestoGiorno, campoNumeroGiorno, campoNumeroMese, campoNumeroAnno);
                        entrateList.add(entrate);
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

    public void modificaEntrata(View v){
        if (! testoModifica.getText().toString().equals("")){
            MainActivity.db.execSQL("UPDATE Entrate SET Cifra = '"+testoModifica.getText().toString()+"' WHERE Data = '"+vecchiaData+"' AND Titolo = '"+vecchioTitolo+"' AND Cifra = '"+vecchiaCifra+"' AND Categoria = '"+vecchioTag+"'");
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

