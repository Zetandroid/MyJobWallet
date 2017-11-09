package com.kubix.myjobwallet.spese;

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

public class SpeseAggiungiActivity extends AppCompatActivity {

    //INDICIZZA OGGETTI
    Spinner TagSpese;
    EditText titoloSpesa;
    EditText cifraSpesa;
    EditText dataSpesa;
    EditText oraSpesa;

    //VARIABILI DI CLASSE
    private String[] arraySpinnerSpesa;

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
            MainActivity.db.execSQL("INSERT INTO Uscite (Data, Titolo, Cifra, Categoria, Ora) VALUES ('"+dataSpesa.getText().toString()+"', '"+titoloSpesa.getText().toString()+"', '"+cifraSpesa.getText().toString()+"', '"+TagSpese.getSelectedItem().toString()+"', '"+oraSpesa.getText().toString()+"')");
            Snackbar.make(v, getString(R.string.spesaAggiunta), Snackbar.LENGTH_LONG).show();
            titoloSpesa.setText("");
            cifraSpesa.setText("");
            dataSpesa.setText("");
            oraSpesa.setText("");

        }else{
            Snackbar.make(v, getString(R.string.compilareDatiSpesa), Snackbar.LENGTH_LONG).show();
        }
    }
}
