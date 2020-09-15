package com.AlbaRecord.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.AlbaRecord.R;


public class AgreeClauseActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree_clause);

        buttonAgree = (Button) findViewById(R.id.buttonAgree);

        buttonAgree.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonAgree) {
            finish();
            startActivity(new Intent(this, SignupActivity.class));
        }

    }
}
