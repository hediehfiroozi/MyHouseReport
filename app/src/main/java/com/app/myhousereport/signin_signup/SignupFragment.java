package com.app.myhousereport.signin_signup;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.myhousereport.databinding.FragmentSignupBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class SignupFragment extends Fragment {

    private FragmentSignupBinding mBinding;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSignupBinding.inflate(getLayoutInflater());
        firebaseAuth = FirebaseAuth.getInstance();
        mBinding.btnSignUp.setOnClickListener(v -> signup());
        return mBinding.getRoot();
    }

    private void showSnackbar(String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_LONG)
                .setAction("بستن", view -> {

                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

    private void signup() {

        String email = mBinding.textInputEditTextEmail.getText().toString();
        String password = mBinding.textInputEditTextPassword.getText().toString();

        if (email == null || TextUtils.isEmpty(email)) {
            showSnackbar("لطفا ایمیل خود را وارد کنید");
            return;
        }
        if (password == null || TextUtils.isEmpty(password)) {
            showSnackbar("لطفا رمزعبور خود را وارد کنید");
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("درحال ارتباط...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showSnackbar("ثبت نام شما با موفقیت انجام شد!");
                    } else {
                        showSnackbar("ثبت نام با خطا مواجه شد. لطفا بعدا تلاش کنید!");
                    }
                    progressDialog.hide();
                });
    }
}