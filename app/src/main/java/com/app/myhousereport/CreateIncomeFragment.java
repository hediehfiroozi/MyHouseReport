package com.app.myhousereport;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.myhousereport.databinding.FragmentAddCostBinding;
import com.app.myhousereport.databinding.FragmentAddIncomeBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class CreateIncomeFragment extends Fragment {

    public CreateIncomeFragment() {
        // Required empty public constructor
    }

    FragmentAddIncomeBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentAddIncomeBinding.inflate(getLayoutInflater());
        mBinding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataToFirebase();
            }
        });
        return mBinding.getRoot();
    }

    private void showSnackbar(String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_LONG)
                .setAction("بستن", view -> {

                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    private boolean checkAll() {
        if (TextUtils.isEmpty(mBinding.tietTitle.getText().toString())) {
            showSnackbar("لطفا عنوان را وارد کنید.");
            return false;
        }
        if (TextUtils.isEmpty(mBinding.tietPrice.getText().toString())) {
            showSnackbar("لطفا مقدار هزینه را وارد کنید.");
            return false;
        }
        if (TextUtils.isEmpty(mBinding.tietDate.getText().toString())) {
            showSnackbar("لطفا تاریخ را وارد کنید.");
            return false;
        }
        if (TextUtils.isEmpty(mBinding.tietDescription.getText().toString())) {
            showSnackbar("لطفا توضیحات را وارد کنید.");
            return false;
        }
        if (TextUtils.isEmpty(mBinding.tietCategory.getText().toString())) {
            showSnackbar("لطفا نوع دسته بندی را وارد کنید.");
            return false;
        }
        return true;
    }

    private void addDataToFirebase() {

        // check inputs
        if (!checkAll()) return;

        //create model from form
        Map<String, Object> user = new HashMap<>();
        user.put("user", FirebaseAuth.getInstance().getCurrentUser().getUid());
        user.put("title", mBinding.tietTitle.getText().toString());
        user.put("price", Integer.parseInt(mBinding.tietPrice.getText().toString()));
        user.put("date", mBinding.tietDate.getText().toString());
        user.put("description", mBinding.tietDescription.getText().toString());
        user.put("category", mBinding.tietCategory.getText().toString());
        user.put("type", 1);//daramad

        //create progress dialog
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("در حال ارتباط...");
        progressDialog.show();

        //create firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("report").add(user).addOnSuccessListener(documentReference -> {
            showSnackbar("آیتم با موفقیت ثبت شد.");
            progressDialog.hide();

            mBinding.tietTitle.setText("");
            mBinding.tietPrice.setText("");
            mBinding.tietDate.setText("");
            mBinding.tietDescription.setText("");
            mBinding.tietCategory.setText("");

            ((MainActivity) getActivity()).updateReportFragment();


        })
                .addOnFailureListener(e -> {
                    showSnackbar(e.getMessage());
                    progressDialog.hide();
                });
    }
}