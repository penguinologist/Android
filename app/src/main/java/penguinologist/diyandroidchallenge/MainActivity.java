package penguinologist.diyandroidchallenge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import penguinologist.menu.SatelliteMenu;
import penguinologist.menu.SatelliteMenuItem;


public class MainActivity extends AppCompatActivity {

    private List<RowItem> rowItems;


    //TODO get the data from the server and push it into the arrays onCreation of the activity
    //TODO take care of the comments and the addition of new comments

    private static Integer[] images = {
            R.mipmap.ic_launcher,
            Color.RED,
            Color.RED,
            Color.RED,
            Color.RED,
            Color.RED,
            Color.RED,
            Color.RED
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView lv = (ListView) findViewById(R.id.myList);
        rowItems = new ArrayList<RowItem>();

        String[] titles = {"Movie1", "Movie2", "Movie3", "Movie4", "Movie5", "Movie6", "Movie7", "Movie8"};
        String[] descriptions = {"First Movie", "Second movie", "Third Movie", "Fourth Movie", "Fifth Movie",
                "Sixth Movie", "Seventh Movie", "Eighth Movie"};
        //Populate the List
        for (int i = 0; i < titles.length; i++) {
            RowItem item = new RowItem(images[i], titles[i], descriptions[i]);
            rowItems.add(item);
        }

        // Set the adapter on the ListView
        CustomAdapter adapter = new CustomAdapter(getApplicationContext(), R.layout.list_row, rowItems);
        lv.setAdapter(adapter);

        Log.e("loaded", "main");

        lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("yay", "it worked!!!!");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //--------------------------------------
        //menu


        SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.menu);
        List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(1, R.drawable.ic_comm));
        items.add(new SatelliteMenuItem(0, R.drawable.ic_projects));
        menu.addItems(items);
        //because there's only 2 items here, the linking will be hardcoded rather than looped

        menu.setOnItemClickedListener(new SatelliteMenu.SateliteClickedListener() {
            public void eventOccured(int id) {

                Log.e("id", "" + id);
                if (id == 0) {
                    //refresh the page when the first button is clicked.
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (id == 1) {
                    //go to comments if the second button is clicked
                    Intent intent = new Intent(MainActivity.this, Comments.class);
                    startActivity(intent);
                    finish();
                }


            }
        });


        //----- end of menu ----

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
