package com.AlbaRecord.Notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.AlbaRecord.Adapter.NotificationAdapter;
import com.AlbaRecord.Board.DetailActivity;
import com.AlbaRecord.Model.NotiInfo;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AlramActivity extends AppCompatActivity {
    private RecyclerView Alram_RecyclerView;
    private List<NotiInfo> list ;
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    private NotificationAdapter mNotificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alram);
        Alram_RecyclerView=findViewById(R.id.Alram_RecyclerView);
        mStore.collection("users").document(firebaseUser.getUid()).collection("notification")
                .orderBy("date", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list= new ArrayList<NotiInfo>();
                if(task.getResult()!=null){
                    for(QueryDocumentSnapshot data:task.getResult()){
                        NotiInfo notiInfo=data.toObject(NotiInfo.class);
                        list.add(notiInfo);
                    }
                    mNotificationAdapter=new NotificationAdapter(list);
                    mNotificationAdapter.setOnIemlClickListner(new NotificationAdapter.OnItemClickListener() {
                        @Override
                        public void onitemClick(View v, int pos) {
//                            Log.d("클릭","내가쓴글:"+String.valueOf(pos));
//                            Intent intent=new Intent(getApplicationContext(), DetailActivity.class);
//                            intent.putExtra("DocumentId",list.get(pos).getDocumentId());
//                            intent.putExtra("BoardName",list.get(pos).getBoardName());
//                            startActivity(intent);
                        }
                    });
                    Alram_RecyclerView.setAdapter(mNotificationAdapter);
                }
            }
        });

    }
}