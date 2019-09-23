package tec.findmyrestaurant.view;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import tec.findmyrestaurant.R;
import tec.findmyrestaurant.model.Restaurant;

public class DetalleRestauranteActivity extends AppCompatActivity {

    Restaurant restaurant;
    TextView nombreRestauranteTV, horarioRestauranteTV, tipoComidaTV, precioRestauranteTV, datosContactoRestauranteTV, ubicacionRestauranteTV;
    ViewPager caruselFotosVP;
    FloatingActionButton caruselAgregarBTN;
    RatingBar calificacionRestauranteRB;
    EditText campoComentarioED;
    Button comentarioEnviarBTN;
    RecyclerView listaComentarioRLV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_restaurante);
        nombreRestauranteTV = (TextView) findViewById(R.id.detalle_restaurante_nombre_text_view);
        horarioRestauranteTV = (TextView) findViewById(R.id.detalle_restaurante_horario_contenido_TV);
        tipoComidaTV = (TextView) findViewById(R.id.detalle_restaurante_tipo_comida_contenido_TV);
        precioRestauranteTV = (TextView) findViewById(R.id.detalle_restaurante_precio_contenido_TV);
        datosContactoRestauranteTV = (TextView) findViewById(R.id.detalle_restaurante_datos_contacto_contenido_TV);
        ubicacionRestauranteTV = (TextView) findViewById(R.id.detalle_restaurante_ubicacion_contenido_TV);
        caruselFotosVP = (ViewPager) findViewById(R.id.detalle_restaurante_Carusel);
        caruselAgregarBTN = (FloatingActionButton) findViewById(R.id.detalle_restaurante_carusel_BTN);
        calificacionRestauranteRB = (RatingBar) findViewById(R.id.detalle_restaurante_rating_bar);
        campoComentarioED = (EditText) findViewById(R.id.detalle_restaurante_comentario_ET);
        comentarioEnviarBTN = (Button) findViewById(R.id.detalle_restaurante_comentario_BTN);
        listaComentarioRLV = (RecyclerView) findViewById(R.id.detalle_restaurante_lista_comentarios_RV);

        //Recupera el  restaurante seleccionado
        try {
            restaurant = (Restaurant) ObjectSerializer.deserialize(getIntent().getStringExtra("restaurant"));

            nombreRestauranteTV.setText(restaurant.getName());
            horarioRestauranteTV.setText(restaurant.getSchedule());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
