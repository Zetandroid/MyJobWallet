package com.kubix.myjobwallet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by mowmo on 15/10/17.
 */

public class EntrateAggiungiActivity extends AppCompatActivity {

    //TODO INDICIZZA OGGETTI
    Spinner TagEntrate;
    Spinner Priorita;

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
        TagEntrate = (Spinner) findViewById(R.id.tagEntrateSpinner);
        Priorita = (Spinner) findViewById(R.id.prioritaEntrateSpinner);

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


    }
}
