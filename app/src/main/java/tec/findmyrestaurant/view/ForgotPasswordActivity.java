package tec.findmyrestaurant.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import tec.findmyrestaurant.R;
import tec.findmyrestaurant.api.Message;
import tec.findmyrestaurant.api.Response;
import tec.findmyrestaurant.api.UserRequest;

public class ForgotPasswordActivity extends AppCompatActivity {

    LinearLayout lytCode, lytConfirm;
    TextInputLayout txtEmail,txtCode,txtPassword,txtConfirm;
    ProgressBar progressBar;
    Button btnCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lytCode = findViewById(R.id.lytCode);
        lytConfirm = findViewById(R.id.lytConfirm);
        txtCode = findViewById(R.id.txtCode);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirm = findViewById(R.id.txtPasswordConfirm);
        progressBar = findViewById(R.id.progressSendCode);
        btnCode = findViewById(R.id.btnCode);
    }
    public void submitCode(View view){
        progressBar.setVisibility(View.VISIBLE);
        btnCode.setEnabled(false);
        String email = txtEmail.getEditText().getText().toString();
        if(!email.isEmpty()){
            UserRequest.forgotPassword(this,email,new Response(){
                @Override
                public void onSuccess(Message message) {
                    progressBar.setVisibility(View.GONE);
                    btnCode.setEnabled(true);
                    lytCode.setVisibility(View.GONE);
                    lytConfirm.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Message message) {
                    btnCode.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                    if(message.isAuth()){
                        Toast.makeText(ForgotPasswordActivity.this,"The code could not be sent",Toast.LENGTH_SHORT).show();
                    }else{
                        txtEmail.setError("The emal is not registered");
                    }
                }
            });
        }
    }
    public void submitNewPassword(View view){
        String password = txtPassword.getEditText().getText().toString();
        String confirm = txtConfirm.getEditText().getText().toString();
        String code = txtCode.getEditText().getText().toString();
        String email = txtEmail.getEditText().getText().toString();
        if(code.length()!=4){
            txtCode.setError("The length must be 4");
            return;
        }
        if(!password.equals(confirm)){
            txtConfirm.setError("Passwords don't match");
            return;
        }
        UserRequest.newPassword(this,email,code,password,new Response(){
            @Override
            public void onSuccess(Message message) {
                Toast.makeText(ForgotPasswordActivity.this,"Password changed",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Message message) {
                Toast.makeText(ForgotPasswordActivity.this,"Password could not be changed. Try again",Toast.LENGTH_LONG).show();
            }
        });
    }
}
