package com.example.newchat.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.newchat.Base.BaseActivity;
import com.example.newchat.R;
import com.example.newchat.database.UsersDao;
import com.example.newchat.database.model.User;
import com.example.newchat.util.DataUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private TextInputLayout mNameUser, mAge;
    private EditText mEmail, mPassword, mConfirmPassword;

    private RadioGroup radioGenderGroup;
    private RadioButton radioGenderButton;
    private Button mRegister;

    private boolean emailValue = false;
    private final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ImageView mImage;
    private RadioGroup mRadiogroup;
    private RadioButton mRadioButtonGender;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private TextView mNewAccountNeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
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

        mConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mPassword.getText().toString().trim().equals(mConfirmPassword.getText().toString()))
                    mConfirmPassword.setError("Password Not Matching");
                else
                    mConfirmPassword.setError(null);

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
        mConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        mNameUser = findViewById(R.id.user_name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.password_confirm);
        mAge = findViewById(R.id.age);
        mRegister = findViewById(R.id.register);
        mRegister.setOnClickListener(this);
        mImage = findViewById(R.id.image);
        mRadiogroup = findViewById(R.id.radiogroup);
        int selectedId = mRadiogroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        mRadioButtonGender = findViewById(selectedId);

        mNewAccountNeed = findViewById(R.id.need_new_account);
        mNewAccountNeed.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register) {

            String sNameUser = mNameUser.getEditText().getText().toString().trim();
            String sEmail = mEmail.getText().toString().trim();
            String sPassword = mPassword.getText().toString().trim();
            String sConfirmPassword = mConfirmPassword.getText().toString().trim();
            String sAge = mAge.getEditText().getText().toString().trim();
            //String sGender=mRadioButtonGender.getText().toString().trim();
            String sGender = "";
            String status="";


            if (isValidForm(sNameUser, sEmail, sPassword, sConfirmPassword, sAge, sGender))
                registerAccount(sNameUser, sEmail, sPassword , Integer.parseInt(sAge),status);
        }
        if(v.getId()==R.id.need_new_account) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void registerAccount(final String username, String sEmail, String sPassword, final int age , final String status) {

        mAuth = FirebaseAuth.getInstance();
        showProgressDialog("please wait ....");

        mAuth.createUserWithEmailAndPassword(sEmail, sPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addUserToDataBase(username,age , status);

                        }
                        else {

                            hideProgressDialog();

                            showMessage(task.getException() + "", "ok");
                        }
                    }
                });
    }

    private void addUserToDataBase(String username , int age , String status) {
        firebaseUser=mAuth.getCurrentUser();
        final User user =new User();
        user.setEmail(firebaseUser.getEmail());
        user.setId(firebaseUser.getUid());
        user.setName(username);
        user.setAge(age);
        user.setStatus("Welcome");
        UsersDao.addUser(user, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressDialog();
                if(task.isSuccessful()){
                    DataUtil.currentUser=user;
                    showMessage("user register succesful ", "ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            finish();
                        }
                    },false);

                }
                else
                    showMessage("user register succesful "+task.getException().getLocalizedMessage(),"OK");
            }
        });
    }

    private boolean isValidForm(String sNameUser, String sEmail, String sPassword, String sConfirmPassword, String sAge, String sGender) {
        boolean isValid = true;


        if (sNameUser.trim().isEmpty()) {
            mNameUser.setError("Required");
            isValid = false;


        } else {
            mNameUser.setError(null);
            isValid = true;

        }

        if (sEmail.trim().isEmpty()) {
            mEmail.setError("Required");
            isValid = false;


        } else {
            mEmail.setError(null);
            isValid = true;

        }

        if (sPassword.trim().isEmpty()) {
            mPassword.setError("Required");
            isValid = false;

        } else {
            mPassword.setError(null);
            isValid = true;


        }

        if (sConfirmPassword.trim().isEmpty()) {
            mConfirmPassword.setError("Required");
            isValid = false;

        } else {
            mConfirmPassword.setError(null);
            isValid = true;

        }


        if (sAge.trim().isEmpty()) {
            mAge.setError("Required");
            isValid = false;


        } else if (Integer.parseInt(sAge) < 2 || Integer.parseInt(sAge) > 100) {
            mAge.setError("enter your real age ");
            isValid = false;


        } else {
            mAge.setError(null);
            isValid = true;
        }
        return isValid;
    }

}

