package com.aditya.pindrop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GeoFenceReceiver extends BroadcastReceiver {

    public static final String TAG = GeoFenceReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

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