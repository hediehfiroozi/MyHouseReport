package com.app.myhousereport;

public interface ReportAdapterListener {
    void onDeleteReportClick(ReportModel reportModel);

    void onShowReportClick(ReportModel reportModel);

    void onEditReportClick(ReportModel reportModel);
}
