package com.AlbaRecord.Employ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.AlbaRecord.Adapter.BossEvaluateAdapter;
import com.AlbaRecord.Adapter.EvaluateAdapter;
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
                for (QueryDocumentSnapshot document : task.getResult()){
                    bossEvaluateModel=document.toObject(BossEvaluateModel.class);
                    evaluateModels.add(bossEvaluateModel);

                }
                bossEvaluateAdapter=new BossEvaluateAdapter(evaluateModels);
                recyclerView.setAdapter(bossEvaluateAdapter);

            }
        });


    }
}