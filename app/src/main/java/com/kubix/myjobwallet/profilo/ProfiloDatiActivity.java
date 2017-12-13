package com.kubix.myjobwallet.profilo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;

import com.kubix.myjobwallet.setting.ClsSettings;
import com.kubix.myjobwallet.utility.VariabiliGlobali;

public class ProfiloDatiActivity extends AppCompatActivity {

    EditText aggiornaOrdinarie;
    EditText aggiornaPaga;
    EditText aggiornaPagaStraordinari;
    Spinner aggiornaSimboloValuta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ClsSettings settings = new ClsSettings(getBaseContext());
        if (settings.get_temadark()) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_dati);

        //TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfiloDati);
        setTitle("Dati Utente");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        aggiornaOrdinarie = (EditText) findViewById(R.id.oreOrdinarieText);
        aggiornaPaga = (EditText) findViewById(R.id.pagaOrariaText);
        aggiornaPagaStraordinari = (EditText) findViewById(R.id.pagaStraordinariText);
        aggiornaSimboloValuta = (Spinner) findViewById(R.id.valutaMondiale);

        //RIEMPIO LO SPINNER CON I SIMBOLI DI VALUTA
        String[] arraySpinner;
        arraySpinner = new String[] {
                "€", "$", "¥", "£"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        aggiornaSimboloValuta.setAdapter(adapter);
    }

    //AGGIORNAMENTO INFO ORE E PAGA
    public void AggiornaDatiProfilo(View v){

        if(!aggiornaOrdinarie.getText().toString().equals("") && !aggiornaPaga.getText().toString().equals("") && !aggiornaPagaStraordinari.getText().toString().equals("")){
            MainActivity.db.execSQL("UPDATE InfoProfilo SET OreOrdinarie = '"+aggiornaOrdinarie.getText().toString()+"', NettoOrario = '"+aggiornaPaga.getText().toString()+"', NettoStraordinario = '"+aggiornaPagaStraordinari.getText().toString()+"', ValutaSimbolo = '"+aggiornaSimboloValuta.getSelectedItem().toString()+"' WHERE ID = '0'");
            VariabiliGlobali.oreOrdinarie = Integer.valueOf(aggiornaOrdinarie.getText().toString());
            VariabiliGlobali.nettoOrario = Double.valueOf(aggiornaPaga.getText().toString());
            VariabiliGlobali.nettoStraordinario = Double.valueOf(aggiornaPagaStraordinari.getText().toString());
            VariabiliGlobali.simboloValuta = String.valueOf(aggiornaSimboloValuta.getSelectedItem().toString());
            Toast.makeText(this, "Dati inseriti!", Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(this, "Compila tutti i Dati", Toast.LENGTH_SHORT).show();
        }
    }

    // Freccia Indietro
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
