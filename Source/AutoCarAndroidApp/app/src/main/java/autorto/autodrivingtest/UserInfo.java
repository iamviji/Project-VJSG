package autorto.autodrivingtest;

import android.os.AsyncTask;
import android.os.Bundle;
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


public class UserInfo extends AppCompatActivity {

    public void detailsDisplayHandler(View target) {
        //String URL = "http://127.0.0.1:8000/testreg/12345680/searchid";
        //String result=HTTPGet.getURL(URL);
       // TextView txtView=(TextView)findViewById(R.id.name);
        //txtView.setText(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        String uid = bundle.getString("uid");

        Toast.makeText(this, "Hello, creating new view", Toast.LENGTH_LONG).show();
        String URL = "http://192.168.2.4:8000/testreg/" + uid +"/searchid";
        //String URL = "http://www.survivingwithandroid.com/2014/04/parsing-html-in-android-with-jsoup-2.html";
        new RequestTask().execute(URL);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

    class RequestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            //String response = HTTPGet.getData(urls[0]);
            StringBuffer buffer = new StringBuffer();
            try {

                Document doc = Jsoup.connect(urls[0]).get();
                buffer.append(doc.html());
            }
            catch(Throwable t) {
                t.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String html) {
            Log.d("In background onPostExecute", "HttpGET");
            Log.d(html, "HttpGET");
            Document doc = Jsoup.parse(html);
            super.onPostExecute(html);

            Element fname = doc.select("font[name=first_name]").first();
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

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
