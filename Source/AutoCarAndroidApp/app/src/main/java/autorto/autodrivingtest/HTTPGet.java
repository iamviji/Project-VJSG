package autorto.autodrivingtest;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by SPoojary on 12/07/2016.
 */
class HTTPGet {
    private static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public static Boolean isConnectionOK(String uri) {
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            return (conn.getResponseCode() == HttpsURLConnection.HTTP_OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getData(String uri) {
        Log.d("Starting task", "HttpGET");
        String responseString = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setAllowUserInteraction(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                // Do normal input or output stream reading
                InputStream is = conn.getInputStream();
                responseString = readStream(is) + "hmmmmm";
            } else {
                responseString = "FAILED"; // See documentation for more info on response handling
            }
        } catch (IOException e) {
            responseString = "Exception:" + e.getMessage();
        }
        return responseString;
    }
}