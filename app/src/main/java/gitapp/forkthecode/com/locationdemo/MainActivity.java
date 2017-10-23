package gitapp.forkthecode.com.locationdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {

    GoogleApiClient apiClient;
    SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        Sensor light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(light != null){
            sensorManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {

                    float lux = event.values[0];
                    Log.i("CN log","Lux: " + lux);

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            },light,SensorManager.SENSOR_DELAY_NORMAL);
        }

        Sensor acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(acc != null){

            sensorManager.registerListener(new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    Log.i("CN log","X: " + x + " Y: " + y + " Z: " + z);

                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {

                }
            },acc,SensorManager.SENSOR_DELAY_NORMAL);

        }

    }

    @Override
    protected void onStart() {
        Log.i("CN Log", "Start");
        super.onStart();
        //apiClient.connect();
    }

    @Override
    protected void onStop() {
        Log.i("CN Log", "Stop");
        //apiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("CN Log", "Connected");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        Location location = LocationServices.FusedLocationApi.getLastLocation(apiClient);
//        if(location != null){
//            Toast.makeText(MainActivity.this,"Lat: " + location.getLatitude() + " Long: " + location.getLongitude(),Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Log.i("CN Log","Location null");
//        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);

        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient,locationRequest,MainActivity.this);


        LocationServices.FusedLocationApi.removeLocationUpdates(apiClient,MainActivity.this);



    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("CN Log","Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("CN Log","Failed " + connectionResult.getErrorCode());

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("CN log","Lat: " + location.getLatitude() + " Long: " + location.getLongitude());
    }
}
