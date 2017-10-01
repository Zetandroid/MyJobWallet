package com.kubix.myjobwallet;

import android.content.Intent;
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
        db.execSQL("CREATE TABLE IF NOT EXISTS Stipendi (Mese Varchar (50), Importo Varchar (50))");
        db.execSQL("CREATE TABLE IF NOT EXISTS Note (Testo Varchar (50))");
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

    //TODO Questo evento apre le activity, va modificato il codice a seconda dell'activity da aprire
    //TODO Ogni activity va aperta creando un metodo come questo.
    //TODO Poi dopo nell'xml del layout, alla voce android:onclick dell'elemento da cliccare
    //TODO Ci scrivi apriActivity1

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
