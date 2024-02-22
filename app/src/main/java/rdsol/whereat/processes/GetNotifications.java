package rdsol.whereat.processes;

import android.os.AsyncTask;

import java.util.List;

import rdsol.whereat.database.room.entities.DBNotificationsItems;

import static rdsol.whereat.netwox.HandleRequests.getMyNotifications;

public class GetNotifications extends AsyncTask<String,Void, List<DBNotificationsItems>> {

    private HttpgetNotifications httpgetNotifications;

    public GetNotifications ( HttpgetNotifications httpgetNotifications ) {
        this.httpgetNotifications = httpgetNotifications;
    }

    @Override
    protected List<DBNotificationsItems> doInBackground ( String... strings ) {
        return getMyNotifications();
    }

    @Override
    protected void onPostExecute ( List<DBNotificationsItems> items ) {
        super.onPostExecute( items );
        httpgetNotifications.onDoneResultNotifications( items );
    }

    public interface HttpgetNotifications{
        void onDoneResultNotifications(List<DBNotificationsItems> items);
    }
}
