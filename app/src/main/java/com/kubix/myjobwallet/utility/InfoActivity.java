package com.kubix.myjobwallet.utility;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.kubix.myjobwallet.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarInfo);
        setTitle(R.string.toolbarInfo);
        toolbar.setTitleTextColor(getResources().getColor(R.color.coloreTestoBianco));
        setSupportActionBar(toolbar);

        Button googleMorgan = (Button) findViewById(R.id.googleMorgan);
        Button twitterMorgan = (Button) findViewById(R.id.twitterMorgan);
        Button googlelessio = (Button) findViewById(R.id.googleAlessio);
        Button twitterAlessio = (Button) findViewById(R.id.twitterAlessio);

        googleMorgan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/+MOWMO")));
            }
        });
        twitterMorgan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://twitter.com/_mowmo_")));
            }
        });

        googlelessio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://plus.google.com/u/0/108762755836299160559")));
            }
        });
        twitterAlessio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("")));
            }
        });
    }

}
