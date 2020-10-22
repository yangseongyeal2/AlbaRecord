package com.AlbaRecord.Employ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.AlbaRecord.Adapter.ColleagueAdapter;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyBrandActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private ColleagueAdapter colleagueAdapter;
    private ArrayList<EmployeeModel> employeeModels=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_brand);
        initViewId();
        RetrieveInfo();
    }

    private void RetrieveInfo() {
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        EmployeeModel employeeModel=documentSnapshot.toObject(EmployeeModel.class);
                        ArrayList<String> myBoss=employeeModel.getMyBoss();
                        for(String boss:myBoss){
                            db.collection("users").document(boss).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    BossModel bossModel=documentSnapshot.toObject(BossModel.class);
                                    ArrayList<String> myemp=bossModel.getMyEmployee();
                                    for(String emp:myemp){
                                        db.collection("users").document(emp).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                EmployeeModel employeeModel1=documentSnapshot.toObject(EmployeeModel.class);
                                                employeeModels.add(employeeModel1);
                                                colleagueAdapter=new ColleagueAdapter(employeeModels);
                                                recyclerView.setAdapter(colleagueAdapter);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
    }

    private void initViewId() {
        recyclerView=findViewById(R.id.recyclerView);
    }
}