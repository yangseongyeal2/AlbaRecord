package com.AlbaRecord.Employ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.AlbaRecord.Adapter.MyBossAdapter;
import com.AlbaRecord.Adapter.MyShopAdapter;
import com.AlbaRecord.Boss.SearchEmployeeActivity;
import com.AlbaRecord.Boss.ShowEmployeeActivity;
import com.AlbaRecord.Interface.OnItemClick;
import com.AlbaRecord.Model.BossModel;
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

public class ShowBossActivity extends AppCompatActivity implements OnItemClick {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String brandName;
    MyShopAdapter myShopAdapter;
    MyBossAdapter myBossAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_boss);
        brandName = getIntent().getStringExtra("BrandName");
        recyclerView = findViewById(R.id.recyclerView);
        RetrieveBossInfo();
    }

    private void RetrieveBossInfo() {
        db.collection("users").whereEqualTo("brand", brandName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<BossModel>list =new ArrayList<>();
                if (task.isSuccessful()) {
                    boolean flag=false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        flag=true;
                        BossModel bossModel=document.toObject(BossModel.class);
                        list.add(bossModel);
                        //myShopAdapter=new MyShopAdapter(list,getApplicationContext());
                        myBossAdapter=new MyBossAdapter(list,getApplicationContext(),ShowBossActivity.this,ShowBossActivity.this);
                        recyclerView.setAdapter(myBossAdapter);
                    }
                    if(!flag){
                        //다이어로그 추가
                        AlertDialog.Builder ad = new AlertDialog.Builder(ShowBossActivity.this);
                        ad.setIcon(R.drawable.main);
                        ad.setTitle("상호명이 존재하지않습니다");
                        ad.setMessage("상호명을 다시한번 확인해서 작성해주십시오");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), EmployeeBossSearchActivity.class));
                                dialog.dismiss();
                            }
                        });
                        ad.show();
                    }
                }

            }
        });

    }

    @Override
    public void onClick(String value) {

    }
}