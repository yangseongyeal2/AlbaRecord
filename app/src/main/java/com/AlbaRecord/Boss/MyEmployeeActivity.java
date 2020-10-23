package com.AlbaRecord.Boss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.AlbaRecord.Adapter.EmplyeeInfoAdapter;
import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyEmployeeActivity extends AppCompatActivity {
    RecyclerView myalba_recyclerview,lastAlba_recyclerview;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_employee);
        initViewId();
        RetreiveMyEmployee();




    }



    private void RetreiveMyEmployee() {
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                BossModel bossModel =documentSnapshot.toObject(BossModel.class);
                List<EmployeeModel> myEmploy=new ArrayList<>();
                assert bossModel != null;
                for(String documentId: bossModel.getDocumentIdList()){
                    db.collection("users").document(documentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            EmployeeModel employeeModel=documentSnapshot.toObject(EmployeeModel.class);
                            myEmploy.add(employeeModel);
                            EmplyeeInfoAdapter emplyeeInfoAdapter=new EmplyeeInfoAdapter(myEmploy,MyEmployeeActivity.this,MyEmployeeActivity.this,bossModel);
                            myalba_recyclerview.setAdapter(emplyeeInfoAdapter);
                        }
                    });
                }//for 끝

            }
        });
    }

    private void initViewId() {
        myalba_recyclerview=(RecyclerView)findViewById(R.id.myalba_recyclerview);

    }
}