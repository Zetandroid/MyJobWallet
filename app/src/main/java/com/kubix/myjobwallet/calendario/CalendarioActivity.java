package com.kubix.myjobwallet.calendario;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;
import com.kubix.myjobwallet.setting.ClsSettings;
import com.kubix.myjobwallet.utility.VariabiliGlobali;

import java.util.Date;

public class CalendarioActivity extends AppCompatActivity {

    //DICHIARAZIONE OGGETTI
    EditText inserisciEntrata;
    EditText inserisciUscita;
    CalendarView dataTurno;

    //VARIABILI DI CLASSE
    String giornoTestuale;
    String giornoTestualeAbbreviato;
    int numeroGiorno;
    int numeroMese;
    int numeroAnno;

    //INTERSTITIAL
    private String TAG = CalendarioActivity.class.getSimpleName();
    InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ClsSettings settings = new ClsSettings(getBaseContext());
        if (settings.get_temadark()) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        //TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOrologio);
        setTitle(R.string.toolbarOre);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //INDICIZZA OGETTI
        dataTurno = (CalendarView) findViewById(R.id.calendarView);
        inserisciEntrata = (EditText) findViewById(R.id.editTextOraEntrata);
        inserisciUscita = (EditText) findViewById(R.id.editTextOraUscita);

        //APRI DATABASE
        MainActivity.db = this.openOrCreateDatabase("Turnazioni.db", MODE_PRIVATE, null);

        //CONTROLLO GESTIONE INTERSTITIAL
        if (VariabiliGlobali.statoPremium.equals("SI")){

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

        //OTTIENI DATA ODIERNA
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        //PASSA IN VARIABILI DI CLASSE
        numeroGiorno = day;
        numeroMese = month;
        numeroAnno = year;

        //OTTIENI GIORNO DA DATA
        try{
            SimpleDateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inFormat.parse(day+"/"+month+"/"+year);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String goal = outFormat.format(date);
            giornoTestuale = goal;

            //ABBREVIA PER IMPATTO GRAFICO
            if(giornoTestuale.equals("domenica")){
                giornoTestualeAbbreviato = "DOM";
            }else if(giornoTestuale.equals("lunedì")){
                giornoTestualeAbbreviato= "LUN";
            }else if(giornoTestuale.equals("martedì")){
                giornoTestualeAbbreviato = "MAR";
            }else if (giornoTestuale.equals("mercoledì")){
                giornoTestualeAbbreviato = "MER";
            }else if(giornoTestuale.equals("giovedì")){
                giornoTestualeAbbreviato = "GIO";
            }else if(giornoTestuale.equals("venerdì")){
                giornoTestualeAbbreviato = "VEN";
            }else if (giornoTestuale.equals("sabato")){
                giornoTestualeAbbreviato = "SAB";
            }

            Snackbar.make(inserisciUscita, giornoTestuale, Snackbar.LENGTH_LONG).show();

        }catch (Exception e){
            Toast.makeText(CalendarioActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //LISTNER CLICK CALENDARIO
        dataTurno.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                int m = month + 1;
                int y = year;

                //PASSA IN VARIABILI DI CLASSE
                numeroGiorno = d;
                numeroMese = m;
                numeroAnno = y;

                //OTTIENI GIORNO DA DATA
                try{
                    SimpleDateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = inFormat.parse(d+"/"+m+"/"+y);
                    SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                    String goal = outFormat.format(date);
                    giornoTestuale = goal;

                    //ABBREVIA PER FOTTUTO IMPATTO GRAFICO
                    if(giornoTestuale.equals("domenica")){
                        giornoTestualeAbbreviato = "DOM";
                    }else if(giornoTestuale.equals("lunedì")){
                        giornoTestualeAbbreviato= "LUN";
                    }else if(giornoTestuale.equals("martedì")){
                        giornoTestualeAbbreviato = "MAR";
                    }else if (giornoTestuale.equals("mercoledì")){
                        giornoTestualeAbbreviato = "MER";
                    }else if(giornoTestuale.equals("giovedì")){
                        giornoTestualeAbbreviato = "GIO";
                    }else if(giornoTestuale.equals("venerdì")){
                        giornoTestualeAbbreviato = "VEN";
                    }else if (giornoTestuale.equals("sabato")){
                        giornoTestualeAbbreviato = "SAB";
                    }

                    Snackbar.make(inserisciUscita, giornoTestuale, Snackbar.LENGTH_LONG).show();

                }catch (Exception e){
                    Toast.makeText(CalendarioActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void dialogTimePickerClickEntrata(View v){

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CalendarioActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String selezioneOra;
                String selezioneMinuti;

                if (String.valueOf(selectedHour).length() == 1 && String.valueOf(selectedMinute).length() == 1){
                    selezioneOra = "0" + selectedHour;
                    selezioneMinuti = "0" +selectedMinute;
                    inserisciEntrata.setText(selezioneOra + ":" + selezioneMinuti);
                }else if(String.valueOf(selectedHour).length() == 1){
                    selezioneOra = "0" + selectedHour;
                    selezioneMinuti = String.valueOf(selectedMinute);
                    inserisciEntrata.setText(selezioneOra + ":" + selezioneMinuti);
                }else if (String.valueOf(selectedMinute).length() == 1){
                    selezioneOra = String.valueOf(selectedHour);
                    selezioneMinuti = "0" + selectedMinute;
                    inserisciEntrata.setText(selezioneOra + ":" + selezioneMinuti);
                }else{
                    selezioneOra = String.valueOf(selectedHour);
                    selezioneMinuti = String.valueOf(selectedMinute);
                    inserisciEntrata.setText(selezioneOra + ":" + selezioneMinuti);
                }

            }

        }, hour, minute, true);
        mTimePicker.setTitle(getString(R.string.imposta_entrata));
        mTimePicker.show();
    }

    public void inserisciTurno(View v){

        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        int days = 0;

        //CALCOLI FINALI
        String resaCalcoloOrdinarie = null;
        String resaCalcoloStraordinarie = null;

        if(! inserisciEntrata.getText().toString().equals("") && ! inserisciUscita.getText().toString().equals("")){
            try {
                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                fmt.setLenient(false);

                // CONVERTI IN ORARIO.
                Date d1 = fmt.parse(inserisciEntrata.getText().toString());
                Date d2 = fmt.parse(inserisciUscita.getText().toString());

                // CALCOLA LA DIFFERENZA IN MILLISECONDI.
                long millisDiff = d2.getTime() - d1.getTime();

                // CALCOLA SU GIONRI/ORE/MINUTI/SECONDI.
                seconds = (int) (millisDiff / 1000 % 60);
                minutes = (int) (millisDiff / 60000 % 60);
                hours = (int) (millisDiff / 3600000 % 24);
                days = (int) (millisDiff / 86400000);

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

            //INSERISCI TURNO
            MainActivity.db.execSQL("INSERT INTO Turni (giornoSettimana, numeroGiorno, mese, anno, oraEntrata, oraUscita, Ordinarie, Straordinarie) VALUES ('"+giornoTestualeAbbreviato+"', '"+numeroGiorno+"', '"+numeroMese+"', '"+numeroAnno+"', '"+inserisciEntrata.getText().toString()+"', '"+inserisciUscita.getText().toString()+"', '"+resaCalcoloOrdinarie+"', '"+resaCalcoloStraordinarie+"')");
            Double pagaTurnoOrdinaria = VariabiliGlobali.nettoOrario * hours;
            int oreStraordinarie = hours - VariabiliGlobali.oreOrdinarie;
            Double pagaTurnoStraordinaria = VariabiliGlobali.nettoStraordinario * oreStraordinarie;
            MainActivity.db.execSQL("INSERT INTO CalcoloStipendio (Importo) VALUES ('"+pagaTurnoOrdinaria + pagaTurnoStraordinaria+"')");
            Snackbar.make(v, R.string.dati_inseriti_successo, Snackbar.LENGTH_LONG).show();
            onBackPressed();
            startActivity(new Intent(this, TurniActivity.class));

        }else{
            Snackbar.make(v, R.string.compila_tutti_dati, Snackbar.LENGTH_LONG).show();
        }
    }

    public void inserisciRiposo(View v){
        MainActivity.db.execSQL("INSERT INTO Turni (giornoSettimana, numeroGiorno, mese, anno, oraEntrata, oraUscita, ordinarie, straordinarie) VALUES ('"+giornoTestualeAbbreviato+"', '"+numeroGiorno+"', '"+numeroMese+"', '"+numeroAnno+"', 'RIPOSO', 'RIPOSO', 'RIPOSO', 'RIPOSO')");
        Snackbar.make(v, R.string.dati_inseriti_successo, Snackbar.LENGTH_LONG).show();
        onBackPressed();
        startActivity(new Intent(this, TurniActivity.class));


    }

    public void dialogTimePickerClickUscita(View v){

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(CalendarioActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String selezioneOra;
                String selezioneMinuti;
                if (String.valueOf(selectedHour).length() == 1 && String.valueOf(selectedMinute).length() == 1){
                    selezioneOra = "0" + selectedHour;
                    selezioneMinuti = "0" +selectedMinute;
                    inserisciUscita.setText(selezioneOra + ":" + selezioneMinuti);
                }else if(String.valueOf(selectedHour).length() == 1){
                    selezioneOra = "0" + selectedHour;
                    selezioneMinuti = String.valueOf(selectedMinute);
                    inserisciUscita.setText(selezioneOra + ":" + selezioneMinuti);
                }else if (String.valueOf(selectedMinute).length() == 1){
                    selezioneOra = String.valueOf(selectedHour);
                    selezioneMinuti = "0" + selectedMinute;
                    inserisciUscita.setText(selezioneOra + ":" + selezioneMinuti);
                }else{
                    selezioneOra = String.valueOf(selectedHour);
                    selezioneMinuti = String.valueOf(selectedMinute);
                    inserisciUscita.setText(selezioneOra + ":" + selezioneMinuti);
                }

            }
        }, hour, minute, true);
        mTimePicker.setTitle(getString(R.string.imposta_uscita));
        mTimePicker.show();
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
