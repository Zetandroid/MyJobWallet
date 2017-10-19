package com.kubix.myjobwallet;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.kubix.myjobwallet.calendario.CalendarioActivity;
import com.kubix.myjobwallet.calendario.TurniActivity;
import com.kubix.myjobwallet.entrate.EntrateActivity;
import com.kubix.myjobwallet.note.NoteActivity;
import com.kubix.myjobwallet.profilo.ProfiloActivity;
import com.kubix.myjobwallet.spese.SpeseActivity;

import com.kubix.myjobwallet.utility.EmailActivity;
import com.kubix.myjobwallet.utility.HomeGridAdapter;
import com.kubix.myjobwallet.utility.PremiumActivity;
import com.kubix.myjobwallet.utility.VariabiliGlobali;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GridView grid;
    String[] web = {

            "Ore",
            "Turni",
            "Spese",
            "Uscite",
            "Valute",
            "Memo"
    };

    int[] imageId = {
            R.drawable.ic_home_ora,
            R.drawable.ic_home_turni,
            R.drawable.ic_home_spesa,
            R.drawable.ic_home_entrate,
            R.drawable.ic_home_valuta,
            R.drawable.ic_home_memo
    };

    //TODO ADMOB NATIVA
    private static String LOG_TAG = "EXAMPLE";

    NativeExpressAdView mAdView;
    VideoController mVideoController;

    //TODO VARIABILI DI INDICIZZAZIONE
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoTitolo));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //TODO ADMOB NATIVA
        mAdView = (NativeExpressAdView) findViewById(R.id.adView);
        mAdView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());
        mVideoController = mAdView.getVideoController();
        mVideoController.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            @Override
            public void onVideoEnd() {
                Log.d(LOG_TAG, "Video playback is finished.");
                super.onVideoEnd();
            }
        });
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (mVideoController.hasVideoContent()) {
                    Log.d(LOG_TAG, "Received an ad that contains a video asset.");
                } else {
                    Log.d(LOG_TAG, "Received an ad that does not contain a video asset.");
                }
            }
        });

        mAdView.loadAd(new AdRequest.Builder().build());

        //TODO GRIGLIA
        HomeGridAdapter adapter = new HomeGridAdapter(MainActivity.this, web, imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            if (position == 0) {
                                                Intent intent = new Intent(MainActivity.this, CalendarioActivity.class);
                                                startActivity(intent);
                                            } else if (position == 1) {
                                                Intent intent = new Intent(MainActivity.this, TurniActivity.class);
                                                startActivity(intent);
                                            } else if (position == 2) {
                                                Intent intent = new Intent(MainActivity.this, SpeseActivity.class);
                                                startActivity(intent);
                                            } else if (position == 3) {
                                                Intent intent = new Intent(MainActivity.this, EntrateActivity.class);
                                                startActivity(intent);
                                            } else if (position == 4) {
                                                Intent intent = new Intent(MainActivity.this, ConvertitoreActivity.class);
                                                startActivity(intent);
                                            } else if (position == 5) {
                                                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });

        //TODO INDICIZZARE DATABASE CON QUERY SQL
        db = this.openOrCreateDatabase("Turnazioni.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Turni (Data Varchar(50) Unique, oraEntrata Varchar (50), oraUscita Varchar(50));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Controlli (Data Varchar (50) Unique);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Note (Titolo Varchar (50), Nota Varchar (1000));");
        db.execSQL("CREATE TABLE IF NOT EXISTS InfoProfilo (ID Varchar (10) Unique, OreOrdinarie Varchar (50), NettoOrario Varchar (50), NettoStraordinario Varchar (50));");
        try{
            db.execSQL("INSERT INTO InfoProfilo (ID, OreOrdinarie, NettoOrario, NettoStraordinario) VALUES ('0', '8', '8', '10')");
        }catch (SQLException e){
            //NOTHING
        }
        db.execSQL("CREATE TABLE IF NOT EXISTS Entrate (Data Varchar (50), Titolo Varchar (50), Cifra Varchar (50));");
        db.execSQL("CREATE TABLE IF NOT EXISTS Uscite (Data Varchar (50), Titolo Varchar (50), Cifra Varchar (50));");

        //TODO INDICIZZA DA DATABASE IN VARIABILI GLOBALI DATI PROFILO SU PAGA ORARIA, STRAORDINARIA E ORE ORDINARIE
        try {
            Cursor cr=MainActivity.db.rawQuery("SELECT * FROM InfoProfilo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        VariabiliGlobali.oreOrdinarie = Integer.valueOf(cr.getString(cr.getColumnIndex("OreOrdinarie")));
                        VariabiliGlobali.nettoOrario = Integer.valueOf(cr.getString(cr.getColumnIndex("NettoOrario")));
                        VariabiliGlobali.nettoStraordinario = Integer.valueOf(cr.getString(cr.getColumnIndex("NettoStraordinario")));
                    }while (cr.moveToNext());
                }else{
                    Toast.makeText(getApplicationContext(), "NESSUN TURNO INSERITO", Toast.LENGTH_LONG).show();
                }
            }
            cr.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //TODO SELEZIONE ICONE NAVIGATION DRAWER
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfiloActivity.class));

        } else if (id == R.id.nav_premium) {
            startActivity(new Intent(this, PremiumActivity.class));

        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(this,SettingsActivity.class));

        } else if (id == R.id.nav_email){
            startActivity(new Intent(this, EmailActivity.class));

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
