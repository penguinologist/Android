package penguinologist.diyandroidchallenge;

/**
 * Created by Jeroen on 8/21/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<RowItem> {

    Context context;

    public CustomAdapter(Context context, int resourceId, List<RowItem> items){
        super(context, resourceId, items);
        this.context = context;


    }

    public class ViewHolder{
        ImageView image;
        TextView title;
        TextView description;
        ProgressBar spin;
        LinearLayout card;
    }


    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.card = (LinearLayout) convertView.findViewById(R.id.card);
            holder.image = (ImageView)convertView.findViewById(R.id.list_image);
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.description = (TextView)convertView.findViewById(R.id.description);
            holder.spin = (ProgressBar)convertView.findViewById(R.id.progress_circular);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder)convertView.getTag();

        holder.image.setImageResource(rowItem.getImageId());
        holder.image.setVisibility(View.INVISIBLE);
        holder.title.setText(rowItem.getTitle());
        holder.description.setText(rowItem.getDesc());



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setup the moving from one place to another



            }
        });

        return convertView;
    }
}