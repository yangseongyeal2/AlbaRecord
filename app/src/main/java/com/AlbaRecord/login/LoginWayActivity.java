package com.AlbaRecord.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import com.AlbaRecord.Boss.BossMainActivity;
import com.AlbaRecord.Employ.EmployMainActivity;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginWayActivity extends AppCompatActivity implements View.OnClickListener {
    private Button boss, employee;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginway);
        boss = findViewById(R.id.bosswaybutton);
        employee = findViewById(R.id.employeewaybutton);
        boss.setOnClickListener(this);
        employee.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        if (mAuth.getCurrentUser() != null) {
            if (mAuth.getCurrentUser().isEmailVerified()) {
                progressDialog.setMessage("자동 로그인중입니다. 잠시기다려주세요...");
                progressDialog.show();
                //이미 로그인 되었다면 이 액티비티를 종료함

                //그리고 profile 액티비티를 연다.
                mStore.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //Log.d("로그인웨이",documentSnapshot.toString());
                        BossModel bossModel = documentSnapshot.toObject(BossModel.class);
                        Log.d("로그인웨이", bossModel.toString());
                        if (bossModel.getFlag() == 1) {
                            startActivity(new Intent(getApplicationContext(), EmployMainActivity.class));
                        } else {
                            startActivity(new Intent(getApplicationContext(), BossMainActivity.class));
                        }
                        progressDialog.dismiss();
                        finish();
                    }
                });
                FirebaseMessaging.getInstance().subscribeToTopic(mAuth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("구독하기", "성공");
                    }
                });

            }else{
                startActivity(new Intent(getApplicationContext(), EmailCheckActivity.class)); //추가해 줄 ProfileActivity
            }

        }


    }

    @Override
    public void onClick(View v) {
        if (v == boss) {
            Intent bossIntent = new Intent(this, LoginBossActivity.class);
            bossIntent.putExtra("Flag", "사장");
            startActivity(bossIntent);
            //  finish();
        }
        if (v == employee) {
            Intent employeeIntent = new Intent(this, LoginEmployeeActivity.class);
            employeeIntent.putExtra("Flag", "직원");
            startActivity(employeeIntent);
            //    finish();
        }

    }
}