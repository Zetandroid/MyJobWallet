package com.kubix.myjobwallet;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfiloActivity extends AppCompatActivity{

    //TODO INDICIZZAZIONE OGGETTI ORA
    public static TextView oreOrdinarieText;
    public static TextView pagaOrariaText;
    public static TextView pagaStraordinariaText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfilo);
        setTitle(R.string.toolbarProfile);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        setSupportActionBar(toolbar);

        //TODO BOTTONE DATI
        Button button = (Button) findViewById(R.id.bottoneDati);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ProfiloDatiActivity.class);
                startActivity(i);
            }
        });


        //TODO INDIRIZZAMENTO OGGETTI CALCOLO ORA
        oreOrdinarieText = (TextView) findViewById(R.id.txtOre);
        pagaOrariaText = (TextView) findViewById(R.id.txtPagaOraria);
        pagaStraordinariaText = (TextView) findViewById(R.id.txtStraordinaria);

        caricaDatiProfilo();
    }

    public void caricaDatiProfilo(){

        //TODO CARICA INFO PROFILO DA DATABASE
        try {
            Cursor cr=MainActivity.db.rawQuery("SELECT * FROM InfoProfilo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        oreOrdinarieText.setText(cr.getString(cr.getColumnIndex("OreOrdinarie")).toString());
                        pagaOrariaText.setText(cr.getString(cr.getColumnIndex("NettoOrario")).toString());
                        pagaStraordinariaText.setText(cr.getString(cr.getColumnIndex("NettoStraordinario")).toString());
                    }while (cr.moveToNext());
                }else{

                }
            }
            cr.close();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}
