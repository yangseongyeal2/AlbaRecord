package com.AlbaRecord.searchemployee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.AlbaRecord.MainActivity;
import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.R;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class ShowEmployeeActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "ShowEmployeeActivity";
    private final String URL = "https://fcm.googleapis.com/fcm/send";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView name,age,education,selfintrobody;
    ImageView photo;
    Button setmyemployee,wish;
    private RequestQueue mRequesQue;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_employee);
        initViewId();
        email = getIntent().getStringExtra("Email");

        Log.d(TAG, email);

        List<EmployeeModel> list=new ArrayList<>();
        RetrieveEmployeeInfo();


        setmyemployee.setOnClickListener(this);
        wish.setOnClickListener(this);



    }

    private void RetrieveEmployeeInfo() {
        db.collection("employee")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean flag=false;
                            for (QueryDocumentSnapshot document : task.getResult()) {//데이터가 없으면 여긴 실행안댐
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                flag=true;  //데이터를 찾으면 flag=true
                                EmployeeModel employeeModel=document.toObject(EmployeeModel.class);
                                name.setText(employeeModel.getName());
                                age.setText(employeeModel.getAge());
                                education.setText(employeeModel.getDegree());
                                selfintrobody.setText(employeeModel.getSelf_introduce());
                                Glide.with(photo)//
                                        .load(employeeModel.getPhoto())
                                        .into(photo);


                                Log.d(TAG,employeeModel.toString());

                            }
                            if(!flag){
                                Log.d(TAG,"실패");
                                //다이어로그 추가
                                AlertDialog.Builder ad = new AlertDialog.Builder(ShowEmployeeActivity.this);
                                ad.setIcon(R.mipmap.ic_launcher);
                                ad.setTitle("이메일이 존재하지 않습니다");
                                ad.setMessage("직원의 이메일을 다시한번 확인해서 작성해주십시오");
                                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        startActivity(new Intent(getApplicationContext(),SearchEmployeeActivity.class));
                                        dialog.dismiss();
                                    }
                                });
                                ad.show();
                                //다이어로그 끝
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void initViewId() {
        name=(TextView)findViewById(R.id.name);
        age=(TextView)findViewById(R.id.age);
        education=(TextView)findViewById(R.id.education);
        photo=(ImageView)findViewById(R.id.photo);
        setmyemployee=(Button)findViewById(R.id.setmyemployee);
        wish=(Button)findViewById(R.id.wish);
        mRequesQue = Volley.newRequestQueue(this);
        selfintrobody=(TextView)findViewById(R.id.selfintrobody);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wish:
                break;
            case R.id.setmyemployee:
                AlertDialog.Builder ad = new AlertDialog.Builder(ShowEmployeeActivity.this);
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("직원 채용이 확정되었습니까?");
                ad.setMessage("직원에게 신청서를 보내시겠습니까?");
                ad.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        sendNotification(email,"제목","내용");
                        startActivity(new Intent(getApplicationContext(),SearchEmployeeActivity.class));
                        dialog.dismiss();
                    }
                });
                ad.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(getApplicationContext(),SearchEmployeeActivity.class));
                        dialog.dismiss();
                    }
                });
                ad.show();
                break;
        }
    }
    private void sendNotification(String Email, String title, String content) {
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
            mainObj.put("to", "/topics/" + Email);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title + "에 댓글이 달렸습니다");
            notificationObj.put("body", "댓글:" + content);
            notificationObj.put("Email", Email);


            // mainObj.put("notification",notificationObj);
            mainObj.put("data", notificationObj);


            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL,
                    mainObj,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAkWD7qvM:APA91bHQt54EOO2qoqX68TPM8juIVVkm8kRUByCqQwbWHdEHArgBZTIpun-F3ryFKwY1zRKjFSGXNwPwpZaPUhGpdUIYdZi07doU2twSIvA9zKSy13hgEX3XTZb2oOCbanEcaAKqNfrp");
                    return header;
                }
            };
            mRequesQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}