package edu.uw.tcss450.chatapp_group1.services;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONException;

import edu.uw.tcss450.chatapp_group1.AuthActivity;
import edu.uw.tcss450.chatapp_group1.R;
import edu.uw.tcss450.chatapp_group1.ui.chat.ChatMessage;
import me.pushy.sdk.Pushy;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

public class PushReceiver extends BroadcastReceiver {

    public static final String RECEIVED_NEW_MESSAGE = "new message from pushy";
    public static final String RECEIVED_NEW_REQUEST = "new request from pushy";
    public static final String RECEIVED_NEW_ACCEPT = "new accept from pushy";

    private static final String CHANNEL_ID = "1";
    private String acceptorEmail;


    @Override
    public void onReceive(Context context, Intent intent) {

        //the following variables are used to store the information sent from Pushy
        //In the WS, you define what gets sent. You can change it there to suit your needs
        //Then here on the Android side, decide what to do with the message you got

        //for the lab, the WS is only sending chat messages so the type will always be msg
        //for your project, the WS needs to send different types of push messages.
        //So perform logic/routing based on the "type"
        //feel free to change the key or type of values.
        String typeOfMessage = intent.getStringExtra("type");
        ChatMessage message = null;
        String senderEmail = null;
        String acceptorEmail = null;
        int acceptorid = -1;
        int senderid = -1;
        int receiverid = -1;

        int chatId = -1;

        if(typeOfMessage.equals("msg")){
            try{
                message = ChatMessage.createFromJsonString(intent.getStringExtra("message"));
                chatId = intent.getIntExtra("chatid", -1);
            } catch (JSONException e) {
                //Web service sent us something unexpected...I can't deal with this.
                throw new IllegalStateException("Error from Web Service. Contact Dev Support");
            }

            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);

            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
                //app is in the foreground so send the message to the active Activities
                Log.d("PUSHY", "Message received in foreground: " + message);

                //create an Intent to broadcast a message to other parts of the app.
                Intent i = new Intent(RECEIVED_NEW_MESSAGE);
                i.putExtra("chatMessage", message);
                i.putExtra("chatid", chatId);
                i.putExtras(intent.getExtras());

                context.sendBroadcast(i);

            } else {
                //app is in the background so create and post a notification
                Log.d("PUSHY", "Message received in background: " + message.getMessage());

                Intent i = new Intent(context, AuthActivity.class);
                i.putExtras(intent.getExtras());

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        i, PendingIntent.FLAG_UPDATE_CURRENT);

                //research more on notifications the how to display them
                //https://developer.android.com/guide/topics/ui/notifiers/notifications
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_chat_notification)
                        .setContentTitle("Message from: " + message.getSender())
                        .setContentText(message.getMessage())
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);

                // Automatically configure a ChatMessageNotification Channel for devices running Android O+
                Pushy.setNotificationChannel(builder, context);

                // Get an instance of the NotificationManager service
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                // Build the notification and display it
                notificationManager.notify(1, builder.build());
            }
        }
        if (typeOfMessage.equals("request")){
            try{
                senderEmail = intent.getStringExtra("senderEmail");
                senderid = intent.getIntExtra("senderid",-1);
                receiverid = intent.getIntExtra("receiver",-1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);

            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
                //app is in the foreground so send the message to the active Activities
                Log.d("PUSHY", "Request received in foreground: " + senderEmail);

                //create an Intent to broadcast a message to other parts of the app.
                Intent i = new Intent(RECEIVED_NEW_REQUEST);
                i.putExtra("senderEmail", senderEmail);
                i.putExtra("senderid", senderid);
                i.putExtra("receiverid", receiverid);
                i.putExtras(intent.getExtras());

                context.sendBroadcast(i);

            } else {
                //app is in the background so create and post a notification
                Log.d("PUSHY", "Request received in background: " + senderEmail);

                Intent i = new Intent(context, AuthActivity.class);
                i.putExtras(intent.getExtras());

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        i, PendingIntent.FLAG_UPDATE_CURRENT);

                //research more on notifications the how to display them
                //https://developer.android.com/guide/topics/ui/notifiers/notifications
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_contact_icon_24dp)
                        .setContentTitle("You got new friend request!")
                        .setContentText(senderEmail+" sent friend request")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);

                // Automatically configure a ChatMessageNotification Channel for devices running Android O+
                Pushy.setNotificationChannel(builder, context);

                // Get an instance of the NotificationManager service
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                // Build the notification and display it
                notificationManager.notify(1, builder.build());
            }
        }
        if (typeOfMessage.equals("accept")){
            try{
                acceptorEmail = intent.getStringExtra("acceptorEmail");
                acceptorid = intent.getIntExtra("acceptorid",-1);
                receiverid = intent.getIntExtra("receiver",-1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);

            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
                //app is in the foreground so send the message to the active Activities
                Log.d("PUSHY", "Request received in foreground: " + acceptorEmail);

                //create an Intent to broadcast a message to other parts of the app.
                Intent i = new Intent(RECEIVED_NEW_ACCEPT);
                i.putExtra("acceptorEmail", acceptorEmail);
                i.putExtra("acceptorid", acceptorid);
                i.putExtra("receiverid", receiverid);
                i.putExtras(intent.getExtras());
                context.sendBroadcast(i);

            } else {
                //app is in the background so create and post a notification
                Log.d("PUSHY", "Request received in background: " + acceptorEmail);

                Intent i = new Intent(context, AuthActivity.class);
                i.putExtras(intent.getExtras());

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        i, PendingIntent.FLAG_UPDATE_CURRENT);

                //research more on notifications the how to display them
                //https://developer.android.com/guide/topics/ui/notifiers/notifications
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_contact_icon_24dp)
                        .setContentTitle("You have a new friend!")
                        .setContentText(acceptorEmail+" accept your friend request")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent);

                // Automatically configure a ChatMessageNotification Channel for devices running Android O+
                Pushy.setNotificationChannel(builder, context);

                // Get an instance of the NotificationManager service
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                // Build the notification and display it
                notificationManager.notify(1, builder.build());
            }
        }
    }
}