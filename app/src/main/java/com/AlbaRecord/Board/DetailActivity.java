package com.AlbaRecord.Board;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.AlbaRecord.Adapter.ReplyAdapter;
import com.AlbaRecord.Interface.OnItemClick;
import com.AlbaRecord.Model.BoardInfo;
import com.AlbaRecord.Model.ReplyInfo;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.R;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorContent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DetailActivity extends AppCompatActivity implements OnItemClick {
    private static final String TAG = "DetailActivity";
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private TextView mTitle;
    private TextView mContent;
    private TextInputEditText mEditText;
    private TextInputEditText mEditText2;
    private DocumentReference documentReference;
    private String uid;
    private ReplyAdapter mReplyAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView mToiletImageView;

    //device to device notification
    //private RequestQueue mRequesQue;
    private RequestQueue mRequesQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";
    private ProgressDialog loadingbar;
    private String mDocumentId_send;
    private TextInputLayout mTextInputLayout;
    private TextInputLayout mTextInputLayout2;
    //private ReplytoreplyAdapter mReplytoreplyAdapter;
    private DocumentReference documentReference_reply;

    private TextView mLikecount;
    private TextView mReplycount;
    private TextView mViewcount;


    //private SliderAdapterExample mSliderAdapterExample;

    private ConstraintLayout constraintLayout;
    private ScrollView detail_ScrollView;
    private BoardInfo boardInfo;
    private FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private LikeButton mLikeButton;
    private TextView mBoardTitleName,username,birthdate;
    private ImageView Top_menu,fresh,plusarrow;

    private OnFragmentInteractionListener mListener;
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        final String mDocumentId = getIntent().getStringExtra("DocumentId");//mDocumentId는 디테일 정보받아오기
        final String mBoardName = getIntent().getStringExtra("BoardName");
        if (mDocumentId != null) {
            documentReference = mStore.collection("Board").document(mDocumentId);
        }
        constraintLayout = new ConstraintLayout(this);
        //

        setContentView(R.layout.activity_detail);
        detail_ScrollView = findViewById(R.id.detail_ScrollView);
        mTitle = findViewById(R.id.detail_title);
        // mContent = findViewById(R.id.detail_content);
        mRecyclerView = findViewById(R.id.detail_recyclerview);
        mRequesQue = Volley.newRequestQueue(this);
        //
        mTextInputLayout = findViewById(R.id.detail_TextIputLayout);
        mTextInputLayout2 = findViewById(R.id.detail_TextIputLayout2);
        mEditText = findViewById(R.id.detail_reply_EditText);
        mEditText2 = findViewById(R.id.detail_reply_EditText2);
        //
        mLikecount = findViewById(R.id.item_detail_like_TextView);
        mReplycount = findViewById(R.id.item_detail_replycount_TextView);
        mViewcount = findViewById(R.id.item_detail_viewcount_TextView);
        mLikeButton = findViewById(R.id.item_likeButton_likeButton);
        mBoardTitleName=findViewById(R.id.detail_title_title);
        mBoardTitleName.setText(mBoardName);//게시판위치
        username=findViewById(R.id.detail_username);
        birthdate=findViewById(R.id.detail_birthdate);
        Top_menu=findViewById(R.id.Top_menu);
        Top_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_menu(v,mDocumentId,mBoardName);
            }
        });
        fresh=findViewById(R.id.fresh);
        fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retreiveReply(documentReference);
            }
        });
//        Log.d("롤",String.valueOf(mRecyclerView.getVisibility()));
        plusarrow=findViewById(R.id.plusarrow);
        plusarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecyclerView.getVisibility()==View.GONE){
                    mRecyclerView.setVisibility(View.VISIBLE);
                    plusarrow.setImageResource(R.drawable.minusarrow);
                }else{
                    mRecyclerView.setVisibility(View.GONE);
                    plusarrow.setImageResource(R.drawable.plusarrow);
                }
            }
        });



        mDocumentId_send = mDocumentId;
        //swipeRefreshLayout=findViewById(R.id.detail_SwipeRefreshLayout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {//새로고침
//                //retreiveDocumentReference(documentReference);
//                retreiveReply(documentReference);
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
        //mToiletImageView = findViewById(R.id.detail_toiletimage);
        loadingbar = new ProgressDialog(this);

        initUid();//uid 전역변수로 사용가능
        upviewcount();//조회수 1올리기
        retreiveDocumentReference(documentReference);
        retreiveReply(documentReference);
        //retreivePhoto(documentReference);
        //updateReply(documentReference);
        mRecyclerView.setAdapter(mReplyAdapter);


        mTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {//에딧텍스트 업로드
            @Override
            public void onClick(View v) {//그냥 댓글 달때
                loadingbar.setTitle("Set profile image");
                loadingbar.setMessage("pleas wait업로딩중");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();
                final DocumentReference replyDocumentreference = documentReference.collection("reply").document();
                if (mEditText.getText() != null) {
                    String reply_string = mEditText.getText().toString();
                    assert firebaseUser != null;

                    final ReplyInfo replyInfo = new ReplyInfo(firebaseUser.getUid(), "0", reply_string, new Date(), replyDocumentreference.getId(), Arrays.asList(""));

                    //mReplyInfoList.add(replyInfo);
                    replyDocumentreference.set(replyInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (!uid.equals(firebaseUser.getUid())) {//다른사람이 내 게시판에 글 올릴떄만 알림
                                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        // BoardInfo boardInfo = documentSnapshot.toObject(BoardInfo.class);
                                        boardInfo = documentSnapshot.toObject(BoardInfo.class);
                                        assert boardInfo != null;
                                        String title = boardInfo.getTitle();
                                        String cotent = replyInfo.getContent();
                                        sendNotification(mDocumentId_send, title, cotent,mBoardName);

                                    }
                                });
                            } else if (uid.equals(firebaseUser.getUid())) {//올린사람이 댓글을 달았을때 댓글달린사람 uidlist로 매세지보내기.
                                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(final DocumentSnapshot documentSnapshot) {
                                        //final BoardInfo boardInfo = documentSnapshot.toObject(BoardInfo.class);
                                        boardInfo = documentSnapshot.toObject(BoardInfo.class);
                                        documentReference.collection("reply").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.getResult() != null) {
                                                    Set<String> uidset = new HashSet<>();
                                                    for (QueryDocumentSnapshot data : task.getResult()) {
                                                        ReplyInfo ri = data.toObject(ReplyInfo.class);
                                                        if (!ri.getUid().equals(uid)) {
                                                            String title = boardInfo.getTitle();
                                                            String cotent = replyInfo.getContent();
                                                            uidset.add(ri.getUid());
                                                        }
                                                    }
                                                    for (String str : uidset) {//중복되지않게 보내기
                                                        sendNotification(str, boardInfo.getTitle(), replyInfo.getContent(),mBoardName);
                                                    }

                                                }

                                            }
                                        });

                                    }
                                });
                            }
                            documentReference.update("replycount", FieldValue.increment(1));//댓글수 1증가.
                            mEditText.setText("");
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            assert imm != null;
                            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                            retreiveReply(documentReference);


                            // users 테이블의 user의 reply정보를 업데이트 해줘야 합니다.
                            uploadStoreUserReplyModel(replyInfo);

                            loadingbar.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingbar.dismiss();
                        }
                    });


                } else {
                    Toast.makeText(getApplicationContext(), "댓글을 입력하시오", Toast.LENGTH_LONG).show();
                }
            }
        });//그냥 댓글 달때 끝


        mLikeButton.setOnLikeListener(new OnLikeListener() {//좋아요 기능
            @Override
            public void liked(LikeButton likeButton) {
                final FirebaseFirestore mStore = FirebaseFirestore.getInstance();
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final BoardInfo boardInfo = documentSnapshot.toObject(BoardInfo.class);
                        assert boardInfo != null;
                        final DocumentReference documentReference = mStore.collection("Board").document(boardInfo.getDocumentId());
                        documentReference.update("uidList", FieldValue.arrayUnion(mFirebaseUser.getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {//좋아요 누른사람 배열에 추가.
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        int count = task.getResult().toObject(boardInfo.getClass()).getUidList().size();
                                        mLikecount.setText(String.valueOf(count - 1));
                                    }
                                });
                                mStore.collection("users").document(boardInfo.getUid())//게시물 올린사람 경험치 +1
                                        .update("likecount", FieldValue.increment(1));
                            }
                        });
                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {//싫어요 기능
                final FirebaseFirestore mStore = FirebaseFirestore.getInstance();
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final BoardInfo boardInfo = documentSnapshot.toObject(BoardInfo.class);
                        assert boardInfo != null;
                        final DocumentReference documentReference = mStore.collection(mBoardName).document(boardInfo.getDocumentId());
                        documentReference.update("uidList", FieldValue.arrayRemove(mFirebaseUser.getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {//좋아요 누른사람 배열에 추가.
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        int count = task.getResult().toObject(boardInfo.getClass()).getUidList().size();
                                        mLikecount.setText(String.valueOf(count - 1));
                                    }
                                });
                                mStore.collection("users").document(boardInfo.getUid())//게시물 올린사람 경험치 +1
                                        .update("likecount", FieldValue.increment(-1));
                            }
                        });
                    }
                });
            }
        });//좋아요 버튼

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                boardInfo = documentSnapshot.toObject(BoardInfo.class);
                if (boardInfo.getUidList().contains(mFirebaseUser.getUid())) {//이미 좋아요 누른사람은
                    mLikeButton.setLiked(true);
                }
            }
        });


    }


    private void upviewcount() {
        documentReference.update("viewcount", FieldValue.increment(1));
    }

    private void initUid() {
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {//uid 얻어오기
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        uid = document.get("uid").toString();
                    }
                }
            }
        });
    }

    private void retreiveDocumentReference(DocumentReference documentReference) {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                BoardInfo boardInfo = documentSnapshot.toObject(BoardInfo.class);

                //스크롤뷰 위치지정
                assert boardInfo != null;
                mTitle.setText(boardInfo.getTitle());
                birthdate.setText(boardInfo.getDate().toString());
                username.setText(boardInfo.getNickname());
                // mContent.setText(boardInfo.getContent());
                //내용 대신 에디터 (html)파일
                Editor renderer= (Editor)findViewById(R.id.renderer);
                Map<Integer, String> headingTypeface = getHeadingTypeface();
                Map<Integer, String> contentTypeface = getContentface();
                renderer.setHeadingTypeface(headingTypeface);
                renderer.setContentTypeface(contentTypeface);
                renderer.setDividerLayout(R.layout.tmpl_divider_layout);
                renderer.setEditorImageLayout(R.layout.tmpl_image_view);
                renderer.setListItemLayout(R.layout.tmpl_list_item);
                //String content= mSerialized;
                Log.d("콘텐츠",boardInfo.getContent());
                EditorContent Deserialized= renderer.getContentDeserialized(boardInfo.getContent());
                renderer.setEditorListener(new EditorListener() {
                    @Override
                    public void onTextChanged(EditText editText, Editable text) {

                    }
                    @Override
                    public void onUpload(Bitmap image, String uuid) {

                    }
                    @Override
                    public View onRenderMacro(String name, Map<String, Object> settings, int index) {
                        View view = getLayoutInflater().inflate(R.layout.layout_authored_by, null);
                        return view;
                    }
                });
                renderer.render(Deserialized);
                mReplycount.setText(String.valueOf(boardInfo.getReplycount()));
                mViewcount.setText(String.valueOf(boardInfo.getViewcount()));
                mLikecount.setText(String.valueOf(boardInfo.getUidList().size() - 1));

            }
        });


    }
    public Map<Integer,String> getHeadingTypeface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL, "fonts/GreycliffCF-Bold.ttf");
        typefaceMap.put(Typeface.BOLD, "fonts/GreycliffCF-Heavy.ttf");
        typefaceMap.put(Typeface.ITALIC, "fonts/GreycliffCF-Heavy.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC, "fonts/GreycliffCF-Bold.ttf");
        return typefaceMap;
    }

    public Map<Integer,String> getContentface() {
        Map<Integer, String> typefaceMap = new HashMap<>();
        typefaceMap.put(Typeface.NORMAL,"fonts/Lato-Medium.ttf");
        typefaceMap.put(Typeface.BOLD,"fonts/Lato-Bold.ttf");
        typefaceMap.put(Typeface.ITALIC,"fonts/Lato-MediumItalic.ttf");
        typefaceMap.put(Typeface.BOLD_ITALIC,"fonts/Lato-BoldItalic.ttf");
        return typefaceMap;
    }

    private void retreiveReply(final DocumentReference documentReference) {
        loadingbar.setTitle("Set profile image");
        loadingbar.setMessage("pleas wait업로딩중");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        CollectionReference collectionReference = documentReference.collection("reply");
        collectionReference
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<ReplyInfo> list = new ArrayList<>();
                        if (task.getResult() != null) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                ReplyInfo replyInfo = documentSnapshot.toObject(ReplyInfo.class);

                                assert replyInfo != null;
                                if (replyInfo.getDeleted_at().equals("0")) {
                                    list.add(replyInfo);
                                }
                            }
                            mReplyAdapter = new ReplyAdapter(list, documentReference, DetailActivity.this, DetailActivity.this
                                    , mTextInputLayout, mTextInputLayout2, mEditText, mEditText2, detail_ScrollView);
                            mRecyclerView.setAdapter(mReplyAdapter);

                            loadingbar.dismiss();
                        }
                    }
                });
    }

    private void updateReply(final DocumentReference documentReference) {
        documentReference.collection("reply").orderBy("date", Query.Direction.ASCENDING).whereEqualTo("deleted_at", "0")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {

                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                            return;
                        }
                        List<ReplyInfo> list = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : value) {
                            ReplyInfo replyInfo = doc.toObject(ReplyInfo.class);


                            list.add(replyInfo);

                        }
                        mReplyAdapter = new ReplyAdapter(list, documentReference, DetailActivity.this, DetailActivity.this
                                , mTextInputLayout, mTextInputLayout2, mEditText, mEditText2, detail_ScrollView);
                        mRecyclerView.setAdapter(mReplyAdapter);

                    }
                });
    }


    @Override
    public void onClick(String value) {//어댑터에서 클릭후 오는 정보는 여기로옴
        // value this data you receive when increment() / decrement()
        if (value.equals("실시간 댓글 삭제")) {
            retreiveReply(documentReference);
        }
    }

    void subscribe(String mDocumentId) {
        FirebaseMessaging.getInstance().subscribeToTopic(mDocumentId);
    }

    private void sendNotification(String documentId, String title, String content,String mBoardName) {
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
            mainObj.put("to", "/topics/" + documentId);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title + "에 댓글이 달렸습니다");
            notificationObj.put("body", "댓글:" + content);
            notificationObj.put("documentId", mDocumentId_send);
            notificationObj.put("BoardName",mBoardName);

            // mainObj.put("notification",notificationObj);
            mainObj.put("data", notificationObj);


            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL,
                    mainObj,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        Log.d("리스폰",response.toString());
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
    private void show_menu(View v,String documentId,final String boardname) {
        final FirebaseFirestore mStore = FirebaseFirestore.getInstance();
        documentReference_reply=mStore.collection("Board").document(documentId);
        final Context mContext=DetailActivity.this;
        PopupMenu popup = new PopupMenu(mContext, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.remove_superficially) {//Toast.makeText(mContext, "무슨수정이냐 그냥 쳐 삭제해라", Toast.LENGTH_LONG).show();
                    // mBoardInfo.remove(position);
                    return true;
                } else if (itemId == R.id.remove_firebase) {
                    Date date = new Date();
                    if (boardInfo.getUid().equals(mFirebaseUser.getUid())) {
                        documentReference_reply
                                .update("deleted_at", date.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(mContext, "파이어베이스 deleted_at 현재신간으로 업데이트", Toast.LENGTH_LONG).show();
                                //mCallback.onClick("실시간 댓글 삭제");//삭제하면 콜백함수로 양성열 보내짐.//이 어댑터에서 보낼 정보는 이렇게쓰면댐
                                // documentReference_reply.update("replycount", FieldValue.increment(-1));//댓글수 1증가.
//                                Intent intent=new Intent(DetailActivity.this, MainActivity.class);
//                                intent.putExtra("데이터삭제",boardname);
//                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext, "파이어베이스 deleted_at 업데이트실패", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(mContext, "너가 올린 댓글이 아니다", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
                return false;
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_showup, popup.getMenu());
        popup.show();

    }

    private void uploadStoreUserReplyModel(final ReplyInfo replyInfo){

        final String userUid = firebaseUser.getUid();
        final String mDocumentId = getIntent().getStringExtra("DocumentId");//mDocumentId는 디테일 정보받아오기
        final String mBoardName = getIntent().getStringExtra("BoardName");

       // final ReplyActModel replyActModel = new ReplyActModel(replyInfo.getDeleted_at(), replyInfo.getContent(), replyInfo.getDate(), replyInfo.getDocumentId(), replyInfo.getUidLikelist(), mDocumentId, mBoardName );

        mStore.collection("users").document(userUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                BossModel bossModel = documentSnapshot.toObject(BossModel.class);
                assert bossModel != null;
//                try{
//                    userModel.getReplyList().add(replyActModel);
//                }catch (Exception e){
//                    ArrayList<ReplyActModel> replyList = new ArrayList<>();
//                    replyList.add(replyActModel);
//                    userModel.setReplyList(replyList);
//                }

                mStore.collection("users").document(userUid).set(bossModel, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("DetailActivity", "UploadStoreUserModel update Success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DetailActivity", "UploadStoreUserModel update fail");
                    }
                });;

            }
        });
    }



}

