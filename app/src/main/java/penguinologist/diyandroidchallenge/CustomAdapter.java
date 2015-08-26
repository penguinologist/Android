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
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Jeroen on 8/21/2015.
 * Long live the Penguin!
 */

/**
 * This class extends the ArrayAdapter of type RowItem. This is the adapter for the RowItems inside of the listview on the Projects page.
 */
public class CustomAdapter extends ArrayAdapter<RowItem> {

    Context context;

    /**
     * The constructor
     *
     * @param context    The context where the custom adapter is supposed to be stored.
     * @param resourceId The id of the resource to be linked.
     * @param items      The list of RowItems linked to the adapter.
     */
    public CustomAdapter(Context context, int resourceId, List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;


    }

    /**
     * Inner class serving as an object holder for all the types inside the RowItem
     */
    public class ViewHolder {
        ImageView image;
        TextView title;
        TextView description;
        LinearLayout card;
    }


    /**
     * This method sets up and executes code on click. It also links the correct images to the correct projects.
     *
     * @param position    The position where the user clicked.
     * @param convertView The view currently in question.
     * @param parent      The parent view.
     * @return Returns a view version of the entire ViewHolder.
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final RowItem rowItem = getItem(position);

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

        //this method call defines what happens when a user clicks on a project
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setup the moving from one activity to another.
                //position is kept in the getView method as a parameter for future use


                Toast.makeText(v.getContext(), "Loading comments... ", Toast.LENGTH_SHORT).show();

                String username = rowItem.getCurrentUser();
                String token = Projects.getToken();
                String password = "";


                password = Projects.getPassword();

                String projectOwner = rowItem.getProjectOwner();


                Intent intent = new Intent(v.getContext(), Comments.class);
                intent.putExtra("id", rowItem.getProjectID());

                intent.putExtra("title", rowItem.getTitle());
                intent.putExtra("username", username);
                intent.putExtra("token", token);
                intent.putExtra("password", password);
                intent.putExtra("projectOwner", projectOwner);


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