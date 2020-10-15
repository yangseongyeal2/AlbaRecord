package com.AlbaRecord.Employ;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.AlbaRecord.Adapter.MyShopAdapter;
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

public class ShowBossActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String brandName;
    MyShopAdapter myShopAdapter;
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
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        BossModel bossModel=document.toObject(BossModel.class);
                        list.add(bossModel);
                        myShopAdapter=new MyShopAdapter(list,getApplicationContext());
                        recyclerView.setAdapter(myShopAdapter);
                    }
                }
            }
        });

    }
}