package com.AlbaRecord.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.AlbaRecord.Interface.OnItemClick;
import com.AlbaRecord.Model.ReplyInfo;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder> {
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    // private List<String> documentIdList;
    private FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private Context mContext;
    private OnItemClick mCallback;
    private DocumentReference documentReference_reply;//reply 전까지
    ///
    private TextInputLayout textInputLayout_original;
    private TextInputLayout textInputLayout_temp;
    private TextInputEditText textInputEditText_original;
    private TextInputEditText textInputEditText_temp;
    private ProgressDialog loadingbar;
   // private ReplytoreplyAdapter mReplytoreplyAdapter;
    private List<ReplyInfo> mReplyList;
    private int count = 0;
    private ScrollView scrollView;
    //private ScrollView detail_ScrollView;


//    ///////////////////////////클릭리스너
//    public interface OnItemClickListener{
//        void onitemClick(View v, int pos);
//    }
//    private BoardAdapter.OnItemClickListener mListener=null;
//    public void setOnIemlClickListner(BoardAdapter.OnItemClickListener listner){
//        this.mListener=listner;
//    }
//    ////////////////////////////////


    public ReplyAdapter(List<ReplyInfo> mReplyList, DocumentReference documentReference_reply, Context context, OnItemClick listener
            , TextInputLayout t1, TextInputLayout t2, TextInputEditText e1, TextInputEditText e2, ScrollView s1) {//생성자
        this.mReplyList = mReplyList;
        this.documentReference_reply = documentReference_reply;
        this.mContext = context;
        this.mCallback = listener;
        textInputLayout_original = t1;
        textInputLayout_temp = t2;
        textInputEditText_original = e1;
        textInputEditText_temp = e2;
        scrollView = s1;
    }

    public ReplyAdapter() {
    }

    @NonNull
    @Override
    public ReplyAdapter.ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReplyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ReplyAdapter.ReplyViewHolder holder, final int position) {
        final ConstraintLayout constraintLayout = new ConstraintLayout(mContext);
        final ReplyInfo replyInfo = mReplyList.get(position);
        final DocumentReference replyInreplyRef = documentReference_reply//대댓글 document 까지 가는 docuRef
                .collection("reply").document(replyInfo.getDocumentId())
                .collection("replyInreply").document();
      //  retreiveReplyInReply(replyInreplyRef.getParent(), holder, position);//대 댓글 불러오기 기능
        holder.mContent.setText(replyInfo.getContent());
        holder.mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String documentId = replyInfo.getDocumentId();
                show_menu(v, position);
            }


        });

        mStore.collection("users")//닉네임 가져오기
                .document(replyInfo.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        BossModel fm = documentSnapshot.toObject(BossModel.class);
                        assert fm != null;
                        Log.d("댓글시간", replyInfo.getDate().toString());
                        String date = replyInfo.getDate().toString();
                        String date1 = date.substring(11, 16);
                        String date2 = replyInfo.getDate().toString().substring(11, 13);//시간부분
                        int hour = (Integer.parseInt(date2) + 9) % 24;
                        //String finaldate=String.valueOf(hour)+replyInfo.getDate().toString().substring(13,16);
                        String finaldate = date1;
                        Log.d("홈 댓글시간", finaldate);
                        //String str=fm.getUserNickName()+"("+fm.getNickname()+")\n"+finaldate;
                        String str = fm.getName();
                        holder.mNickname.setText(str);

                    }
                });
        holder.mLikebutton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                final FirebaseFirestore mStore = FirebaseFirestore.getInstance();
                documentReference_reply.collection("reply").document(replyInfo.getDocumentId())
                        .update("uidLikelist", FieldValue.arrayUnion(mFirebaseUser.getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        documentReference_reply.collection("reply").document(replyInfo.getDocumentId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                int count = task.getResult().toObject(replyInfo.getClass()).getUidLikelist().size();
                                Log.d("댓글", String.valueOf(count));
                                holder.mLikecount.setText(String.valueOf(count - 1));
                            }
                        });
                        mStore.collection("users").document(replyInfo.getUid()).update("likecount", FieldValue.increment(1));//경험치+1
                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {

                documentReference_reply.collection("reply").document(replyInfo.getDocumentId())
                        .update("uidLikelist", FieldValue.arrayRemove(mFirebaseUser.getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        documentReference_reply.collection("reply").document(replyInfo.getDocumentId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                int count = task.getResult().toObject(replyInfo.getClass()).getUidLikelist().size();
                                Log.d("댓글", String.valueOf(count));
                                holder.mLikecount.setText(String.valueOf(count - 1));
                            }
                        });
                        mStore.collection("users").document(replyInfo.getUid()).update("likecount", FieldValue.increment(-1));//경험치 -1
                    }
                });
            }
        });
        holder.mLikecount.setText(String.valueOf(replyInfo.getUidLikelist().size() - 1));
        assert firebaseUser != null;
        if (replyInfo.getUidLikelist().contains(firebaseUser.getUid())) {
            holder.mLikebutton.setLiked(true);
        }
        holder.mReplyimage.setOnClickListener(new View.OnClickListener() {//대 댓글을 실행하시겠습니까 ?
            @Override
            public void onClick(View v) {//대댓글 다이어로그 실행.
                AlertDialog.Builder ad = new AlertDialog.Builder(mContext);
                ad.setIcon(R.drawable.main);
                //ad.setTitle("제목");
                ad.setMessage("대댓글을 실행하시겠습니까?");
                //yes or no
                ad.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textInputLayout_original.setVisibility(View.INVISIBLE);
                        textInputLayout_temp.setVisibility(View.VISIBLE);
//                        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(INPUT_METHOD_SERVICE);
//                        assert imm != null;// 일반적인 에디트텍스트 액티비티에서 보여줄때
//                        imm.showSoftInput(mEditText, 0);
                        textInputEditText_temp.requestFocus();//에디트 텍스트 활성화
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert imm != null;
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        holder.itemView.setBackgroundColor(Color.GRAY);

                        textInputLayout_temp.setEndIconOnClickListener(new View.OnClickListener() {//대댓글 달때
                            @Override
                            public void onClick(View v) {

                                //Toast.makeText(mContext,"안보이던놈이 눌림",Toast.LENGTH_SHORT).show();
                                final DocumentReference replyInreplyRef = documentReference_reply
                                        .collection("reply").document(replyInfo.getDocumentId())
                                        .collection("replyInreply").document();
                                if (textInputEditText_temp.getText() != null) {
                                    final String uid = firebaseUser.getUid();
                                    String reply_string = textInputEditText_temp.getText().toString();
                                    assert firebaseUser != null;
                                    final ReplyInfo replyInfo = new ReplyInfo(
                                            firebaseUser.getUid()
                                            , "0"
                                            , reply_string
                                            , new Date()
                                            , replyInreplyRef.getId()
                                            , Arrays.asList(""));
                                    replyInreplyRef.set(replyInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            textInputLayout_original.setVisibility(View.VISIBLE);
                                            textInputLayout_temp.setVisibility(View.INVISIBLE);
                                            holder.itemView.setBackgroundColor(Color.rgb(224, 224, 224));
                                            textInputEditText_temp.setText("");
                                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
                                            assert imm != null;
                                            imm.hideSoftInputFromWindow(textInputEditText_temp.getWindowToken(), 0);

                                           // retreiveReplyInReply(replyInreplyRef.getParent(), holder, position);
                                            // scrollView.smoothScrollTo(0,holder.mContent.getBottom());

                                        }
                                    });

                                } else {
                                    Toast.makeText(mContext, "댓글을 입력하시오", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        dialog.dismiss();
                    }
                });

                ad.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });
        String dateTime2 = new Date().toString();
        String dateTime = replyInfo.getDate().toString().substring(4, 10);
        Log.d("date1", dateTime);
        if (dateTime2.substring(4, 10).equals(dateTime)) {
//            holder.mN.setVisibility(View.VISIBLE);
        }
        holder.date.setText(replyInfo.getDate().toString());


    }

    @Override
    public int getItemCount() {
        return mReplyList.size();
    }

//    private void retreiveReplyInReply(final CollectionReference replyInreplyRef, @NonNull final ReplyAdapter.ReplyViewHolder holder, final int position) {
//
//        replyInreplyRef
//                .orderBy("date", Query.Direction.ASCENDING)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    List<ReplyInfo> list = new ArrayList<>();
//
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.getResult() != null) {
//                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
//                                ReplyInfo replyInfo = documentSnapshot.toObject(ReplyInfo.class);
//                                assert replyInfo != null;
//                                if (replyInfo.getDeleted_at().equals("0")) {
//                                    list.add(replyInfo);
//                                }
//                            }
//                            mReplytoreplyAdapter = new ReplytoreplyAdapter(list, mContext, replyInreplyRef.getParent(), mCallback);
//                            holder.mRecyclerView.setAdapter(mReplytoreplyAdapter);
//
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(mContext, "대댓글 불러오기 실패", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    class ReplyViewHolder extends RecyclerView.ViewHolder {
        private TextView mContent;
        private ImageView mMenu;
        private TextView mNickname;
        private LikeButton mLikebutton;
        private TextView mLikecount;
        private ImageView mReplyimage;

        private ImageView mN;
        private TextView date;


        ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.item_reply_content);
            mMenu = itemView.findViewById(R.id.item_reply_menu_imageView);
            mNickname = itemView.findViewById(R.id.item_nickname_level);
            mLikebutton = itemView.findViewById(R.id.item_reply_likebutton);
            mLikecount = itemView.findViewById(R.id.item_reply_likecount);
            mReplyimage = itemView.findViewById(R.id.item_reply);
            loadingbar = new ProgressDialog(mContext);

//            mN = itemView.findViewById(R.id.reply_new);
            date=itemView.findViewById(R.id.date);

//            itemView.setOnClickListener(new View.OnClickListener() {//클릭했을때
//                @Override
//                public void onClick(View v) {//들어가는 기능 detail로
//                    int pos=getAdapterPosition();
//                    if(pos!=RecyclerView.NO_POSITION){
//                        if(mListener!=null){
//                            mListener.onitemClick(v,pos);
//                        }
//                    }
//                }
//            });

        }
    }

    private void show_menu(View v, final int position) {
        final FirebaseFirestore mStore = FirebaseFirestore.getInstance();
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
                    if (mReplyList.get(position).getUid().equals(mFirebaseUser.getUid())) {
                        documentReference_reply.collection("reply").document(mReplyList.get(position).getDocumentId())
                                .update("deleted_at", date.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(mContext, "파이어베이스 deleted_at 현재신간으로 업데이트", Toast.LENGTH_LONG).show();
                                mCallback.onClick("실시간 댓글 삭제");//삭제하면 콜백함수로 양성열 보내짐.//이 어댑터에서 보낼 정보는 이렇게쓰면댐
                                documentReference_reply.update("replycount", FieldValue.increment(-1));//댓글수 1증가.
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

}
