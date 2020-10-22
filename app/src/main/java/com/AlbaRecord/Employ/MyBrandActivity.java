package com.AlbaRecord.Employ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.AlbaRecord.Adapter.ColleagueAdapter;
import com.AlbaRecord.Adapter.MyBossAdapter;
import com.AlbaRecord.Interface.OnItemClick;
import com.AlbaRecord.Map.NaverMapActivity;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyBrandActivity extends AppCompatActivity implements OnItemClick {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;
    private ColleagueAdapter colleagueAdapter;
    private ArrayList<EmployeeModel> employeeModels=new ArrayList<>();
    private TextView brandname,phonenum,address_result;
    private Button map;
    private RecyclerView constraintLayout8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_brand);
        initViewId();
       // RetreiveMyInfo();
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
                                    MyBossAdapter myBossAdapter=new MyBossAdapter(bossModels,getApplicationContext(),MyBrandActivity.this,MyBrandActivity.this);
                                    constraintLayout8.setAdapter(myBossAdapter);

                                    assert bossModel != null;
//                                    brandname.setText(bossModel.getBrand());
//                                    phonenum.setText(bossModel.getPhoneNumber());
//                                    address_result.setText(bossModel.getAddress());

                                    ArrayList<String> myemp=bossModel.getMyEmployee();

                                    for(String emp:myemp){
                                        db.collection("users").document(emp).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                EmployeeModel employeeModel1=documentSnapshot.toObject(EmployeeModel.class);
                                                employeeModels.add(employeeModel1);
                                                colleagueAdapter=new ColleagueAdapter(employeeModels,MyBrandActivity.this);
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
        brandname=findViewById(R.id.brandname);
        phonenum=findViewById(R.id.phonenum);
        address_result=findViewById(R.id.address_result);
        map=findViewById(R.id.map);
        constraintLayout8=findViewById(R.id.constraintLayout8);



    }

    @Override
    public void onClick(String value) {

    }
}