package com.app.myhousereport;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.myhousereport.databinding.FragmentReportBinding;
import com.google.android.gms.tasks.Task;
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

    private void getDataFromFirebase() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("در حال ارتباط...");
//        progressDialog.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("report")
                .orderBy("date")
                .whereEqualTo("user", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task) -> {
                    if (task.isSuccessful()) {
                        List<ReportModel> reportModels = new ArrayList<>();
                        int daramad = 0;
                        int hazine = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ReportModel reportModel = new ReportModel(document.getId(), document.getData());
                            if (reportModel.type == 1)
                                daramad += reportModel.price;
                            if (reportModel.type == 0)
                                hazine += reportModel.price;
                            reportModels.add(reportModel);
                        }

                        int mojoodi = daramad - hazine;
                        bindData(reportModels, daramad, hazine, mojoodi);

                    } else {
                        Log.e("ReportFragment", "Error!", task.getException());
                        progressDialog.hide();
                    }
                });
    }

    private void bindData(List<ReportModel> reportModels, int daramad, int hazine, int mojoodi) {
        mBinding.textViewCosts.setText(String.format("%d تومان", hazine));
        mBinding.textViewIncomes.setText(String.format("%d تومان", daramad));
        mBinding.textView.setText(String.format("%d تومان", mojoodi));
        ReportAdapter adapter = new ReportAdapter(getContext(), reportModels);
        mBinding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromFirebase();
    }

}

