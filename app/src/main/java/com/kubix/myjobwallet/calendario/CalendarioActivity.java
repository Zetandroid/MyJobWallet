package com.kubix.myjobwallet.calendario;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;

import com.kubix.myjobwallet.utility.VariabiliGlobali;

import java.util.Date;

public class CalendarioActivity extends AppCompatActivity {
    //DICHIARAZIONE OGGETTI
    EditText inserisciEntrata;
    EditText inserisciUscita;
    CalendarView dataTurno;

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

        //INDICIZZA OGETTI
        dataTurno = (CalendarView) findViewById(R.id.calendarView);
        inserisciEntrata = (EditText) findViewById(R.id.editTextOraEntrata);
        inserisciUscita = (EditText) findViewById(R.id.editTextOraUscita);

        //INDICIZZA DATABASE
        MainActivity.db = this.openOrCreateDatabase("Turnazioni.db", MODE_PRIVATE, null);

        //ASSEGNAZIONE DATI,
        thisDate = VariabiliGlobali.dataTurno;

    }



    public void inserisciEntrataTurno(View v){

        if (! inserisciEntrata.getText().toString().equals("")){

            dataTurno.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    //PRELEVA DATA ODIERNA
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String thisDate = sdf.format(new Date(dataTurno.getDate()));
                    Toast.makeText(CalendarioActivity.this, thisDate, Toast.LENGTH_SHORT).show();
                }
            });



            String oraEntrata = String.valueOf(String.format(inserisciEntrata.getText().toString()));
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

        }else{
            Toast.makeText(this, getString(R.string.compilaDatiPerInserimentoTurno), Toast.LENGTH_SHORT).show();
        }

    }

    public void inserisciUscitaTurno(View v){

        if (! inserisciUscita.getText().toString().equals("")){
            //PRELEVA DATA ODIERNA
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String thisDate = sdf.format(new Date(dataTurno.getDate()));

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
                    String oraUscita = String.valueOf(String.format(inserisciUscita.getText().toString()));
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

        }else{

            Toast.makeText(this, getString(R.string.compilaDatiPerInserimentoTurno), Toast.LENGTH_SHORT).show();
        }

    }


    public void dialogTimePickerClickEntrata(View v){

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CalendarioActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String selezioneOra;
                String selezioneMinuti;

                if (String.valueOf(selectedHour).length() == 1 && String.valueOf(selectedMinute).length() == 1){
                    selezioneOra = "0" + selectedHour;
                    selezioneMinuti = "0" +selectedMinute;
                    inserisciEntrata.setText(selezioneOra + ":" + selezioneMinuti);
                }else if(String.valueOf(selectedHour).length() == 1){
                    selezioneOra = "0" + selectedHour;
                    selezioneMinuti = String.valueOf(selectedMinute);
                    inserisciEntrata.setText(selezioneOra + ":" + selezioneMinuti);
                }else if (String.valueOf(selectedMinute).length() == 1){
                    selezioneOra = String.valueOf(selectedHour);
                    selezioneMinuti = "0" + selectedMinute;
                    inserisciEntrata.setText(selezioneOra + ":" + selezioneMinuti);
                }else{
                    selezioneOra = String.valueOf(selectedHour);
                    selezioneMinuti = String.valueOf(selectedMinute);
                    inserisciEntrata.setText(selezioneOra + ":" + selezioneMinuti);
                }

            }

        }, hour, minute, true);
        mTimePicker.setTitle(getString(R.string.imposta_entrata));
        mTimePicker.show();
    }

    public void dialogTimePickerClickUscita(View v){

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(CalendarioActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String selezioneOra;
                String selezioneMinuti;
                if (String.valueOf(selectedHour).length() == 1 && String.valueOf(selectedMinute).length() == 1){
                    selezioneOra = "0" + selectedHour;
                    selezioneMinuti = "0" +selectedMinute;
                    inserisciUscita.setText(selezioneOra + ":" + selezioneMinuti);
                }else if(String.valueOf(selectedHour).length() == 1){
                    selezioneOra = "0" + selectedHour;
                    selezioneMinuti = String.valueOf(selectedMinute);
                    inserisciUscita.setText(selezioneOra + ":" + selezioneMinuti);
                }else if (String.valueOf(selectedMinute).length() == 1){
                    selezioneOra = String.valueOf(selectedHour);
                    selezioneMinuti = "0" + selectedMinute;
                    inserisciUscita.setText(selezioneOra + ":" + selezioneMinuti);
                }else{
                    selezioneOra = String.valueOf(selectedHour);
                    selezioneMinuti = String.valueOf(selectedMinute);
                    inserisciUscita.setText(selezioneOra + ":" + selezioneMinuti);
                }

            }
        }, hour, minute, true);
        mTimePicker.setTitle(getString(R.string.imposta_uscita));
        mTimePicker.show();
    }

    public void apriStorico(View v){
        Intent intent = new Intent(this, TurniActivity.class);
        startActivity(intent);
    }

}
