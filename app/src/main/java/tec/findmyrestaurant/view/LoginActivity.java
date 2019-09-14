package tec.findmyrestaurant.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tec.findmyrestaurant.R;
import tec.findmyrestaurant.api.Message;
import tec.findmyrestaurant.api.Response;
import tec.findmyrestaurant.api.SessionManager;
import tec.findmyrestaurant.api.UserRequest;
import tec.findmyrestaurant.model.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void btnLogin_Click(View view){

        EditText txtEmail = findViewById(R.id.txtEmail);
        EditText txtPassword = findViewById(R.id.txtPassword);

        final User user = new User();
        user.setEmail(txtEmail.getText().toString());
        user.setPassword(txtPassword.getText().toString());

        UserRequest.authUser(getApplicationContext(),user, new Response<User>(){
            @Override
            public void onSuccess(Message message) {
                if(message.isAuth()) {
                    SessionManager.saveToken(LoginActivity.this, user,message.getToken());
                    Intent intent = new Intent(LoginActivity.this, TabbedActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Message message) {
                Toast.makeText(getApplicationContext(),"Email or password incorrect",Toast.LENGTH_LONG).show();
            }
        });




    }
}
