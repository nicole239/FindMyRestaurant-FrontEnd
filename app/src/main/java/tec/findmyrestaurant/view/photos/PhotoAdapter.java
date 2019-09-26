package tec.findmyrestaurant.view.photos;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.net.URI;
import java.util.List;

import tec.findmyrestaurant.R;
import tec.findmyrestaurant.view.AddRestaurantActivity;

public class PhotoAdapter extends PagerAdapter {

    //private int[] songs ={R.drawable.iphone_template_3,R.drawable.folder,R.drawable.iphone_template_3};
    private List<AddRestaurantActivity.PhotoView> images;
    private LayoutInflater layoutInflater;
    private Context context;
    private PhotoEvent event;
    private Uri uri[];
    private ViewGroup container;

    public PhotoAdapter(Context context, List<AddRestaurantActivity.PhotoView> images, PhotoEvent photoEvent) {
        this.context = context;
        this.images=images;
        this.event = photoEvent;
        this.uri = new Uri[images.size()];
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        this.container = container;
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.photo_item,container,false);

        Button btnDelete = view.findViewById(R.id.btnDelete);
        RelativeLayout lytadd = view.findViewById(R.id.lytAdd);
        ImageView imgPhoto = view.findViewById(R.id.imgPhoto);
        ProgressBar progressBar = view.findViewById(R.id.progress);
        lytadd.setTag(position+"lyt");
        imgPhoto.setTag(position+"img");
        btnDelete.setTag(position+"del");
        progressBar.setTag(position+"pro");
        container.addView(view);

        lytadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.onImageAdd(position);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.onImageDelete(position);
            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    public void updatePicture(Uri uri,int position){
        this.uri[position]=uri;
    }

    public interface PhotoEvent{
        void onImageAdd(int position);
        void onImageDelete(int position);
    }
}