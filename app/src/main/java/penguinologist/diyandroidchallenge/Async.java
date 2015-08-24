package penguinologist.diyandroidchallenge;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.net.Proxy;

/**
 * Created by Jeroen on 8/23/2015.
 */
public class Async {

    private static String token;


    public static AsyncTask<Void, Void, String> auth = new AsyncTask<Void, Void, String>() {
        @Override
        protected String doInBackground(Void... params) {
            token = "";
            try {
                token = new Authenticate().run();
            } catch (Exception e) {
                Log.e("error", e.toString());
            }
            return token;
        }
    };


    private static class Authenticate {


        private final OkHttpClient client = new OkHttpClient();

        public String run() throws Exception {
            client.setAuthenticator(new Authenticator() {
                @Override
                public Request authenticate(Proxy proxy, Response response) {

                    //hardcoded the login values
                    String credential = Credentials.basic("hiveworking", "hiveworking");
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


    }
}
