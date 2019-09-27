package tec.findmyrestaurant.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import tec.findmyrestaurant.R;
import tec.findmyrestaurant.api.Message;
import tec.findmyrestaurant.api.Response;
import tec.findmyrestaurant.api.RestaurantRequest;
import tec.findmyrestaurant.model.FoodType;

public class SearchActivity extends AppCompatActivity implements OnMapReadyCallback {

    Spinner spinnerPrice;
    EditText txtName;
    EditText txtRadio;
    Spinner spinnerType;
    RatingBar ratingBarcalification;
    private GoogleMap mMap;
    private MapView mapView;
    Marker locationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getResources().getString(R.string.google_maps_key));
        }

        mapView = findViewById(R.id.mapViewSearch);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        txtName = findViewById(R.id.txtSearchName);
        txtRadio = findViewById(R.id.txtRadio);
        ratingBarcalification = findViewById(R.id.searchCalification);
        ratingBarcalification.setRating(3f);
        spinnerPrice = findViewById(R.id.spinnerPrice);
        spinnerType = findViewById(R.id.spinnerSearchFoodType);

        setPriceSpinner();
        setFoodTypeSpinner();
    }


    public void btnSearch_onClick(View view){
        //Get values from view
        String name = txtName.getText().toString();
        char price;
        switch(spinnerPrice.getSelectedItemPosition()){
            case 0:
                price = 'L';
                break;
            case 1:
                price = 'M';
                break;
            default:
                price = 'H';
                break;
        }
        FoodType type = (FoodType) spinnerType.getSelectedItem();
        float minCalification = ratingBarcalification.getRating();

        Log.i("SEARCHING",name+" "+price+ " "+type.getName()+" "+minCalification);

        if(locationMarker != null) {
            double latitude = locationMarker.getPosition().latitude;
            double longitude = locationMarker.getPosition().longitude;
            int radio = Integer.parseInt(txtRadio.getText().toString());
            Log.i("SEARCHING",latitude+" ,"+longitude+" r:"+radio);
        }


        //TODO: CALL SERVER SEARCH
    }

    private void setPriceSpinner(){
        List<String> prices = new ArrayList<>();
        prices.add("Low");
        prices.add("Medium");
        prices.add("High");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                prices
        );
        spinnerPrice.setAdapter(adapter);
    }

    private void setFoodTypeSpinner(){
        RestaurantRequest.getFoodTypes(this,new Response<FoodType>(){
            @Override
            public void onSuccess(List<FoodType> list) {
                ArrayAdapter<FoodType> adapterFood = new ArrayAdapter<FoodType>(SearchActivity.this,R.layout.spinner_item,list);
                spinnerType.setAdapter(adapterFood);
            }
            @Override
            public void onFailure(Message message) {
                Toast.makeText(SearchActivity.this,"Couldn't get food type",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                if(locationMarker == null)
                    txtRadio.setText("10");
                locationMarker = mMap.addMarker(new MarkerOptions().position(point).draggable(true));

            }
        });

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(getResources().getString(R.string.google_maps_key));
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(getResources().getString(R.string.google_maps_key), mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
        new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                mMap.setMinZoomPreference(15);
                return false;
            }
        };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
        new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15));
            }
        };
}
