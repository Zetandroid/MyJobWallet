package com.kubix.myjobwallet;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //TODO VARIABILI DI INDICIZZAZIONE
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        Toast.makeText(this, "Benvenuto in MyJobWallet", Toast.LENGTH_SHORT).show();
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

    //TODO MENU NAVIGATION DRAWER
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //TODO MENU TOOLBAR
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //TODO SELEZIONE ICONE NAVIGATION DRAWER
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(this,ProfileActivity.class));

        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(this,SettingActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void OreActivity (View v){
        startActivity (new Intent(MainActivity.this, OreActivity.class));
    }
    public void TurniActivity (View v){
        startActivity (new Intent(MainActivity.this,TurniActivity.class));
    }
    public void SpeseActivity (View V){
        startActivity (new Intent(MainActivity.this,SpeseActivity.class));
    }
    public void NoteActivity (View v){
        startActivity (new Intent(MainActivity.this,NoteActivity.class));
    }

}
