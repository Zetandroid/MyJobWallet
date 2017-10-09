package com.kubix.myjobwallet;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity{


    //TODO POPUP
    private Context mContext;
    private Activity mActivity;
    private RelativeLayout mRelativeLayout;
    private Button mButton;
    private PopupWindow mPopupWindow;

    //TODO CALCOLO ORA
    EditText oreOrdinarieText;
    EditText pagaOrariaText;
    EditText pagaStraordinariaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //TODO POPUP
        mContext = getApplicationContext();

        mActivity = ProfileActivity.this;

        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        mButton = (Button) findViewById(R.id.btndati);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);


                View customView = inflater.inflate(R.layout.popup_profilo,null);

                mPopupWindow = new PopupWindow(
                        customView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT

                );


                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(0.0f);
                }

                Button closeButton = (Button) customView.findViewById(R.id.btnChiudi);

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPopupWindow.dismiss();
                    }
                });
                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.BOTTOM,0,0);
            }
        });

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfilo);
        setTitle(R.string.toolbarProfile);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
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
