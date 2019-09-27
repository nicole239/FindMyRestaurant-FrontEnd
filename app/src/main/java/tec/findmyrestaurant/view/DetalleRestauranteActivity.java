package tec.findmyrestaurant.view;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import tec.findmyrestaurant.R;
import tec.findmyrestaurant.api.CalificationRequest;
import tec.findmyrestaurant.api.Message;
import tec.findmyrestaurant.api.Response;
import tec.findmyrestaurant.model.Calification;
import tec.findmyrestaurant.model.Restaurant;

public class DetalleRestauranteActivity extends AppCompatActivity {

    Restaurant restaurant;
    TextView nombreRestauranteTV, horarioRestauranteTV, tipoComidaTV, precioRestauranteTV, datosContactoRestauranteTV, ubicacionRestauranteTV, calicacionValorTV;
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
        calicacionValorTV = (TextView) findViewById(R.id.detalle_restaurante_ratin_bar_value_TV);
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

            CalificationRequest.getCalifications(this, restaurant.getIdRestaurant(), new Response<Calification>(){
                @Override
                public void onSuccess(List<Calification> list) {
                    if(list.size()>0){
                        CalificationRequest.getRestaurantCalification(getApplicationContext(), restaurant.getIdRestaurant(), new Response<Calification>(){
                            @Override
                            public void onSuccess(Calification objet) {
                                Log.d("Rating",""+objet.getCalification());
                                calificacionRestauranteRB.setRating(objet.getCalification());
                                calicacionValorTV.setText(""+ BigDecimal.valueOf(objet.getCalification()).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
                            }

                            @Override
                            public void onFailure(Message message) {
                                super.onFailure(message);
                            }
                        });
                    }
                    else{
                        Log.d("Rating", "nada");
                    }
                }

                @Override
                public void onFailure(Message message) {
                    super.onFailure(message);
                }
            });



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
