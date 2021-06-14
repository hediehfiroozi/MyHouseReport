package com.app.myhousereport;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.myhousereport.databinding.ActivityShowEditReportBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ShowEditActivity extends AppCompatActivity {

    private ActivityShowEditReportBinding mBinding;
    private ReportModel reportModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityShowEditReportBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        String report = getIntent().getStringExtra("report");
        boolean enable_edit_mode = getIntent().getBooleanExtra("enable_edit_mode", false);

        if (enable_edit_mode) {
            mBinding.tvTitle.setText("ویرایش گزارش");
            mBinding.btnSave.setVisibility(View.VISIBLE);
            mBinding.tietPrice.setEnabled(true);
            mBinding.tietCategory.setEnabled(true);
            mBinding.tietTitle.setEnabled(true);
            mBinding.tietDate.setEnabled(true);
            mBinding.tietDescription.setEnabled(true);
        } else {
            mBinding.tvTitle.setText("مشاهده گزارش");
            mBinding.btnSave.setVisibility(View.GONE);
            mBinding.tietPrice.setEnabled(false);
            mBinding.tietCategory.setEnabled(false);
            mBinding.tietTitle.setEnabled(false);
            mBinding.tietDate.setEnabled(false);
            mBinding.tietDescription.setEnabled(false);
        }

        mBinding.buttonBack.setOnClickListener(v -> {
            finish();
        });

        mBinding.btnSave.setOnClickListener(v -> {
            Map<String, Object> map = new HashMap<>();
            map.put("title", mBinding.tietTitle.getText().toString());
            map.put("price", Long.parseLong(mBinding.tietPrice.getText().toString()));
            map.put("date", mBinding.tietDate.getText().toString());
            map.put("description", mBinding.tietDescription.getText().toString());
            map.put("category", mBinding.tietCategory.getText().toString());

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("درحال ارتباط...");
            progressDialog.show();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("report").document(reportModel.id).update(map)
                    .addOnSuccessListener(unused -> {
                        showSnackbar("گزارش با موفقیت بروزرسانی شد.");
                        progressDialog.hide();
                    })
                    .addOnFailureListener(e -> {
                        showSnackbar("خطایی رخ داده است");
                        progressDialog.hide();
                    });
        });

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("درحال ارتباط...");
        progressDialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("report")
                .document(report)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    progressDialog.hide();
                    reportModel = new ReportModel(documentSnapshot);
                    mBinding.tietPrice.setText(reportModel.price + "");
                    mBinding.tietCategory.setText(reportModel.category);
                    mBinding.tietTitle.setText(reportModel.title);
                    mBinding.tietDate.setText(reportModel.date);
                    mBinding.tietDescription.setText(reportModel.description);
                })
                .addOnFailureListener(e -> {
                    progressDialog.hide();
                    showSnackbar("خطایی رخ داده است.");
                    finish();
                });
    }

    private void showSnackbar(String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_LONG)
                .setAction("بستن", view -> {

                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

}