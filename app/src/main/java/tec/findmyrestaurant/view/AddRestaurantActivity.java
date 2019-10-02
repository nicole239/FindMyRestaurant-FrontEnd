package tec.findmyrestaurant.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputLayout;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import tec.findmyrestaurant.R;
import tec.findmyrestaurant.api.Message;
import tec.findmyrestaurant.api.PhotoRequest;
import tec.findmyrestaurant.api.Response;
import tec.findmyrestaurant.api.RestaurantRequest;
import tec.findmyrestaurant.api.SessionManager;
import tec.findmyrestaurant.api.amazon.AmazonRequest;
import tec.findmyrestaurant.model.FoodType;
import tec.findmyrestaurant.model.Restaurant;
import tec.findmyrestaurant.view.photos.PhotoAdapter;
import tec.findmyrestaurant.view.schedule.ScheduleActivity;

public class AddRestaurantActivity extends AppCompatActivity implements PhotoAdapter.PhotoEvent {

    TextInputLayout txtName, txtPhone,txtWebsite;
    Button btnSubmit;
    TextView lblAddSchedule, lblLocation;
    TextView lblSchedule;
    PhotoAdapter adapter;
    List<PhotoView> list;
    Spinner spinnerPrice,spinnerFood;
    int idx=0;
    double lat=Double.MAX_VALUE, lng=Double.MAX_VALUE;
    ViewPager  viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_restaurant);

        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtWebsite = findViewById(R.id.txtWebsite);

        List<Character> prices = Arrays.asList(new Character[]{'H','M','L'});
        ArrayAdapter<Character> adapterPrice = new ArrayAdapter<>(this,R.layout.spinner_item,prices);
        spinnerPrice = findViewById(R.id.spinnerPrice);
        spinnerPrice.setAdapter(adapterPrice);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setEnabled(true);

        lblAddSchedule = findViewById(R.id.lblAddSchedule);
        lblSchedule = findViewById(R.id.lblSchedule);

        lblLocation = findViewById(R.id.lblLocation);

        list = Arrays.asList(new PhotoView[]{new PhotoView(),new PhotoView(),new PhotoView()});
        adapter = new PhotoAdapter(this,list,this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130,0,130,0);

        loadFoofTypes();
    }
    public void addSchedule(View view){
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivityForResult(intent,1);
    }
    public void setMap(View view){
        Intent intent = new Intent(this, SelectPlaceActivity.class);
        intent.putExtra("lat",lat);
        intent.putExtra("lng",lng);
        startActivityForResult(intent,5);
    }
    private void loadFoofTypes(){
        RestaurantRequest.getFoodTypes(this,new Response<FoodType>(){
            @Override
            public void onSuccess(List<FoodType> list) {
                ArrayAdapter<FoodType> adapterFood = new ArrayAdapter<FoodType>(AddRestaurantActivity.this,R.layout.spinner_item,list);
                spinnerFood = findViewById(R.id.spinnerFood);
                spinnerFood.setAdapter(adapterFood);
                btnSubmit.setEnabled(false);
            }
            @Override
            public void onFailure(Message message) {
                Toast.makeText(AddRestaurantActivity.this,"Couldn't get food type",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1){
            if(resultCode== Activity.RESULT_OK){
                lblAddSchedule.setText("Edit schedule");
                lblSchedule.setText(data.getStringExtra("result"));
                lblSchedule.setVisibility(View.VISIBLE);
            }
        }else if(requestCode==2 || requestCode ==3){
            if(resultCode == Activity.RESULT_OK){
                ImageView img = viewPager.findViewWithTag(idx+"img");
                RelativeLayout layout = viewPager.findViewWithTag(idx+"lyt");
                Button btn = viewPager.findViewWithTag(idx+"del");
                ProgressBar progressBar = viewPager.findViewWithTag(idx+"pro");
                img.setImageURI(data.getData());
                layout.setVisibility(View.GONE);
                btn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                list.get(idx).uri=data.getData();
                upload(data.getData(),idx);
            }
        }else if(requestCode==5){
            if(resultCode == Activity.RESULT_OK){
                lat = data.getDoubleExtra("lat",Double.MAX_VALUE);
                lng = data.getDoubleExtra("lng",Double.MAX_VALUE);
                lblLocation.setText("Location set. Want to edit it?");
            }
        }
    }

    @Override
    public void onImageAdd(int position) {
        idx = position;
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
    @Override
    public void onImageDelete(int position) {
        resetImage(position);
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
    private void resetImage(int idx){
        list.get(idx).url="";
        ImageView img = viewPager.findViewWithTag(idx+"img");
        RelativeLayout layout = viewPager.findViewWithTag(idx+"lyt");
        Button btn = viewPager.findViewWithTag(idx+"del");
        layout.setVisibility(View.VISIBLE);
        btn.setVisibility(View.GONE);
    }

    public void submit(View view){
        if(lat == Double.MAX_VALUE || lng == Double.MAX_VALUE){
            Toast.makeText(this,"Restaurant location is not set",Toast.LENGTH_SHORT).show();
            return;
        }
        String name = txtName.getEditText().getText().toString();
        String phone = txtPhone.getEditText().getText().toString();
        String website = txtWebsite.getEditText().getText().toString();

        if(name.isEmpty() || phone.isEmpty() || website.isEmpty()){
            return;
        }


        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setWebsite(website);
        restaurant.setPhoneNumber(Integer.parseInt(phone));
        restaurant.setFoodType((FoodType)spinnerFood.getSelectedItem());
        restaurant.setPrice((char)spinnerPrice.getSelectedItem());
        restaurant.setSchedule(lblSchedule.getText().toString());
        restaurant.setLatitude(lat);
        restaurant.setLongitude(lng);
        restaurant.setUsrCreator(SessionManager.getUser(this));
        for(PhotoView photoView : list){
            if(!photoView.url.isEmpty())
                restaurant.addPhoto(photoView.url);
        }
        RestaurantRequest.registerRestaurant(this,restaurant,new Response<Restaurant>(){
            @Override
            public void onSuccess(Message message) {
                Toast.makeText(AddRestaurantActivity.this,"Restaurant inserted",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Message message) {
                Toast.makeText(AddRestaurantActivity.this,"Restaurant not inserted",Toast.LENGTH_LONG).show();
            }
        });
    }

    public class PhotoView{
        public String url="";
        public  Uri uri;
        public PhotoView(){}
    }
    private void upload(final Uri uri, final int idx){
        btnSubmit.setEnabled(false);
        PhotoRequest.getPhotoID(this,new Response(){
            @Override
            public void onSuccess(Message message) {
                AmazonRequest amazonRequest = new AmazonRequest(AddRestaurantActivity.this,message.getGuid(),uri,new Response(){
                    @Override
                    public void onSuccess(Message message) {
                        ProgressBar progressBar = viewPager.findViewWithTag(idx+"pro");
                        progressBar.setVisibility(View.GONE);
                        btnSubmit.setEnabled(true);
                        list.get(idx).url=message.getMessage();
                    }

                    @Override
                    public void onFailure(Message message) {
                        ProgressBar progressBar = viewPager.findViewWithTag(idx+"pro");
                        progressBar.setVisibility(View.GONE);
                        resetImage(idx);
                        Toast.makeText(AddRestaurantActivity.this,"Image upload failed",Toast.LENGTH_LONG).show();
                        btnSubmit.setEnabled(true);

                    }
                });
                amazonRequest.beginUpload();
            }

            @Override
            public void onFailure(Message message) {
                ProgressBar progressBar = viewPager.findViewWithTag(idx+"pro");
                progressBar.setVisibility(View.GONE);
                resetImage(idx);
                Toast.makeText(AddRestaurantActivity.this,"Image upload failed",Toast.LENGTH_LONG).show();
                btnSubmit.setEnabled(true);
            }
        });

    }
}
