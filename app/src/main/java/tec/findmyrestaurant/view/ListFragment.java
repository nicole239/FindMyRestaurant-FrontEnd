package tec.findmyrestaurant.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tec.findmyrestaurant.R;
import tec.findmyrestaurant.api.Message;
import tec.findmyrestaurant.api.Response;
import tec.findmyrestaurant.api.RestaurantRequest;
import tec.findmyrestaurant.model.FoodType;
import tec.findmyrestaurant.model.Restaurant;


public class ListFragment extends Fragment {
    private static final String TAG = "TabListFragment";
    private ArrayList<Restaurant> arrayList;
    ArrayList<Restaurant> restArray;

    private ListView listViewRestaurants;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_list,container,false);

        listViewRestaurants = view.findViewById(R.id.listViewRestaurants);

        if (getActivity().getIntent().hasExtra(TabbedActivity.KEY_EXTRA)) {
            try {
                ArrayList<Restaurant> restaurants = (ArrayList<Restaurant>)ObjectSerializer.deserialize(getActivity().getIntent().getStringExtra(TabbedActivity.KEY_EXTRA));
                final ListAdapter adapter = new ListAdapter(getActivity(), restaurants);
                listViewRestaurants.setAdapter(adapter);
                listViewRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent = new Intent(getActivity(),DetalleRestauranteActivity.class);
                        try {
                            intent.putExtra("restaurant",ObjectSerializer.serialize((Serializable) adapter.getItem(i)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                });
                listViewRestaurants.invalidateViews();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            getRestaurants();
        }
        return view;
    }


    private ArrayList<Restaurant> getRestaurants(){


        RestaurantRequest.getRestaurants(getActivity().getApplicationContext(), new Response<Restaurant>(){
            @Override
            public void onSuccess(List<Restaurant> list) {
                Log.d("Array", list.toString());
                 ArrayList<Restaurant> restaurantesArray = new ArrayList<>(list);
                 final ListAdapter adp = new ListAdapter(getActivity(), restaurantesArray);
                 listViewRestaurants.setAdapter(adp);

                listViewRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent = new Intent(getActivity(),DetalleRestauranteActivity.class);
                try {
                    intent.putExtra("restaurant",ObjectSerializer.serialize((Serializable) adp.getItem(i)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                        startActivity(intent);
                    }
                });
                 listViewRestaurants.invalidateViews();
            }

            @Override
            public void onFailure(Message message) {
                Toast.makeText(getActivity(),"Error en cargar restaurante",Toast.LENGTH_SHORT).show();
                Log.d("GETREST", "Error al cargar");
            }
        });

        return restArray;
    }


}
