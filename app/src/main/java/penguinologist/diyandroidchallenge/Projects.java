package penguinologist.diyandroidchallenge;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Proxy;
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
    private CustomAdapter adapter;
    private String token;

    //TODO get the data from the server and push it into the arrays onCreation of the activity
    //TODO take care of the comments and the addition of new comments


    private final static String username = "hiveworking";
    private final static String password = "hiveworking";
    private final OkHttpClient client = new OkHttpClient();
    private RowItem o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);


        ListView lv = (ListView) findViewById(R.id.myList);
        rowItems = new ArrayList<RowItem>();

        // Set the adapter on the ListView
        adapter = new CustomAdapter(getApplicationContext(), R.layout.list_row, rowItems);
        lv.setAdapter(adapter);


        //async call to authenticate. Proceed on success, getting the data from the server to add to the list
        //data includes pictures, titles, etc.


        //load user profile
        loadUserProjects();


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

            adapter.clear();
            loadUserProjects();


        } else if (selection == 1) {


        } else {
            Log.e("ERROR", "Unable to load config - incorrect selection");
        }
    }


    private void loadUserProjects() {


        AsyncTask auth = new AsyncTask() {


            @Override
            protected Object doInBackground(Object[] params) {
                client.setAuthenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Proxy proxy, Response response) {

                        //hardcoded the login values
                        String credential = Credentials.basic(username, password);
                        return response.request().newBuilder()
                                .header("Authorization", credential)
                                .build();
                    }

                    @Override
                    public Request authenticateProxy(Proxy proxy, Response response) {
                        return null; // Null indicates no attempt to authenticate.
                    }
                });

                Request request = new Request.Builder()
                        .url("https://api.diy.org/authorize")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    String body = response.body().string();
                    Log.e("output", body);

                    JSONObject resp = new JSONObject(body);
                    JSONObject responseObject = resp.getJSONObject("response");
                    token = responseObject.getString("token");
                    Log.e("Token", token);
                } catch (Exception e) {
                    Log.e("error", "something happened");
                }

                return null;
            }


            @Override
            protected void onPostExecute(Object o) {


            }
        }.execute();

        //end of asynchronous calls


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AsyncTask p = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        Request request = new Request.Builder()
                                .url("http://api.diy.org/makers/" + username + "/projects")
                                .header("x-diy-api-token", token)
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code " + response);


                            String body = response.body().string();
                            Log.e("output", body);

                            JSONObject resp = new JSONObject(body);

                            JSONArray arr = new JSONArray(resp.getString("response"));

                            Log.e("length", arr.length() + "");
                            for (int i = 0; i < arr.length(); i++) {


                                JSONObject current = arr.getJSONObject(i);

                                String title = current.getString("title");
                                Log.e("title", title);
                                JSONArray clips = current.getJSONArray("clips");


                                //TODO check that the clips are ok
                                String picture = clips.getJSONObject(0).getJSONObject("assets").getJSONObject("ios_960").getString("url");


                                String description = "";

                                o = new RowItem(picture, title, description);
                                Log.e("Projects", 1 + "");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.add(o);
                                    }
                                });


                            }

                            Log.e("response", token);

                        } catch (Exception e) {
                            Log.e("second async task error", e.getMessage());
                        }


                        //after all that is completed and everything is correctly added to the adapter, restore visibility of the imageview

                        return null;
                    }
                }.execute();
            }
        });

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
