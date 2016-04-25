package ir.mbaas.push.logic;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.mbaas.push.helper.PrefUtil;
import ir.mbaas.push.mbaas.GeoLocation;

/**
 * Created by Mahdi on 4/24/2016.
 */
public class GoogleLocation implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // http://stackoverflow.com/questions/33398473/accessing-googleapiclient-object-in-all-activities
    public static final String TAG = GoogleLocation.class.getSimpleName();

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Context ctx;

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    public GoogleLocation(Context ctx) {
        this.ctx = ctx;

        buildGoogleApiClient();
        initializeLocationRequest();
        connect();
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void initializeLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    public void connect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void disconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public boolean isConnected() {
        return mGoogleApiClient != null && mGoogleApiClient.isConnected();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                // ActivityCompat#requestPermissions here to request the missing permissions, and then overriding
                // public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
        else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: googleApiClient.connect()");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: connectionResult.toString() = " + connectionResult.toString());
    }

    private void handleNewLocation (Location location) {
        if(location == null)
            return;

        String locStr = "{\"Lat\":\"" + location.getLatitude() + "\", \"Lng\":\"" +
                location.getLongitude() + "\"}";

        Log.d(TAG, locStr);

        String geoLocations = PrefUtil.getString(ctx, PrefUtil.GEO_LOCATIONS, "");
        geoLocations += geoLocations.isEmpty() ? locStr : ',' + locStr;
        PrefUtil.putString(ctx, PrefUtil.GEO_LOCATIONS, geoLocations);

        GeoLocation geoLocation = new GeoLocation(ctx, geoLocations);
        geoLocation.execute();
    }
}
