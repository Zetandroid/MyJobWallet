package com.kubix.myjobwallet.spese;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;

import java.util.Date;

public class SpeseAggiungiActivity extends AppCompatActivity {

    //INDICIZZA OGGETTI
    Spinner TagSpese;
    EditText titoloSpesa;
    EditText cifraSpesa;
    EditText dataSpesa;
    EditText oraSpesa;

    //VARIABILI DI CLASSE
    private String[] arraySpinnerSpesa;
    int numeroGiorno;
    int numeroMese;
    int numeroAnno;
    String giornoTestuale;
    String giornoTestualeAbbreviato;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spese_aggiungi);

        //SPINNER
        TagSpese = (Spinner) findViewById(R.id.tagSpinner);


        //TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSpeseAggiungi);
        setTitle(R.string.toolbarSpeseAggiungi);
        toolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoBianco));
        setSupportActionBar(toolbar);

        this.arraySpinnerSpesa = new String[]{
                getString(R.string.bottomSheet_casa),getString(R.string.bottomSheet_trasporti),getString(R.string.bottomSheet_auto),getString(R.string.bottomSheet_carburante),
               getString(R.string.bottomSheet_bollette),getString(R.string.bottomSheet_shopping),getString(R.string.bottomSheet_cibo),
                getString(R.string.bottomSheet_svago),getString(R.string.bottomSheet_viaggi),getString(R.string.bottomSheet_altro)
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerSpesa);
        TagSpese.setAdapter(adapter);


        titoloSpesa = (EditText) findViewById(R.id.testoTitoloSpesa);
        cifraSpesa = (EditText) findViewById(R.id.testoPrezzoSpesa);
        dataSpesa = (EditText) findViewById(R.id.testoDataSpesa);
        oraSpesa = (EditText) findViewById(R.id.testoPromemoriaSpesa);

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


        }catch (Exception e){
            Toast.makeText(SpeseAggiungiActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void inserisciUscitaMonetaria(View v){
        if(! titoloSpesa.getText().toString().equals("") && ! cifraSpesa.getText().toString().equals("")){
            MainActivity.db.execSQL("INSERT INTO Uscite (Data, Titolo, Cifra, Categoria, Ora, GiornoTesto, GiornoNumero, MeseNumero, AnnoNumero) VALUES ('"+dataSpesa.getText().toString()+"', '"+titoloSpesa.getText().toString()+"', '"+cifraSpesa.getText().toString()+"', '"+TagSpese.getSelectedItem().toString()+"', '"+oraSpesa.getText().toString()+"', '"+giornoTestualeAbbreviato+"', '"+String.valueOf(numeroGiorno)+"', '"+String.valueOf(numeroMese)+"', '"+String.valueOf(numeroAnno)+"')");
            Snackbar.make(v, R.string.dati_inseriti_successo, Snackbar.LENGTH_LONG).show();
            titoloSpesa.setText("");
            cifraSpesa.setText("");
            dataSpesa.setText("");
            oraSpesa.setText("");
            onBackPressed();

        }else{
            Snackbar.make(v, "INSERISCI TITOLO E CIFRA PER AGGIUNGERE LA SPESA MONETARIA", Snackbar.LENGTH_LONG).show();
        }
    }
}
