package com.kubix.myjobwallet;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mowmo on 26/09/17.
 */

public class OreActivity extends AppCompatActivity{
    Button bottoneEntrata;
    Button bottoneUscita;
    TimePicker oraTurno;
    DatePicker dataTurno;
    public static String thisDate;

    //VARIABILI DI CONTROLLO
    String oraDiEntrata;
    String oraDiUscita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ore);

        //INDICIZZA COMPONENTI
        bottoneEntrata = (Button) findViewById(R.id.buttonEntrata);
        bottoneUscita = (Button) findViewById(R.id.buttonUscita);
        oraTurno = (TimePicker) findViewById(R.id.timePicker2);
        dataTurno = (DatePicker) findViewById(R.id.datePicker);

        //INDICIZZA DATABASE
        MainActivity.db = this.openOrCreateDatabase("Turnazioni.db", MODE_PRIVATE, null);

    }

    public void inserisciEntrataTurno(View v){

        //PRELEVA DATA ODIERNA
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        int giorno = dataTurno.getDayOfMonth();
        int mese = dataTurno.getMonth() + 1;
        int anno = dataTurno.getYear();

        String giornoS = String.format("%02d", giorno);
        String meseS = String.format("%02d", mese);
        String annoS = String.format("%02d", anno);
        thisDate = String.valueOf(giornoS + "/" + meseS + "/" + annoS);

        String oraEntrata = String.valueOf(String.format("%02d:%02d",oraTurno.getHour(), oraTurno.getMinute()));
        oraDiEntrata = oraEntrata;

        try{
            MainActivity.db.execSQL("INSERT INTO Turni (Data, oraEntrata, oraUscita) VALUES ('"+thisDate+"', '"+oraEntrata+"', 'IN SERVIZIO')");
            MainActivity.db.execSQL("INSERT INTO Controlli (Data) VALUES ('"+thisDate+"')");
            Toast.makeText(this, "ENTRATA TURNO ESEGUITA CON SUCCESSO", Toast.LENGTH_LONG).show();

        }catch (Exception e) {

            //PARTE L'ECCEZIONE PERCHE IL CAMPO DATA DEL DB E' SETTATO UNIQUE E QUINDI SE SI INSERISCE UN'ALTRA ENTRATA LO STESSO GIORNO NON LA METTE E DA QUESTO MESSAGGIO SOTTOSTANTE
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("TURNAZIONE GIA ESISTENTE PER QUESTA GIORNATA, SE NON LO HAI FATTO: NON DIMENTICARE DI INSERIRE L'USCITA.");
            builder1.setCancelable(true);
            builder1.create();
            builder1.show();
        }
    }

    public void inserisciUscitaTurno(View v){

        //PRELEVA DATA ODIERNA
        int giorno = dataTurno.getDayOfMonth();
        int mese = dataTurno.getMonth() + 1;
        int anno = dataTurno.getYear();
        String giornoS = String.format("%02d", giorno);
        String meseS = String.format("%02d", mese);
        String annoS = String.format("%02d", anno);
        thisDate = String.valueOf(giornoS + "/" + meseS + "/" + annoS);

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
                Toast.makeText(this, "USCITA TURNO ESEGUITA CORRETAMENTE", Toast.LENGTH_LONG).show();
            }


        }else{
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("PRIMA DI INSERIRE L'USCITA DEVI INSERIRE L'ENTRATA PER LA TURNAZIONE ODIERNA.");
            builder1.setCancelable(true);
            builder1.create();
            builder1.show();
        }

        if(oraDiEntrata.equals(oraDiUscita)){
            try{
                MainActivity.db.execSQL("UPDATE Turni SET oraEntrata = 'RIPOSO', oraUscita = 'RIPOSO' WHERE Data = '"+thisDate+"';");
                Toast.makeText(this, "GIORNO DI RIPOSO INSERITO CORRETTAMENTE", Toast.LENGTH_LONG).show();
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }

    }

    public void apriStorico(View v){
        Intent intent = new Intent(this, TurniActivity.class);
        startActivity(intent);
    }
}
