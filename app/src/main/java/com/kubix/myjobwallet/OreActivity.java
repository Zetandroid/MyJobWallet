package com.kubix.myjobwallet;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class OreActivity extends AppCompatActivity{
    DatePicker dataTurno;
    TextView dataScritta;
    public static String thisDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ore);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOre);
        setTitle(R.string.toolbarOre);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        setSupportActionBar(toolbar);

        //INDICIZZA COMPONENTI
        dataTurno = (DatePicker) findViewById(R.id.datePicker);
    }

    public void apriStorico(View v){
        Intent intent = new Intent(this, TurniActivity.class);
        startActivity(intent);
    }

    public void apriInserimentoOre(View v){

        try{
            //PRELEVA DATA ODIERNA
            int giorno = dataTurno.getDayOfMonth();
            int mese = dataTurno.getMonth() + 1;
            int anno = dataTurno.getYear();

            String giornoS = String.format("%02d", giorno);
            String meseS = String.format("%02d", mese);
            String annoS = String.format("%02d", anno);
            thisDate = String.valueOf(giornoS + "/" + meseS + "/" + annoS);
            VariabiliGlobali.dataTurno = thisDate;
            Intent intent = new Intent(this, CalendarioActivity.class);
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
}
