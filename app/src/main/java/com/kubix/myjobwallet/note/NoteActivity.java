package com.kubix.myjobwallet.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;
import com.kubix.myjobwallet.setting.ClsSettings;
import com.kubix.myjobwallet.utility.VariabiliGlobali;

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


    //FAB DISSOLVENZA
    private FloatingActionButton mFloatingActionButton;

    //INDICIZZA OGGETTI PER MODIFICA
    EditText titoloModifica;
    EditText corpoModifica;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ClsSettings settings = new ClsSettings(getBaseContext());
        if (settings.get_temadark()) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //RECYCLER VIEW
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNote);
        setTitle(R.string.toolbarNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        //FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingAddNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity (new Intent(NoteActivity.this,NoteAggiungiActivity.class));

            }
        });

        //SETTAGGI RECYCLER VIEW
        mAdapter = new CustomAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {

            //LISTNER RECYCLER VIEW
            @Override
            public void onClick(View view, int position) {
                //MODIFICA NOTA
                final Note movie = noteList.get(position);
                vecchioTitolo = movie.getTitolo();
                vecchiaNota = movie.getNote();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NoteActivity.this);
                LayoutInflater inflater = NoteActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_modifica_note, null);
                dialogBuilder.setView(dialogView);
                titoloModifica = (EditText) dialogView.findViewById(R.id.testoModificaTitoloNote);
                corpoModifica = (EditText) dialogView.findViewById(R.id.testoModificaCorpoNote);
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }

            @Override
            public void onLongClick(View view, final int position) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(NoteActivity.this);
                builder.setTitle(R.string.elimina)
                        .setMessage(R.string.elimina_veramente)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // ELIMINAZIONE
                                final Note movie = noteList.get(position);
                                MainActivity.db.execSQL("DELETE FROM Note WHERE Titolo = '"+movie.getTitolo()+"' AND Nota = '"+movie.getNote()+"'");
                                noteList.remove(position);
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

        //CONTROLLO PER LA VISUALIZZAZIONE DELL'INTERSTITIAL
        if(VariabiliGlobali.statoPremium.equals("SI")){
            //NOTHING
        }else{
            //GESTIONE INTERSTITIAL
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.adsInterstitial));
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            mInterstitialAd.loadAd(adRequest);
            mInterstitialAd.setAdListener(new AdListener() {

                public void onAdLoaded() {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                }

                public void onAdClosed(){

                }
            });
        }

        caricaNote();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        caricaNote();
    }

    public void caricaNote(){
        //CARICA NOTE IN LISTA
        try {
            noteList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Note ORDER BY Titolo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoTitolo=cr.getString(cr.getColumnIndex("Titolo"));
                        String campoCorpo=cr.getString(cr.getColumnIndex("Nota"));
                        Note note = new Note (campoTitolo, campoCorpo);
                        noteList.add(note);
                        mAdapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }else
                    Snackbar.make(mFloatingActionButton, R.string.nessuna_modifica, Snackbar.LENGTH_LONG).show();
            }
            cr.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    String vecchioTitolo;
    String vecchiaNota;

    public void modificaNota(View v){
        if (! titoloModifica.getText().toString().equals("") && ! corpoModifica.getText().toString().equals("")){
            MainActivity.db.execSQL("UPDATE Note SET Titolo = '"+titoloModifica.getText().toString()+"', Nota = '"+corpoModifica.getText().toString()+"' WHERE Titolo = '"+vecchioTitolo+"' AND Nota = '"+vecchiaNota+"'");
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
