package com.kubix.myjobwallet.profilo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;
import com.kubix.myjobwallet.setting.ClsSettings;
import com.kubix.myjobwallet.utility.VariabiliGlobali;

public class ProfiloActivity extends AppCompatActivity {

    //INDICIZZAZIONE OGGETTI ORA
    public static TextView oreOrdinarieText;
    public static TextView pagaOrariaText;
    public static TextView pagaStraordinariaText;
    public static TextView sommaEntrate;
    public static TextView sommaUscite;
    public static TextView sommaStipendio;

    //LOGIN GOOGLE
    private ProfiloActivity mContext;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private TextView txtName, txtEmail;
    private Button bottoneGoogle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ClsSettings settings = new ClsSettings(getBaseContext());
        if (settings.get_temadark()) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);



        //TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfilo);
        setTitle("Profilo");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //BOTTONE DATI
        Button button = (Button) findViewById(R.id.bottoneDati);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ProfiloDatiActivity.class);
                startActivity(i);
            }
        });


        //INDIRIZZAMENTO OGGETTI CALCOLO ORA
        oreOrdinarieText = (TextView) findViewById(R.id.txtOre);
        pagaOrariaText = (TextView) findViewById(R.id.txtPagaOraria);
        pagaStraordinariaText = (TextView) findViewById(R.id.txtStraordinaria);

        //INDICIZZO OGGETTI CALCOLI VARI
        sommaEntrate = (TextView) findViewById(R.id.txtEntrateCard1);
        sommaUscite = (TextView) findViewById(R.id.txtSpeseCard1);
        sommaStipendio = (TextView) findViewById(R.id.txtCalcoloStipendio);

        contiEntrateBarraSpese();
        calcoloStipendio();
        caricaDatiProfilo();
    }

    @Override
    public void onResume(){
        super.onResume();
        contiEntrateBarraSpese();
        calcoloStipendio();

        caricaDatiProfilo();
    }

    public void caricaDatiProfilo(){

        //CARICA INFO PROFILO DA DATABASE
        try {
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM InfoProfilo",null);
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

    //EVENTO TEMPORANEO
    public void alertAggiornamento(View v){
        Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
    }

    public void contiEntrateBarraSpese(){

        //SOMMA ENTRATE
        try {
            Cursor cr=MainActivity.db.rawQuery("SELECT SUM (Cifra) FROM Entrate",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        VariabiliGlobali.sommaEntrate = cr.getDouble(0);
                        sommaEntrate.setText(String.valueOf(VariabiliGlobali.sommaEntrate) + " €");
                        if (VariabiliGlobali.sommaEntrate == 0){
                            sommaEntrate.setText("0,00 €");
                        }
                    }while (cr.moveToNext());
                }
            }
            cr.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //SOMMA USCITE
        try {
            Cursor cr=MainActivity.db.rawQuery("SELECT SUM (Cifra) FROM Uscite",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        VariabiliGlobali.sommaUscite = cr.getDouble(0);
                        sommaUscite.setText(String.valueOf(VariabiliGlobali.sommaUscite) + " €");
                        if (VariabiliGlobali.sommaUscite == 0){
                            sommaUscite.setText("0,00 €");
                        }
                    }while (cr.moveToNext());
                }
            }
            cr.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Double calcoloDelCazzo = VariabiliGlobali.sommaEntrate - VariabiliGlobali.sommaUscite;
        sommaEntrate.setText(String.valueOf(calcoloDelCazzo) + " €");

        if(calcoloDelCazzo == 0){
            sommaUscite.setText("0,00 €");
            sommaEntrate.setText("0,00 €");
        }
    }

    public void calcoloStipendio(){
        try {
            Cursor cr=MainActivity.db.rawQuery("SELECT SUM (Importo) FROM CalcoloStipendio",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        VariabiliGlobali.calcoloCompletoStipendio = cr.getDouble(0);
                        sommaStipendio.setText(String.valueOf(VariabiliGlobali.calcoloCompletoStipendio) + " €");
                        if (VariabiliGlobali.calcoloCompletoStipendio == 0){
                            sommaStipendio.setText("0,00 €");
                        }
                    }while (cr.moveToNext());
                }
            }
            cr.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

