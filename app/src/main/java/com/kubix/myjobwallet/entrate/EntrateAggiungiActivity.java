package com.kubix.myjobwallet.entrate;

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

public class EntrateAggiungiActivity extends AppCompatActivity {

    //INDICIZZA OGGETTI
    EditText titoloEntrata;
    EditText valoreEntrata;
    Spinner TagEntrate;
    EditText dataEntrata;
    EditText oraEntrata;

    //VARIABILI DI CLASSE
    private String[] arraySpinnerEntrata;

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
    }

    public void inserisciEntrataMonetaria(View v){
        if(! titoloEntrata.getText().toString().equals("") && ! valoreEntrata.getText().toString().equals("") && ! dataEntrata.getText().toString().equals("") && ! oraEntrata.getText().toString().equals("")){
            MainActivity.db.execSQL("INSERT INTO Entrate (Data, Titolo, Cifra, Categoria, Ora) VALUES ('"+dataEntrata.getText().toString()+"', '"+titoloEntrata.getText().toString()+"', '"+valoreEntrata.getText().toString()+"', '"+TagEntrate.getSelectedItem().toString()+"', '"+oraEntrata.getText().toString()+"')");
            Snackbar.make(v, "ENTRATA AGGIUNTA CON SUCCESSO", Snackbar.LENGTH_LONG).show();
            titoloEntrata.setText("");
            valoreEntrata.setText("");
            dataEntrata.setText("");
            oraEntrata.setText("");

        }else{
            Snackbar.make(v, "COMPILA TUTI I DATI PER INSERIRE UN ENTRATA", Snackbar.LENGTH_LONG).show();
        }
    }
}
