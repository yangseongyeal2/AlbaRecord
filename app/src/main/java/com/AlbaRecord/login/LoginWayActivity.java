package com.AlbaRecord.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.AlbaRecord.R;

public class LoginWayActivity extends AppCompatActivity implements View.OnClickListener {
    private Button boss,employee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginway);
        boss=findViewById(R.id.bosswaybutton);
        employee=findViewById(R.id.employeewaybutton);
        boss.setOnClickListener(this);
        employee.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == boss) {
            startActivity(new Intent(this,BossSignupActivity.class));
        }
        if (v == employee) {
            startActivity(new Intent(this, EmployeeSignupActivity.class));
        }

    }
}