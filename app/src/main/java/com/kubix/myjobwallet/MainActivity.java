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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.kubix.myjobwallet.setting.SettingsActivity;
import com.kubix.myjobwallet.spese.SpeseActivity;
import com.kubix.myjobwallet.spese.SpeseAggiungiActivity;
import com.kubix.myjobwallet.utility.VariabiliGlobali;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    //INDICIZZA ADMOB NATIVA
    private static String LOG_TAG = "EXAMPLE";
    private AdView mAdView;
    VideoController mVideoController;

    //FAB
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private Animation fab_apri, fab_chiudi, fab_ruota_avanti, fab_ruota_indietro;

    //DICHIARA DATABASE SQLITE
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // AdMob
        MobileAds.initialize(this, "ca-app-pub-9460579775308491~5760945149");
        mAdView = findViewById(R.id.ad_view_home);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        //SETTAGGI TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setTitle(R.string.toolbarHome);
        setSupportActionBar(toolbar);

        //SETTAGGI NAVIGATION DRAWER
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //FAB
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        fab_apri = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_apri);
        fab_chiudi = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_chiudi);
        fab_ruota_avanti = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_ruota_avanti);
        fab_ruota_indietro = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_ruota_indietro);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        fab4.setOnClickListener(this);

        //CREAZIONE EFFETTIVA DEL DATABASE ESEGUENDO QUERY
        db = this.openOrCreateDatabase("Turnazioni.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Turni (giornoSettimana Varchar(50), numeroGiorno Varchar (50), mese Varchar(50), anno Varchar (50), oraEntrata Varchar (50), oraUscita Varchar (50), Ordinarie Varchar (50), Straordinarie Varchar (50));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Note (Titolo Varchar (50), Nota Varchar (1000));");
        db.execSQL("CREATE TABLE IF NOT EXISTS InfoProfilo (ID Varchar (10) Unique, OreOrdinarie Varchar (50), NettoOrario Varchar (50), NettoStraordinario Varchar (50), ValutaSimbolo Varchar (50));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Entrate (Data Varchar (50), Titolo Varchar (50), Cifra Varchar (50), Categoria Varchar (50), Ora Varchar(50), GiornoTesto Varchar (50), GiornoNumero Varchar (50), MeseNumero Varchar (50), AnnoNumero Varchar (50));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Uscite (Data Varchar (50), Titolo Varchar (50), Cifra Varchar (50), Categoria Varchar (50), Ora Varchar (50), GiornoTesto Varchar (50), GiornoNumero Varchar (50), MeseNumero Varchar (50), AnnoNumero Varchar (50));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Acquisti (Premium Varchar (50));");
        db.execSQL("CREATE TABLE IF NOT EXISTS CalcoloStipendio (Importo Varchar (50));");

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
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
                startActivity(new Intent(this, EntrateAggiungiActivity.class));
                break;
            case R.id.fab2:
                startActivity(new Intent(this, SpeseAggiungiActivity.class));
                break;
            case R.id.fab3:
                startActivity(new Intent(this, CalendarioActivity.class));
                break;
            case R.id.fab4:
                startActivity(new Intent(this, NoteAggiungiActivity.class));
                break;
        }
    }

    public void animateFAB() {

        if (isFabOpen) {

            fab.startAnimation(fab_ruota_indietro);
            fab1.startAnimation(fab_chiudi);
            fab2.startAnimation(fab_chiudi);
            fab3.startAnimation(fab_chiudi);
            fab4.startAnimation(fab_chiudi);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);
            isFabOpen = false;
            Log.d("Raj", "close");

        } else {

            fab.startAnimation(fab_ruota_avanti);
            fab1.startAnimation(fab_apri);
            fab2.startAnimation(fab_apri);
            fab3.startAnimation(fab_apri);
            fab4.startAnimation(fab_apri);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab2.setClickable(true);
            fab4.setClickable(true);
            isFabOpen = true;
            Log.d("Raj", "open");
        }
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
            builder.setNegativeButton(getString(R.string.bottone_ripristina_acquista), new DialogInterface.OnClickListener() {
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
                _Intent.putExtra(android.content.Intent.EXTRA_TEXT, "Ciao Kubix Studio...");
                startActivity(Intent.createChooser(_Intent, "Invia email a Kubix Studio"));

        } else if (id == R.id.nav_valuta){
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id=com.kubix.myjobwallet&ah=Is2oYzVlAaRsq8sYhuOqZ0UACUc&hl=it")));

        } else if (id == R.id.nav_plus){
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://plus.google.com/u/0/+KubixStudio")));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
