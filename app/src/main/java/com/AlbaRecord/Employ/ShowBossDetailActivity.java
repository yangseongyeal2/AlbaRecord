package com.AlbaRecord.Employ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.AlbaRecord.Adapter.BossEvaluateAdapter;
import com.AlbaRecord.Adapter.EvaluateAdapter;
import com.AlbaRecord.Boss.ShowEmployeeActivity;
import com.AlbaRecord.Boss.ShowEmployeeDetailActivity;
import com.AlbaRecord.Model.BossEvaluateModel;
import com.AlbaRecord.Model.EvaluateModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class ShowBossDetailActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    BossEvaluateAdapter bossEvaluateAdapter;
    String DocumentId="";
    BossEvaluateModel bossEvaluateModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_boss_detail);
        recyclerView=findViewById(R.id.recyclerView);
        DocumentId=getIntent().getStringExtra("DocumentId");
        db.collection("users")
                .document(DocumentId)
                .collection("Evaluate")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<BossEvaluateModel> evaluateModels=new ArrayList<>();
                boolean flag=false;
                for (QueryDocumentSnapshot document : task.getResult()){
                    flag=true;
                    bossEvaluateModel=document.toObject(BossEvaluateModel.class);
                    evaluateModels.add(bossEvaluateModel);

                }
                if(!flag) {
                    //다이어로그 추가
                    AlertDialog.Builder ad = new AlertDialog.Builder(ShowBossDetailActivity.this);
                    ad.setIcon(R.drawable.main);
                    ad.setTitle("사장 평가");
                    ad.setMessage("아직 사장님에 대한 평가가 이루어지지 않았습니다.");
                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }
                bossEvaluateAdapter=new BossEvaluateAdapter(evaluateModels);
                recyclerView.setAdapter(bossEvaluateAdapter);

            }
        });


    }
}