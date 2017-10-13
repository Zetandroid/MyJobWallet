package com.kubix.myjobwallet;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpeseAggiungiActivity extends AppCompatActivity {

    //TODO INDICIZZA OGGETTI
    Spinner TagSpese;
    Spinner Priorita;

    //TODO VARIABILI DI CLASSE
    private String[] arraySpinnerSpesa;
    private String[] arraySpinnerPriorita;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spese_aggiungi);

        //TODO SPINNER
        TagSpese = (Spinner) findViewById(R.id.tagSpinner);
        Priorita = (Spinner) findViewById(R.id.prioritaSpinner);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSpeseAggiungi);
        setTitle(R.string.toolbarSpeseAggiungi);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        setSupportActionBar(toolbar);

        this.arraySpinnerSpesa = new String[]{
                "Casa","Trasporti","Auto","Carburante","Bollette","Shopping","Cibo & Bevande","Svago"
        };
        this.arraySpinnerPriorita = new String[]{"Bassa","Media","Alta"};



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerSpesa);
        TagSpese.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinnerPriorita);
        Priorita.setAdapter(adapter1);
    }
}
