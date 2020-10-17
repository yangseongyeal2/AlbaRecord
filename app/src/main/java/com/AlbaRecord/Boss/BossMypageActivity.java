package com.AlbaRecord.Boss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.R;
import com.AlbaRecord.login.FindActivity;
import com.AlbaRecord.login.LoginBossActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BossMypageActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText brand,phone;
    Button setbrand,setpassword,setphone;
    TextView email;
    String emailAddress="";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_mypage);
        initViewID();
        RetreiveBossInfo();
    }

    private void initViewID() {
        brand=(EditText)findViewById(R.id.brand);
        setbrand=(Button)findViewById(R.id.setbrand);
        setbrand.setOnClickListener(this);
        setpassword=(Button)findViewById(R.id.setpassword);
        setpassword.setOnClickListener(this);
        email=(TextView)findViewById(R.id.email);
        setphone=(Button)findViewById(R.id.setphone);
        setphone.setOnClickListener(this);
        phone=(EditText) findViewById(R.id.phone);
        progressDialog= progressDialog = new ProgressDialog(this);
    }

    private void RetreiveBossInfo() {
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                BossModel bossModel=documentSnapshot.toObject(BossModel.class);
                brand.setText(bossModel.getBrand());
                emailAddress=bossModel.getEmail();
                email.setText(bossModel.getEmail());
                phone.setText(bossModel.getPhoneNumber());

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setpassword:
                progressDialog.setMessage("처리중입니다. 잠시 기다려 주세요...");
                progressDialog.show();
                mAuth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
//                                    Toast.makeText(FindActivity.this, "이메일을 보냈습니다.", Toast.LENGTH_LONG).show();
                                    AlertDialog.Builder ad = new AlertDialog.Builder(BossMypageActivity.this);
                                    ad.setIcon(R.mipmap.ic_launcher);
                                    ad.setTitle("비밀번호 재설정");
                                    ad.setMessage("본인의 Email에 비밀번호 재설정 Link를 보냈습니다");
                                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();

                                        }
                                    });
                                    ad.show();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(BossMypageActivity.this, "메일 보내기 실패!", Toast.LENGTH_LONG).show();
                                }

                            }
                        });

                break;
            case R.id.setbrand:
            if(!brand.getText().toString().isEmpty()){
                db.collection("users")
                        .document(mAuth.getCurrentUser().getUid())
                        .update("brand",brand.getText().toString().trim())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"브랜드명이 수정되었습니다",Toast.LENGTH_SHORT).show();
                    }
                });
            }
                break;
            case R.id.setphone:
                db.collection("users")
                        .document(mAuth.getCurrentUser().getUid())
                        .update("phoneNumber",phone.getText().toString().trim())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"휴대폰번호정보가 수정되었습니다",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}