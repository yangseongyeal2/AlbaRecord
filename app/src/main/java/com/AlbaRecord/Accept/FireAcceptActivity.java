package com.AlbaRecord.Accept;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.AlbaRecord.Employ.EmployMainActivity;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireAcceptActivity extends AppCompatActivity {
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_accept);
        Log.d("해고메세지",mAuth.getCurrentUser().getUid());
        String BossDocumentId=getIntent().getStringExtra("DocumentId");
        Log.d("해고메세지",BossDocumentId);
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .update("myBoss", FieldValue.arrayRemove(BossDocumentId))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("해고메세지","마이보스에서 삭제완료");
            }
        });
        findViewById(R.id.accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), EmployMainActivity.class));
            }
        });

    }
}