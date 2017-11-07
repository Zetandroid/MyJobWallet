package com.kubix.myjobwallet.entrate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;

public class EntrateAggiungiActivity extends AppCompatActivity {

    //TODO INDICIZZA OGGETTI
    EditText titoloEntrata;
    Spinner Priorita;
    EditText valoreEntrata;
    Spinner TagEntrate;
    EditText dataEntrata;
    EditText oraEntrata;

    //TODO VARIABILI DI CLASSE
    private String[] arraySpinnerEntrata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrate_aggiungi);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEntrataAggiungi);
        setTitle(R.string.toolbarEntrateAggiungi);
        toolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoBianco));
        setSupportActionBar(toolbar);

        //TODO SPINNER
        TagEntrate = (Spinner) findViewById(R.id.testoTagEntrateSpinner);

        this.arraySpinnerEntrata = new String[]{
                getString(R.string.bonifico),getString(R.string.assegno),getString(R.string.vincita),getString(R.string.regalo),getString(R.string.beni),
                getString(R.string.rimborsi),getString(R.string.vendite),getString(R.string.altro)
        };


        titoloEntrata = (EditText) findViewById(R.id.testoTitoloSpesa);
        valoreEntrata = (EditText) findViewById(R.id.testoPrezzoEntrate);
        dataEntrata = (EditText) findViewById(R.id.testoDataEntrate);
        oraEntrata = (EditText) findViewById(R.id.testoPromemoriaEntrata);
    }

    public void inserisciEntrataMonetaria(View v){
        if(! titoloEntrata.getText().toString().equals("") && ! valoreEntrata.getText().toString().equals("") && ! dataEntrata.getText().toString().equals("") && ! oraEntrata.getText().toString().equals("")){
            MainActivity.db.execSQL("INSERT INTO Entrate (Data, Titolo, Cifra, Ora) VALUES ('"+dataEntrata.getText().toString()+"', '"+titoloEntrata.getText().toString()+"', '"+valoreEntrata.getText().toString()+"', '"+oraEntrata.getText().toString()+"')");
            Toast.makeText(this, getString(R.string.entrataAggiunta), Toast.LENGTH_SHORT).show();
            titoloEntrata.setText("");
            valoreEntrata.setText("");
            dataEntrata.setText("");
            oraEntrata.setText("");

            //EVENTO CALCOLO PER HOME PAGE ENTRATE

        }else{
            Toast.makeText(this, getString(R.string.compilareDatiEntrata), Toast.LENGTH_SHORT).show();
        }
    }
}
