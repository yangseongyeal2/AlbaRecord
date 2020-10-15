package com.AlbaRecord.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.AlbaRecord.Model.NotiInfo;
import com.AlbaRecord.R;

import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationAdapterViewHolder> {

    private List<NotiInfo> mNotiInfoListlist;

    ///////////////////////////클릭리스너
    public interface OnItemClickListener {
        void onitemClick(View v, int pos);
    }

    private static NotificationAdapter.OnItemClickListener mListener = null;

    public void setOnIemlClickListner(NotificationAdapter.OnItemClickListener listner) {
        mListener = listner;
    }

    public NotificationAdapter(List<NotiInfo> notiInfoListlist) {
        this.mNotiInfoListlist = notiInfoListlist;
    }

    ////////////////////////////////
    @NonNull
    @Override
    public NotificationAdapter.NotificationAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.item_noti, parent, false);
        return new NotificationAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationAdapterViewHolder holder, int position) {
        NotiInfo notiInfo = mNotiInfoListlist.get(position);
        holder.title.setText(notiInfo.getTitle());
        holder.content.setText(notiInfo.getContent());


        //시간간
       String date = notiInfo.getDate().toString();
        String date1 = date.substring(11, 16);
        String date2 = date.substring(11, 13);//시간부분
        int hour = (Integer.parseInt(date2) + 9) % 24;
        //String finaldate = String.valueOf(hour) + date.substring(13, 16);
        String finaldate=date1;
        holder.mDate.setText(finaldate);
        String dateTime2 = new Date().toString();
        String dateTime = dateTime2.substring(4, 10);
        Log.d("date1", dateTime);
        if (date.substring(4, 10).equals(dateTime)) {
//            holder.mN.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mNotiInfoListlist.size();
    }

    public class NotificationAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView content, mDate;
        private ImageView mN;

        public NotificationAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_noti_title);
            content = itemView.findViewById(R.id.item_noti_content);
            mDate = itemView.findViewById(R.id.item_noti_date);
            //mN=itemView.findViewById(R.id.item_noti_n);

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
