package com.kubix.myjobwallet.spese;

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

public class SpeseAggiungiActivity extends AppCompatActivity {

    //TODO INDICIZZA OGGETTI
    Spinner TagSpese;

    EditText titoloSpesa;
    EditText cifraSpesa;
    EditText dataSpesa;
    EditText oraSpesa;

    //TODO VARIABILI DI CLASSE
    private String[] arraySpinnerSpesa;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spese_aggiungi);

        //TODO SPINNER
        TagSpese = (Spinner) findViewById(R.id.tagSpinner);


        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSpeseAggiungi);
        setTitle(R.string.toolbarSpeseAggiungi);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        setSupportActionBar(toolbar);

        this.arraySpinnerSpesa = new String[]{
                getString(R.string.casa),getString(R.string.trasporti),getString(R.string.auto),getString(R.string.carburante),
                getString(R.string.bollette),getString(R.string.shopping),getString(R.string.ciboBevande),getString(R.string.svago),getString(R.string.viaggi),getString(R.string.altro)
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerSpesa);
        TagSpese.setAdapter(adapter);


        titoloSpesa = (EditText) findViewById(R.id.testoTitoloSpesa);
        cifraSpesa = (EditText) findViewById(R.id.testoPrezzoSpesa);
        dataSpesa = (EditText) findViewById(R.id.testoDataSpesa);
        oraSpesa = (EditText) findViewById(R.id.testoPromemoriaSpesa);
    }

    public void inserisciUscitaMonetaria(View v){
        if(! titoloSpesa.getText().toString().equals("") && ! cifraSpesa.getText().toString().equals("") && ! dataSpesa.getText().toString().equals("") && ! oraSpesa.getText().toString().equals("")){
            MainActivity.db.execSQL("INSERT INTO Uscite (Data, Titolo, Cifra) VALUES ('"+dataSpesa.getText().toString()+"', '"+titoloSpesa.getText().toString()+"', '"+cifraSpesa.getText().toString()+"')");
            Toast.makeText(this, getString(R.string.spesaAggiunta), Toast.LENGTH_SHORT).show();
            titoloSpesa.setText("");
            cifraSpesa.setText("");
            dataSpesa.setText("");
            oraSpesa.setText("");

            //EVENTO CALCOLO PER HOME PAGE SPESE

        }else{
            Toast.makeText(this, getString(R.string.compilareDatiSpesa), Toast.LENGTH_SHORT).show();
        }
    }
}
