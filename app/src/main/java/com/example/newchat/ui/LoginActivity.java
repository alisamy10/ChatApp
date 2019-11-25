package com.example.newchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import com.example.newchat.Base.BaseActivity;
import com.example.newchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImage;
    private EditText mEmail , mPassword , mNumberPhone , mNumberCode;
    private Button mLogin , mCodeGet , mSignin;
    private boolean emailValue = false;
    private final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
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
        mImage = findViewById(R.id.image);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mLogin = findViewById(R.id.login);
        mLogin.setOnClickListener(this);
        mNumberPhone =  findViewById(R.id.phone_number);
        mCodeGet =  findViewById(R.id.get_code);
        mCodeGet.setOnClickListener(this);
        mNumberCode =  findViewById(R.id.code_number);
        mSignin =  findViewById(R.id.signin);
        mSignin.setOnClickListener(this);
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
            case R.id.get_code:// TODO 19/11/05
                String phoneNumber=mNumberPhone.getText().toString().trim();
                if(isValidPhone(phoneNumber)){
                    Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show();
                    sendVerificationCode(phoneNumber);
                }
                break;
            case R.id.signin:// TODO 19/11/05
                String codeNumber=mNumberCode.getText().toString().trim();
                if(isValidCode(codeNumber))
                    verifySigninWithPhone(codeNumber);
                break;
            default:
                break;
        }
    }

    private void verifySigninWithPhone(String codeNumber) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, codeNumber);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                             Toast.makeText(LoginActivity.this, "success", Toast.LENGTH_SHORT).show();
                         else
                             Toast.makeText(LoginActivity.this, "failed"+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isValidCode(String codeNumber) {
        boolean isValid=true;
        if(codeNumber .isEmpty()) {
            mNumberCode.setError("Required");
            mNumberCode.requestFocus();
            isValid=false;
        }
        else{
            mNumberCode.setError(null);
            isValid=true;
        }
        return isValid;
    }

    private void sendVerificationCode(String phoneNumber) {
      //  mAuth=FirebaseAuth.getInstance();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                callbacks);        // OnVerificationStateChangedCallbacks

    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks  callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.i("ali",e+"");
            Toast.makeText(LoginActivity.this, "invalid Phone Number "+e, Toast.LENGTH_LONG).show();
            mCodeGet.setText(e.getLocalizedMessage());
            mNumberCode.setText(e.getLocalizedMessage());
        }
        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            mVerificationId = verificationId;
            mResendToken = token;
            Toast.makeText(LoginActivity.this, "code has been sent "+verificationId, Toast.LENGTH_SHORT).show();
            mNumberCode.setText(verificationId);
        }
    };

    private boolean isValidPhone(String phoneNumber) {
     boolean isValid=true;
     if(phoneNumber.isEmpty()){
         mNumberPhone.setError("Required");
         mNumberPhone.requestFocus();
         isValid=false;
     }
     else if(phoneNumber.length()<9) {
         mNumberPhone.setError("password should be equal 11 numbers ");
         mNumberPhone.requestFocus();
         isValid=false;
     }
     else{
         mNumberPhone.setError(null);
         isValid=true;
     }
     return isValid;
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
                        } else
                            showMessage("user Login failed " + task.getException().getLocalizedMessage(), "ok");
                    }
                });
    }

    private boolean isValidForm(String email, String password) {
        boolean isValid = true;
        if (email.isEmpty()) {
            mEmail.setError("Required");
            mEmail.requestFocus();
            isValid = false;
        } else {
            mEmail.setError(null);
            isValid = true;
        }
        if (password.isEmpty()) {
            mPassword.setError("Required");
            mPassword.requestFocus();
            isValid = false;
        } else {
            mPassword.setError(null);
            isValid = true;
        }
        return isValid;
    }
}
