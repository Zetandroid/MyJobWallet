package com.kubix.myjobwallet.calendario;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.kubix.myjobwallet.R;

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

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarElaborazioni);
        setTitle(R.string.toolbarElaborazioni);
        toolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoBianco));
        setSupportActionBar(toolbar);

        this.arraySpinner = new String[] {
                getString(R.string.gennaio), getString(R.string.febbraio), getString(R.string.marzo), getString(R.string.aprile), getString(R.string.maggio),
                getString(R.string.giugno), getString(R.string.luglio), getString(R.string.agosto), getString(R.string.settembre), getString(R.string.ottobre),
                getString(R.string.novembre), getString(R.string.dicembre)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        mesiAnno.setAdapter(adapter);
    }
}
