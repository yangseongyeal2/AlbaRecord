package com.AlbaRecord.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.AlbaRecord.MainActivity;
import com.AlbaRecord.Model.UserModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class BossSignupActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignupActivity";
    // Views
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPasswordCheck;
    EditText editTextPhone;
    EditText editTextNickname;

    TextView textviewSingin;
    TextView textviewMessage;
    ProgressDialog progressDialog;

    // Firebase 정의
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseStore;

    EditText email_edittext, password_edittext, password_re_edittext, phone_edittext;
    EditText name_edittext, brand_edittext,address_edittext,  business_edittext;
    Button buttonSignup,address_button;
    TextView address_result;
    String arg1="주소를입력하시오";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_signup);
        initfindId();

        takeTempoInfo();



        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStore = FirebaseFirestore.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 profile 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(), MainActivity.class)); //추가해 줄 ProfileActivity
        }

        progressDialog = new ProgressDialog(this);





        buttonSignup.setOnClickListener(this);
        address_button.setOnClickListener(this);


    }

    private void takeTempoInfo() {
        SharedPreferences pref = getSharedPreferences("UserModel", 0);
        String email = pref.getString("email", "");
        email_edittext.setText(email);
        String password = pref.getString("password", "");
        password_edittext.setText(password);
        String passwordRe = pref.getString("passwordRe", "");
        password_re_edittext.setText(passwordRe);
        String phoneNumber = pref.getString("phoneNumber", "");
        phone_edittext.setText(phoneNumber);
        String name = pref.getString("name", "");
        name_edittext.setText(name);
        String brand = pref.getString("brand", "");
        brand_edittext.setText(brand);
        String address = pref.getString("address", "");
        address_result.setText(address);
        String businessNum = pref.getString("businessNum", "");
        business_edittext.setText(businessNum);
        String arg1=getIntent().getStringExtra("주소");
        address_result.setText(arg1);
    }

    private void initfindId() {
        email_edittext = findViewById(R.id.email_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        password_re_edittext = findViewById(R.id.password_re_edittext);
        phone_edittext = findViewById(R.id.phone_edittext);
        name_edittext = findViewById(R.id.name_edittext);
        brand_edittext = findViewById(R.id.brand_edittext);
        address_button=findViewById(R.id.address_button);
        business_edittext = findViewById(R.id.business_edittext);
        buttonSignup = findViewById(R.id.buttonSignup);
        address_result=findViewById(R.id.address_result);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        //editTextPasswordCheck = (EditText) findViewById(R.id.editTextPasswordCheck);
        editTextPhone = (EditText) findViewById(R.id.phone_edittext);
        //editTextNickname = (EditText) findViewById(R.id.editTextNickname);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
    }

    //button click event
    @Override
    public void onClick(View view) {
        if (view == buttonSignup) {
            registerUser();
        }

        if (view == textviewSingin) {
            finish();
            startActivity(new Intent(this, LoginActivity.class)); //추가해 줄 로그인 액티비티
        }
        if(view==address_button){

            saveTempoInfo();
            finish();
            startActivity(new Intent(this, DaumWebViewActivity.class)); //추가해 줄 로그인 액티비티

        }
    }

    private void saveTempoInfo() {
        String email = email_edittext.getText().toString().trim();
        String password = password_edittext.getText().toString().trim();
        String passwordRe = password_re_edittext.getText().toString().trim();
        String phoneNumber = phone_edittext.getText().toString().trim();
        String name = name_edittext.getText().toString().trim();
        String brand = brand_edittext.getText().toString().trim();
        String address = arg1;
        String businessNum = business_edittext.getText().toString().trim();

        SharedPreferences pref = getSharedPreferences("UserModel", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("passwordRe", passwordRe);
        editor.putString("phoneNumber", phoneNumber);
        editor.putString("name", name);
        editor.putString("brand", brand);
        editor.putString("address", address);
        editor.putString("businessNum", businessNum);
        //editor.commit();
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //saveTempoInfo();
    }

    //Firebse creating a new user
    private void registerUser() {
        //사용자가 입력하는 email, password를 가져온다.
        String email = email_edittext.getText().toString().trim();
        String password = password_edittext.getText().toString().trim();
        String passwordRe = password_re_edittext.getText().toString().trim();
        String phoneNumber = phone_edittext.getText().toString().trim();
        String name = name_edittext.getText().toString().trim();
        String brand = brand_edittext.getText().toString().trim();
        String address = arg1;
        String businessNum = business_edittext.getText().toString().trim();




        //email과 password가 비었는지 아닌지를 체크 한다.
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(passwordRe)) {
            Toast.makeText(this, "비밀번호 확인을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(passwordRe)) {
            Toast.makeText(this, "비밀번호와 비밀번호 확인이 다릅니다", Toast.LENGTH_SHORT).show();
            return;
        }

        if(address_result.getText().toString().equals("주소를입력하시오")){
            Toast.makeText(this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        signupFunc(email, password);
    }

    public void signupFunc(final String email, final String password) {
        //creating a new user
        firebaseAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.show();
                        if (task.isSuccessful()) {
                            loginProcess(email, password);
                        } else {
                            //에러발생시
                            textviewMessage.setText("회원가입에 실패했습6니다. \n\n - 이미 등록된 이메일  \n - 암호 최소 6자리 이상");
                            Toast.makeText(BossSignupActivity.this, "등록 에러!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    public void loginProcess(final String email, final String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            UserModel userModel = new UserModel(
                                    email_edittext.getText().toString().trim(),
                                    password_edittext.getText().toString().trim(),
                                    phone_edittext.getText().toString().trim(),
                                    name_edittext.getText().toString().trim(),
                                    brand_edittext.getText().toString().trim(),
                                    //address_edittext.getText().toString().trim(),
                                    arg1,
                                    business_edittext.getText().toString().trim(),
                                    0//사장님
                            );
                            firebaseStore.collection("boss")
                                    .document(firebaseUser.getUid())
                                    .set(userModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // 이메일 인증 확인 메일을 전송합니다.
                                            sendEmail();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), EmailCheckActivity.class));

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void sendEmail() {
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(BossSignupActivity.this, "이메일 인증 메일을 전송했습니다. \n가입한 이메일에서 확인해주세요!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BossSignupActivity.this, "인증 메일 전송에 실패했습니다. \n쿠플존에 문의해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}