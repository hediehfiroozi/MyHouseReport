package com.app.myhousereport;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ReportViewHolder extends RecyclerView.ViewHolder {
    TextView tvPrice;
    TextView tvTitle;
    TextView tvCategory;
    TextView tvposition;
    View parent;
    Button buttonDelete;
    Button buttonEdit;
    Button buttonShow;


    public ReportViewHolder(@NonNull View itemView) {
        super(itemView);
        tvPrice = itemView.findViewById(R.id.tvPrice);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvCategory = itemView.findViewById(R.id.tvCategory);
        tvposition = itemView.findViewById(R.id.tvposition);
        parent = itemView.findViewById(R.id.parent);
        buttonDelete = itemView.findViewById(R.id.buttonDelete);
        buttonEdit = itemView.findViewById(R.id.buttonEdit);
        buttonShow = itemView.findViewById(R.id.buttonShow);
    }
}