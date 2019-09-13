package tec.findmyrestaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tec.findmyrestaurant.model.Restaurant;

public class ListAdapter extends BaseAdapter {

    Context context;
    private static LayoutInflater inflater = null;
    ArrayList<Restaurant> data;

    public ListAdapter(Context context, ArrayList<Restaurant> data){
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return data.get(i).getIdRestaurant();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);

        ImageView img = vi.findViewById(R.id.imgItemRestaurant);
        //TODO: Set restaurant image to ImageView

        TextView txtName =  vi.findViewById(R.id.txtItemtName);
        txtName.setText(data.get(i).getName());

        TextView txtDetails = vi.findViewById(R.id.txtItemDetail);
        txtDetails.setText(data.get(i).getFoodType());
        return vi;
    }
}
