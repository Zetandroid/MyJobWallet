package com.kubix.myjobwallet.calendario;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;

import java.util.Date;

public class CalendarioActivity extends AppCompatActivity {

    //DICHIARAZIONE OGGETTI
    EditText inserisciEntrata;
    EditText inserisciUscita;
    CalendarView dataTurno;

    //VARIABILI DI CLASSE
    String giornoTestuale;
    String giornoTestualeAbbreviato;
    int numeroGiorno;
    int numeroMese;
    int numeroAnno;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        //TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOrologio);
        setTitle(R.string.toolbarOre);
        toolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoBianco));
        setSupportActionBar(toolbar);

        //INDICIZZA OGETTI
        dataTurno = (CalendarView) findViewById(R.id.calendarView);
        inserisciEntrata = (EditText) findViewById(R.id.editTextOraEntrata);
        inserisciUscita = (EditText) findViewById(R.id.editTextOraUscita);

        //APRI DATABASE
        MainActivity.db = this.openOrCreateDatabase("Turnazioni.db", MODE_PRIVATE, null);

        //LISTNER CLICK CALENDARIO
        dataTurno.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                int m = month + 1;
                int y = year;

                //PASSA IN VARIABILI DI CLASSE
                numeroGiorno = d;
                numeroMese = m;
                numeroAnno = y;

                //OTTIENI GIORNO DA DATA
                try{
                    SimpleDateFormat inFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = inFormat.parse(d+"/"+m+"/"+y);
                    SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                    String goal = outFormat.format(date);
                    giornoTestuale = goal;

                    //RIPORTA IN ITALIANO
                    if(giornoTestuale.equals("Sunday")){
                        giornoTestuale = "Domenica";
                    }else if(giornoTestuale.equals("Monday")){
                        giornoTestuale = "Lunedi";
                    }else if(giornoTestuale.equals("Tuesday")){
                        giornoTestuale = "Martedi";
                    }else if (giornoTestuale.equals("Wednesday")){
                        giornoTestuale = "Mercoledi";
                    }else if(giornoTestuale.equals("Thursday")){
                        giornoTestuale = "Giovedi";
                    }else if(giornoTestuale.equals("Friday")){
                        giornoTestuale = "Venerdi";
                    }else if (giornoTestuale.equals("Saturday")){
                        giornoTestuale = "Sabato";
                    }

                    //ABBREVIA PER FOTTUTO IMPATTO GRAFICO
                    if(giornoTestuale.equals("Domenica")){
                        giornoTestualeAbbreviato = "DOM";
                    }else if(giornoTestuale.equals("Lunedi")){
                        giornoTestualeAbbreviato= "LUN";
                    }else if(giornoTestuale.equals("Martedi")){
                        giornoTestualeAbbreviato = "MAR";
                    }else if (giornoTestuale.equals("Mercoledi")){
                        giornoTestualeAbbreviato = "MER";
                    }else if(giornoTestuale.equals("Giovedi")){
                        giornoTestualeAbbreviato = "GIO";
                    }else if(giornoTestuale.equals("Venerdi")){
                        giornoTestualeAbbreviato = "VEN";
                    }else if (giornoTestuale.equals("Sabato")){
                        giornoTestualeAbbreviato = "SAB";
                    }

                }catch (Exception e){
                    Toast.makeText(CalendarioActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    public void inserisciTurno(View v){
        if(! inserisciEntrata.getText().toString().equals("") && ! inserisciUscita.getText().toString().equals("")){
            //INSERISCI TURNO
            MainActivity.db.execSQL("INSERT INTO Turni (giornoSettimana, numeroGiorno, mese, anno, oraEntrata, oraUscita) VALUES ('"+giornoTestualeAbbreviato+"', '"+numeroGiorno+"', '"+numeroMese+"', '"+numeroAnno+"', '"+inserisciEntrata.getText().toString()+"', '"+inserisciUscita.getText().toString()+"')");
            Toast.makeText(this, getString(R.string.turnoInserito), Toast.LENGTH_SHORT).show();
            finish();

            //EFFETTUA CALCOLI SU ORE ORDINARIE / STRAORDINARIE EFFETTUATE (Da programmare causa sonno abnorme)

        }else{
            Snackbar.make(v, getString(R.string.compilaDatiPerInserimentoTurno), Snackbar.LENGTH_LONG).show();
        }
    }

    //DA AGGANCIARE A UN FUTURO BOTTOMSHEET (So io che intendo xD)
    public void inserisciRiposo(View v){
        MainActivity.db.execSQL("INSERT INTO Turni (giornoSettimana, numeroGiorno, mese, anno, oraEntrata, oraUscita, ordinarie, straordinarie) VALUES ('"+giornoTestualeAbbreviato+"', '"+numeroGiorno+"', '"+numeroMese+"', '"+numeroAnno+"', 'RIPOSO', 'RIPOSO')");
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
