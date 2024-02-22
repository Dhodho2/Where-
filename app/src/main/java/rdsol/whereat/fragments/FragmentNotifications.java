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
import rdsol.whereat.adapters.recyclerview.NotificationsRecyclerViewAdapter;
import rdsol.whereat.customeviews.RecyclerViewItemSeparator;
import rdsol.whereat.datamodels.NotificationViewModel;

public class FragmentNotifications extends Fragment {
    private NotificationsRecyclerViewAdapter adapter;
    private RecyclerView recyclerview;
    NotificationViewModel homeViewModel;

    public View onCreateView ( @NonNull LayoutInflater inflater,
                               ViewGroup container, Bundle savedInstanceState ) {

        View root = inflater.inflate( R.layout.fragment_notification, container, false );
        recyclerview = root.findViewById( R.id.recyclerview );
        recyclerview.addItemDecoration( new RecyclerViewItemSeparator( 15 ) );
        recyclerview.setHasFixedSize( true );
        homeViewModel = new ViewModelProvider( this ).get( NotificationViewModel.class );
        homeViewModel.getLiveData().getAll().observe( getViewLifecycleOwner(), datalist -> {


                adapter = new NotificationsRecyclerViewAdapter( datalist, getContext() );
                recyclerview.setAdapter( adapter );


        } );
        homeViewModel.getData();
        return root;
    }

}
