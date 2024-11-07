package thiennh23.potholehunter;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private MapView mapView;
    private LocationManager locationManager;
    private Marker userLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);

        // Set up osmdroid configuration
        Configuration.getInstance().setUserAgentValue(getPackageName());

        mapView = findViewById(R.id.map);

        // Configure the map view
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Initialize the location manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Check and request location permissions
        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            initUserLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initUserLocation();
            } else {
                Log.e("MapActivity", "Location permission denied!");
            }
        }
    }

    private void initUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Check if GPS provider is enabled
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.e("MapActivity", "GPS is not enabled!");
                return;
            }

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                updateLocationMarker(lastKnownLocation);
            }

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    updateLocationMarker(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}

                @Override
                public void onProviderDisabled(String provider) {}
            });
        }
    }

    private void updateLocationMarker(Location location) {
        // Get user's current location
        GeoPoint userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());

        // Set up or update the marker for user's location
        if (userLocationMarker == null) {
            userLocationMarker = new Marker(mapView);
            userLocationMarker.setTitle("Your Location");
            mapView.getOverlays().add(userLocationMarker);
        }

        userLocationMarker.setPosition(userLocation);
        mapView.getController().setCenter(userLocation);
        mapView.getController().setZoom(15.0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume(); // Needed for osmdroid to function correctly
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause(); // Needed for osmdroid to function correctly
    }
}
