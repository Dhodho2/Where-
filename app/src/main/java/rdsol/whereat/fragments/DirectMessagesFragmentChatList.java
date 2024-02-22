package rdsol.whereat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import rdsol.whereat.R;
import rdsol.whereat.adapters.recyclerview.DirectMessagesRecyclerViewAdapter;
import rdsol.whereat.customeviews.RecyclerViewItemSeparator;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.datamodels.DirectMesagesModel;

public class DirectMessagesFragmentChatList extends Fragment {
    private MyPreferences myPreferences;
    private DirectMessagesRecyclerViewAdapter adapter;
    View root;

    public View onCreateView ( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        root = inflater.inflate( R.layout.fragment_directmessage_chatlist, container, false );
        myPreferences = new MyPreferences( getContext() );
        RecyclerView recyclerView = root.findViewById( R.id.recyclerview );
        recyclerView.addItemDecoration( new RecyclerViewItemSeparator( 15 ) );
        DirectMesagesModel hwModel = new ViewModelProvider( this ).get( DirectMesagesModel.class );
        hwModel.getLiveData().getAll(myPreferences.getUserId()).observe( getViewLifecycleOwner(), datalist -> {

            adapter = new DirectMessagesRecyclerViewAdapter( datalist, getContext() );
            recyclerView.setAdapter( adapter );


        } );


        return root;
    }


}
