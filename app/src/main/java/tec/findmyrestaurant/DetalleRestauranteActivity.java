package tec.findmyrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.IOException;

import tec.findmyrestaurant.model.Restaurant;

public class DetalleRestauranteActivity extends AppCompatActivity {

    Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_restaurante);

        //Recupera el  restaurante seleccionado
        try {
            restaurant = (Restaurant) ObjectSerializer.deserialize(getIntent().getStringExtra("restaurant"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
