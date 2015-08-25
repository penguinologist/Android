package penguinologist.diyandroidchallenge;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.List;

import penguinologist.menu.SatelliteMenu;
import penguinologist.menu.SatelliteMenuItem;

/**
 * Created by Jeroen on 8/23/2015.
 * Long live the Penguin!
 */


public class Projects extends AppCompatActivity {

    private List<RowItem> rowItems;
    private String token;

    //TODO get the data from the server and push it into the arrays onCreation of the activity
    //TODO take care of the comments and the addition of new comments


    // Drawable d = Drawable.createFromPath(pathName);

    //TODO initialize this array
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        //


        Async.auth.execute();

        Async.loadUserProjects.execute(getCurrentFocus());





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

                if (id == 0) {
                    //refresh the page when the first button is clicked.

                    //load user projects

                    try {
                        loadConfig(0);
                    } catch (Exception e) {
                        Log.e("ERROR", "Something broke...");
                    }
                }
                if (id == 1) {
                    //if this is clicked, all the projects friends have posted should be shown.
                    //load friends' projects
                    try {
                        loadConfig(1);
                    } catch (Exception e) {
                        Log.e("ERROR", "Something broke...");
                    }
                }


            }
        });


        //----- end of menu ----

    }





    private void loadConfig(int selection) throws Exception {
        if (selection == 0) {



        } else if (selection == 1) {

        } else {
            Log.e("ERROR", "Unable to load config - incorrect selection");
        }
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
