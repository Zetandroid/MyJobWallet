package com.kubix.myjobwallet.calendario;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;
import com.kubix.myjobwallet.entrate.Entrate;
import com.kubix.myjobwallet.entrate.EntrateActivity;

import java.util.ArrayList;
import java.util.List;

public class TurniActivity extends AppCompatActivity  {

    //TODO ADMOB NATIVA
    private static String LOG_TAG = "EXAMPLE";
    NativeExpressAdView mAdView;
    VideoController mVideoController;

    // LISTA RECYCLER VIEW
    private List<Turni> turniList = new ArrayList<>();
    private RecyclerView recyclerView;
    private com.kubix.myjobwallet.calendario.CustomAdapter mAdapter;

    //OGGETTI PER MODIFICA
    EditText oraEntrataPerModificaTurno;
    EditText oraUscitaPerModificaTurno;
    TimePicker selezionaOrariPerModificaTurno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turni);

        //TODO ADMOB NATIVA
        mAdView = (NativeExpressAdView) findViewById(R.id.adViewTurni);
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

        //TODO TOOLBAR
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTurni);
        setTitle(R.string.toolbarTurni);
        toolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoBianco));
        setSupportActionBar(toolbar);

        //INDICIZZA
        recyclerView = (RecyclerView) findViewById(R.id.listaTurni);

        //SETTAGGI RECYCLER VIEW
        mAdapter = new com.kubix.myjobwallet.calendario.CustomAdapter(turniList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new com.kubix.myjobwallet.entrate.RecyclerTouchListener(getApplicationContext(), recyclerView, new com.kubix.myjobwallet.entrate.RecyclerTouchListener.ClickListener() {

            //EVENTI DI CLICK DEL RECYCLER
            @Override
            public void onClick(View view, int position) {
                //MODIFICA TURNO
                final Turni movie = turniList.get(position);
                vecchiaData = movie.getNumeroGiorno() + " " + movie.getMese() + " " + movie.getAnno();
                vecchiaEntrata = movie.getOraEntrata();
                vecchiaUscita = movie.getOraUscita();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TurniActivity.this);
                LayoutInflater inflater = TurniActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_modifica_turni, null);
                dialogBuilder.setView(dialogView);
                oraEntrataPerModificaTurno = (EditText) dialogView.findViewById(R.id.oraEntrataPerModificaTurno);
                oraUscitaPerModificaTurno = (EditText) dialogView.findViewById(R.id.oraUscitaPerModificaTurno);
                selezionaOrariPerModificaTurno = (TimePicker) dialogView.findViewById(R.id.orariTurnoPerModifica);
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }

            @Override
            public void onLongClick(View view, final int position) {

                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(TurniActivity.this);
                builder.setTitle(R.string.elimina)
                        .setMessage("VUOI VERAMENTE ELIMINARE?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // ELIMINA
                                final Turni movie = turniList.get(position);
                                MainActivity.db.execSQL("DELETE FROM Turni WHERE giornoSettimana = '"+movie.getGiornoSettimana()+"' AND numeroGiorno = '"+movie.getNumeroGiorno()+"' AND mese = '"+movie.getMese()+"' AND anno = '"+movie.getAnno()+"'");
                                turniList.remove(position);
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

        caricaTurni();

    }

    public void caricaTurni(){
        //CARICA TURNI IN RECYCLER
        try {
            turniList.clear();
            mAdapter.notifyDataSetChanged();
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM Turni",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        String campoGiornoSettimana=cr.getString(cr.getColumnIndex("giornoSettimana"));
                        String campoNumeroGiorno=cr.getString(cr.getColumnIndex("numeroGiorno"));
                        String campoMese=cr.getString(cr.getColumnIndex("mese"));
                        String campoAnno = cr.getString(cr.getColumnIndex("anno"));
                        String campoOraEntrata= cr.getString(cr.getColumnIndex("oraEntrata"));
                        String campoOraUscita= cr.getString(cr.getColumnIndex("oraUscita"));
                        String campoOrdinarie= cr.getString(cr.getColumnIndex("Ordinarie"));
                        String campoStraordinarie= cr.getString(cr.getColumnIndex("Straordinarie"));
                        Turni turni = new Turni (campoGiornoSettimana, campoNumeroGiorno, campoMese,campoAnno, campoOraEntrata, campoOraUscita, campoOrdinarie, campoStraordinarie);
                        turniList.add(turni);
                        mAdapter.notifyDataSetChanged();
                    }while (cr.moveToNext());
                }else
                    Snackbar.make(mAdView, R.string.dati_non_inseriti, Snackbar.LENGTH_LONG).show();
            }
            cr.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    String vecchiaData;
    String vecchiaEntrata;
    String vecchiaUscita;

    public void modificaEntrata(View v){
        if (! oraEntrataPerModificaTurno.getText().toString().equals("") && ! oraUscitaPerModificaTurno.getText().toString().equals("")){
            //MainActivity.db.execSQL("UPDATE Entrate SET Cifra = '"+testoModifica.getText().toString()+"' WHERE Data = '"+vecchiaData+"' AND Titolo = '"+vecchioTitolo+"' AND Cifra = '"+vecchiaCifra+"' AND Categoria = '"+vecchioTag+"'");
            //Toast.makeText(this, R.string.dati_inseriti_successo, Toast.LENGTH_SHORT).show();
            //finish();
        }else{
            Toast.makeText(this, R.string.nessuna_modifica, Toast.LENGTH_SHORT).show();
        }
    }

    public void settaEntrataPerModifica(View v){

                int selectedHour = selezionaOrariPerModificaTurno.getHour();
                int selectedMinute = selezionaOrariPerModificaTurno.getMinute();
                String selezioneOra;
                String selezioneMinuti;

                if (String.valueOf(selectedHour).length() == 1 && String.valueOf(selectedMinute).length() == 1){
                    selezioneOra = "0" + selectedHour;
                    selezioneMinuti = "0" +selectedMinute;
                    oraEntrataPerModificaTurno.setText(selezioneOra + ":" + selezioneMinuti);
                }else if(String.valueOf(selectedHour).length() == 1){
                    selezioneOra = "0" + selectedHour;
                    selezioneMinuti = String.valueOf(selectedMinute);
                    oraEntrataPerModificaTurno.setText(selezioneOra + ":" + selezioneMinuti);
                }else if (String.valueOf(selectedMinute).length() == 1){
                    selezioneOra = String.valueOf(selectedHour);
                    selezioneMinuti = "0" + selectedMinute;
                    oraEntrataPerModificaTurno.setText(selezioneOra + ":" + selezioneMinuti);
                }else{
                    selezioneOra = String.valueOf(selectedHour);
                    selezioneMinuti = String.valueOf(selectedMinute);
                    oraEntrataPerModificaTurno.setText(selezioneOra + ":" + selezioneMinuti);
                }

            }

    public void settaUscitaPerModifica(View v){

        int selectedHour = selezionaOrariPerModificaTurno.getHour();
        int selectedMinute = selezionaOrariPerModificaTurno.getMinute();
        String selezioneOra;
        String selezioneMinuti;

        if (String.valueOf(selectedHour).length() == 1 && String.valueOf(selectedMinute).length() == 1){
            selezioneOra = "0" + selectedHour;
            selezioneMinuti = "0" +selectedMinute;
            oraUscitaPerModificaTurno.setText(selezioneOra + ":" + selezioneMinuti);
        }else if(String.valueOf(selectedHour).length() == 1){
            selezioneOra = "0" + selectedHour;
            selezioneMinuti = String.valueOf(selectedMinute);
            oraUscitaPerModificaTurno.setText(selezioneOra + ":" + selezioneMinuti);
        }else if (String.valueOf(selectedMinute).length() == 1){
            selezioneOra = String.valueOf(selectedHour);
            selezioneMinuti = "0" + selectedMinute;
            oraUscitaPerModificaTurno.setText(selezioneOra + ":" + selezioneMinuti);
        }else{
            selezioneOra = String.valueOf(selectedHour);
            selezioneMinuti = String.valueOf(selectedMinute);
            oraUscitaPerModificaTurno.setText(selezioneOra + ":" + selezioneMinuti);
        }

    }

}
