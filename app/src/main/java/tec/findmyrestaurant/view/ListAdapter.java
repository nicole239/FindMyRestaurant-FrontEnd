package tec.findmyrestaurant.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import tec.findmyrestaurant.R;
import tec.findmyrestaurant.api.CalificationRequest;
import tec.findmyrestaurant.api.Message;
import tec.findmyrestaurant.api.Response;
import tec.findmyrestaurant.model.Calification;
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
        final RatingBar rb = vi.findViewById(R.id.ratingBarItemDetail);

        TextView txtDetails = vi.findViewById(R.id.txtItemDetail);
        txtDetails.setText(data.get(i).getFoodType().getName());
        CalificationRequest.getRestaurantCalification(vi.getContext(),data.get(i).getIdRestaurant(),new Response<Calification>(){
            @Override
            public void onSuccess(Calification objet) {
                rb.setRating(objet.getCalification());
            }

            @Override
            public void onFailure(Message message) {
                super.onFailure(message);
            }
        });

        return vi;
    }
}
