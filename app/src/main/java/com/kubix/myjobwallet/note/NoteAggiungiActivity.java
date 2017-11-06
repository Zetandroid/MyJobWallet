package com.kubix.myjobwallet.note;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;

public class NoteAggiungiActivity extends AppCompatActivity {

    EditText titoloNota;
    EditText corpoNota;
    ImageView aggiungiCopertina;
    ImageView salvaNota;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_aggiungi);
        titoloNota = (EditText) findViewById(R.id.testoTitoloSpesa);
        corpoNota = (EditText) findViewById(R.id.testoNota);
        aggiungiCopertina = (ImageView) findViewById(R.id.aggiungiCopertina);
        salvaNota = (ImageView) findViewById(R.id.tastoSalvaNota);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAggiungiNota);
        setTitle(R.string.toolbarNoteAggiungi);
        toolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoBianco));
        setSupportActionBar(toolbar);
    }

    public void scriviNotaInDB (View v){
        try{
           if(!titoloNota.getText().toString().equals("") && !corpoNota.getText().toString().equals("")){
               MainActivity.db.execSQL("INSERT INTO Note (Titolo, Nota) VALUES ('"+titoloNota.getText().toString()+"', '"+corpoNota.getText().toString()+"')");
               titoloNota.setText("");
               corpoNota.setText("");
               Snackbar.make(v, getString(R.string.nota_inserita), Snackbar.LENGTH_LONG).show();
           }else{
               Snackbar.make(v, getString(R.string.dati_nota), Snackbar.LENGTH_LONG).show();
           }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
