package rdsol.whereat.utils;

import android.content.Context;

import java.util.UUID;

public class Installation {
    private static String sID = "";
    private static final String INSTALLATION = "INSTALLATION";

    public synchronized static String installationID(Context context) {
        long time = System.currentTimeMillis();
        sID = UUID.randomUUID().toString() + "::" + time;


        return sID;
    }


}
