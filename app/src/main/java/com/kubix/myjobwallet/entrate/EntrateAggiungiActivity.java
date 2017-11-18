package com.kubix.myjobwallet.entrate;

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
import com.kubix.myjobwallet.calendario.CalendarioActivity;

import java.util.Date;

public class EntrateAggiungiActivity extends AppCompatActivity {

    //INDICIZZA OGGETTI
    EditText titoloEntrata;
    EditText valoreEntrata;
    Spinner TagEntrate;
    EditText dataEntrata;
    EditText oraEntrata;

    //VARIABILI DI CLASSE
    private String[] arraySpinnerEntrata;
    int numeroGiorno;
    int numeroMese;
    int numeroAnno;
    String giornoTestuale;
    String giornoTestualeAbbreviato;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrate_aggiungi);

        //TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEntrataAggiungi);
        setTitle(R.string.toolbarEntrateAggiungi);
        toolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoBianco));
        setSupportActionBar(toolbar);

        //SPINNER
        TagEntrate = (Spinner) findViewById(R.id.testoTagEntrateSpinner);

        this.arraySpinnerEntrata = new String[]{
                "Bonifico", "Assegno", "Vincita", "Regalo", "Beni",
                "Rimborsi", "Vendite", "Altro"
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerEntrata);
        TagEntrate.setAdapter(adapter);
        titoloEntrata = (EditText) findViewById(R.id.testoTitoloSpesa);
        valoreEntrata = (EditText) findViewById(R.id.testoPrezzoEntrate);
        dataEntrata = (EditText) findViewById(R.id.testoDataEntrate);
        oraEntrata = (EditText) findViewById(R.id.testoPromemoriaEntrata);

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
            Toast.makeText(EntrateAggiungiActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void inserisciEntrataMonetaria(View v){
        if(! titoloEntrata.getText().toString().equals("") && ! valoreEntrata.getText().toString().equals("")){
            MainActivity.db.execSQL("INSERT INTO Entrate (Data, Titolo, Cifra, Categoria, Ora, GiornoTesto, GiornoNumero, MeseNumero, AnnoNumero) VALUES ('"+dataEntrata.getText().toString()+"', '"+titoloEntrata.getText().toString()+"', '"+valoreEntrata.getText().toString()+"', '"+TagEntrate.getSelectedItem().toString()+"', '"+oraEntrata.getText().toString()+"', '"+giornoTestualeAbbreviato+"', '"+String.valueOf(numeroGiorno)+"', '"+String.valueOf(numeroMese)+"', '"+String.valueOf(numeroAnno)+"')");
            Snackbar.make(v, "ENTRATA AGGIUNTA CON SUCCESSO", Snackbar.LENGTH_LONG).show();
            titoloEntrata.setText("");
            valoreEntrata.setText("");
            dataEntrata.setText("");
            oraEntrata.setText("");

        }else{
            Snackbar.make(v, "INSERISCI TITOLO E CIFRA PER AGGIUNGERE L'ENTRATA MONETARIA", Snackbar.LENGTH_LONG).show();
        }
    }
}
