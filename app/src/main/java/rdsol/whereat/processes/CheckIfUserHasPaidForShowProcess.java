package rdsol.whereat.processes;

import android.os.AsyncTask;

import static rdsol.whereat.netwox.HandleRequests.checkIfUserHasPaidForShow;

public class CheckIfUserHasPaidForShowProcess extends AsyncTask<String,Void,String> {
    private HttpGetShowPaymentCheck httpGetShowPaymentCheck;

    public CheckIfUserHasPaidForShowProcess ( HttpGetShowPaymentCheck httpGetShowPaymentCheck ) {
        this.httpGetShowPaymentCheck = httpGetShowPaymentCheck;
    }

    @Override
    protected String doInBackground ( String... strings ) {
        return checkIfUserHasPaidForShow(strings[0]) ;
    }

    @Override
    protected void onPostExecute ( String s ) {
        super.onPostExecute( s );
        httpGetShowPaymentCheck.onResultFinnished(  s);
    }

    public interface HttpGetShowPaymentCheck {
        void onResultFinnished(String booleanString);
    }
}
