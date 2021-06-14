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
    ReportAdapterListener listener;

    public ReportAdapter(Context context, List<ReportModel> reportModels, ReportAdapterListener listener) {
        this.context = context;
        this.reportModels = reportModels;
        this.listener = listener;
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

        holder.buttonDelete.setOnClickListener(v -> {
            listener.onDeleteReportClick(reportModel);
        });

        holder.buttonDelete.setOnClickListener(v -> {
            listener.onDeleteReportClick(reportModel);
        });
        holder.buttonShow.setOnClickListener(v -> {
            listener.onShowReportClick(reportModel);
        });
        holder.buttonEdit.setOnClickListener(v -> {
            listener.onEditReportClick(reportModel);
        });
    }

    @Override
    public int getItemCount() {
        return reportModels.size();
    }

}
