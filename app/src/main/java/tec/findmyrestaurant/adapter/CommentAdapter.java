package tec.findmyrestaurant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import tec.findmyrestaurant.R;
import tec.findmyrestaurant.model.Comment;

public class CommentAdapter extends ArrayAdapter<Comment> {

    Context context;
    List<Comment> listComment;

    private static class ViewHolder{
        TextView correo;
        TextView contenido;
    }

    public CommentAdapter(Context context, List<Comment> lista){
        super(context,R.layout.comment_row,lista);
        this.context=context;
        this.listComment = lista;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Comment comment = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.comment_row,parent,false);
            viewHolder.contenido = (TextView) convertView.findViewById(R.id.comment_row_content_TV);
            viewHolder.correo = (TextView) convertView.findViewById(R.id.comment_row_user_email_TV);
            result = convertView;
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.correo.setText(comment.getUser().getEmail()+" dijo: ");
        viewHolder.contenido.setText(comment.getComment());


        return convertView;
    }
}
