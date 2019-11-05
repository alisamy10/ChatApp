package com.example.newchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.newchat.Base.BaseActivity;
import com.example.newchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImage;
    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;
    private boolean emailValue = false;
    private final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mEmail.getText().toString().trim().matches(EMAIL_PATTERN)) {
                    mEmail.setError("Invalid Email user@domain.com");
                    mEmail.setFocusable(true);
                } else {
                    mEmail.setError(null);
                    emailValue = true;
                }

            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if (mPassword.getText().toString().trim().length() < 6)
                    mPassword.setError("password should be > 6  chars ");
                else
                    mPassword.setError(null);

            }
        });
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    mImage.setImageResource(R.drawable.facepalm);
                else
                    mImage.setImageResource(R.drawable.user);
            }
        });


    }

    private void initView() {
        mImage =  findViewById(R.id.image);
        mEmail = findViewById(R.id.email);
        mPassword =  findViewById(R.id.password);
        mLogin =  findViewById(R.id.login);
        mLogin.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (isValidForm(email, password))
                    loginUser(email, password);

                break;
            case R.id.need_new_account:// TODO 19/11/03
                startActivity(new Intent(this, RegisterActivity.class));
                finish();

                break;

            default:
                break;
        }
    }

    private void loginUser(String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        showProgressDialog("please wait ....");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            showMessage("user Login succesful ", "ok");
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        } else {

                            showMessage("user Login failed "+task.getException().getLocalizedMessage(), "ok");
                        }

                        // ...
                    }
                });
    }

    private boolean isValidForm(String email, String password) {
        boolean isValid = true;
        if (email.isEmpty()) {
            mEmail.setError("Required");
            isValid = false;
        } else {
            mEmail.setError(null);
            isValid = true;
        }
        if (password.isEmpty()) {
            mPassword.setError("Required");
            isValid = false;
        } else {
            mPassword.setError(null);
            isValid = true;
        }
        return isValid;
    }
}
