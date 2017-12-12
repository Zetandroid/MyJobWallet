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
import com.kubix.myjobwallet.setting.ClsSettings;

public class NoteAggiungiActivity extends AppCompatActivity {

    EditText titoloNota;
    EditText corpoNota;
    ImageView aggiungiCopertina;
    ImageView salvaNota;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ClsSettings settings = new ClsSettings(getBaseContext());
        if (settings.get_temadark()) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_aggiungi);
        titoloNota = (EditText) findViewById(R.id.testoTitoloSpesa);
        corpoNota = (EditText) findViewById(R.id.testoNota);
        salvaNota = (ImageView) findViewById(R.id.tastoSalvaNota);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAggiungiNota);
        setTitle("Aggiungi Nota");
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
                onBackPressed();
            } else {
                Snackbar.make(v, "Compila tutti i Dati", Snackbar.LENGTH_LONG).show();
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
