package penguinologist.diyandroidchallenge;


import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
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
    private static String token;

    //hardcoded the login value
    private final static String username = "hiveworking";
    private final static String password = "hiveworking";


    private final OkHttpClient client = new OkHttpClient();
    private RowItem o;
    private static boolean friends = false;

    private static ArrayList<String> projectIDs;
    private static ArrayList<String> titles;
    private static String currentUser;
    private static String other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        Log.e("username", username);
        Log.e("password", password);

        Toast.makeText(this, " Click the logo to switch between your projects and those of your friends! ", Toast.LENGTH_SHORT).show();
        projectIDs = new ArrayList<>();
        titles = new ArrayList<>();

        ListView lv = (ListView) findViewById(R.id.myList);
        rowItems = new ArrayList<RowItem>();

        // Set the adapter on the ListView
        adapter = new CustomAdapter(getApplicationContext(), R.layout.list_row, rowItems);
        lv.setAdapter(adapter);

        currentUser = username;
        //default is loading of user profile
        if (!friends) {
            loadUserProjects();
        } else {
            other = "";
            loadFriendProjects();

        }

        //async call to authenticate. Proceed on success, getting the data from the server to add to the list
        //data includes pictures, titles, etc.


        //--------------------------------------
        //menu


        SatelliteMenu menu = (SatelliteMenu) findViewById(R.id.menu);
        List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(1, R.drawable.ic_friends));
        items.add(new SatelliteMenuItem(0, R.drawable.ic_user));

        menu.addItems(items);
        //because there's only 2 items here, the linking will be hardcoded rather than looped

        menu.setOnItemClickedListener(new SatelliteMenu.SateliteClickedListener() {
            public void eventOccured(int id) {

                if (id == 0) {
                    //refresh the page when the first button is clicked.

                    //load user projects

                    try {
                        loadConfig(0);
                        friends = false;
                    } catch (Exception e) {
                        Log.e("ERROR", "Something broke...");
                    }
                }
                if (id == 1) {
                    //if this is clicked, all the projects friends have posted should be shown.
                    //load friends' projects
                    try {
                        loadConfig(1);
                        friends = true;
                    } catch (Exception e) {
                        Log.e("ERROR", "Something broke...");
                    }
                }


            }
        });


        //----- end of menu ----

    }


    public static ArrayList<String> getIDs() {

        return projectIDs;
    }


    public static ArrayList<String> getTitles() {
        return titles;
    }


    private void loadConfig(int selection) throws Exception {
        if (selection == 0) {


            //reset all the data
            adapter.clear();
            token = "";
            projectIDs = new ArrayList<>();
            titles = new ArrayList<>();
            other = "";//not like it's needed but whatever...


            loadUserProjects();


        } else if (selection == 1) {
            adapter.clear();
            token = "";
            projectIDs = new ArrayList<>();
            titles = new ArrayList<>();
            other = "";//not like it's needed but whatever...
            loadFriendProjects();


        } else {
            Log.e("ERROR", "Unable to load config - incorrect selection");
        }
    }


    private void loadFriendProjects() {

        projectIDs = new ArrayList<>();//clear out previous projects


        final ArrayList<String> peeps = new ArrayList<>();
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
//                    Log.e("output", body);

                    JSONObject resp = new JSONObject(body);
                    JSONObject responseObject = resp.getJSONObject("response");
                    token = responseObject.getString("token");
//                    Log.e("Token", token);
                } catch (Exception e) {
                    Log.e("error", "something happened");
                }

                return null;
            }


            @Override
            protected void onPostExecute(Object o) {
                Log.e("auth", "succeeded");


                //get all the ids of those following you

                //http://api.diy.org/makers/:id/following
                Request request = new Request.Builder()
                        .url("http://api.diy.org/makers/" + username + "/following")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException throwable) {
                        Log.e("FAIL", throwable.getMessage());
                    }

                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(Response response) throws IOException {
                        try {
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code " + response);


                            String resp = response.body().string();
                            Log.e("response", resp);
                            //TODO
                            JSONObject bd = new JSONObject(resp);
                            Log.e("bd", bd.toString());
                            JSONArray body = bd.getJSONArray("response");


                            for (int i = 0; i < body.length(); i++) {
                                JSONObject ob = body.getJSONObject(i);
                                String name = ob.getString("url");

                                Log.e("added", name);


                                Request req = new Request.Builder()
                                        .url("http://api.diy.org/makers/" + name + "/projects")
                                        .build();

                                client.newCall(req).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Request request, IOException throwable) {
                                        Log.e("FAIL", throwable.getMessage());
                                    }

                                    @Override
                                    public void onResponse(Response response) throws IOException {
                                        try {
                                            if (!response.isSuccessful())
                                                throw new IOException("Unexpected code " + response);


                                            String body = response.body().string();
                                            Log.e("output", body);

                                            try {
                                                JSONObject resp = new JSONObject(body);

                                                JSONArray arr = new JSONArray(resp.getString("response"));

//                            Log.e("length", arr.length() + "");
                                                for (int j = 0; j < arr.length(); j++) {


                                                    JSONObject current = arr.getJSONObject(j);

                                                    int id = current.getInt("id");
//                                Log.e("id", "" + id);


                                                    String makerID = "";
                                                    JSONObject maker = current.getJSONObject("maker");
                                                    makerID = maker.getString("url");
                                                    String title = current.getString("title");
                                                    titles.add(j, title);
//                                Log.e("title", title);
                                                    JSONArray clips = current.getJSONArray("clips");

                                                    String picture = clips.getJSONObject(0).getJSONObject("assets").getJSONObject("ios_960").getString("url");

                                                    String description = "";

                                                    final RowItem p = new RowItem(picture, title, description, id + "", makerID);
//                                Log.e("Projects", 1 + "");
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            adapter.add(p);

                                                            Log.e("added", p.getTitle());
                                                        }
                                                    });

                                                }

                                            } catch (JSONException e) {
                                                Log.e("JSON", e.getMessage());
                                            }


                                        } catch (IOException e) {
                                            Log.e("error", e.getMessage());
                                        }
                                    }
                                });


                            }


                        } catch (IOException e) {
                            Log.e("error", e.getMessage());
                        } catch (JSONException e) {
                            Log.e("JSONException", e.getMessage());
                        }
                    }
                });


            }
        }.execute();
    }

    //end of asynchronous calls


//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


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
//                    Log.e("output", body);

                    JSONObject resp = new JSONObject(body);
                    JSONObject responseObject = resp.getJSONObject("response");
                    token = responseObject.getString("token");
//                    Log.e("Token", token);
                } catch (Exception e) {
//                    Log.e("error", "something happened");
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
//                            Log.e("output", body);

                            JSONObject resp = new JSONObject(body);

                            JSONArray arr = new JSONArray(resp.getString("response"));

//                            Log.e("length", arr.length() + "");
                            for (int i = 0; i < arr.length(); i++) {


                                JSONObject current = arr.getJSONObject(i);

                                int id = current.getInt("id");
//                                Log.e("id", "" + id);
                                projectIDs.add(id + "");
                                String title = current.getString("title");
                                titles.add(i, title);
//                                Log.e("title", title);
                                JSONArray clips = current.getJSONArray("clips");


                                //TODO check that the clips are ok
                                String picture = clips.getJSONObject(0).getJSONObject("assets").getJSONObject("ios_960").getString("url");


                                String description = "";

                                o = new RowItem(picture, title, description, id + "", username);
//                                Log.e("Projects", 1 + "");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.add(o);
                                    }
                                });


                            }

//                            Log.e("response", token);

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

    public static String getUsername() {
        if (friends) { //if it's for friends, the username of the friends should be passed along, not the current user's username...
            return other;
        }

        return username;
    }

    public static String getToken() {
        return token;
    }

    public static String getPassword() {
        return password;
    }

    public static String getCurrentUser() {
        return currentUser;
    }
}
