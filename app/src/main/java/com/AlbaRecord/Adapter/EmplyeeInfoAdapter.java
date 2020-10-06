package com.AlbaRecord.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AlbaRecord.Model.EmployeeModel;
import com.AlbaRecord.Model.UserModel;
import com.AlbaRecord.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class EmplyeeInfoAdapter extends RecyclerView.Adapter<EmplyeeInfoAdapter.EmplyeeInfoViewHolder> {
    private List<EmployeeModel> employeeModels;

    public EmplyeeInfoAdapter(List<EmployeeModel> employeeModels) {
        this.employeeModels = employeeModels;
    }

    public class EmplyeeInfoViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView name, age, phone;
        EditText position;
        Button setposition, evaluate, fire;

        public EmplyeeInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            name = (TextView) itemView.findViewById(R.id.name);
            age = (TextView) itemView.findViewById(R.id.age);
            phone = (TextView) itemView.findViewById(R.id.phone);
            position = (EditText) itemView.findViewById(R.id.position);
            setposition = (Button) itemView.findViewById(R.id.setposition);
            evaluate = (Button) itemView.findViewById(R.id.evaluate);
            fire = (Button) itemView.findViewById(R.id.fire);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull EmplyeeInfoAdapter.EmplyeeInfoViewHolder holder, int position) {
        EmployeeModel employeeModel = employeeModels.get(position);
        Glide.with(holder.photo)
                .load(employeeModel.getPhoto())
                .into(holder.photo);
        holder.name.setText(employeeModel.getName());
        holder.age.setText(employeeModel.getAge());
        holder.phone.setText(employeeModel.getPhonenumber());


    }


    @Override
    public int getItemCount() {
        return employeeModels.size();
    }

    @NonNull
    @Override
    public EmplyeeInfoAdapter.EmplyeeInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmplyeeInfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employ_info, parent, false));
    }


}
