package com.AlbaRecord.Employ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.AlbaRecord.Adapter.EvaluateAdapter;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.Model.EvaluateModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EmployeeMyhomeActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String DocumentId;;
    RecyclerView recyclerView;
    BossModel bossModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_myhome);
        //DocumentId=getIntent().getStringExtra("DocumentId");
        recyclerView=findViewById(R.id.recyclerView);
        RetreiveMyInfo();
        RetrieveEvaluateInfo();

    }
    private void RetreiveMyInfo() {
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                bossModel=documentSnapshot.toObject(BossModel.class);
            }
        });
    }
    private void RetrieveEvaluateInfo(){
        db.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("Evaluate").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<EvaluateModel> evaluateModels=new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()){
                            EvaluateModel evaluateModel=document.toObject(EvaluateModel.class);
                            evaluateModels.add(evaluateModel);
                        }
                        EvaluateAdapter evaluateAdapter=new EvaluateAdapter(evaluateModels,getApplicationContext(),bossModel);
                        recyclerView.setAdapter(evaluateAdapter);
                    }
                });
    }
}