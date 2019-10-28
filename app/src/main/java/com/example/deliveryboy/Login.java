package com.example.deliveryboy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private EditText vendorId;
    public Button submitId;
    private String id;
    final Context context = this;
    private FirebaseAuth mAuth;
    private String verificationid;
    private ApiInterface mAPIService;
    public String deliveryboyno;
    private EditText otpView;
    public boolean loginornot;
    public static final String SHARED_PREFS = "deliveryboysharedpre";
    public static final String SWITCH1 = "loginornot2";
    public static final String TEXT = "mobileno2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadData();
        mAuth = FirebaseAuth.getInstance();
        mAPIService = ApiUtils.getAPIService();
        if (loginornot)
        {
            Toast.makeText(Login.this, "Signing In", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(Login.this,MainActivity.class);
            ((UserClient)(getApplicationContext())).setPhoneno(deliveryboyno);
            //intent.putExtra("deliveryboyno",deliveryboyno);
            startActivity(intent);
            finish();
        }
        vendorId = (EditText) findViewById(R.id.vendorID);
        submitId = (Button) findViewById(R.id.submitID);
        String helll=" jfk";
        submitId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = vendorId.getText().toString().trim();

                Call<LoginResponse> calldash = mAPIService.getLogin(id);
                calldash.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, final Response<LoginResponse> response) {
                        if(response.isSuccessful()) {
                            if(response.body().getFound().equals("false")){
                                Toast.makeText(Login.this, "Wrong Credentials ", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(Login.this,"success",Toast.LENGTH_SHORT).show();
                                deliveryboyno=response.body().getDel_boy_phone();
                                sendVerificationCode("+91"+response.body().getDel_boy_phone());
                                final Dialog dialog = new Dialog(context);
                                dialog.setContentView(R.layout.customdialogotp);
                                dialog.show();

                                otpView = (EditText) dialog.findViewById(R.id.otp_view);
                                final String code = otpView.getText().toString();
                                Button btnSave = (Button) dialog.findViewById(R.id.otpget);

                                btnSave.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(Login.this, code, Toast.LENGTH_SHORT).show();
                                        verifyCode(code);
                                        dialog.dismiss();


                                    }
                                });

                            }
                        }
                        else{
                            Toast.makeText(Login.this, "Response from server is not successful", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(Login.this,t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Login.this, "Signing In", Toast.LENGTH_LONG).show();
                            loginornot=true;
                            saveData();
                            Intent intent=new Intent(Login.this,MainActivity.class);
                            ((UserClient)(getApplicationContext())).setPhoneno(deliveryboyno);
                            //intent.putExtra("deliveryboyno",deliveryboyno);
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
            Toast.makeText(Login.this, "onCodeSent",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            Toast.makeText(Login.this, "onVerificationCompleted" +  code,Toast.LENGTH_LONG).show();
            if (code != null){
                otpView.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Login.this, "onVerificationFailed " +  e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, deliveryboyno);
        editor.putBoolean(SWITCH1, loginornot);
        editor.apply();

        //Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        deliveryboyno = sharedPreferences.getString(TEXT, "");
        loginornot = sharedPreferences.getBoolean(SWITCH1, false);
    }
}
