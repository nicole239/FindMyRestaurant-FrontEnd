package tec.findmyrestaurant.view;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import tec.findmyrestaurant.R;
import tec.findmyrestaurant.adapter.CommentAdapter;
import tec.findmyrestaurant.api.CalificationRequest;
import tec.findmyrestaurant.api.CommentRequest;
import tec.findmyrestaurant.api.Message;
import tec.findmyrestaurant.api.Response;
import tec.findmyrestaurant.api.RestaurantRequest;
import tec.findmyrestaurant.api.SessionManager;
import tec.findmyrestaurant.model.Calification;
import tec.findmyrestaurant.model.Comment;
import tec.findmyrestaurant.model.FoodType;
import tec.findmyrestaurant.model.Restaurant;
import tec.findmyrestaurant.model.User;

public class DetalleRestauranteActivity extends AppCompatActivity {

    Restaurant restaurant;
    TextView nombreRestauranteTV, horarioRestauranteTV, tipoComidaTV, precioRestauranteTV, datosContactoRestauranteTV, ubicacionRestauranteTV, calicacionValorTV;
    ViewPager caruselFotosVP;
    FloatingActionButton caruselAgregarBTN;
    RatingBar calificacionRestauranteRB;
    EditText campoComentarioED;
    Button comentarioEnviarBTN, calificarBtn;
    ListView listaComentarioRLV;
    Dialog calificacionDialog;
    float numStars =0.0f;



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
        //caruselFotosVP = (ViewPager) findViewById(R.id.detalle_restaurante_Carusel);
        //caruselAgregarBTN = (FloatingActionButton) findViewById(R.id.detalle_restaurante_carusel_BTN);
        calificacionRestauranteRB = (RatingBar) findViewById(R.id.detalle_restaurante_rating_bar);
        campoComentarioED = (EditText) findViewById(R.id.detalle_restaurante_comentario_ET);
        comentarioEnviarBTN = (Button) findViewById(R.id.detalle_restaurante_comentario_BTN);
        listaComentarioRLV = (ListView) findViewById(R.id.detalle_restaurante_lista_comentarios_RV);
        calificacionDialog = new Dialog(this);

        calificarBtn = (Button) findViewById(R.id.detalle_restaurante_calificar_BTN);

        calificarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TextView tv_close, tv_num;
                RatingBar rb_cal;
                Button calificar;
                calificacionDialog.setContentView(R.layout.calificacion_popup);
                tv_close = (TextView)calificacionDialog.findViewById(R.id.calificacion_popup_close_TV);
                tv_num = (TextView)calificacionDialog.findViewById(R.id.calificacion_popup_number_TV);
                rb_cal = (RatingBar)calificacionDialog.findViewById(R.id.calificacion_popup_ratingbar);
                calificar = (Button) calificacionDialog.findViewById(R.id.calificacion_popup_BTN);

                tv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calificacionDialog.dismiss();
                    }
                });

                rb_cal.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        Toast.makeText(DetalleRestauranteActivity.this, "num: "+v, Toast.LENGTH_SHORT).show();
                        numStars = v;
                        tv_num.setText(""+numStars);
                    }
                });

                calificacionDialog.show();
            }
        });


        listaComentarioRLV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //Recupera el  restaurante seleccionado
        try {
            restaurant = (Restaurant) ObjectSerializer.deserialize(getIntent().getStringExtra("restaurant"));

            nombreRestauranteTV.setText(restaurant.getName());
            horarioRestauranteTV.setText(restaurant.getSchedule());

            CalificationRequest.getCalifications(this, restaurant.getIdRestaurant(), new Response<Calification>(){
                @Override
                public void onSuccess(List<Calification> list) {
                    if(list.size()>0){
                        boolean haveUser = false;
                        for(Calification calification : list){
                            if(calification.getUser().getEmail().equals(SessionManager.getUserEmail(getApplicationContext()))){
                                haveUser = true;
                            }
                        }
                        if(haveUser){
                            calificarBtn.setEnabled(false);
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
                    }
                    else{
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
                        Log.d("Rating", "nada");
                    }
                }

                @Override
                public void onFailure(Message message) {
                    super.onFailure(message);
                }
            });

            tipoComidaTV.setText(restaurant.getFoodType().getName());
            switch (restaurant.getPrice()){
                case 'H':
                    precioRestauranteTV.setText("$$$");
                    break;
                case 'M':
                    precioRestauranteTV.setText("$$");
                    break;
                case 'L':
                    precioRestauranteTV.setText("$");
                    break;
                default:
                    precioRestauranteTV.setText("Error");
                    break;
            }
            String contenidoDatos = "Telefono: "+restaurant.getPhoneNumber()+" \nWebsite: "+restaurant.getWebsite();
            datosContactoRestauranteTV.setText(contenidoDatos);

            CommentRequest.getComments(this, restaurant.getIdRestaurant(), new Response<Comment>(){
                @Override
                public void onSuccess(List<Comment> list) {
                    if(list.size()>0){
                        CommentAdapter commentAdapter = new CommentAdapter(getApplicationContext(), list);
                        Log.d("Comment", list.toString());
                        Log.d("Comment", "Suer: "+list.get(0).getUser().getEmail()+" comment: "+list.get(0).getComment());
                        listaComentarioRLV.setAdapter(commentAdapter);
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



        comentarioEnviarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(campoComentarioED.getText().toString())){
                    User usuario = SessionManager.getUser(getApplicationContext());
                    Comment comentario = new Comment();
                    comentario.setUser(usuario);
                    comentario.setIdRestaurant(restaurant.getIdRestaurant());
                    comentario.setComment(campoComentarioED.getText().toString());
                    CommentRequest.registerComment(getApplicationContext(),comentario, new Response<Restaurant>(){
                        @Override
                        public void onSuccess(Message message) {
                            Log.d("Comment", "Success: "+message.getMessage());
                            CommentRequest.getComments(getApplicationContext(), restaurant.getIdRestaurant(), new Response<Comment>(){
                                @Override
                                public void onSuccess(List<Comment> list) {
                                    if(list.size()>0){
                                        CommentAdapter commentAdapter = new CommentAdapter(getApplicationContext(), list);
                                        Log.d("Comment", list.toString());
                                        Log.d("Comment", "Suer: "+list.get(0).getUser().getEmail()+" comment: "+list.get(0).getComment());
                                        listaComentarioRLV.setAdapter(commentAdapter);
                                    }
                                    try {
                                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                        campoComentarioED.getText().clear();
                                    } catch (Exception e) {
                                        Log.d("Comment", "Error en: "+e.getMessage());
                                    }
                                }

                                @Override
                                public void onFailure(Message message) {
                                    super.onFailure(message);
                                }
                            });
                        }

                        @Override
                        public void onFailure(Message message) {
                            Log.d("Comment","Error: "+message.getMessage());
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(),"Debe escribir el comentario",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
