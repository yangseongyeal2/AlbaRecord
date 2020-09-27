package com.AlbaRecord;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.AlbaRecord.login.LoginActivity;
import com.AlbaRecord.login.LoginWayActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;


import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private Button logout,salary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
        initViewId();

        salary.setOnClickListener(this);








    }

    private void initViewId() {
        salary=findViewById(R.id.salary);
    }

    private void initToolBar() {
        logout=findViewById(R.id.logout);
        logout.setOnClickListener(this);
        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginWayActivity.class));
                finish();
                break;
            case R.id.salary:
                startActivity(new Intent(getApplicationContext(), SalaryActivity.class));
                break;
        }

    }
}
