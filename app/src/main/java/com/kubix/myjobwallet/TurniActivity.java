package com.kubix.myjobwallet;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TurniActivity extends AppCompatActivity {

    ImageButton eliminaTurno;
    GridView listaTurni;
    String entrataCalcolo;
    String uscitaCalcolo;
    String dataSelezionata;
    int posizioneCorrente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turni);
        eliminaTurno = (ImageButton)findViewById(R.id.bottoneEliminaTurno);
        listaTurni=(GridView)findViewById(R.id.gridview1);
        List<String> li=new ArrayList<>();
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,li);
        dataAdapter.setDropDownViewResource(R.layout.activity_turni);
        eliminaTurno.setVisibility(View.INVISIBLE);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTurni);
        setTitle(R.string.toolbarTurni);
        toolbar.setTitleTextColor(getResources().getColor(R.color.lightText));
        setSupportActionBar(toolbar);

        listaTurni.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                eliminaTurno.setVisibility(View.VISIBLE);
                try {
                    //CALCOLA ORE LAVORATE TURNO SELEZIONATO
                    dataSelezionata = arg0.getItemAtPosition(position).toString().substring(0,10);
                    entrataCalcolo = arg0.getItemAtPosition(position).toString().substring(20, 25).replace(" ", "");
                    uscitaCalcolo = arg0.getItemAtPosition(position).toString().substring(35);
                    posizioneCorrente = position;
                    SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                    fmt.setLenient(false);

                    // Parses the two strings.
                    Date d1 = fmt.parse(entrataCalcolo);
                    Date d2 = fmt.parse(uscitaCalcolo);

                    // Calculates the difference in milliseconds.
                    long millisDiff = d2.getTime() - d1.getTime();

                    // Calculates days/hours/minutes/seconds.
                    int seconds = (int) (millisDiff / 1000 % 60);
                    int minutes = (int) (millisDiff / 60000 % 60);
                    int hours = (int) (millisDiff / 3600000 % 24);
                    int days = (int) (millisDiff / 86400000);

                    if (hours < 0 && minutes <0 || hours <0 && minutes >=0) {
                        hours = (int) (millisDiff / 3600000 % 24 + 23);
                        minutes = (int) (millisDiff / 60000 % 60 + 60);

                    }

                    if (minutes > 59){
                        minutes = minutes - 60;
                        hours = hours + 1;
                    }

                    int oreOrdinarie = VariabiliGlobali.oreOrdinarie;
                    if(oreOrdinarie > 0 && hours > oreOrdinarie){
                        Toast.makeText(TurniActivity.this, "PER IL TURNO SELEZIONATO RISULTANO " + hours + " ORE E " +minutes + " MINUTI LAVORATI, DI CUI " + "'"+String.valueOf(Integer.valueOf(hours - oreOrdinarie))+"' ORE E " + minutes + " MINUTI DI STRAORDINARIO"  , Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(TurniActivity.this, "PER IL TURNO SELEZIONATO RISULTANO " + hours + " ORE E " +minutes + " MINUTI LAVORATI" , Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(TurniActivity.this, "NON POSSO EFFETTUARE CALCOLI SU TURNI NON COMPLETI O DI RIPOSO", Toast.LENGTH_SHORT).show();
                }
            }
        });

        try {
            Cursor cr=MainActivity.db.rawQuery("SELECT * FROM Turni",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{

                        String campoData=cr.getString(cr.getColumnIndex("Data"));
                        String campoEntrata=cr.getString(cr.getColumnIndex("oraEntrata"));
                        String campoUscita=cr.getString(cr.getColumnIndex("oraUscita"));

                        li.add(campoData + "          " + campoEntrata + "          " + campoUscita);
                        listaTurni.setAdapter(dataAdapter);

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

    public void eliminaTurno(View v){
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("ELIMINA TURNO");
            builder.setMessage("IL TURNO VERRA' ELIMINATO, PROCEDERE ?");
            builder.setPositiveButton("SI, ELIMINA",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            if (uscitaCalcolo.equals("IN SERVIZIO")) {
                                MainActivity.db.execSQL("DELETE FROM Turni WHERE Data = '" + dataSelezionata + "' AND oraEntrata = '" + entrataCalcolo + "' AND oraUscita = 'IN SERVIZIO'");
                                MainActivity.db.execSQL("DELETE FROM Controlli Where Data = '" + dataSelezionata + "'");
                                dialog.dismiss();
                                Toast.makeText(TurniActivity.this, "TURNO RIMOSSO CON SUCCESSO", Toast.LENGTH_SHORT).show();
                                finish();
                            }else if (uscitaCalcolo.equals(" RIPOSO")){
                                MainActivity.db.execSQL("DELETE FROM Turni WHERE Data = '" + dataSelezionata + "' AND oraEntrata = 'RIPOSO' AND oraUscita = 'RIPOSO'");
                                MainActivity.db.execSQL("DELETE FROM Controlli Where Data = '" + dataSelezionata + "'");
                                dialog.dismiss();
                                Toast.makeText(TurniActivity.this, "TURNO RIMOSSO CON SUCCESSO", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                MainActivity.db.execSQL("DELETE FROM Turni WHERE Data = '"+dataSelezionata+"' AND oraEntrata = '"+entrataCalcolo+"' AND oraUscita = '"+uscitaCalcolo+"'");
                                MainActivity.db.execSQL("DELETE FROM Controlli Where Data = '"+dataSelezionata+"'");
                                eliminaTurno.setVisibility(View.INVISIBLE);
                                dialog.dismiss();
                                Toast.makeText(TurniActivity.this, "TURNO RIMOSSO CON SUCCESSO", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }
                    });
            builder.setNegativeButton("NON ELIMINARE",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            eliminaTurno.setVisibility(View.INVISIBLE);
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void apriCalcoloPaga (View v){
        startActivity (new Intent(TurniActivity.this, ElaborazioniActivity.class));
    }
}
