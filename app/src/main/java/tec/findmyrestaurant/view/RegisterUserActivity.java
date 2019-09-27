package tec.findmyrestaurant.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import tec.findmyrestaurant.R;
import tec.findmyrestaurant.api.Message;
import tec.findmyrestaurant.api.Response;
import tec.findmyrestaurant.api.UserRequest;
import tec.findmyrestaurant.model.User;

public class RegisterUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
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
    }
    public void submit(View view){
        boolean err = false;
        final TextInputLayout name = findViewById(R.id.txtName),
                        password = findViewById(R.id.txtPassword),
                        confirm = findViewById(R.id.txtPasswordConfirm),
                        email = findViewById(R.id.txtEmail);
        if(name.getEditText().getText().toString().isEmpty()) {
            //name.setError("Required field");
            err = true;
        }
        if(email.getEditText().getText().toString().isEmpty()){
            //email.setError("Required field");
            err = true;
        }
        if(password.getEditText().getText().toString().isEmpty()){
            //password.setError("Required field");
            err = true;
        }else {
            if(!password.getEditText().getText().toString().equals(confirm.getEditText().getText().toString())){
                confirm.setError("Passwords don't match");
            }
        }
        if(!err){
            UserRequest.getUser(this,email.getEditText().getText().toString(),new Response<User>(){
                @Override
                public void onSuccess(User objet) {
                    Toast.makeText(RegisterUserActivity.this,"The email is already registered",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Message message) {
                    User user = new User();
                    user.setEmail(email.getEditText().getText().toString());
                    user.setName(name.getEditText().getText().toString());
                    user.setPassword(password.getEditText().getText().toString());
                    user.setType(User.REGULAR);
                    UserRequest.registerUser(RegisterUserActivity.this,user,new Response<User>(){
                        @Override
                        public void onSuccess(Message message) {
                            Toast.makeText(RegisterUserActivity.this,"User registered",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(Message message) {
                            Toast.makeText(RegisterUserActivity.this,"Couldn't register user",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }
}
