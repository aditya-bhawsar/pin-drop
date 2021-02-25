package com.aditya.pindrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.BuildConfig;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Collections;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GeoFenceAdapter.ItemListener,
        GoogleMap.OnMapLongClickListener
{
    private GoogleMap mMap;
    private GeofencingClient mGeoFencingClient;
    private GeoFenceAdapter mAdapter;
    private GeoFenceHelper mGeoFenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try { Thread.sleep(700); }

        catch (Exception e) { e.printStackTrace(); }

        setTheme(R.style.Theme_PinDrop);

        setContentView(R.layout.activity_maps);

        setupMap();
        setupGeoFencing();
        setupToolbar();
        setupRv();
        updateRv();

        askPermissions(new String[]{
                Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, 101);
        /*if (Build.VERSION.SDK_INT==29){
            askPermissions(
                    new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_NOTIFICATION_POLICY
                    },
                    101
            );
        }
        else if (Build.VERSION.SDK_INT>=23){
            askPermissions(
                    new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_NOTIFICATION_POLICY
                    },
                    102
            );
        }
        else {
            askPermissions(
                    new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    103
            );
        }*/
    }

    /** Permissions Related **/

    private void askPermissions(String[] permissions, int CODE) {
        ActivityCompat.requestPermissions(this, permissions, CODE);
    }

    private boolean checkPermissions(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean showRationale(String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
    }

    /** Toolbar Related **/

    private void setupToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setIcon(R.drawable.ic_app_40);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.info_menu) {
            View view = LayoutInflater.from(this).inflate(R.layout.app_info_view, findViewById(R.id.list_rv), false);
            MaterialTextView codeTv = view.findViewById(R.id.code_show_tv);
            MaterialTextView nameTv = view.findViewById(R.id.name_show_tv);
            nameTv.setText(BuildConfig.VERSION_NAME);
            codeTv.setText(String.valueOf(BuildConfig.VERSION_CODE));
            new MaterialAlertDialogBuilder(this).setCancelable(true).setView(view).show();
        }
        return super.onOptionsItemSelected(item);
    }

    /** Rv Related **/

    private void setupRv() {
        RecyclerView listRv = findViewById(R.id.list_rv);
        listRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new GeoFenceAdapter(Collections.emptyList(), this);
        listRv.setAdapter(mAdapter);
    }

    private void updateRv() {
        List<GeoFenceEntity> geoFenceEntityList = getGeoFences();
        if (geoFenceEntityList.isEmpty()) {
            findViewById(R.id.list_rv).setVisibility(View.GONE);
        } else {
            findViewById(R.id.list_rv).setVisibility(View.VISIBLE);
        }
        mAdapter.refresh(geoFenceEntityList);
    }

    @Override
    public void deletePlace(GeoFenceEntity geoFence) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout, findViewById(R.id.list_rv), false);
        TextInputEditText nameEt, latEt, longEt;
        nameEt = view.findViewById(R.id.place_name_et);
        longEt = view.findViewById(R.id.place_long_et);
        latEt = view.findViewById(R.id.place_lat_et);

        nameEt.setFocusableInTouchMode(false);
        nameEt.setFocusable(false);
        nameEt.setClickable(false);
        latEt.setText(String.valueOf(geoFence.getLat()));
        longEt.setText(String.valueOf(geoFence.getLongt()));

        new MaterialAlertDialogBuilder(this)
                .setView(view)
                .setIcon(R.drawable.ic_app_40)
                .setTitle("Delete Place")
                .setMessage("Are you sure you want to delete this place?")
                .setCancelable(true)
                .show();
    }

    @Override
    public void placeShow(GeoFenceEntity geoFence) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout, findViewById(R.id.list_rv), false);
        TextInputEditText nameEt, latEt, longEt;
        nameEt = view.findViewById(R.id.place_name_et);
        longEt = view.findViewById(R.id.place_long_et);
        latEt = view.findViewById(R.id.place_lat_et);

        nameEt.setFocusableInTouchMode(false);
        nameEt.setFocusable(false);
        nameEt.setClickable(false);
        nameEt.setText(geoFence.getName());
        latEt.setText(String.valueOf(geoFence.getLat()));
        longEt.setText(String.valueOf(geoFence.getLongt()));

        new MaterialAlertDialogBuilder(this)
                .setView(view)
                .setCancelable(true)
                .show();
    }

    /** GeoFence Related **/

    private void setupGeoFencing() {
        mGeoFencingClient = LocationServices.getGeofencingClient(this);
        mGeoFenceHelper = new GeoFenceHelper(this);
    }

    /** Map Related **/

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout, findViewById(R.id.list_rv), false);
        TextInputEditText nameEt, latEt, longEt;
        nameEt = view.findViewById(R.id.place_name_et);
        longEt = view.findViewById(R.id.place_long_et);
        latEt = view.findViewById(R.id.place_lat_et);

        latEt.setText(String.valueOf(latLng.latitude));
        longEt.setText(String.valueOf(latLng.longitude));

        new MaterialAlertDialogBuilder(this)
                .setView(view)
                .setIcon(R.drawable.ic_app_40)
                .setTitle("Add Place")
                .setMessage("Are you sure you want to add this place?")
                .setPositiveButton("Save", (dialog, which) -> {
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(true)
                .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        mMap.setMyLocationEnabled(true);
    }

    /** Database Related **/

    private void addGeoFence(LatLng latLng, String name){
        updateRv();
    }

    private void deleteGeoFence(GeoFenceEntity geoFenceEntity){
        updateRv();
    }

    private List<GeoFenceEntity> getGeoFences(){
        return Collections.emptyList();
    }
}