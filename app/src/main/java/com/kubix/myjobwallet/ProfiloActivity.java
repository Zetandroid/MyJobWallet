package com.kubix.myjobwallet;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfiloActivity extends AppCompatActivity{

    //TODO INDICIZZAZIONE OGGETTI ORA
    TextView oreOrdinarieText;
    TextView pagaOrariaText;
    TextView pagaStraordinariaText;
    EditText aggiornaOrdinarie;
    EditText aggiornaPaga;
    EditText aggiornaPagaStraordinari;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfilo);
        setTitle(R.string.toolbarProfile);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        setSupportActionBar(toolbar);

        //TODO BOTTONE DATI
        Button button = (Button) findViewById(R.id.bottoneDati);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ProfiloDatiActivity.class);
                startActivity(i);
            }
        });


        //TODO INDIRIZZAMENTO OGGETTI CALCOLO ORA
        oreOrdinarieText = (TextView) findViewById(R.id.txtOre);
        pagaOrariaText = (TextView) findViewById(R.id.txtPagaOraria);
        pagaStraordinariaText = (TextView) findViewById(R.id.txtStraordinaria);

        aggiornaOrdinarie = (EditText) findViewById(R.id.oreOrdinarieText);
        aggiornaPaga = (EditText) findViewById(R.id.pagaOrariaText);
        aggiornaPagaStraordinari = (EditText) findViewById(R.id.pagaStraordinariText);
    }

    public void leggiDatiProfilo(){

        //TODO CARICA INFO PROFILO DA DATABASE
        try {
            Cursor cr=MainActivity.db.rawQuery("SELECT * FROM InfoProfilo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        oreOrdinarieText.setText(cr.getString(cr.getColumnIndex("OreOrdinarie")).toString());
                        pagaOrariaText.setText(cr.getString(cr.getColumnIndex("NettoOrario")).toString());
                        pagaStraordinariaText.setText(cr.getString(cr.getColumnIndex("NettoStraordinario")).toString());
                    }while (cr.moveToNext());
                }else{

                }
            }
            cr.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

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
            oreOrdinarieText.setText(String.valueOf(VariabiliGlobali.oreOrdinarie));
            pagaOrariaText.setText(String.valueOf(VariabiliGlobali.nettoOrario));
            pagaStraordinariaText.setText(String.valueOf(VariabiliGlobali.nettoStraordinario));
        }else{
            Toast.makeText(this, "I dati profilo devono essere compilati correttamente.", Toast.LENGTH_SHORT).show();
        }
    }

}
