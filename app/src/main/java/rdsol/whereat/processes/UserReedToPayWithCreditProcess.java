package rdsol.whereat.processes;

import android.os.AsyncTask;

import static rdsol.whereat.netwox.HandleRequests.userPayForShow;

public class UserReedToPayWithCreditProcess extends AsyncTask<String ,Void,String> {
    private HttpMakePaymentReq httpMakePaymentReq;

    public UserReedToPayWithCreditProcess ( HttpMakePaymentReq httpMakePaymentReq ) {
        this.httpMakePaymentReq = httpMakePaymentReq;
    }

    @Override
    protected String doInBackground ( String... strings ) {
        return userPayForShow(strings[0]);
    }

    @Override
    protected void onPostExecute ( String s ) {
        super.onPostExecute( s );
        httpMakePaymentReq.onDoneRequest(s);
    }

    public interface HttpMakePaymentReq{
        void onDoneRequest(String response);
    }
}
