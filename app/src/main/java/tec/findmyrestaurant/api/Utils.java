package tec.findmyrestaurant.api;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Utils {
    public static String toAbsolutePath (Context context, Uri uri){
        Cursor cursor = null;
        try{
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(uri,proj,null,null,null);
            int column_indx = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_indx);
        }catch (Exception e){
            throw e;
        }finally {
            if (cursor!=null)
                cursor.close();
        }
    }
}
