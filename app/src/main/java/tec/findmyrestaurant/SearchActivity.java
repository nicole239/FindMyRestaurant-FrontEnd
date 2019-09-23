package tec.findmyrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tec.findmyrestaurant.model.FoodType;

public class SearchActivity extends AppCompatActivity {

    Spinner spinnerPrice;
    EditText txtName;
    EditText txtRadio;
    Spinner spinnerType;
    RatingBar ratingBarcalification;
    List<FoodType> foodTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        txtName = findViewById(R.id.txtSearchName);
        txtRadio = findViewById(R.id.txtRadio);
        ratingBarcalification = findViewById(R.id.searchCalification);
        spinnerPrice = findViewById(R.id.spinnerPrice);
        spinnerType = findViewById(R.id.spinnerFoodType);

        setPriceSpinner();
        setFoodTypeSpinner();
    }


    public void btnSearch_onClick(View view){

        //Get values from view
        String name = txtName.getText().toString();
        int radio = Integer.parseInt(txtRadio.getText().toString());
        char price;
        switch(spinnerPrice.getSelectedItemPosition()){
            case 0:
                price = 'L';
                break;
            case 1:
                price = 'M';
                break;
            case 2:
                price = 'H';
                break;
        }
        FoodType type = foodTypes.get(spinnerType.getSelectedItemPosition());
        float minCalification = ratingBarcalification.getRating();

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
        getFoodTypes();
        String[] types = new String[foodTypes.size()];
        for (int i = 0; i < foodTypes.size(); i++)
        {
            types[i] = foodTypes.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                types
        );
        spinnerType.setAdapter(adapter);


    }

    private void getFoodTypes(){
        //TODO: GET FOOD TYPES FROM BD
        foodTypes = new ArrayList<>();
        foodTypes.add(new FoodType(1,"Tipica"));
        foodTypes.add(new FoodType(4,"Chatarra"));
        foodTypes.add(new FoodType(7,"China"));
    }
}
