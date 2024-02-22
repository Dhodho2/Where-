package rdsol.whereat.services.others;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import rdsol.whereat.activities.ViewWatchRead;

import static rdsol.whereat.utils.NotificationsUtils.showNotification;


public class ShowReminderService extends BroadcastReceiver {

    @Override
    public void onReceive ( Context context, Intent intent ) {
        showNotification(context, ViewWatchRead.class,"Show About to Start" ,"Open Now !" );
    }
}
