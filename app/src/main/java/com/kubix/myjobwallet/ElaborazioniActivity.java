package com.kubix.myjobwallet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ElaborazioniActivity extends AppCompatActivity {

    //INDICIZZA OGGETTI
    Spinner mesiAnno;

    //VARIABILI DI CLASSE
    private String[] arraySpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elaborazioni);
        mesiAnno = (Spinner) findViewById(R.id.spinner);
        this.arraySpinner = new String[] {
                "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        mesiAnno.setAdapter(adapter);
    }
}