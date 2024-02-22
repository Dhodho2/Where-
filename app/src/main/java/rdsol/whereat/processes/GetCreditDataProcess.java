package rdsol.whereat.processes;

import android.os.AsyncTask;

import static rdsol.whereat.netwox.HandleRequests.getUserBalanceDetails;

public class GetCreditDataProcess extends AsyncTask<Void,Void,Object> {
 // get-user-account-details
    private HttpGetCreditData httpGetCreditData;

    public GetCreditDataProcess ( HttpGetCreditData httpGetCreditData ) {
        this.httpGetCreditData = httpGetCreditData;
    }

    @Override
    protected Object doInBackground ( Void... voids ) {
        return getUserBalanceDetails();
    }

    @Override
    protected void onPostExecute ( Object s ) {
        super.onPostExecute( s );
        httpGetCreditData.onResultCompletedCall( s );
    }

    public interface HttpGetCreditData {
        void onResultCompletedCall(Object object);
    }
}
