package com.AlbaRecord.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.R;
import com.AlbaRecord.Adapter.CertificateAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EmployeeSignupActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseStore;

    EditText email_edittext, password_edittext, password_re_edittext, phone_edittext, name_edittext, age_edittext, education_edittext, license1, selfintrobody;
    Button address_button, buttonSignup, setimage_button, licenseplus, licensedelete;
    TextView address_result, textviewMessage;
    String arg1 ="";
    ProgressDialog progressDialog;
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    Uri downloadUri;
    ImageView image;
    ArrayList<String> contents = new ArrayList<>();
    RecyclerView certificate_recyclerView;
    EditText Bank_edit,Account_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_signup);
        initViewId();
        takeTempoInfo();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            //이미 로그인 되었다면 이 액티비티를 종료함
            if(firebaseAuth.getCurrentUser().isEmailVerified()){
                finish();
                //그리고 profile 액티비티를 연다.
                startActivity(new Intent(getApplicationContext(), LoginWayActivity.class)); //추가해 줄 ProfileActivity
            }else{
                startActivity(new Intent(getApplicationContext(), EmailCheckActivity.class)); //추가해 줄 ProfileActivity
            }
        }

        progressDialog = new ProgressDialog(this);


        buttonSignup.setOnClickListener(this);
        address_button.setOnClickListener(this);
        setimage_button.setOnClickListener(this);
        licenseplus.setOnClickListener(this);
        licensedelete.setOnClickListener(this);

    }

    private void initViewId() {
        email_edittext = (EditText) findViewById(R.id.email_edittext);
        password_edittext = (EditText) findViewById(R.id.password_edittext);
        password_re_edittext = (EditText) findViewById(R.id.password_re_edittext);
        phone_edittext = (EditText) findViewById(R.id.phone_edittext);
        name_edittext = (EditText) findViewById(R.id.name_edittext);
        age_edittext = (EditText) findViewById(R.id.age_edittext);
        education_edittext = (EditText) findViewById(R.id.education_edittext);
        address_result = (TextView) findViewById(R.id.address_result);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        setimage_button = (Button) findViewById(R.id.setimage_button);
        address_button = (Button) findViewById(R.id.address_button);
        image = (ImageView) findViewById(R.id.image);
        licenseplus = (Button) findViewById(R.id.licenseplus);
        licensedelete = (Button) findViewById(R.id.licensedelete);
        license1 = (EditText) findViewById(R.id.license1);
        certificate_recyclerView = (RecyclerView) findViewById(R.id.certificate_recyclerView);
        selfintrobody = (EditText) findViewById(R.id.selfintrobody);
        Bank_edit=(EditText)findViewById(R.id.Bank_edit);
        Account_edit=(EditText)findViewById(R.id.Account_edit);
    }

    private void takeTempoInfo() {
        SharedPreferences pref = getSharedPreferences("EmployUserModel", 0);
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
        String age = pref.getString("age", "");
        age_edittext.setText(age);
        String address = pref.getString("address", "");
        address_result.setText(address);
        arg1 = getIntent().getStringExtra("주소");
        address_result.setText(arg1);
        String degree = pref.getString("degree", "");
        education_edittext.setText(degree);
        String Bank = pref.getString("Bank", "");
        education_edittext.setText(Bank);
        String Account = pref.getString("Account", "");
        education_edittext.setText(Account);
        String Image = pref.getString("Image", "");
       // Uri downloadUri=Uri.parse(Image);
//        Glide.with(image)
//                .load(downloadUri)
//                .into(image);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSignup:
                buttonSignup.setClickable(false);
                registerUser();
                break;
            case R.id.address_button:
                saveTempoInfo();
                finish();
                Intent intent1 = new Intent(this, DaumWebViewActivity.class);
                intent1.putExtra("flag", "직원");
                startActivity(intent1); //추가해 줄 로그인 액티비티
                break;
            case R.id.setimage_button:
                if(arg1==null){
                    Toast.makeText(getApplicationContext(),"주소를 먼저 입력 바랍니다.",Toast.LENGTH_SHORT).show();
                }else {
                    Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://media/internal/images/media"));
                    mIntent.setAction(Intent.ACTION_GET_CONTENT);
                    mIntent.setType("image/*");
//                startActivity(mIntent);
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(mIntent, 1);
                }


                break;
            case R.id.licenseplus:
                Log.d("클릭", "성공");
                if (license1.getText().toString().isEmpty()) {
                    license1.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                } else {
                    InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    String content = license1.getText().toString().trim();
                    contents.add(content);
                    CertificateAdapter mAdapter = new CertificateAdapter(contents);
                    certificate_recyclerView.setVisibility(View.VISIBLE);
                    certificate_recyclerView.setAdapter(mAdapter);
                    license1.setText("");
                }


                break;
            case R.id.licensedelete:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
//                    InputStream in = getContentResolver().openInputStream(data.getData());
//                    Bitmap img = BitmapFactory.decodeStream(in);
//                    in.close();
                    StorageReference ref = mStorageRef.child("profile/" + new Date() + ".jpg");
                    UploadTask uploadTask = ref.putFile(Objects.requireNonNull(data.getData()));
                    progressDialog.show();
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Log.d("스토리지업로드성공", task.getResult().toString());
                                throw task.getException();

                            }

                            // Continue with the task to get the download URL
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Log.d("스토리지업로드성공", task.getResult().toString());
                                downloadUri = task.getResult();
//                                Drawable mDrawble=loadImage(downloadUri.toString());
//                                image.setImageDrawable(mDrawble);
                                Glide.with(image)
                                        .load(downloadUri)
                                        .into(image);
                                progressDialog.dismiss();
                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });


                    // 이미지 표시
                    //imageView.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void saveTempoInfo() {
        String email = email_edittext.getText().toString().trim();
        String password = password_edittext.getText().toString().trim();
        String passwordRe = password_re_edittext.getText().toString().trim();
        String phoneNumber = phone_edittext.getText().toString().trim();
        String name = name_edittext.getText().toString().trim();
        String age = age_edittext.getText().toString().trim();
        String address = arg1;
        String degree = education_edittext.getText().toString().trim();
        String Account=Account_edit.getText().toString().trim();
        String Bank=Bank_edit.getText().toString().trim();


        SharedPreferences pref = getSharedPreferences("EmployUserModel", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("passwordRe", passwordRe);
        editor.putString("phoneNumber", phoneNumber);
        editor.putString("name", name);
        editor.putString("age", age);
        editor.putString("address", address);
        editor.putString("degree", degree);
        editor.putString("Account",Account);
        editor.putString("Bank",Bank);
//        if(downloadUri!=null){
//            editor.putString("Image",downloadUri.toString());
//        }

        //editor.commit();
        editor.apply();
    }

    private void registerUser() {
        progressDialog.show();
        //사용자가 입력하는 email, password를 가져온다.
        String email = email_edittext.getText().toString().trim();
        String password = password_edittext.getText().toString().trim();
        String passwordRe = password_re_edittext.getText().toString().trim();
        String Bank=Bank_edit.getText().toString().trim();
        String Account=Account_edit.getText().toString().trim();


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

        if (address_result.getText()==null) {

            Toast.makeText(this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
//        else{
//            arg1=address_result.getText().toString();
//        }


        if (TextUtils.isEmpty(Bank)) {
            Toast.makeText(this, "은행이름을 입력해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Account)) {
            Toast.makeText(this, "계좌번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        signupFunc(email, password);
    }

    private void signupFunc(final String email, final String password) {
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
                            Toast.makeText(EmployeeSignupActivity.this, "등록 에러!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void loginProcess(final String email, final String password) {
      //  List<Address> list = null;
        //Log.d("DownLoadURI", downloadUri.toString());
        double lat=0.0, lon=0.0;
    //    final Geocoder geocoder = new Geocoder(this);
//        try {
//            list = geocoder.getFromLocationName(arg1, 10);
//            Address addr = list.get(0);
//            lat = addr.getLatitude();
//            lon = addr.getLongitude();
//            Log.d("위도", String.valueOf(lat));
//            Log.d("경도", String.valueOf(lon));
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d("실패", "list.get(0).toString()");
//            return;
//        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            try{
                                EmployeeModel userModel = new EmployeeModel(
                                        email_edittext.getText().toString().trim(),
                                        password_edittext.getText().toString().trim(),
                                        phone_edittext.getText().toString().trim(),
                                        name_edittext.getText().toString().trim(),
                                        age_edittext.getText().toString().trim(),
                                        arg1,
                                        education_edittext.getText().toString().trim(),
                                        lat,
                                        lon,
                                        downloadUri.toString(),
                                        contents,
                                        selfintrobody.getText().toString().trim(),
                                        1,
                                        firebaseUser.getUid(),
                                        new ArrayList<String>(),
                                        Account_edit.getText().toString().trim(),
                                        Bank_edit.getText().toString().trim()
                                );
                                firebaseStore.collection("users")
                                        .document(firebaseUser.getUid())
                                        .set(userModel)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // 이메일 인증 확인 메일을 전송합니다.
                                                sendEmail();
                                                Intent emailIntent=new Intent(getApplicationContext(), EmailCheckActivity.class);
                                                //  emailIntent.putExtra("주소",arg1);
                                                emailIntent.putExtra("Flag","직원");
                                                startActivity(emailIntent);

                                                progressDialog.dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("시발","스토어업로드실패");
                                            }
                                        });
                                FirebaseMessaging.getInstance().subscribeToTopic(firebaseUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d("구독하기", firebaseUser.getUid());
                                        finish();
                                    }
                                });
                            }catch (Exception e){

                            }



                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void sendEmail() {
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EmployeeSignupActivity.this, "이메일 인증 메일을 전송했습니다. \n가입한 이메일에서 확인해주세요!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EmployeeSignupActivity.this, "인증 메일 전송에 실패했습니다. \n쿠플존에 문의해주세요!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}