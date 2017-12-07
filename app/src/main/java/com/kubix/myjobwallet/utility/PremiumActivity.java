package com.kubix.myjobwallet.utility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kubix.myjobwallet.BuildConfig;
import com.kubix.myjobwallet.MainActivity;
import com.kubix.myjobwallet.R;
import com.kubix.myjobwallet.util.IabHelper;
import com.kubix.myjobwallet.util.IabResult;
import com.kubix.myjobwallet.util.Inventory;
import com.kubix.myjobwallet.util.Purchase;
import com.kubix.myjobwallet.util.Security;

import java.security.PublicKey;

public class PremiumActivity extends AppCompatActivity {

    private static final String TAG = "com.kubix.myjobwallet";
    IabHelper mHelper;
    static final String ITEM_SKU ="com.kubix.myjobwallet.pro" ;

    private Button bottoneRipristina;
    private Button bottoneAcquista;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        if (VariabiliGlobali.statoPremium.equals("SI")){
            bottoneRipristina.setEnabled(true);
        }
    }

    protected void onStart() {
        super.onStart();
        bottoneAcquista = (Button) findViewById(R.id.btnAcquista);
        bottoneAcquista.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                Toast.makeText(getBaseContext(), R.string.prossimamente , Toast.LENGTH_SHORT ).show();
            }
        });
        bottoneRipristina = (Button) findViewById(R.id.btnRipristana);
        bottoneRipristina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)  {
                Toast.makeText(getBaseContext(), R.string.prossimamente , Toast.LENGTH_SHORT ).show();
            }
        });
        bottoneRipristina.setEnabled(true);

        //TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPremium);
        setTitle(R.string.toolbarPremium);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt9EzTDIwPWeQUYYAjvPsutoXF1m+HvH5QO4jEjJkeFKvcNYVt99nTjJbT+DZ/AEpoLPhkFaFiUasFOdN2L262GJxg9Iz4bFXeYswievQ8afvWqsjkLNcuuHKnE8ZFTf/jlHkXsce1VsHsmRL8a1l7iJ/B93nS7aZb8wpIB8k54561diPLxS1pEUTxPTBjxPyQNb3XYmmMFJpJaowAPSG2smleZePka+I0FfLHufvGbOUcbGN239m9IPESHcgs20Mudx6mK0BzD77kBHgDNz6jutuqgP/UYEM2TXZf6+CVoO6/QOqeP3SNE3zNn68pUwfXbjKjCSDoEJZLI8I7OzrvwIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, getString(R.string.aquisto_fallito) + result);
                } else {
                    Log.d(TAG, getString(R.string.acquisto_ok));
                }
            }
        });


    }

        public void bottoneAcquista(View view) {
            mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                    mPurchaseFinishedListener, "com.kubix.myjobwallet.pro");
        }

        protected void onActivityResult(int requestCode, int resultCode,
        Intent data)
        {
            if (!mHelper.handleActivityResult(requestCode,
                    resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
                bottoneAcquista.setEnabled(false);
                
            }

        }

        public void consumeItem() {
            mHelper.queryInventoryAsync(mReceivedInventoryListener);
        }

        IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
                = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result,
                                                 Inventory inventory) {


                if (result.isFailure()) {
                    // Handle failure
                } else {
                    mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                            mConsumeFinishedListener);
                }
            }
        };

        IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
                new IabHelper.OnConsumeFinishedListener() {
                    public void onConsumeFinished(Purchase purchase,
                                                  IabResult result) {

                        if (result.isSuccess()) {
                            bottoneRipristina.setEnabled(true);

                            //CODICE SORGENTE CHE PARTE DOPO ACQUISTO RIUSCITO
                            MainActivity.db.execSQL("UPDATE Acquisti SET Premium = 'SI' WHERE Premium = 'NO'");
                            Toast.makeText(PremiumActivity.this, "Grazie per aver acquistato MyJobWallet Premium, al prossimo avvio dell'applicazione non visualizzerai più alcun tipo di annuncio", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        } else {
                            // handle error
                        }
                    }
                };

        @Override
        public void onDestroy() {
            mPurchaseFinishedListener.onDestroy();
            if (mHelper != null) mHelper.dispose();
            mHelper = null;
        }
    };

    public static boolean verifyPurchase(String base64PublicKey,
                                         String signedData, String signature) {
        if (TextUtils.isEmpty(signedData) ||
                TextUtils.isEmpty(base64PublicKey) ||
                TextUtils.isEmpty(signature)) {
            Log.e(TAG, "Purchase verification failed: missing data.");
            if (BuildConfig.DEBUG) {
                return true;
            }
            return false;
        }

        PublicKey key = Security.generatePublicKey(base64PublicKey);
        return Security.verify(key, signedData, signature);
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


