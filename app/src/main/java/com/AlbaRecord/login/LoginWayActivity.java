package com.AlbaRecord.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;

import com.AlbaRecord.Boss.BossMainActivity;
import com.AlbaRecord.Employ.EmployMainActivity;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginWayActivity extends AppCompatActivity implements View.OnClickListener {
    private Button boss,employee;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();;
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginway);
        boss=findViewById(R.id.bosswaybutton);
        employee=findViewById(R.id.employeewaybutton);
        boss.setOnClickListener(this);
        employee.setOnClickListener(this);

        if (mAuth.getCurrentUser() != null) {
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 profile 액티비티를 연다.
            mStore.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //Log.d("로그인웨이",documentSnapshot.toString());
                    BossModel bossModel =documentSnapshot.toObject(BossModel.class);
                    Log.d("로그인웨이", bossModel.toString());
                    if(bossModel.getFlag()==1){
                        startActivity(new Intent(getApplicationContext(), EmployMainActivity.class));
                    }else{
                        startActivity(new Intent(getApplicationContext(), BossMainActivity.class));
                    }
                }
            });

        }


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