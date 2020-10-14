package com.AlbaRecord.Boss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.AlbaRecord.Adapter.EvaluateAdapter;
import com.AlbaRecord.Model.EvaluateModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShowEmployeeDetailActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String DocumentId;;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_employee_detail);
        DocumentId=getIntent().getStringExtra("DocumentId");
        recyclerView=findViewById(R.id.recyclerView);
        RetrieveEvaluateInfo();
    }
    private void RetrieveEvaluateInfo(){
        db.collection("users").document(DocumentId).collection("Evaluate").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<EvaluateModel> evaluateModels=new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()){
                            EvaluateModel evaluateModel=document.toObject(EvaluateModel.class);
                            evaluateModels.add(evaluateModel);
                        }
                        EvaluateAdapter evaluateAdapter=new EvaluateAdapter(evaluateModels,getApplicationContext());
                        recyclerView.setAdapter(evaluateAdapter);
                    }
                });
    }
}