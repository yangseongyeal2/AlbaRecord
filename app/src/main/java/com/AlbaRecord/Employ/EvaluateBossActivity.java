package com.AlbaRecord.Employ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.AlbaRecord.Model.BossEvaluateModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EvaluateBossActivity extends AppCompatActivity implements View.OnClickListener {
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8, checkBox9,
            checkBox10, checkBox11, checkBox12, checkBox13;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button submit;
    private ArrayList<String> stringlist = new ArrayList<>();
    private BossEvaluateModel bossEvaluateModel;
    private String BossDocumentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_boss);
        initViewId();
        BossDocumentId = getIntent().getStringExtra("DocumentId");
        submit.setOnClickListener(this);

    }

    private void initViewId() {
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBox5 = findViewById(R.id.checkBox5);
        checkBox6 = findViewById(R.id.checkBox6);
        checkBox7 = findViewById(R.id.checkBox7);
        checkBox8 = findViewById(R.id.checkBox8);
        checkBox9 = findViewById(R.id.checkBox9);
        checkBox10 = findViewById(R.id.checkBox10);
        checkBox11 = findViewById(R.id.checkBox11);
        checkBox12 = findViewById(R.id.checkBox12);
        checkBox13 = findViewById(R.id.checkBox13);


        submit = findViewById(R.id.submit);

    }
//    public String Checked(View v) { // 체크되었을 때 동작할 메소드 구현
//        // TODO Auto-generated method stub
//        String resultText = ""; // 체크되었을 때 값을 저장할 스트링 값
//        if (checkBox1.isChecked()) { // option1 이 체크되었다면
//            resultText = "option1";
//        }
//        if (option2.isChecked()) {
//            resultText = "option2"; // option2 이 체크되었다면
//        }
//
//        return resultText; // 체크된 값 리턴
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (checkBox1.isChecked()) {
                    stringlist.add("근무환경 쾌적해요");

                }
                if (checkBox2.isChecked()) {
                    stringlist.add("자유로운 분위기예요");
                }
                if (checkBox3.isChecked()) {
                    stringlist.add("스펙에 도움되어요");
                }
                if (checkBox4.isChecked()) {
                    stringlist.add("퇴근시간 칼이예요");
                }
                if (checkBox5.isChecked()) {
                    stringlist.add("인센티브 챙겨줘요");
                }
                if (checkBox6.isChecked()) {
                    stringlist.add("주휴수당 챙겨줘요");
                }
                if (checkBox7.isChecked()) {
                    stringlist.add("초보자도 금방 배워요");
                }
                if (checkBox8.isChecked()) {
                    stringlist.add("식대 따로 나와요");
                }
                if (checkBox9.isChecked()) {
                    stringlist.add("업무량 조금 많아요");
                }
                if (checkBox10.isChecked()) {
                    stringlist.add("잔일도 해요");
                }
                if (checkBox11.isChecked()) {
                    stringlist.add("이직률이 높아요");
                }
                if (checkBox12.isChecked()) {
                    stringlist.add("경직된 분위기예요");
                }
                if (checkBox13.isChecked()) {
                    stringlist.add("진상고객 가끔 있어요");
                }

                Date currenttime = Calendar.getInstance().getTime();
                String date_text = new SimpleDateFormat("yyyy년 MM월", Locale.getDefault()).format(currenttime);
                bossEvaluateModel = new BossEvaluateModel(stringlist, date_text);
                db.collection("users")
                        .document(BossDocumentId)
                        .collection("Evaluate")
                        .document(date_text)
                        .set(bossEvaluateModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),BossExEvaluateActivity.class));
                        finish();
                    }
                });


                break;
        }
    }
}