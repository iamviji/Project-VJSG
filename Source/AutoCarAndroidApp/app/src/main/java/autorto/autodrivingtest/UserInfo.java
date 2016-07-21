package autorto.autodrivingtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class UserInfo extends AppCompatActivity {

    public void detailsDisplayHandler(View target) {
        Intent intent = new Intent(this, GatherTestData.class);
        TextView textView = (TextView) findViewById(R.id.id);
        String uid = textView.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        intent.putExtras(bundle);
        Toast.makeText(this, "Start the test:", Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        String uid = bundle.getString("uid");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(UserInfo.this);
        String addr = prefs.getString("server_addr", "192.168.2.5");
        String port = prefs.getString("server_port", "8000");

        String URL = "http://" + addr + ":" + port +"/testreg/" + uid +"/searchid";
        //String URL = "http://www.survivingwithandroid.com/2014/04/parsing-html-in-android-with-jsoup-2.html";
        //Boolean connected = HTTPGet.isConnectionOK(URL);
        new RequestTask(this).execute(URL);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_test, menu);
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

    public void cancelTestStartHandler(View target) {
        Intent intent = new Intent(this, StartTest.class);
        startActivity(intent);
    }

    class RequestTask extends AsyncTask<String, String, String> {
        public String FAILED_CONNECTON = "FailedConnection";
        private AppCompatActivity myActivity;
        public RequestTask(AppCompatActivity activity) {
            myActivity = activity;
        }
        @Override
        protected String doInBackground(String... urls) {
            //String response = HTTPGet.getData(urls[0]);
            StringBuffer buffer = new StringBuffer();
            try {

                Document doc = Jsoup.connect(urls[0]).get();
                buffer.append(doc.html());
            }
            catch(IOException e) {
                e.printStackTrace();
                publishProgress("Unable to connect to the webserver");
                return FAILED_CONNECTON;
            }

            return buffer.toString();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onProgressUpdate(String... item) {
            String errorText = item[0];
            AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
            builder.setMessage(errorText)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(myActivity, StartTest.class);
                            startActivity(intent);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        @Override
        protected void onPostExecute(String html) {
            Log.d("In background onPostExecute", "HttpGET");
            Log.d(html, "HttpGET");
            if (html == FAILED_CONNECTON) {
                onProgressUpdate("Unable to connect to the webserver");
                Intent intent = new Intent(myActivity, PrefsActivity.class);
                startActivity(intent);
                return;
            }
            if (html.isEmpty()) {
                onProgressUpdate("The specified ID doesn't exist");
                return;
            }

            Document doc = Jsoup.parse(html);
            super.onPostExecute(html);

            Elements fnames = doc.select("font[name=first_name]");
            Element fname = null;
            if (fnames.isEmpty() || fnames.size() < 1) {
                onProgressUpdate("The specified ID doesn't exist");
                return;
            } else {
                fname = fnames.first();
            }

            Element lname = doc.select("font[name=last_name]").first();
            String name = fname.text() + " " + lname.text();
            TextView txtView1 = (TextView) findViewById(R.id.name);
            txtView1.setText(name);

            Element bdate = doc.select("font[name=bdate]").first();
            TextView txtView2 = (TextView) findViewById(R.id.dob);
            txtView2.setText(bdate.text());

            Element uid = doc.select("font[name=id]").first();
            TextView txtView3 = (TextView) findViewById(R.id.id);
            txtView3.setText(uid.text());
        }

        @Override
        protected void onPreExecute() {}
    }
}
