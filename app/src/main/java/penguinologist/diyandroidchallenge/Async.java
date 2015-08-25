package penguinologist.diyandroidchallenge;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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

/**
 * Created by Jeroen on 8/23/2015.
 * Long live the Penguin!
 */
public class Async {

    private static String token;
    private static Authenticate au;


    public static AsyncTask<Void, Void, String> auth = new AsyncTask<Void, Void, String>() {
        @Override
        protected String doInBackground(Void... params) {

            au = new Authenticate();
            token = "";
            try {
                token = new Authenticate().run();
            } catch (Exception e) {
                Log.e("error", e.toString());
            }
            return token;
        }
    };





    public static AsyncTask<View, Void, Void> loadUserProjects = new AsyncTask<View, Void, Void>() {
        @Override
        protected Void doInBackground(View... params) {
            try {
                au.getUserProjects(params[0]);
            } catch (Exception e) {
                Log.e("error", e.toString());
            }
            return null;
        }
    };


    private static class Authenticate {


        //hardcoded the username and password
        private final static String username = "hiveworking";
        private final static String password = "hiveworking";
        private final OkHttpClient client = new OkHttpClient();

        public String run() throws Exception {
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

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String body = response.body().string();
            Log.e("output", body);

            JSONObject resp = new JSONObject(body);
            JSONObject responseObject = resp.getJSONObject("response");
            String token = responseObject.getString("token");
            Log.e("Token", token);
            return token;
        }


        public void getUserProjects(View view) throws Exception {

            Request request = new Request.Builder()
                    .url("http://api.diy.org/makers/" + username + "/projects")
                    .header("x-diy-api-token", token)
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);


            String body = response.body().string();
            Log.e("output", body);

            JSONObject resp = new JSONObject(body);

            JSONArray arr = new JSONArray(resp.getString("response"));


            for (int i = 0; i < arr.length(); i++) {
                JSONObject resp2 = arr.getJSONObject(i);
                Log.e("length", arr.length() + "");
                Log.e("title", resp2.getString("title"));





                ArrayList<RowItem> rowItems;
//                  Integer[] images = {
//            R.mipmap.ic_launcher,
//            Color.RED,
//            Color.RED,
//            Color.RED,
//            Color.RED,
//            Color.RED,
//            Color.RED,
//            Color.RED
//    };


                ListView lv = (ListView) view.findViewById(R.id.myList);
                rowItems = new ArrayList<RowItem>();

//        String[] titles = {"Movie1", "Movie2", "Movie3", "Movie4", "Movie5", "Movie6", "Movie7", "Movie8"};
//        String[] descriptions = {"First Movie", "Second movie", "Third Movie", "Fourth Movie", "Fifth Movie",
//                "Sixth Movie", "Seventh Movie", "Eighth Movie"};
                //Populate the List
//        for (int i = 0; i < titles.length; i++) {
//            RowItem item = new RowItem(images[i], titles[i], descriptions[i]);
//            rowItems.add(item);
//        }

                // Set the adapter on the ListView
                CustomAdapter adapter = new CustomAdapter(view.getContext(), R.layout.list_row, rowItems);
                lv.setAdapter(adapter);











            }


            Log.e("response", token);


        }

    }
}
