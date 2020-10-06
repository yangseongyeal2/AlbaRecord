package com.AlbaRecord.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.AlbaRecord.BossMainActivity;
import com.AlbaRecord.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginWayActivity extends AppCompatActivity implements View.OnClickListener {
    private Button boss,employee;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginway);
        boss=findViewById(R.id.bosswaybutton);
        employee=findViewById(R.id.employeewaybutton);
        boss.setOnClickListener(this);
        employee.setOnClickListener(this);

//        if (mAuth.getCurrentUser() != null) {
//            //이미 로그인 되었다면 이 액티비티를 종료함
//            finish();
//            //그리고 profile 액티비티를 연다.
//
//            startActivity(new Intent(getApplicationContext(), BossMainActivity.class)); //추가해 줄 ProfileActivity
//        }


    }

    @Override
    public void onClick(View v) {
        if (v == boss) {
            Intent bossIntent=new Intent(this, LoginBossActivity.class);
            bossIntent.putExtra("Flag","사장");
            startActivity(bossIntent);
            finish();
        }
        if (v == employee) {
            Intent employeeIntent=new Intent(this, LoginEmployeeActivity.class);
            employeeIntent.putExtra("Flag","직원");
            startActivity(employeeIntent);
            finish();
        }

    }
}