package com.kubix.myjobwallet.calendario;

import android.content.DialogInterface;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;
import com.kubix.myjobwallet.setting.ClsSettings;
import com.kubix.myjobwallet.utility.VariabiliGlobali;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TurniActivity extends AppCompatActivity  {

    //TODO ADMOB NATIVA
    private static String LOG_TAG = "EXAMPLE";
    private AdView mAdView;
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
        ClsSettings settings = new ClsSettings(getBaseContext());
        if (settings.get_temadark()) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turni);

        // AdMob
        MobileAds.initialize(this, "ca-app-pub-9460579775308491~5760945149");
        mAdView = findViewById(R.id.ad_view_turni);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        //TODO TOOLBAR
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTurni);
        setTitle(R.string.toolbarTurni);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                vecchioNumeroGiorno = movie.getNumeroGiorno();
                vecchioNumeroMese = movie.getMese();
                vecchioNumeroAnno = movie.getAnno();
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
                        .setIcon(R.drawable.ic_dialog_alert)
                        .show();
            }
        }));

        //CONTROLLO PER LA VISUALIZZAZIONE DELL'ADD VIEW
        if (VariabiliGlobali.statoPremium.equals("SI")){
            mAdView.setVisibility(View.GONE);
        }

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

    String vecchioNumeroGiorno;
    String vecchioNumeroMese;
    String vecchioNumeroAnno;
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

    String resaCalcoloOrdinarie;
    String resaCalcoloStraordinarie;
    public void aggiornaTurnoEsistente(View v) {
        if(! oraEntrataPerModificaTurno.getText().toString().equals("") && ! oraUscitaPerModificaTurno.getText().toString().equals("")){
            try {
                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                fmt.setLenient(false);

                // CONVERTI IN ORARIO.
                Date d1 = fmt.parse(oraEntrataPerModificaTurno.getText().toString());
                Date d2 = fmt.parse(oraUscitaPerModificaTurno.getText().toString());

                // CALCOLA LA DIFFERENZA IN MILLISECONDI.
                long millisDiff = d2.getTime() - d1.getTime();

                // CALCOLA SU GIONRI/ORE/MINUTI/SECONDI.
                int seconds = (int) (millisDiff / 1000 % 60);
                int minutes = (int) (millisDiff / 60000 % 60);
                int hours = (int) (millisDiff / 3600000 % 24);
                int days = (int) (millisDiff / 86400000);

                if (hours < 0 && minutes <0 || hours <0 && minutes >=0) {
                    hours = (int) (millisDiff / 3600000 % 24 + 23);
                    minutes = (int) (millisDiff / 60000 % 60 + 60);

                }

                if (minutes > 59){
                    minutes = minutes - 60;
                    hours = hours + 1;
                }

                int oreOrdinarie = VariabiliGlobali.oreOrdinarie;

                if(hours > oreOrdinarie){
                    resaCalcoloOrdinarie = hours + getString(R.string.ore) +minutes + getString(R.string.minuti);
                    resaCalcoloStraordinarie = String.valueOf(Integer.valueOf(hours - oreOrdinarie)) + " Ore " + minutes + " Minuti ";
                }else{
                    resaCalcoloOrdinarie = hours + " Ore "+ minutes + " Minuti ";
                    resaCalcoloStraordinarie = "0";
                }

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            //MODIFICA TURNO
            MainActivity.db.execSQL("UPDATE Turni SET oraEntrata = '"+oraEntrataPerModificaTurno.getText().toString()+"', oraUscita = '"+oraUscitaPerModificaTurno.getText().toString()+"', Ordinarie = '"+resaCalcoloOrdinarie+"', Straordinarie = '"+resaCalcoloStraordinarie+"' WHERE numeroGiorno = '"+vecchioNumeroGiorno+"' AND mese = '"+vecchioNumeroMese+"' AND anno  = '"+vecchioNumeroAnno+"'");
            Toast.makeText(this, "MODIFICA TURNO EFFETTUATA CON SUCCESSO", Toast.LENGTH_SHORT).show();
            finish();

        }else{
            Snackbar.make(v, R.string.compila_tutti_dati, Snackbar.LENGTH_LONG).show();
        }
    }

    public void trasformaInRiposo (View v){
        //TRASFORMA TURNO IN RIPOSO
        MainActivity.db.execSQL("UPDATE Turni SET oraEntrata = 'RIPOSO', oraUscita = 'RIPOSO', Ordinarie = 'RIPOSO', Straordinarie = 'RIPOSO' WHERE numeroGiorno = '"+vecchioNumeroGiorno+"' AND mese = '"+vecchioNumeroMese+"' AND anno  = '"+vecchioNumeroAnno+"'");
        Toast.makeText(this, "MODIFICA TURNO EFFETTUATA CON SUCCESSO", Toast.LENGTH_SHORT).show();
        finish();
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
