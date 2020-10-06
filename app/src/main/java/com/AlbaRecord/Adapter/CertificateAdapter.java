package com.AlbaRecord.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AlbaRecord.R;

import java.util.ArrayList;
import java.util.List;

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.certificateViewHolder> {
    List<String> contents=new ArrayList<>();

    public static class certificateViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView content;
        public certificateViewHolder(View v) {
            super(v);
            content=(TextView)v.findViewById(R.id.content);

        }
    }

    public CertificateAdapter(List<String> contents) {
        this.contents = contents;
    }

    @NonNull
    @Override
    public CertificateAdapter.certificateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new certificateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_certificate, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateAdapter.certificateViewHolder holder, int position) {
        String str=contents.get(position);
        holder.content.setText(str);
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }


}
