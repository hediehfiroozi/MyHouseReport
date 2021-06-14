package com.app.myhousereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ReportAdapter extends RecyclerView.Adapter<ReportViewHolder> {
    Context context;
    List<ReportModel> reportModels;

    public ReportAdapter(Context context, List<ReportModel> reportModels) {
        this.context = context;
        this.reportModels = reportModels;
    }

    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_report, parent, false);
        ReportViewHolder reportViewHolder = new ReportViewHolder(view);
        return reportViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        ReportModel reportModel = reportModels.get(position);
        holder.tvposition.setText((position + 1) + "");
        if (reportModel.type == 0)
            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.red));
        if (reportModel.type == 1)
            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.green));
        holder.tvPrice.setText(reportModel.price + " تومان");
        holder.tvTitle.setText(reportModel.title);
        holder.tvCategory.setText(reportModel.category);

        holder.parent.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return reportModels.size();
    }

}
