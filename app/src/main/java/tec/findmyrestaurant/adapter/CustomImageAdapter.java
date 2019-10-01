package tec.findmyrestaurant.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import tec.findmyrestaurant.R;

public class CustomImageAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> lista;
    private List<Bitmap> imgs;

    public CustomImageAdapter(Context context, List<String> lista,List<Bitmap> imgs) {
        this.context = context;
        this.lista = lista;
        this.imgs=imgs;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.pager_item, container,false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setTag(position+"img-view");

        if(imgs.get(position)!=null) {
            imageView.setImageBitmap(imgs.get(position));
        }
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
