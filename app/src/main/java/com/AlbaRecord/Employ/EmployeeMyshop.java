package com.AlbaRecord.Employ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.AlbaRecord.Adapter.MyShopAdapter;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EmployeeMyshop extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    MyShopAdapter myShopAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_myshop);
        recyclerView=findViewById(R.id.recyclerView);

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                EmployeeModel employeeModel=documentSnapshot.toObject(EmployeeModel.class);
                ArrayList<BossModel>list=new ArrayList<>();
                Log.d("MyShop","for문전");
                for(String boss:employeeModel.getMyBoss()){
                    Log.d("MyShop","for문안");
                    db.collection("users").document(boss).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            BossModel bossModel=documentSnapshot.toObject(BossModel.class);
                            list.add(bossModel);
                            myShopAdapter=new MyShopAdapter(list,getApplicationContext());
                            recyclerView.setAdapter(myShopAdapter);
                        }
                    });
                }
                Log.d("MyShop","for문후");


            }
        });



    }
}