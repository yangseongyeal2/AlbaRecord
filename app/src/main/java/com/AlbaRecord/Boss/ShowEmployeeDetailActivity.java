package com.AlbaRecord.Boss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.AlbaRecord.Adapter.EvaluateAdapter;
import com.AlbaRecord.Employ.EmployeeBossSearchActivity;
import com.AlbaRecord.Employ.ShowBossActivity;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.Model.EvaluateModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShowEmployeeDetailActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String DocumentId;;
    RecyclerView recyclerView;
    EvaluateModel evaluateModel;
    BossModel bossModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_employee_detail);
        DocumentId=getIntent().getStringExtra("DocumentId");
        recyclerView=findViewById(R.id.recyclerView);
        RetreiveMyInfo();

    }

    private void RetreiveMyInfo() {
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                bossModel=documentSnapshot.toObject(BossModel.class);
                RetrieveEvaluateInfo();
            }
        });
    }

    private void RetrieveEvaluateInfo(){
        db.collection("users")
                .document(DocumentId).collection("Evaluate")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<EvaluateModel> evaluateModels=new ArrayList<>();
                        boolean flag=false;
                        for (QueryDocumentSnapshot document : task.getResult()){
                            flag=true;
                            evaluateModel=document.toObject(EvaluateModel.class);
                            evaluateModels.add(evaluateModel);
                        }
                        if(!flag) {
                            //다이어로그 추가
                            AlertDialog.Builder ad = new AlertDialog.Builder(ShowEmployeeDetailActivity.this);
                            ad.setIcon(R.mipmap.ic_launcher);
                            ad.setTitle("직원 평가");
                            ad.setMessage("아직 직원님에 대한 평가가 이루어지지 않았습니다.");
                            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), ShowEmployeeActivity.class));
                                    dialog.dismiss();
                                }
                            });
                            ad.show();
                        }
                        EvaluateAdapter evaluateAdapter=new EvaluateAdapter(evaluateModels,getApplicationContext(),bossModel);
                        recyclerView.setAdapter(evaluateAdapter);
                    }
                });
    }
}