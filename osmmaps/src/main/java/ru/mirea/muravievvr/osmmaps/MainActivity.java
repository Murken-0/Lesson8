package ru.mirea.muravievvr.osmmaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import ru.mirea.muravievvr.osmmaps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MapView mapView = null;
    private boolean isWork = false;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mapView = binding.mapView;
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);
        int loc1PermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2PermissionStatus = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (loc1PermissionStatus == PackageManager.PERMISSION_GRANTED && loc2PermissionStatus
                == PackageManager.PERMISSION_GRANTED ) {
            isWork = true;

        }
        else {
            Log.d("HHHHHHHHHHHHH","AAAAAAAAA");
            ActivityCompat.requestPermissions(this, new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
        }

        Log.d("HHHHHHHHHHHHH",String.valueOf(isWork));
        if (isWork){
            Log.d("PERM","AAAAAAAAA");
            MyLocationNewOverlay locationNewOverlay = new MyLocationNewOverlay(new
                    GpsMyLocationProvider(getApplicationContext()),mapView);
            locationNewOverlay.enableMyLocation();
            mapView.getOverlays().add(locationNewOverlay);

            locationNewOverlay.runOnFirstFix(() -> {
                try {
                    double lat = locationNewOverlay.getMyLocation().getLatitude();
                    double lon = locationNewOverlay.getMyLocation().getLongitude();
                    Log.w("dff", String.valueOf(lat));

                    runOnUiThread(() -> {
                        IMapController mapController = mapView.getController();
                        mapController.setZoom(12.0);
                        GeoPoint point = new GeoPoint(lat, lon);
                        mapController.setCenter(point);
                    });
                }
                catch (Exception e){}
            });
        }

        CompassOverlay compassOverlay = new CompassOverlay(getApplicationContext(), new
                InternalCompassOrientationProvider(getApplicationContext()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        final Context context = this.getApplicationContext();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mapView.getOverlays().add(scaleBarOverlay);

        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(55.79402, 37.69978));
        marker.setOnMarkerClickListener((marker1, mapView) -> {
            Toast.makeText(getApplicationContext(),"РТУ МИРЕА", Toast.LENGTH_SHORT).show();
            return true;
        });
        mapView.getOverlays().add(marker);
        marker.setIcon(AppCompatResources.getDrawable(context, org.osmdroid.library.R.drawable.osm_ic_follow_me_on));
        marker.setTitle("МИРЕА");

        Marker marker2 = new Marker(mapView);
        marker2.setPosition(new GeoPoint(55.92611, 37.70978));
        marker2.setOnMarkerClickListener((marker12, mapView) -> {
            Toast.makeText(getApplicationContext(),"Волейболная площадка в лесу",
                    Toast.LENGTH_SHORT).show();
            return true;
        });
        mapView.getOverlays().add(marker2);
        marker2.setIcon(AppCompatResources.getDrawable(context, org.osmdroid.library.R.drawable.osm_ic_follow_me_on));
        marker2.setTitle("Волейбол");

        Marker marker3 = new Marker(mapView);
        marker3.setPosition(new GeoPoint(55.89101, 37.68235));
        marker3.setOnMarkerClickListener((marker13, mapView) -> {
            Toast.makeText(getApplicationContext(),"Парк на Яузе",
                    Toast.LENGTH_SHORT).show();
            return true;
        });
        mapView.getOverlays().add(marker3);
        marker3.setIcon(AppCompatResources.getDrawable(context, org.osmdroid.library.R.drawable.osm_ic_follow_me_on));
        marker3.setTitle("Парк культуры и отдыха");

    }
    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onResume();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onPause();
        }
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            isWork = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }
}