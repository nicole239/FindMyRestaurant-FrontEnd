package tec.findmyrestaurant.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import tec.findmyrestaurant.R;
import tec.findmyrestaurant.model.Restaurant;


public class ListFragment extends Fragment {
    private static final String TAG = "TabListFragment";

    private ListView listViewRestaurants;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_list,container,false);

        listViewRestaurants = view.findViewById(R.id.listViewRestaurants);
        final ListAdapter adapter = new ListAdapter(getActivity(), getDummyRestaurants());
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
        return view;
    }


    private ArrayList<Restaurant> getRestaurants(){
        //TODO: implement get restaurants
        return null;
    }

    private ArrayList<Restaurant> getDummyRestaurants(){
        ArrayList<Restaurant> restuarants = new ArrayList<>();
        Restaurant res1 = new Restaurant();
        res1.setName("McDondals");
        res1.setFoodType("Chatarra");
        Restaurant res2 = new Restaurant();
        res2.setName("El chino");
        res2.setFoodType("Comida china");
        Restaurant res3 = new Restaurant();
        res3.setName("Donde Marta");
        res3.setFoodType("Tipica");

        restuarants.add(res1);
        restuarants.add(res2);
        restuarants.add(res3);

        return restuarants;
    }
}
