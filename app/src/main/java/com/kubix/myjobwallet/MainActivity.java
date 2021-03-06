package com.kubix.myjobwallet;

//QUESTO SOFTWARE E' STATO INTERAMENTE SVILUPPATO DA KUBIX STUDIO, TUTTI I DIRITTI SONO RISERVATI,
//SOLAMENTE LA SUDDETTA AZIENDA HA LA POSSIBILITA' DI MODIFICARE, AGGIORNARE, MODIFICARE E
//DISTRIBUIRE L'APPLICATIVO...

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.kubix.myjobwallet.calendario.CalendarioActivity;
import com.kubix.myjobwallet.calendario.TurniActivity;
import com.kubix.myjobwallet.entrate.EntrateActivity;
import com.kubix.myjobwallet.entrate.EntrateAggiungiActivity;
import com.kubix.myjobwallet.note.NoteActivity;
import com.kubix.myjobwallet.note.NoteAggiungiActivity;
import com.kubix.myjobwallet.profilo.ProfiloActivity;
import com.kubix.myjobwallet.setting.ClsSettings;
import com.kubix.myjobwallet.setting.SettingsActivity;
import com.kubix.myjobwallet.spese.SpeseActivity;
import com.kubix.myjobwallet.spese.SpeseAggiungiActivity;
import com.kubix.myjobwallet.utility.BottomNavigationViewHelper;
import com.kubix.myjobwallet.utility.VariabiliGlobali;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //INDICIZZA ADMOB NATIVA
    private static String LOG_TAG = "EXAMPLE";
    private AdView mAdView;
    VideoController mVideoController;

    //DICHIARA DATABASE SQLITE
    public static SQLiteDatabase db;

    //INDICIZZA COSE HOME
    TextView sommaSoloEntrate;
    TextView sommaEntrate;
    TextView sommaUscite;
    TextView sommaStipendio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ClsSettings settings = new ClsSettings(getBaseContext());
        if (settings.get_temadark()) {
            setTheme(R.style.AppTheme_Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //BottomBar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_spese:
                        startActivity(new Intent(MainActivity.this, SpeseAggiungiActivity.class));
                        break;
                    case R.id.action_entrate:
                        startActivity(new Intent(MainActivity.this, EntrateAggiungiActivity.class));
                        break;
                    case R.id.action_turni:
                        startActivity(new Intent(MainActivity.this, CalendarioActivity.class));
                        break;
                    case R.id.action_memo:
                        startActivity(new Intent(MainActivity.this, NoteAggiungiActivity.class));
                        break;

                }
                return true;
            }
        });

        // AdMob
        MobileAds.initialize(this, "ca-app-pub-9460579775308491~5760945149");
        mAdView = findViewById(R.id.ad_view_home);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        //DICHIARAZIONE COSE HOME
        sommaSoloEntrate = (TextView) findViewById(R.id.txtEntrate1Card1);
        sommaEntrate  = (TextView) findViewById(R.id.txtEntrateCard1);
        sommaUscite = (TextView) findViewById(R.id.txtSpeseCard1);
        sommaStipendio = (TextView) findViewById(R.id.txtCalcoloStipendio1);

        //SETTAGGI TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setTitle("Pannello di Controllo");
        setSupportActionBar(toolbar);

        //SETTAGGI NAVIGATION DRAWER
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //CREAZIONE EFFETTIVA DEL DATABASE ESEGUENDO QUERY
        db = this.openOrCreateDatabase("Turnazioni.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Turni (giornoSettimana Varchar(50), numeroGiorno Varchar (50), mese Varchar(50), anno Varchar (50), oraEntrata Varchar (50), oraUscita Varchar (50), Ordinarie Varchar (50), Straordinarie Varchar (50));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Note (Titolo Varchar (50), Nota Varchar (1000));");
        db.execSQL("CREATE TABLE IF NOT EXISTS InfoProfilo (ID Varchar (10) Unique, OreOrdinarie Varchar (50), NettoOrario Varchar (50), NettoStraordinario Varchar (50), ValutaSimbolo Varchar (50));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Entrate (Data Varchar (50), Titolo Varchar (50), Cifra Varchar (50), Categoria Varchar (50), Ora Varchar(50), GiornoTesto Varchar (50), GiornoNumero Varchar (50), MeseNumero Varchar (50), AnnoNumero Varchar (50));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Uscite (Data Varchar (50), Titolo Varchar (50), Cifra Varchar (50), Categoria Varchar (50), Ora Varchar (50), GiornoTesto Varchar (50), GiornoNumero Varchar (50), MeseNumero Varchar (50), AnnoNumero Varchar (50));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Acquisti (Premium Varchar (50));");
        db.execSQL("CREATE TABLE IF NOT EXISTS CalcoloStipendio (numeroGiorno Varchar(50), mese Varchar(50), anno Varchar(50), Importo Varchar (50));");

        //IMPOSTA DATI INIZIALI TABELLA ACQUISTI
        try{
            db.execSQL("INSERT INTO Acquisti (Premium) VALUES ('NO')");
        }catch (SQLException e){
            //NOTHING
        }

        //CARICA DA DATABASE IN VARIABILI GLOBALI I DATI DELLO STATO PREMIUM
        try {
            Cursor cr=MainActivity.db.rawQuery("SELECT * FROM Acquisti",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        VariabiliGlobali.statoPremium = cr.getString(cr.getColumnIndex("Premium"));
                    }while (cr.moveToNext());
                }else{

                }
            }
            cr.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //PER TESTARE PREMIUM
        //VariabiliGlobali.statoPremium = "SI";

        //IMPOSTA DATI INIZIALI TABELLA INFOPROFILO
        try{
            db.execSQL("INSERT INTO InfoProfilo (ID, OreOrdinarie, NettoOrario, NettoStraordinario) VALUES ('0', '8', '8', '10')");
            db.execSQL("UPDATE InfoProfilo SET ValutaSimbolo = '€' WHERE ID = '0'");
        }catch (SQLException e){
            //NOTHING
        }

        //CARICA DA DATABASE IN VARIABILI GLOBALI I DATI DEL PROFILO
        try {
            Cursor cr=MainActivity.db.rawQuery("SELECT * FROM InfoProfilo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        VariabiliGlobali.oreOrdinarie = Integer.valueOf(cr.getString(cr.getColumnIndex("OreOrdinarie")));
                        VariabiliGlobali.nettoOrario = Double.valueOf(cr.getString(cr.getColumnIndex("NettoOrario")));
                        VariabiliGlobali.nettoStraordinario = Double.valueOf(cr.getString(cr.getColumnIndex("NettoStraordinario")));
                        VariabiliGlobali.simboloValuta = String.valueOf(cr.getString(cr.getColumnIndex("ValutaSimbolo")));
                    }while (cr.moveToNext());
                }else{
                    Toast.makeText(getApplicationContext(), "NESSUN DATO INSERITO", Toast.LENGTH_LONG).show();
                }
            }
            cr.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //CONTROLLO PER LA VISUALIZZAZIONE DEGLI ANNUNCI
        if (VariabiliGlobali.statoPremium.equals("SI")){
            mAdView.setVisibility(View.GONE);
        }

        contiEntrateBarraSpese();
        calcoloStipendio();

        sommaStipendio.setText(sommaStipendio.getText().toString().replace(".",",").toString());
        sommaSoloEntrate.setText(sommaSoloEntrate.getText().toString().replace(".",",").toString());
        sommaEntrate.setText(sommaEntrate.getText().toString().replace(".",",").toString());
        sommaUscite.setText(sommaUscite.getText().toString().replace(".",",").toString());
    }


    //TOOLBAR MENU DESTRA
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_destra, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_layout) {
            Toast.makeText(getBaseContext(), "Coming Soon" , Toast.LENGTH_SHORT ).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //EVENTO TASTO INDIETRO
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        contiEntrateBarraSpese();
        calcoloSoloEntrate();
        calcoloStipendio();
        sommaStipendio.setText(sommaStipendio.getText().toString().replace(".",",").toString());
        sommaSoloEntrate.setText(sommaSoloEntrate.getText().toString().replace(".",",").toString());
        sommaEntrate.setText(sommaEntrate.getText().toString().replace(".",",").toString());
        sommaUscite.setText(sommaUscite.getText().toString().replace(".",",").toString());
    }


    //SELEZIONE ICONE NAVIGATION DRAWER
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfiloActivity.class));

        } else if (id == R.id.nav_turno) {
            startActivity(new Intent(this,CalendarioActivity.class));

        } else if (id == R.id.nav_lista_turni) {
            startActivity(new Intent(this,TurniActivity.class));

        } else if (id == R.id.nav_entrate) {
            startActivity(new Intent(this,EntrateActivity.class));

        } else if (id == R.id.nav_spesa) {
            startActivity(new Intent(this,SpeseActivity.class));

        } else if (id == R.id.nav_riepilogo) {
            startActivity(new Intent(this,RiepilogoActivity.class));

        } else if (id == R.id.nav_note) {
            startActivity(new Intent(this,NoteActivity.class));

            //Alert Dialog
        } else if (id == R.id.nav_premium) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Premium");
            // Lista Alert
            String[] animals = {getString(R.string.premium_linea_1), getString(R.string.premium_linea_2), getString(R.string.premium_linea_3), getString(R.string.premium_linea_4),};
            builder.setItems(animals, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                    }
                }
            });
            // Bottoni
            builder.setPositiveButton(getString(R.string.bottone_acquista), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getBaseContext(), "Coming Soon" , Toast.LENGTH_SHORT ).show();
                }
            });
            builder.setNegativeButton(getString(R.string.bottone_ripristina_acquisto), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getBaseContext(), "Coming Soon" , Toast.LENGTH_SHORT ).show();
                }
            });

            // Visualizzazione Alert
            AlertDialog dialog = builder.create();
            dialog.show();

        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(this,SettingsActivity.class));

        } else if (id == R.id.nav_email){
                final Intent _Intent = new Intent(android.content.Intent.ACTION_SEND);
                _Intent.setType("text/html");
                _Intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ "studio.kubix@gmail.com" });
                _Intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MyJobWallet");
                _Intent.putExtra(android.content.Intent.EXTRA_TEXT, "Hello Kubix Studio...");
                startActivity(Intent.createChooser(_Intent, "Send email a Kubix Studio"));

        } else if (id == R.id.nav_valuta){
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=com.kubix.myjobwallet&ah=Is2oYzVlAaRsq8sYhuOqZ0UACUc&hl=it")));

        } else if (id == R.id.nav_plus){
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://plus.google.com/u/0/+KubixStudio")));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void contiEntrateBarraSpese(){

        //SOMMA ENTRATE
        try {
            Cursor cr=MainActivity.db.rawQuery("SELECT SUM (Cifra) FROM Entrate",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        VariabiliGlobali.sommaEntrate = cr.getDouble(0);
                        sommaEntrate.setText(String.valueOf(VariabiliGlobali.sommaEntrate) + " " + VariabiliGlobali.simboloValuta);
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
                        sommaUscite.setText(String.valueOf(VariabiliGlobali.sommaUscite) + " "+VariabiliGlobali.simboloValuta);
                        if (VariabiliGlobali.sommaUscite == 0){
                            sommaUscite.setText("0,00 " + VariabiliGlobali.simboloValuta);
                        }
                    }while (cr.moveToNext());
                }
            }
            cr.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Double calcoloDelCazzo = VariabiliGlobali.sommaEntrate - VariabiliGlobali.sommaUscite;
        sommaEntrate.setText(String.valueOf(calcoloDelCazzo) + " " + VariabiliGlobali.simboloValuta);

        if(calcoloDelCazzo == 0){
            sommaUscite.setText("0,00 " + VariabiliGlobali.simboloValuta);
            sommaEntrate.setText("0,00 " + VariabiliGlobali.simboloValuta);
        }
    }

    public void calcoloStipendio(){
        try {
            Cursor cr=MainActivity.db.rawQuery("SELECT SUM (Importo) FROM CalcoloStipendio",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        VariabiliGlobali.calcoloCompletoStipendio = cr.getDouble(0);
                        sommaStipendio.setText(String.valueOf(VariabiliGlobali.calcoloCompletoStipendio) + " " + VariabiliGlobali.simboloValuta);
                        if (VariabiliGlobali.calcoloCompletoStipendio == 0){
                            sommaStipendio.setText("0,00 " + VariabiliGlobali.simboloValuta);
                        }
                    }while (cr.moveToNext());
                }
            }
            cr.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void calcoloSoloEntrate(){
        //SOMMA ENTRATE SINGOLE
        try {
            Cursor cr=MainActivity.db.rawQuery("SELECT SUM (Cifra) FROM Entrate",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        VariabiliGlobali.sommaEntrate = cr.getDouble(0);
                        sommaSoloEntrate.setText(String.valueOf(VariabiliGlobali.sommaEntrate) + " " + VariabiliGlobali.simboloValuta);
                        if (VariabiliGlobali.sommaEntrate == 0){
                            sommaSoloEntrate.setText("0,00 " + VariabiliGlobali.simboloValuta);
                        }
                    }while (cr.moveToNext());
                }
            }
            cr.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //CALCOLO ORE LAVORATIVE E STRAORDINARI TOTALI
    public void calcoloOreLavorative(){


    }
}


