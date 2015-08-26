package penguinologist.diyandroidchallenge;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeroen on 8/21/2015.
 * Long live the Penguin!
 */


public class CustomAdapter extends ArrayAdapter<RowItem> {

    Context context;

    public CustomAdapter(Context context, int resourceId, List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;


    }

    public class ViewHolder {
        ImageView image;
        TextView title;
        TextView description;
        LinearLayout card;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.card = (LinearLayout) convertView.findViewById(R.id.card);
            holder.image = (ImageView) convertView.findViewById(R.id.list_image);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.description = (TextView) convertView.findViewById(R.id.description);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(super.getContext()).load(rowItem.getImageId()).into(holder.image);
        holder.title.setText(rowItem.getTitle());
        holder.description.setText(rowItem.getDesc());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setup the moving from one activity to another.
                //position is kept in the getView method as a parameter for future use


                Toast.makeText(v.getContext(), "Loading comments... ", Toast.LENGTH_SHORT).show();

                ArrayList<String> ids = Projects.getIDs();

                ArrayList<String> titles = Projects.getTitles();
                String username = Projects.getUsername();
                String token = Projects.getToken();
                String password = Projects.getPassword();

                Intent intent = new Intent(v.getContext(), Comments.class);
                intent.putExtra("id", ids.get(position));
                intent.putExtra("title", titles.get(position));
                intent.putExtra("username", username);
                intent.putExtra("token", token);
                intent.putExtra("password", password);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                v.getContext().startActivity(intent);


            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                //TODO set favorite to this project

                return false;
            }
        });

        return convertView;
    }




}