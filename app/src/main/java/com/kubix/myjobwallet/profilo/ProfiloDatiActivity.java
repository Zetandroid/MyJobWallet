package com.kubix.myjobwallet.profilo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;

import com.kubix.myjobwallet.utility.VariabiliGlobali;

public class ProfiloDatiActivity extends AppCompatActivity {

    EditText aggiornaOrdinarie;
    EditText aggiornaPaga;
    EditText aggiornaPagaStraordinari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_dati);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfiloDati);
        setTitle(R.string.toolbarProfileDati);
        toolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoNero));
        setSupportActionBar(toolbar);

        aggiornaOrdinarie = (EditText) findViewById(R.id.oreOrdinarieText);
        aggiornaPaga = (EditText) findViewById(R.id.pagaOrariaText);
        aggiornaPagaStraordinari = (EditText) findViewById(R.id.pagaStraordinariText);
    }

    //TODO METODO AGGIORNAMENTO INFO ORE E PAGA
    public void AggiornaDatiProfilo(View v){

        if(!aggiornaOrdinarie.getText().toString().equals("") && !aggiornaPaga.getText().toString().equals("") && !aggiornaPagaStraordinari.getText().toString().equals("")){
            MainActivity.db.execSQL("UPDATE InfoProfilo SET OreOrdinarie = '"+aggiornaOrdinarie.getText().toString()+"', NettoOrario = '"+aggiornaPaga.getText().toString()+"', NettoStraordinario = '"+aggiornaPagaStraordinari.getText().toString()+"' WHERE ID = '0'");
            VariabiliGlobali.oreOrdinarie = Integer.valueOf(aggiornaOrdinarie.getText().toString());
            VariabiliGlobali.nettoOrario = Integer.valueOf(aggiornaPaga.getText().toString());
            VariabiliGlobali.nettoStraordinario = Integer.valueOf(aggiornaPagaStraordinari.getText().toString());
            Toast.makeText(this, "Info profilo aggiornate con successo.", Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(this, "I dati profilo devono essere compilati correttamente.", Toast.LENGTH_SHORT).show();
        }
    }

}
