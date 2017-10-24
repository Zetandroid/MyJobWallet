package com.kubix.myjobwallet.profilo;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;

public class ProfiloActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    //TODO INDICIZZAZIONE OGGETTI ORA
    public static TextView oreOrdinarieText;
    public static TextView pagaOrariaText;
    public static TextView pagaStraordinariaText;

    //TODO LOGIN GOOGLE
    private ProfiloActivity mContext;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private TextView txtName, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        //TODO LOGIN
        mContext = ProfiloActivity.this;
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);

        findViewById(R.id.bottoneLoginGoogle).setOnClickListener(this);
        findViewById(R.id.bottoneDisconnetti).setOnClickListener(this);
        findViewById(R.id.bottoneRevoca).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(this, mContext /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.bottoneLoginGoogle);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            update_ui("From cache: " + result.getSignInAccount().getDisplayName() + " email: " + result.getSignInAccount().getEmail());

            Glide.with(this).load(result.getSignInAccount().getPhotoUrl()).into((ImageView) findViewById(R.id.imgProfilePic));

            handleSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }


        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfilo);
        setTitle(R.string.toolbarProfile);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoTitolo));
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

    @Override
    public void onResume(){
        super.onResume();
        caricaDatiProfilo();
    }

    public void caricaDatiProfilo(){

        //TODO CARICA INFO PROFILO DA DATABASE
        try {
            Cursor cr= MainActivity.db.rawQuery("SELECT * FROM InfoProfilo",null);
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

    //EVENTO TEMPORANEO
    public void alertAggiornamento(View v){
        Toast.makeText(this, getString(R.string.funzioneConAggiornamento), Toast.LENGTH_SHORT).show();
    }

    //TODO LOGIN GOOGLE
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email);

            txtName.setText(personName);
            txtEmail.setText(email);

            Glide.with(this).load(acct.getPhotoUrl()).into((ImageView) findViewById(R.id.imgProfilePic));
            updateUI(true);
        } else {
            updateUI(false);
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        update_ui("onConnectionFailed");
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.cardLogin).setVisibility(View.GONE);
            findViewById(R.id.bottoneDisconnetti).setVisibility(View.VISIBLE);
            findViewById(R.id.bottoneRevoca).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.bottoneLoginGoogle).setVisibility(View.VISIBLE);
            findViewById(R.id.bottoneDisconnetti).setVisibility(View.GONE);
            findViewById(R.id.bottoneRevoca).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottoneLoginGoogle:
                signIn();
                break;
            case R.id.bottoneDisconnetti:
                signOut();
                break;
            case R.id.bottoneRevoca:
                revokeAccess();
                break;
        }
    }

    private void update_ui(String message) {
        txtName.setText(message);

    }
}

