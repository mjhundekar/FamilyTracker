package com.example.mjhundekar.family_tracker;

/**
 * Created by umanggala on 10/20/16.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    public Button but1;

    Context mcontext;
    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void init(){
        but1 = (Button)findViewById(R.id.sign_in_button);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, GoogleSignIn.class);
                startActivity(intent);
            }
        });
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mcontext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

}