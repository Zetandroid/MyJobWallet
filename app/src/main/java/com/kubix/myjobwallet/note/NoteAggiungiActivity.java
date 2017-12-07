package com.kubix.myjobwallet.note;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
        salvaNota = (ImageView) findViewById(R.id.tastoSalvaNota);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAggiungiNota);
        setTitle(R.string.toolbarNoteAggiungi);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    public void scriviNotaInDB(View v) {
        try {
            if (!titoloNota.getText().toString().equals("") && !corpoNota.getText().toString().equals("")) {
                MainActivity.db.execSQL("INSERT INTO Note (Titolo, Nota) VALUES ('" + titoloNota.getText().toString() + "', '" + corpoNota.getText().toString() + "')");
                titoloNota.setText("");
                corpoNota.setText("");
                Snackbar.make(v, R.string.dati_inseriti_successo, Snackbar.LENGTH_LONG).show();
                onBackPressed();
            } else {
                Snackbar.make(v, R.string.compila_tutti_dati, Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Freccia Indietro
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
