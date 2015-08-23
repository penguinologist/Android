package penguinologist.diyandroidchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import penguinologist.menu.SatelliteMenu;
import penguinologist.menu.SatelliteMenuItem;

public class Comments extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

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
                    Intent intent = new Intent(Comments.this, Projects.class);
                    startActivity(intent);
                    finish();
                }
                if (id == 1) {
                    //go to comments if the second button is clicked
                    Intent intent = new Intent(Comments.this, Comments.class);
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
        getMenuInflater().inflate(R.menu.menu_comments, menu);
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
