package com.kubix.myjobwallet;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TimePicker;
import android.widget.Toast;

import util.VariabiliGlobali;

import static com.kubix.myjobwallet.R.id.timePicker2;

public class CalendarioActivity extends AppCompatActivity {
    //DICHIARAZIONE OGGETTI
    TimePicker oraTurno;

    //DICHIARAZIONE VARIABILE DI RICEVIMENTO DATA TURNO
    public static String thisDate;

    //VARIABILI DI CONTROLLO
    String oraDiEntrata;
    String oraDiUscita;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOrologio);
        setTitle(R.string.toolbarOre);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        setSupportActionBar(toolbar);

        //TODO BOTTOMBAR

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottombar_calendario);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottom_entrata:
                                inserisciEntrataTurno();
                                break;
                            case R.id.bottom_uscita:
                                inserisciUscitaTurno();
                                break;
                            case R.id.bottom_turni:
                                apriStorico();
                                break;
                        }
                        return false;
                    }
                });

        Toast.makeText(this, VariabiliGlobali.dataTurno, Toast.LENGTH_SHORT).show();

        oraTurno = (TimePicker) findViewById(timePicker2);


        //INDICIZZA DATABASE
        MainActivity.db = this.openOrCreateDatabase("Turnazioni.db", MODE_PRIVATE, null);

        //ASSEGNAZIONE DATI,
        thisDate = VariabiliGlobali.dataTurno;

    }

    public void inserisciEntrataTurno(){

        String oraEntrata = String.valueOf(String.format("%02d:%02d",oraTurno.getHour(), oraTurno.getMinute()));
        oraDiEntrata = oraEntrata;

        try{
            MainActivity.db.execSQL("INSERT INTO Turni (Data, oraEntrata, oraUscita) VALUES ('"+thisDate+"', '"+oraEntrata+getString(R.string.in_servizio));
            MainActivity.db.execSQL("INSERT INTO Controlli (Data) VALUES ('"+thisDate+"')");
            Toast.makeText(this, R.string.entrata_turno, Toast.LENGTH_LONG).show();

        }catch (Exception e) {

            //PARTE L'ECCEZIONE PERCHE IL CAMPO DATA DEL DB E' SETTATO UNIQUE E QUINDI SE SI INSERISCE UN'ALTRA ENTRATA LO STESSO GIORNO NON LA METTE E DA QUESTO MESSAGGIO SOTTOSTANTE
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(R.string.turno_esistente);
            builder1.setCancelable(true);
            builder1.create();
            builder1.show();
        }
    }

    public  void inserisciUscitaTurno(){

        String controllo = "";

        try{

            Cursor cs = MainActivity.db.rawQuery("SELECT * FROM Controlli WHERE Data = '"+thisDate+"'", null);
            if (cs.getCount() > 0) {
                cs.moveToFirst();
                controllo = cs.getString(0);
            }

        }catch (Exception e){

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if(controllo != ""){

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                String oraUscita = String.valueOf(String.format("%02d:%02d" ,oraTurno.getHour(), oraTurno.getMinute()));
                oraDiUscita = oraUscita;

                try{
                    MainActivity.db.execSQL("UPDATE Turni SET oraUscita = '"+oraUscita+"' WHERE Data = '"+thisDate+"';");
                }catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            //EVITA QUESTO MESSAGGIO QUANDO INSERISCI IL RIPOSO
            if(! oraDiEntrata.equals(oraDiUscita)){
                Toast.makeText(this, R.string.uscita_turno, Toast.LENGTH_LONG).show();
            }


        }else{
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(R.string.inserire_uscita);
            builder1.setCancelable(true);
            builder1.create();
            builder1.show();
        }

        if(oraDiEntrata.equals(oraDiUscita)){
            try{
                MainActivity.db.execSQL("UPDATE Turni SET oraEntrata = 'RIPOSO', oraUscita = 'RIPOSO' WHERE Data = '"+thisDate+"';");
                Toast.makeText(this, R.string.giorno_riposo, Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

    }

    public void apriStorico(){
        Intent intent = new Intent(this, TurniActivity.class);
        startActivity(intent);
    }

}
