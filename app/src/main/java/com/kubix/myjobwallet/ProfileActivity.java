package com.kubix.myjobwallet;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity{

    //TODO INDICIZZAZIONE OGGETTI ORA
    TextView oreOrdinarieText;
    TextView pagaOrariaText;
    TextView pagaStraordinariaText;
    EditText aggiornaOrdinarie;
    EditText aggiornaPaga;
    EditText aggiornaPagaStraordinari;

    //TODO INDICIZZAZIONE OGGETTO FAB ANIMAZIONE
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfilo);
        setTitle(R.string.toolbarProfile);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        setSupportActionBar(toolbar);

        //TODO INDIRIZZAMENTO OGGETTI CALCOLO ORA
        oreOrdinarieText = (TextView) findViewById(R.id.txtOre);
        pagaOrariaText = (TextView) findViewById(R.id.txtPagaOraria);
        pagaStraordinariaText = (TextView) findViewById(R.id.txtStraordinaria);

        aggiornaOrdinarie = (EditText) findViewById(R.id.oreOrdinarieText);
        aggiornaPaga = (EditText) findViewById(R.id.pagaOrariaText);
        aggiornaPagaStraordinari = (EditText) findViewById(R.id.pagaStraordinariText);


        //TODO ANIMAZIONE FAB
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDiag();
            }
        });

        leggiDatiProfilo();

    }

    private void showDiag() {

        final View dialogView = View.inflate(this,R.layout.fab_dialog_profilo,null);
        final Dialog dialog = new Dialog(this,R.style.MyAlertDialogStyle);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);

        ImageView imageView = (ImageView)dialog.findViewById(R.id.closeDialogImg);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                revealShow(dialogView, false, dialog);
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                revealShow(dialogView, true, null);
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK){

                    revealShow(dialogView, false, dialog);
                    return true;
                }
                return false;
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

    }

    private void revealShow(View dialogView, boolean b, final Dialog dialog) {

        final View view = dialogView.findViewById(R.id.dialog);

        int w = view.getWidth();
        int h = view.getHeight();

        int endRadius = (int) Math.hypot(w, h);

        int cx = (int) (fab.getX() + (fab.getWidth()/2));
        int cy = (int) (fab.getY())+ fab.getHeight() + 56;


        if(b){
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx,cy, 0, endRadius);

            view.setVisibility(View.VISIBLE);
            revealAnimator.setDuration(600);
            revealAnimator.start();

        } else {

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);

                }
            });
            anim.setDuration(600);
            anim.start();

        }

    }

    public void leggiDatiProfilo(){

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

    //TODO METODO AGGIORNAMENTO INFO ORE E PAGA
    public void AggiornaDatiProfilo(View v){

        if(!aggiornaOrdinarie.getText().toString().equals("") && !aggiornaPaga.getText().toString().equals("") && !aggiornaPagaStraordinari.getText().toString().equals("")){
            MainActivity.db.execSQL("UPDATE InfoProfilo SET OreOrdinarie = '"+aggiornaOrdinarie.getText().toString()+"', NettoOrario = '"+aggiornaPaga.getText().toString()+"', NettoStraordinario = '"+aggiornaPagaStraordinari.getText().toString()+"' WHERE ID = '0'");
            VariabiliGlobali.oreOrdinarie = Integer.valueOf(aggiornaOrdinarie.getText().toString());
            VariabiliGlobali.nettoOrario = Integer.valueOf(aggiornaPaga.getText().toString());
            VariabiliGlobali.nettoStraordinario = Integer.valueOf(aggiornaPagaStraordinari.getText().toString());
            Toast.makeText(this, "Info profilo aggiornate con successo.", Toast.LENGTH_LONG).show();
            finish();
            oreOrdinarieText.setText(String.valueOf(VariabiliGlobali.oreOrdinarie));
            pagaOrariaText.setText(String.valueOf(VariabiliGlobali.nettoOrario));
            pagaStraordinariaText.setText(String.valueOf(VariabiliGlobali.nettoStraordinario));
        }else{
            Toast.makeText(this, "I dati profilo devono essere compilati correttamente.", Toast.LENGTH_SHORT).show();
        }
    }

}
