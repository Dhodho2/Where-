package rdsol.whereat.processes;

import android.os.AsyncTask;

import rdsol.whereat.callbacks.UpdateLoginCalls;

public class UpdateLoginProcess extends AsyncTask<String,Void,String> {
    private UpdateLoginCalls updateLoginCalls;

    public UpdateLoginProcess ( UpdateLoginCalls updateLoginCalls ) {
        this.updateLoginCalls = updateLoginCalls;
    }

    @Override
    protected String doInBackground ( String... strings ) {
        try {
            Thread.sleep( 6_000 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute ( String s ) {
        super.onPostExecute( s );
        updateLoginCalls.onUpdateLoginResponse( s );
    }
}
