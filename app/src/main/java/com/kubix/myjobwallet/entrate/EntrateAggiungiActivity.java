package com.kubix.myjobwallet.entrate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;

/**
 * Created by mowmo on 15/10/17.
 */

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
    private String[] arraySpinnerPriorita;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrate_aggiungi);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEntrataAggiungi);
        setTitle(R.string.toolbarEntrateAggiungi);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        setSupportActionBar(toolbar);

        //TODO SPINNER
        TagEntrate = (Spinner) findViewById(R.id.testoTagEntrateSpinner);
        Priorita = (Spinner) findViewById(R.id.testoPrioritaEntrateSpinner);

        this.arraySpinnerEntrata = new String[]{
                getString(R.string.bonifico),getString(R.string.assegno),getString(R.string.vincita),getString(R.string.regalo),getString(R.string.beni),
                getString(R.string.rimborsi),getString(R.string.vendite)
        };

        this.arraySpinnerPriorita = new String[]{getString(R.string.bassa),getString(R.string.media),getString(R.string.alta)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerEntrata);
        TagEntrate.setAdapter(adapter);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerPriorita);
        Priorita.setAdapter(adapter1);

        titoloEntrata = (EditText) findViewById(R.id.testoTitoloEntrata);
        valoreEntrata = (EditText) findViewById(R.id.testoPrezzoEntrate);
        dataEntrata = (EditText) findViewById(R.id.testoDataEntrate);
        oraEntrata = (EditText) findViewById(R.id.testoPromemoriaEntrata);
    }

    public void inserisciEntrataMonetaria(View v){
        if(! titoloEntrata.getText().toString().equals("") && ! valoreEntrata.getText().toString().equals("") && ! dataEntrata.getText().toString().equals("") && ! oraEntrata.getText().toString().equals("")){
            MainActivity.db.execSQL("INSERT INTO Entrate (Data, Titolo, Cifra, Tag) VALUES ('"+dataEntrata.getText().toString()+"', '"+titoloEntrata.getText().toString()+"', '"+valoreEntrata.getText().toString()+"', '"+TagEntrate.getSelectedItem().toString()+"')");
            Toast.makeText(this, getString(R.string.entrataAggiunta), Toast.LENGTH_SHORT).show();
            titoloEntrata.setText("");
            valoreEntrata.setText("");
            dataEntrata.setText("");
            oraEntrata.setText("");
        }else{
            Toast.makeText(this, getString(R.string.compilareDatiEntrata), Toast.LENGTH_SHORT).show();
        }
    }
}
