package com.AlbaRecord.Boss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.AlbaRecord.Adapter.BossEvaluateAdapter;
import com.AlbaRecord.Adapter.BossEvalute_detailAdapter;
import com.AlbaRecord.Adapter.CommonAdapter;
import com.AlbaRecord.Board.BoardActivity;
import com.AlbaRecord.Board.DetailActivity;
import com.AlbaRecord.Model.BoardInfo;
import com.AlbaRecord.Model.BossEvaluateModel;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.R;
import com.AlbaRecord.login.DaumWebViewActivity;
import com.AlbaRecord.login.FindActivity;
import com.AlbaRecord.login.LoginBossActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BossMypageActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText brand,phone;
    Button setbrand,setpassword,setphone,setaddress;
    TextView email,adress;
    String emailAddress="";
    String chagedAdress="";
    private ProgressDialog progressDialog;
    RecyclerView recyclerView;
    BossEvaluateAdapter bossEvaluateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_mypage);
        chagedAdress=getIntent().getStringExtra("주소");
        try {
            if(!chagedAdress.isEmpty()){
                db.collection("users")
                        .document(mAuth.getCurrentUser().getUid())
                        .update("address",chagedAdress)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"주소변경이 완료되었습니다",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        initViewID();
        RetreiveBossInfo();
        RetreiveBossEvaluateInfo();
    }

    private void RetreiveBossEvaluateInfo() {
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("Evaluate")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    ArrayList<BossEvaluateModel>arr=new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        BossEvaluateModel bossEvaluateModel = document.toObject(BossEvaluateModel.class);
                        arr.add(bossEvaluateModel);


                    }
                    bossEvaluateAdapter=new BossEvaluateAdapter(arr);
                    recyclerView.setAdapter(bossEvaluateAdapter);


                } else {

                }
            }
        });
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
        adress=findViewById(R.id.adress);
        setaddress=findViewById(R.id.setaddress);
        setaddress.setOnClickListener(this);
        recyclerView=findViewById(R.id.recyclerView);
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
                adress.setText(bossModel.getAddress());

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
                                    ad.setIcon(R.drawable.main);
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
            case R.id.setaddress:
                Intent intent=new Intent(this, DaumWebViewActivity.class);
                intent.putExtra("flag","사장MyPage");
                startActivity(intent); //추가해 줄 로그인 액티비티
                finish();
                break;
        }
    }
}