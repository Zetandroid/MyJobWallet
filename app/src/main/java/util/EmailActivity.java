package util;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kubix.myjobwallet.R;

/**
 * Created by mowmo on 13/10/17.
 */

public class EmailActivity extends AppCompatActivity{

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        //TODO TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEmail);
        setTitle(R.string.toolbarEmail);
        toolbar.setTitleTextColor(getResources().getColor(R.color.testoBianco));
        setSupportActionBar(toolbar);

        findViewById(R.id.bottoneEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });
    }


    //Start a new activity for sending a feedback email
    private void sendFeedback() {
        final Intent _Intent = new Intent(android.content.Intent.ACTION_SEND);
        _Intent.setType("text/html");
        _Intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ getString(R.string.email) });
        _Intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.emailOggetto));
        _Intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.emailTesto));
        startActivity(Intent.createChooser(_Intent, getString(R.string.emailTitolo)));
    }
}
