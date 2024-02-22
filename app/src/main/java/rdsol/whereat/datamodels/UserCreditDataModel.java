package rdsol.whereat.datamodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import rdsol.whereat.database.RoomDB;
import rdsol.whereat.database.room.entities.DBBankDetails;
import rdsol.whereat.processes.GetCreditDataProcess;

public class UserCreditDataModel extends AndroidViewModel implements GetCreditDataProcess.HttpGetCreditData {
    private final DBBankDetails.DBBankDetailsDOA liveData;
    private RoomDB roomDB;

    public UserCreditDataModel ( @NonNull Application application ) {
        super( application );
        roomDB = RoomDB.getRoomInstance( application );

        liveData = roomDB.getTableDBBankDetailsData();
       getData();
    }


    public DBBankDetails.DBBankDetailsDOA getLiveData ( ) {
        return liveData;
    }


    public void getData ( ) {
        GetCreditDataProcess getHomeFeedProcess = new GetCreditDataProcess( UserCreditDataModel.this );
        getHomeFeedProcess.execute();
    }



    @Override
    public void onResultCompletedCall ( Object object ) {
        DBBankDetails obj = (DBBankDetails) object;
        if ( obj != null ) {
            liveData.clearTable();
            liveData.save( obj );
        }
    }
}