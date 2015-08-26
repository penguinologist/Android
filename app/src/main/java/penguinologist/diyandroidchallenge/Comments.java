package penguinologist.diyandroidchallenge;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;

/**
 * Created by Jeroen on 8/23/2015.
 * Long live the Penguin!
 */
public class Comments extends AppCompatActivity {
    private Button back;
    private Button post;
    private TextView title;
    private String token = "";
    private String username = "";
    private String password;

    private ListView list;
    private ArrayAdapter adapter;
    private ArrayList<String> comments;

    private String comment;
    private String input;

    private final OkHttpClient client = new OkHttpClient();

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");


    private Context context;
    private  int postID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);


        //retrieve the ID of the project and load comments accordingly...

        String ttle = "";


        Intent intent = getIntent();
        if (intent != null) {
            postID = Integer.valueOf(intent.getStringExtra("id"));
            ttle = intent.getStringExtra("title");
            username = intent.getStringExtra("username");
            password = intent.getStringExtra("password");
        }

        list = (ListView) findViewById(R.id.listView);
        comments = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, comments);
        list.setAdapter(adapter);


        loadComments(postID);
        title = (TextView) findViewById(R.id.textView);
        back = (Button) findViewById(R.id.button2);
        post = (Button) findViewById(R.id.button);

        title.setText(ttle);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get prompts.xml view
                context = v.getContext();
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Submit",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, final int id) {
                                        // get user input and set it to result
                                        // edit text
//                                        result.setText(userInput.getText());



                                        //TODO submit this message

                                        ///makers/:id/projects/:id/favorites

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                            }
                                        });
                                        AsyncTask auth = new AsyncTask() {


                                            @Override
                                            protected Object doInBackground(Object[] params) {

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {


                                                        input = userInput.getText().toString();

                                                        AsyncTask auth = new AsyncTask() {


                                                            //authenticate
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


                                                            }
                                                        }.execute();

                                                        Log.e("authenticated","posting now.....");
                                                        //post

                                                        AsyncTask post = new AsyncTask() {
                                                            @Override
                                                            protected Object doInBackground(Object[] params) {
                                                                //TODO fix ID to be the right nr....
                                                                Request request = new Request.Builder()
                                                                        .url("https://api.diy.org/makers/"+username+"/projects/"+postID+"/comments")
                                                                        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, input))
                                                                        .build();
                                                                try {
                                                                    Response response = client.newCall(request).execute();
                                                                    if (!response.isSuccessful())
                                                                        throw new IOException("Unexpected code " + response);


                                                                }catch(IOException e){
                                                                    if(e==null){
                                                                        Log.e("oops","null");
                                                                    }
                                                                    Log.e("message",e.getMessage());
                                                                    Log.e("ERROR","Post unsuccesful");
                                                                }
                                                                return null;
                                                            }
                                                        }.execute();
                                                    }
                                                });

                                                return null;
                                            }

                                            @Override
                                            protected void onPostExecute(Object o) {
                                                Toast.makeText(context, "Comment is live!!! ", Toast.LENGTH_SHORT).show();
                                            }
                                        }.execute();


                                        //authori





                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });


    }

    private void loadComments(int id) {


//authorize again (prevents timeouts)

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


            }
        }.execute();


        final int commentID = id;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                AsyncTask p = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {


                        comment = "";//initialize the comment to be anything other than null to avoid nullpointer exceptions....


                        //http://api.diy.org/makers/:id/projects/:id/comments
                        Request request = new Request.Builder()
                                .url("http://api.diy.org/makers/" + username + "/projects/" + commentID + "/comments")
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

                            for (int i = 0; i < arr.length(); i++) {


                                JSONObject current = arr.getJSONObject(i);
                                comment = current.getString("html");
//                                Log.e("outside",comment);
                                comments.add(comment);
//
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Log.e("inside",comment);
//                                        adapter.add(comment);
//
//
//                                    }
//                                });


                            }

//                            Log.e("response", token);

                        } catch (Exception e) {
                            Log.e("second async task error", e.getMessage());
                        }


                        //after all that is completed and everything is correctly added to the adapter, restore visibility of the imageview

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {

                        adapter.notifyDataSetChanged();

                    }
                }.execute();
            }
        });

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
