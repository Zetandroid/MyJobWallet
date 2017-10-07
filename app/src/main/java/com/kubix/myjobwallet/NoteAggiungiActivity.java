package com.kubix.myjobwallet;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class NoteAggiungiActivity extends AppCompatActivity {
    EditText titoloNota;
    EditText corpoNota;
    ImageView aggiungiCopertina;
    ImageView salvaNota;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_aggiungi);
        titoloNota = (EditText) findViewById(R.id.titoloNota);
        corpoNota = (EditText) findViewById(R.id.testoNota);
        aggiungiCopertina = (ImageView) findViewById(R.id.aggiungiCopertina);
        salvaNota = (ImageView) findViewById(R.id.tastoSalvaNota);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAggiungiNota);
        setTitle(R.string.toolbarNoteAggiungi);
        toolbar.setTitleTextColor(getResources().getColor(R.color.lightText));
        setSupportActionBar(toolbar);
    }

    public void scriviNotaInDB (View v){
        try{
           if(!titoloNota.getText().toString().equals("") && !corpoNota.getText().toString().equals("")){
               MainActivity.db.execSQL("INSERT INTO Note (Titolo, Nota) VALUES ('"+titoloNota.getText().toString()+"', '"+corpoNota.getText().toString()+"')");
               Toast.makeText(this, "Nota inserita correttamente", Toast.LENGTH_SHORT).show();
               finish();
           }else{
               Toast.makeText(this, "Inserire tutti i dati per la nota.", Toast.LENGTH_SHORT).show();
           }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
