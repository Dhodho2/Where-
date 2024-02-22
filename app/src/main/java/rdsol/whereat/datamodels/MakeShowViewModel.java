package rdsol.whereat.datamodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MakeShowViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MakeShowViewModel ( ) {
        mText = new MutableLiveData<>();
        mText.setValue( "This is dashboard fragment" );
    }

    public LiveData<String> getText ( ) {
        return mText;
    }
}