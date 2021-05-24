package com.aditya.pindrop;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeoFenceReceiver extends BroadcastReceiver {

    public static final String TAG = GeoFenceReceiver.class.getSimpleName();
    public static final String NOTIFICATION_CHANNEL_ID = "Transition";
    public static final String NOTIFICATION_CHANNEL_NAME = "GeoFence Transition";
    public static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if(event.hasError()){
            Log.e(TAG, String.valueOf(event.getErrorCode()));
            return;
        }

        int transition = event.getGeofenceTransition();

        if(transition == Geofence.GEOFENCE_TRANSITION_ENTER){
            //Silence System
            notifyAndSilence(context, true);
        }
        else if(transition == Geofence.GEOFENCE_TRANSITION_EXIT){
            //DeSilence System
            notifyAndSilence(context, false);
        }
        else {}
    }


    private void notifyAndSilence(Context ctx, boolean silence){

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        createChannel(notificationManager);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx, NOTIFICATION_CHANNEL_ID);

        String title= "";
        String text= "";
        if(silence) {
            title = "Device in Silence Mode";
            text = "You entered a marked zone silence mode is activated";
        }
        else {
            title = "Device in General Mode";
            text = "You exited a marked zone general mode is activated";
        }

        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(text);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

    }

    private void createChannel(NotificationManager notificationManager){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            notificationManager.createNotificationChannel(channel);
        }
    }

    /*@Override
    public void onReceive(Context context, Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, String.format("Error code : %d", geofencingEvent.getErrorCode()));
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            setRingerMode(context, AudioManager.RINGER_MODE_SILENT);
            Log.e("print opened it","done");
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            setRingerMode(context, AudioManager.RINGER_MODE_NORMAL);
            Log.e("print closed it","done");
        } else {
            Log.e(TAG, String.format("Unknown transition : %d", geofenceTransition));
            return;
        }
        sendNotification(context, geofenceTransition);
    }


    private void sendNotification(Context context, int transitionType) {

        Intent notificationIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.e("Did that Happen","true");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);


        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
            builder.setSmallIcon(R.drawable.ic_volume_off_white_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_volume_off_white_24dp))
                    .setContentTitle("Silent mode activated");
        } else if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
            builder.setSmallIcon(R.drawable.ic_volume_up_white_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_volume_up_white_24dp))
                    .setContentTitle("Back to normal");
        }

        builder.setContentText("Touch to launch the app.");
        builder.setContentIntent(notificationPendingIntent);

        builder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("222", "Pin Drop", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);

        }

        mNotificationManager.notify(0, builder.build());
    }

    private void setRingerMode(Context context, int mode) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.e("Did it Happen","true");
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(mode);
        Log.e("executed","done:");
    }*/

}