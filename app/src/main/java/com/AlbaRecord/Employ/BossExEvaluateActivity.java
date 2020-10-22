package com.AlbaRecord.Employ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.AlbaRecord.Adapter.CommonAdapter;
import com.AlbaRecord.Adapter.MyBossAdapter;
import com.AlbaRecord.Interface.OnItemClick;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class BossExEvaluateActivity extends AppCompatActivity implements OnItemClick {
    RecyclerView recyclerView;
    MyBossAdapter myBossAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_ex_evaluate);
        recyclerView=findViewById(R.id.recyclerView);
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
                        ArrayList<BossModel>bossModels=new ArrayList<>();
                        for(String boss:myBoss){
                            db.collection("users").document(boss).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    BossModel bossModel=documentSnapshot.toObject(BossModel.class);
                                    bossModels.add(bossModel);
                                    MyBossAdapter myBossAdapter=new MyBossAdapter(bossModels,getApplicationContext(), BossExEvaluateActivity.this,BossExEvaluateActivity.this);
                                    myBossAdapter.setOnIemlClickListner(new CommonAdapter.OnItemClickListener() {
                                        @Override
                                        public void onitemClick(View v, int pos) {
                                            finish();
                                            Intent intent=new Intent(getApplicationContext(), EvaluateBossActivity.class);
                                            intent.putExtra("DocumentId",boss);
                                            startActivity(intent);
                                        }
                                    });
                                    recyclerView.setAdapter(myBossAdapter);


                                    assert bossModel != null;


                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void onClick(String value) {

    }
}