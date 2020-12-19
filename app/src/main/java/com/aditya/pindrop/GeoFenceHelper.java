package com.aditya.pindrop;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

public class GeoFenceHelper extends ContextWrapper {

    private static final int PENDING_INTENT_REQUEST_CODE = 200;
    PendingIntent pendingIntent;

    public GeoFenceHelper(Context base) {
        super(base);
    }

    public GeofencingRequest getGeoFencingRequest(Geofence geofence) {
        return new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();
    }

    public PendingIntent getPendingIntent() {
        if(pendingIntent!= null)
            return pendingIntent;

        Intent intent = new Intent(this, GeoFenceReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, PENDING_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public String getErrorString(Exception e){
        if(e instanceof ApiException){
            ApiException apiException = (ApiException) e;
            switch (apiException.getStatusCode()){
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE NOT AVAILABLE";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    return "GEOFENCE TOO MANY GEOFENCES";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "GEOFENCE TOO MANY PENDING INTENTS";
            }
        }
        return e.getLocalizedMessage();
    }

    public Geofence getGeoFence(String ID, LatLng latLng, float r, int transitionTypes){
        return new Geofence.Builder()
                .setCircularRegion(latLng.latitude,latLng.longitude,r)
                .setRequestId(ID)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(120000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }



}
