package com.app.myhousereport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.myhousereport.databinding.FragmentReportBinding;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {

    private FragmentReportBinding mBinding;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentReportBinding.inflate(getLayoutInflater());
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataFromFirebase();

    }

    private void getDataFromFirebase() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("در حال ارتباط...");
//        progressDialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("report")
                .whereEqualTo("user", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task) -> {
                    if (task.isSuccessful()) {
                        bindData(task.getResult());
                    } else {
                        Log.e("ReportFragment", "Error!", task.getException());
                        progressDialog.hide();
                    }
                });
    }

    List<ReportModel> reportModels = new ArrayList<>();

    private void bindData(QuerySnapshot result) {
        reportModels = new ArrayList<>();
        int daramad = 0;
        int hazine = 0;
        for (QueryDocumentSnapshot document : result) {
            ReportModel reportModel = new ReportModel(document);
            if (reportModel.type == 1)
                daramad += reportModel.price;
            if (reportModel.type == 0)
                hazine += reportModel.price;
            reportModels.add(reportModel);
        }
        ReportAdapter adapter = new ReportAdapter(getContext(), reportModels, new ReportAdapterListener() {
            @Override
            public void onDeleteReportClick(ReportModel reportModel) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("report").document(reportModel.id).delete()
                        .addOnFailureListener(e -> {
                            showSnackbar("خطایی رخ داد است. دوباره تلاش کنید.");
                        })
                        .addOnSuccessListener(unused -> {
                            showSnackbar("آیتم با موفقیت حذف شد.");
                            update();
                        });
            }

            @Override
            public void onShowReportClick(ReportModel reportModel) {
                Intent intent = new Intent(getContext(), ShowEditActivity.class);
                intent.putExtra("report", reportModel.id);
                intent.putExtra("enable_edit_mode", false);
                activityResultLauncher.launch(intent);
            }

            @Override
            public void onEditReportClick(ReportModel reportModel) {
                Intent intent = new Intent(getContext(), ShowEditActivity.class);
                intent.putExtra("report", reportModel.id);
                intent.putExtra("enable_edit_mode", true);
                activityResultLauncher.launch(intent);
            }
        });
        mBinding.recyclerView.setAdapter(adapter);

        int mojoodi = daramad - hazine;

        mBinding.textViewCosts.setText(String.format("%d تومان", hazine));
        mBinding.textViewIncomes.setText(String.format("%d تومان", daramad));
        mBinding.textView.setText(String.format("%d تومان", mojoodi));

    }

    public void update() {
        getDataFromFirebase();
    }

    private void showSnackbar(String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_LONG)
                .setAction("بستن", view -> {

                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result1 -> {
                update();
            });
}

