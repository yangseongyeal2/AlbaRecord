package com.AlbaRecord.Boss;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.AlbaRecord.Adapter.EvaluateAdapter;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.Model.EvaluateModel;
import com.AlbaRecord.R;
import com.bumptech.glide.Glide;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class EvaluateEmployeeActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView position, workstart, phone, name, age;
    ImageView photo;
    String employeeDocumentId;
    CrystalSeekbar diligenceseekBar,flexibilityseekBar,masteryseekBar,attitudeseekBar,communicationseekBar;
    int diligence=0,flexibility=0,mastery=0,attitude=0,communication=0;
    Button addevaluation,call;
    EditText hashtag,hashtagdetail;
    TextView today;
    String brandname="";
    String careerthing="";
    EmployeeModel employeeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_employee);
        initViewId();
        employeeDocumentId = getIntent().getStringExtra("DocumentId");
        Date currenttime= Calendar.getInstance().getTime();
        String date_text=new SimpleDateFormat("yyyy년 MM월", Locale.getDefault()).format(currenttime);
        today.setText(date_text);



       //직원정보 불러오기
        RetrieveEmployInfo();
        //직원디테일 불러오기
        RetrieveEmployInfo_detail();
        //사장 정보 가져오기
        RetrieveBossInfo();
        //버튼클릭
        addevaluation.setOnClickListener(this);
        //평가점수
        diligenceseekBar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                diligence=value.intValue();
            }
        });
        flexibilityseekBar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                flexibility=value.intValue();
            }
        });
        masteryseekBar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                mastery=value.intValue();
            }
        });
        attitudeseekBar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                attitude=value.intValue();
            }
        });
        communicationseekBar.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number value) {
                communication=value.intValue();
            }
        });




    }

    private void RetrieveBossInfo() {
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                BossModel bossModel=documentSnapshot.toObject(BossModel.class);
                brandname=bossModel.getBrand();


            }
        });
    }

    private void RetrieveEmployInfo_detail() {

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("MyEmployeeInfo")
                .document(employeeDocumentId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try {
                            Map<String, Object> data=documentSnapshot.getData();
                            Log.d("어댑터",data.toString());
                            workstart.setText(data.get("근무시작시간").toString());
                            careerthing=data.get("직급").toString();
                            position.setText(data.get("직급").toString());
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    }
                });
    }


    private void RetrieveEmployInfo() {
        db.collection("users").document(employeeDocumentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                employeeModel=documentSnapshot.toObject(EmployeeModel.class);
                assert employeeModel != null;
                phone.setText(employeeModel.getPhonenumber());
                name.setText(employeeModel.getName());
                age.setText(employeeModel.getAge());
                Glide.with(photo).load(employeeModel.getPhoto()).into(photo);

            }
        });
    }

    private void initViewId() {
        photo=(ImageView)findViewById(R.id.photo);
        position = (TextView) findViewById(R.id.position);
        workstart = (TextView) findViewById(R.id.workstart);
        phone = (TextView) findViewById(R.id.phone);
        name = (TextView) findViewById(R.id.name_text);
        age = (TextView) findViewById(R.id.age_text);
        diligenceseekBar=(CrystalSeekbar)findViewById(R.id.diligenceseekBar);
        flexibilityseekBar=(CrystalSeekbar)findViewById(R.id.flexibilityseekBar);
        masteryseekBar=(CrystalSeekbar)findViewById(R.id.masteryseekBar);
        attitudeseekBar=(CrystalSeekbar)findViewById(R.id.attitudeseekBar);
        communicationseekBar=(CrystalSeekbar)findViewById(R.id.communicationseekBar);
        addevaluation=(Button)findViewById(R.id.addevaluation);
        hashtag=(EditText)findViewById(R.id.hashtag);
        hashtagdetail=(EditText)findViewById(R.id.hashtagdetail);
        today=(TextView)findViewById(R.id.today);
        call=(Button)findViewById(R.id.call);
        call.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addevaluation:
                AlertDialog.Builder ad = new AlertDialog.Builder(EvaluateEmployeeActivity.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("직원 평가");
                ad.setMessage("직원 평가를 저장하시겠습니까 ?\n(한달내에 수정 가능합니다.)");
                ad.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         finish();
                        startActivity(new Intent(getApplicationContext(),MyEmployeeActivity.class));
                        Date currenttime= Calendar.getInstance().getTime();
                        String date_text=new SimpleDateFormat("yyyy년 MM월", Locale.getDefault()).format(currenttime);
                        String hashtag_str=hashtag.getText().toString();
                        String hashtagdetail_str=hashtagdetail.getText().toString();
                        EvaluateModel evaluateModel=new EvaluateModel(diligence,flexibility,mastery,attitude,communication,hashtag_str,hashtagdetail_str,date_text,brandname,careerthing);

                        db.collection("users")
                                .document(employeeDocumentId)
                                .collection("Evaluate")
                                .document(date_text)
                                .set(evaluateModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("평가성공","업로드성공");
                                finish();
                                startActivity(new Intent(getApplicationContext(),MyEmployeeActivity.class));
                            }
                        });


                        dialog.dismiss();
                    }
                });
                ad.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                ad.show();
                break;
            case R.id.call:
                String tel="tel:"+employeeModel.getPhonenumber();
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
                break;
        }
    }



}