package com.AlbaRecord.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AlbaRecord.Interface.OnItemClick;
import com.AlbaRecord.Model.BoardInfo;
import com.AlbaRecord.Model.BossModel;
import com.AlbaRecord.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.BoardViewHolder> {
    private List<BoardInfo> mBoardInfo;
    private Context mContext;
    private FirebaseUser mFirebaseUser;//현재 사용중인 앱의 주인의 정보 .getCurrent 까지 된정보
    private OnItemClick mCallback;
    private int count = 0;
    private String mBoardName;


    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    public CommonAdapter(List<BoardInfo> mBoardInfo, Context mContext, FirebaseUser mFirebaseUser, OnItemClick listener, String mBoardName) {
        this.mBoardInfo = mBoardInfo;
        this.mContext = mContext;
        this.mFirebaseUser = mFirebaseUser;
        this.mCallback = listener;

        this.mBoardName = mBoardName;
    }
    public CommonAdapter(List<BoardInfo> mBoardInfo, Context mContext, FirebaseUser mFirebaseUser, OnItemClick listener) {
        this.mBoardInfo = mBoardInfo;
        this.mContext = mContext;
        this.mFirebaseUser = mFirebaseUser;
        this.mCallback = listener;

    }



    ///////////////////////////클릭리스너
    public interface OnItemClickListener {
        void onitemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnIemlClickListner(OnItemClickListener listner) {
        this.mListener = listner;
    }
    ////////////////////////////////


    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BoardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commonbaord, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final BoardViewHolder holder, final int position) {

        final BoardInfo boardInfo = mBoardInfo.get(position);
        holder.mTitleTextView.setText(boardInfo.getTitle());
        //댓글수 가져오기
       // String replycount = String.valueOf(boardInfo.getReplycount()) + "\n" + "댓글";
        holder.mReplycount.setText(String.valueOf(boardInfo.getReplycount()));
        //올린시간 가져오기
        String date = boardInfo.getDate().toString();
        String date1 = date.substring(11, 16);
        String date2 = date.substring(11, 13);//시간부분
        final String finaldate = date1;
        String dateTime2 = new Date().toString();
        String dateTime = dateTime2.substring(4, 10);
        Log.d("date1", dateTime);
        //n표시
        if (date.substring(4, 10).equals(dateTime)) {
            //holder.mN.setVisibility(View.VISIBLE);
        }
        //작성자
        String writer = boardInfo.getUid();
        mStore.collection("users").document(writer).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                BossModel fm = documentSnapshot.toObject(BossModel.class);
                assert fm != null;
                try {
                    holder.mSubinfo.setText(fm.getName() + " " + finaldate + " ");
                    //holder.mSubinfo.setText("테스트");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //이미지그려주기
        if (boardInfo.getmDownloadURIList()!= null&&boardInfo.getmDownloadURIList().size()!=0) {
            Glide.with(holder.imageView).load(boardInfo.getmDownloadURIList().get(0)).into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.INVISIBLE);
        }
        holder.mLikecount.setText(String.valueOf(boardInfo.getUidList().size()));


    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public int getItemCount() {

        return mBoardInfo.size();
    }

    class BoardViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private TextView mSubinfo;
        private TextView mReplycount;
        private ImageView imageView;
        private TextView mLikecount;


        public BoardViewHolder(View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.normal_item_title);
            mSubinfo = itemView.findViewById(R.id.normal_name_date_viewcont);
            imageView = itemView.findViewById(R.id.normal_Imageview);
            mReplycount = itemView.findViewById(R.id.normal_reply);
            mLikecount=itemView.findViewById(R.id.viewcounttext);

            itemView.setOnClickListener(new View.OnClickListener() {//클릭했을때
                @Override
                public void onClick(View v) {//들어가는 기능 detail로
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onitemClick(v, pos);
                        }
                    }
                }
            });

        }
    }

}
