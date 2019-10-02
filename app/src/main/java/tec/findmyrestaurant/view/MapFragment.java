package tec.findmyrestaurant.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tec.findmyrestaurant.R;
import tec.findmyrestaurant.api.Response;
import tec.findmyrestaurant.api.RestaurantRequest;
import tec.findmyrestaurant.model.FoodType;
import tec.findmyrestaurant.model.Restaurant;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private IconGenerator iconFactory;

    private static final String TAG = "TabMapFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_map,container,false);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(getResources().getString(R.string.google_maps_key));
        }

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        iconFactory = new IconGenerator(getActivity());

        Button btnRefresh = view.findViewById(R.id.btnRefreshMap);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRestaurants();
            }
        });

        return view;
    }

    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            mMap.setMyLocationEnabled(true);
            setupLocationManager();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 10, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Si nos dieron permiso
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, locationListener);
            }

        }
        //No nos dieron permiso
        else {
            Toast.makeText(getActivity(), "Location permission not granted, ",Toast.LENGTH_SHORT).show();
        }
    }


    private void setupLocationManager() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 16));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) { }
            @Override
            public void onProviderEnabled(String s) {}
            @Override
            public void onProviderDisabled(String s) {}
        };
    }

    private void addRestaurantMarkers(List<Restaurant> restaurantList){
        for(Restaurant restaurant :restaurantList){
            Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(restaurant.getLatitude(),restaurant.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(restaurant.getName()))));
            marker.showInfoWindow();
            marker.setTag(restaurant);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(getActivity(),DetalleRestauranteActivity.class);
                try {
                    intent.putExtra("restaurant",ObjectSerializer.serialize((Serializable) marker.getTag()));
                    startActivity(intent);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        mMap.setMyLocationEnabled(true);
        getPermissions();
        if (getActivity().getIntent().hasExtra(TabbedActivity.KEY_EXTRA)) {
            try {
                List<Restaurant> restaurants = (List<Restaurant>)ObjectSerializer.deserialize(getActivity().getIntent().getStringExtra(TabbedActivity.KEY_EXTRA));
                addRestaurantMarkers(restaurants);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            loadRestaurants();
        }



    }

    private void loadRestaurants(){
        mMap.clear();
        RestaurantRequest.getRestaurants(getActivity(),new Response<Restaurant>(){
            @Override
            public void onSuccess(List<Restaurant> list) {
                addRestaurantMarkers(list);
            }
        });

    }


    @Override
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
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 16));
                }
            };




}
