package tec.findmyrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

import tec.findmyrestaurant.model.Restaurant;

public class RestaurantDetatilActivity extends AppCompatActivity {

    Restaurant restaurant;
    TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detatil);

        try {
            restaurant = (Restaurant) ObjectSerializer.deserialize(getIntent().getStringExtra("restaurant"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtName = findViewById(R.id.txtDetailName);
        txtName.setText(restaurant.getName());
    }
}
