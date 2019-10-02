package tec.findmyrestaurant.view;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import tec.findmyrestaurant.R;
import tec.findmyrestaurant.adapter.CommentAdapter;
import tec.findmyrestaurant.adapter.CustomImageAdapter;
import tec.findmyrestaurant.api.CalificationRequest;
import tec.findmyrestaurant.api.CommentRequest;
import tec.findmyrestaurant.api.Message;
import tec.findmyrestaurant.api.PhotoRequest;
import tec.findmyrestaurant.api.Response;
import tec.findmyrestaurant.api.RestaurantRequest;
import tec.findmyrestaurant.api.SessionManager;
import tec.findmyrestaurant.api.amazon.AmazonRequest;
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
    CustomImageAdapter imageAdapter;
    ProgressBar progressBar;
    List<Bitmap> bitmaps;
    List<String> urls;
    private MapView mapView;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalle_restaurante);
        progressBar = findViewById(R.id.progress_bar);
        calicacionValorTV = (TextView) findViewById(R.id.detalle_restaurante_ratin_bar_value_TV);
        nombreRestauranteTV = (TextView) findViewById(R.id.detalle_restaurante_nombre_text_view);
        horarioRestauranteTV = (TextView) findViewById(R.id.detalle_restaurante_horario_contenido_TV);
        tipoComidaTV = (TextView) findViewById(R.id.detalle_restaurante_tipo_comida_contenido_TV);
        precioRestauranteTV = (TextView) findViewById(R.id.detalle_restaurante_precio_contenido_TV);
        datosContactoRestauranteTV = (TextView) findViewById(R.id.detalle_restaurante_datos_contacto_contenido_TV);
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
                final Button calificar;
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

                calificar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Calification calification = new Calification();
                        calification.setCalification(numStars);
                        calification.setIdRestaurant(restaurant.getIdRestaurant());
                        User currentUser = SessionManager.getUser(getApplicationContext());
                        calification.setUser(currentUser);
                        CalificationRequest.insertCalification(getApplicationContext(),calification,new Response<Calification>(){
                            @Override
                            public void onSuccess(Message message) {
                                Toast.makeText(getApplicationContext(),"Tu calificacion ha sido enviada!",Toast.LENGTH_LONG).show();
                                calificarBtn.setEnabled(false);
                                calificarBtn.setText("Ya has calificado este retaurante");
                                refreshCalifications();
                            }

                            @Override
                            public void onFailure(Message message) {
                                Toast.makeText(getApplicationContext(),"Hubo un error al enviar tu calificacion",Toast.LENGTH_LONG).show();
                            }
                        });
                        calificacionDialog.dismiss();
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
                            calificarBtn.setText("Ya has calificado este retaurante");
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
            showPictures();

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
    public void showPictures(){
        caruselFotosVP = findViewById(R.id.detalle_restaurante_Carusel);
        PhotoRequest.getPhotos(this,restaurant.getIdRestaurant(),new Response<String>(){
            @Override
            public void onSuccess(List<String> list) {
                caruselFotosVP.setOffscreenPageLimit(list.size()-1);
                List<ImageView> imgs = new ArrayList<>();
                bitmaps = new ArrayList<>();
                for(String s:list)
                    bitmaps.add(null);
                urls = list;
                imageAdapter = new CustomImageAdapter(DetalleRestauranteActivity.this,list,bitmaps);
                caruselFotosVP.setAdapter(imageAdapter);
                for (int i=0;i<list.size();i++){
                    imgs.add((ImageView) caruselFotosVP.findViewWithTag(i+"img-view"));
                }
                new RecoverPictures(imgs,list,bitmaps).execute();
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });
    }
    public void uploadImage(View view) {
        final  CharSequence[] options = {"Take a picture","Chose from gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chosee a restaurant image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(options[i]==options[0])
                    getPermissions();
                else if(options[i]==options[1])
                    getPermissionsGallery();
                else if(options[i]==options[2])
                    dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    private void takePicture(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture,2);
    }
    private void chooseFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto,3);
    }
    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        else
            takePicture();
    }
    private void getPermissionsGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        else
            chooseFromGallery();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        takePicture();
                    else
                        Toast.makeText(this, "Camera permission not granted, ",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        chooseFromGallery();
                }
                else
                    Toast.makeText(this, "Gallery permission not granted, ",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==2 || requestCode ==3){
            if(resultCode == Activity.RESULT_OK){
                progressBar.setVisibility(View.VISIBLE);
                upload(data.getData());
            }
        }
    }

    public void refreshCalifications(){
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

    private void upload(final Uri uri){
        PhotoRequest.getPhotoID(this,new Response(){
            @Override
            public void onSuccess(Message message) {
                AmazonRequest amazonRequest = new AmazonRequest(DetalleRestauranteActivity.this,message.getGuid(),uri,new Response(){
                    @Override
                    public void onSuccess(Message message) {

                        PhotoRequest.uploadPhoto(DetalleRestauranteActivity.this,restaurant.getIdRestaurant(),message.getMessage(),new Response(){
                            @Override
                            public void onSuccess(Message message) {
                                progressBar.setVisibility(View.INVISIBLE);
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(DetalleRestauranteActivity.this.getContentResolver(), uri);
                                    bitmaps.add(bitmap);
                                    urls.add("");
                                    imageAdapter.notifyDataSetChanged();

                                }catch (Exception e){

                                }
                            }

                            @Override
                            public void onFailure(Message message) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(DetalleRestauranteActivity.this,"Image upload failed",Toast.LENGTH_LONG).show();
                            }
                        });
                        //btnSubmit.setEnabled(true);
                        //list.get(idx).url=message.getMessage();
                    }

                    @Override
                    public void onFailure(Message message) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(DetalleRestauranteActivity.this,"Image upload failed",Toast.LENGTH_LONG).show();

                    }
                });
                amazonRequest.beginUpload();
            }

            @Override
            public void onFailure(Message message) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(DetalleRestauranteActivity.this,"Image upload failed",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void setMap(View view){
        Intent intent = new Intent(this, SelectPlaceActivity.class);
        intent.putExtra("lat",restaurant.getLatitude());
        intent.putExtra("lng",restaurant.getLongitude());
        intent.putExtra("fromDetalle",true);
        startActivityForResult(intent,5);
    }

}
