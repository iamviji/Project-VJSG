package autorto.autodrivingtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import AlgoPlane.FailReason;
import AlgoPlane.IParallelParkingTestStateChangeListener;
import AlgoPlane.ISensorStateChangeListener;
import AlgoPlane.ParallelParkingState;
import AlgoPlane.ParallelParkingWarning;
import AutoCarTestCoreMain.*;

public class GatherTestData extends AppCompatActivity {

    private GatherSensorData gatherSensorData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gather_test_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Toast.makeText(this, "Gathering test data:", Toast.LENGTH_LONG).show();
        Log.d("Starting the test data collection", "StartTest");
        gatherSensorData = new GatherSensorData(this);
        gatherSensorData.execute("6000");
    }
    public void cancelTestGatherDataHandler(View target) {
        Intent intent = new Intent(this, StartTest.class);
        startActivity(intent);
    }
    public void finishTestGatherDataHandler(View target) {
        gatherSensorData.stopTest();
        Toast.makeText(this, "Uploading the gathered data:", Toast.LENGTH_LONG).show();
    }

    class GatherSensorData extends AsyncTask<String, Void, String> implements IParallelParkingTestStateChangeListener, ISensorStateChangeListener {
        private AutoCarTestCoreMain core;
        private AppCompatActivity myActivity;
        public GatherSensorData(AppCompatActivity activity) {
            myActivity = activity;
        }
        @Override
        protected String doInBackground(String... ports) {
            int port = Integer.parseInt(ports[0]);

            //Create Folder
            File dataDir = new File(Environment.getExternalStorageDirectory().toString() + "/autorto/");
            dataDir.mkdirs();

            String logFile = dataDir.toString() + "/AutoCarTestLog.txt";
            String dataFile = dataDir.toString() + "/DataLog.txt";
            Log.d("Storing the data in file:" + dataFile, "StartTest");
            core = new AutoCarTestCoreMain (port, this, this, logFile , dataFile);
            core.run();
            return dataFile;
        }

        public void stopTest() {
            core.stopTest();
            core = null;
        }
        @Override
        protected void onPreExecute() {}
        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
            builder.setMessage("Test complete, see your result in " + result)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(myActivity, StartTest.class);
                            startActivity(intent);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            Intent intent = new Intent(myActivity, StartTest.class);
            startActivity(intent);
        }

        public void handleEventStateChange (ParallelParkingState curState, ParallelParkingState prevState)
        {
            System.out.println ("Apl:handleStateChange curState="+curState + " prevState="+prevState);
        }
        public void handleWarning (ParallelParkingState state, ParallelParkingWarning warn)
        {
            System.out.println ("Apl:handleWarning state="+state+" warn="+warn);
        }
        public void handlePassInd()
        {
            System.out.println ("Apl:handlePassInd");
        }
        public void handleFailInd(ParallelParkingState state, FailReason reason)
        {
            System.out.println ("Apl:handleFailInd state="+state+" reason="+reason);
        }
        public void handleSensorStateChangeInd (String str, boolean isActive)
        {
            System.out.println ("Apl:handleSensorStateChangeInd sensor="+str+" isActive="+isActive);
        }
    }
}
