package com.AlbaRecord.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.AlbaRecord.Boss.BossMainActivity;
import com.AlbaRecord.Employ.EmployMainActivity;
import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class EmailCheckActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EmailCheckActivity";

    Button buttonAuthCheck;
    TextView textviewResend;
    Button emailCheckLogout;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_check);

        buttonAuthCheck = (Button) findViewById(R.id.buttonAuthCheck);
        textviewResend = (TextView) findViewById(R.id.textviewResend);
        emailCheckLogout = (Button) findViewById(R.id.emailCheckLogout);
        //String adress=getIntent().getStringExtra("주소");

        buttonAuthCheck.setOnClickListener(this);
        textviewResend.setOnClickListener(this);
        emailCheckLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == emailCheckLogout) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(mAuth.getCurrentUser().getUid());
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginWayActivity.class));
            finish();
        }

        if (view == buttonAuthCheck) {
            firebaseAuthCheck();
        }

        if (view == textviewResend) {
            sendEmail();
        }
    }

    // 유저의 이메일 인증 유무를 확인합니다.ㅗ
    public void firebaseAuthCheck() {
        firebaseAuth.getInstance().getCurrentUser().reload();
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();

        // 회원가입시 이메일 인증을 받은 경우
        if (firebaseUser.isEmailVerified() == true) {
            Toast.makeText(EmailCheckActivity.this, "인증 되었습니다", Toast.LENGTH_SHORT).show();
            String flag=getIntent().getStringExtra("Flag");
//            if (flag.equals("사장")){
//                Intent intent=new Intent(getApplicationContext(), BossMainActivity.class);
//                // intent.putExtra("주소",adress);
//                startActivity(intent);
//                finish();
//            }else {
//                Intent intent=new Intent(getApplicationContext(), EmployMainActivity.class);
//                // intent.putExtra("주소",adress);
//                startActivity(intent);
//                finish();
//            }
            startActivity(new Intent(getApplicationContext(),LoginWayActivity.class));
            finish();


        } else {
            Toast.makeText(EmailCheckActivity.this, "메일 인증이 이루어지지 않았습니다. \n다시 한번 확인해주세요!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendEmail() {
        firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailCheckActivity.this, "이메일 인증 메일을 전송했습니다. \n가입한 이메일에서 확인해주세요!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EmailCheckActivity.this, "인증 메일 전송에 실패했습니다. \n1분 뒤에 다시 시도해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
