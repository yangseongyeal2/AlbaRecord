package com.AlbaRecord.Accept;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.AlbaRecord.Boss.MyEmployeeActivity;
import com.AlbaRecord.Employ.EmployeeMyshop;
import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.R;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AcceptActivity extends AppCompatActivity implements View.OnClickListener {
    Button accept,deny;
    private RequestQueue mRequesQue;
    private final String URL = "https://fcm.googleapis.com/fcm/send";
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    String documentId;
    EmployeeModel employeeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        RetreiveMyEmployeeModel();
        accept=(Button)findViewById(R.id.accept);
        deny=(Button)findViewById(R.id.deny);
        mRequesQue = Volley.newRequestQueue(this);

        documentId=getIntent().getStringExtra("DocumentId");
        String title=getIntent().getStringExtra("title");
        String body=getIntent().getStringExtra("body");
        String flag=getIntent().getStringExtra("flag");


        if(flag.equals("1")){
            Toast.makeText(getApplicationContext(),"수락댐",Toast.LENGTH_SHORT).show();

            mStore.collection("users").document(firebaseUser.getUid()).update("MyEmployee", FieldValue.arrayUnion(documentId));

            finish();
            Intent acceptIntent=new Intent(getApplicationContext(), MyEmployeeActivity.class);
            startActivity(acceptIntent);
        }else if(flag.equals("2")) {
            Toast.makeText(getApplicationContext(), "거절댐", Toast.LENGTH_SHORT).show();
            Intent denyIntent=new Intent(getApplicationContext(), MyEmployeeActivity.class);
            startActivity(denyIntent);
        }
        Log.d("Accepts",documentId);
        accept.setOnClickListener(this);
        deny.setOnClickListener(this);


    }

    private void RetreiveMyEmployeeModel() {
        mStore.collection("users")
                .document(firebaseUser.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                employeeModel=documentSnapshot.toObject(EmployeeModel.class);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.accept:

                sendNotification(documentId,employeeModel.getName(),employeeModel.getName()+" 님이 수락하셨습니다","1");
                mStore.collection("users").document(firebaseUser.getUid()).update("myBoss", FieldValue.arrayUnion(documentId));

                startActivity(new Intent(getApplicationContext(), EmployeeMyshop.class));
                finish();

                break;
            case R.id.deny:
                sendNotification(documentId,employeeModel.getName(),employeeModel.getName()+" 님이 거절 하셨습니다","2");
                finish();
                break;
        }
    }
    private void sendNotification(String DocumentId, String title, String content,String flag) {
        /* our json object will lokk loke
        {
            "to": "topics/topic name",
            notification:{
                title: "some title",
                 body: "some body"
            }
        }
        */
        JSONObject mainObj = new JSONObject();
        try {
            Log.d("양성열",DocumentId);
            mainObj.put("to", "/topics/" + DocumentId );
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title );//사장이보냄
            notificationObj.put("body", content);//브랜드네임
            notificationObj.put("DocumentId",firebaseUser.getUid() );//직원 도큐먼트ID
            notificationObj.put("flag", flag);//사장이보낼때



            // mainObj.put("notification",notificationObj);
            mainObj.put("data", notificationObj);


            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL,
                    mainObj,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("양성열",response.toString());

                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("양성열","onErrorResponse");
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAARXzwbpI:APA91bGeGR-WB_570H5C2xeoal97SKyuO1Bj1aUgehYdHlH4Us5bbgsBrPsoQW7IzFItjSv1PJ2KT-TbPJb-ZCCoCD4DsnY5TCn9D6pkOijllp3qOqhQu1-YO3SccAHUpDaDpGJXDrOq");
                    Log.d("양성열","getHeaders");
                    return header;
                }
            };
            Log.d("양성열","큐 전");
            mRequesQue.add(request);
            Log.d("양성열","큐 후");
        } catch (JSONException e) {
            Log.d("양성열","캐치");
            e.printStackTrace();
        }


    }
}