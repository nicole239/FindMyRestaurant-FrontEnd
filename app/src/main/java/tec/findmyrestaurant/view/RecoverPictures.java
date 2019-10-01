package tec.findmyrestaurant.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.List;

public class RecoverPictures extends AsyncTask<Void,Void, Void> {

    List<ImageView> imageViews;
    List<String> urls;
    List<Bitmap> bmps;

    public RecoverPictures(List<ImageView> imageViews, List<String> urls,List<Bitmap> bitmaps) {
        this.imageViews = imageViews;
        this.urls = urls;
        bmps = bitmaps;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if(imageViews.size()==urls.size()){
            try{
                for(int i=0;i<imageViews.size();i++){
                    Bitmap bmp = null;
                    InputStream in = new java.net.URL(urls.get(i)).openStream();
                    bmp = BitmapFactory.decodeStream(in);
                    bmps.set(i,bmp);
                }

            }catch (Exception e){
                Log.e("BMP-error","");
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        for(int i=0;i<urls.size();i++){
            if(bmps.get(i)!=null && imageViews.get(i)!=null){
                imageViews.get(i).setImageBitmap(bmps.get(i));
            }
        }
    }
}
