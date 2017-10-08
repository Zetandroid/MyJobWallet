package com.kubix.myjobwallet;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class ProfileActivity extends AppCompatActivity{



    //TODO CALCOLO ORA
    EditText oreOrdinarieText;
    EditText pagaOrariaText;
    EditText pagaStraordinariaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfilo);
        setTitle(R.string.toolbarProfile);
        toolbar.setTitleTextColor(getResources().getColor(R.color.lightText));
        setSupportActionBar(toolbar);

        //TODO CALCOLO ORA
        oreOrdinarieText = (EditText) findViewById(R.id.oreOrdinarieText);
        pagaOrariaText = (EditText) findViewById(R.id.pagaOrariaText);
        pagaStraordinariaText = (EditText) findViewById(R.id.pagaStraordinariText);



        Toast.makeText(this, "InfoProfilo", Toast.LENGTH_SHORT).show();

        //TODO CARICA INFO PROFILO DA DATABASE
        try {
            Cursor cr=MainActivity.db.rawQuery("SELECT * FROM InfoProfilo",null);
            if(cr!=null){
                if(cr.moveToFirst()){
                    do{
                        oreOrdinarieText.setText(cr.getString(cr.getColumnIndex("OreOrdinarie")));
                        pagaOrariaText.setText(cr.getString(cr.getColumnIndex("NettoOrario")));
                        pagaStraordinariaText.setText(cr.getString(cr.getColumnIndex("NettoStraordinario")));
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
      //TODO METODO AGGIORNAMENTO INFO ORE E PAGA
    public void aggiornaInfoProfilo(View v){
        if(!oreOrdinarieText.getText().toString().equals("") && !pagaOrariaText.getText().toString().equals("") && !pagaStraordinariaText.getText().toString().equals("")){
            MainActivity.db.execSQL("UPDATE InfoProfilo SET OreOrdinarie = '"+oreOrdinarieText.getText().toString()+"', NettoOrario = '"+pagaOrariaText.getText().toString()+"', NettoStraordinario = '"+pagaStraordinariaText.getText().toString()+"' WHERE ID = '0'");
            VariabiliGlobali.oreOrdinarie = Integer.valueOf(oreOrdinarieText.getText().toString());
            VariabiliGlobali.nettoOrario = Integer.valueOf(pagaOrariaText.getText().toString());
            VariabiliGlobali.nettoStraordinario = Integer.valueOf(pagaStraordinariaText.getText().toString());
            Toast.makeText(this, "Info profilo aggiornate con successo.", Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(this, "I dati profilo devono essere compilati correttamente.", Toast.LENGTH_SHORT).show();
        }
    }
}
