package rdsol.whereat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.List;

import id.ionbit.ionalert.IonAlert;
import rdsol.whereat.R;
import rdsol.whereat.activities.GeneralFragmentHandlerActivity;
import rdsol.whereat.activities.VideoStreamActivity;
import rdsol.whereat.adapters.recyclerview.HomeFeedRecyclerViewAdapter;
import rdsol.whereat.customeviews.RecyclerViewItemSeparator;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.database.room.entities.DBHomeFeed;
import rdsol.whereat.datamodels.HomeViewModel;
import rdsol.whereat.processes.GenarateStreamingLinkProcess;

import static rdsol.whereat.activities.GeneralFragmentHandlerActivity.Schedule_Show;
import static rdsol.whereat.activities.ViewWatchRead.MEDIA_ID_ROW;

public class MakeShowFragment extends Fragment implements GenarateStreamingLinkProcess.HttpGetStreamingData {
    View root;

    public static MakeShowFragment getInstance ( ) {

        return new MakeShowFragment();
    }
private RecyclerView recycler_View_main;
    private HomeFeedRecyclerViewAdapter adapterFeed;
    private List<DBHomeFeed> items = new ArrayList<>();
    private MyPreferences myPreferences;
    private HomeViewModel homeViewModel;
   private IonAlert ionAlert; private MaterialButton schedule_show;
    @Override
    public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        setHasOptionsMenu( false );

    }

    public View onCreateView ( @NonNull LayoutInflater inflater,                               ViewGroup container, Bundle savedInstanceState ) {
        root = inflater.inflate( R.layout.fragment_dashboard, container, false );
        myPreferences = new MyPreferences( getContext() );
        MaterialButton start_stream = root.findViewById( R.id.start_stream );
         schedule_show = root.findViewById( R.id.schedule_show );

        recycler_View_main = root.findViewById( R.id.schedule_showList );
        recycler_View_main.setHasFixedSize( true );
        int spanCount = 2; // columns
        int spacing = 26; // px
        boolean includeEdge = true;
       // recycler_View_main.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recycler_View_main.addItemDecoration( new RecyclerViewItemSeparator( 15 ) );
        MEDIA_ID_ROW = "";

        schedule_show.setOnClickListener( ev->{
            startActivity( new Intent( getContext() , GeneralFragmentHandlerActivity.class ) .putExtra("fragment_name" ,"Schedule Show" ));
        } );
        start_stream.setOnClickListener( ev->{

            ionAlert = new IonAlert( getContext(), IonAlert.PROGRESS_TYPE );
            ionAlert.setSpinKit( "WanderingCubes" );
            ionAlert.showCancelButton( false );
            ionAlert.show();
            GenarateStreamingLinkProcess genarateStreamingLinkProcess = new GenarateStreamingLinkProcess( MakeShowFragment.this );
            genarateStreamingLinkProcess.execute(  );

        } );

        homeViewModel = new ViewModelProvider( this ).get( HomeViewModel.class );
        homeViewModel.getLiveData().getAllScheduledOnly().observe( getViewLifecycleOwner(), datalist -> {
            items = datalist;
            adapterFeed = new HomeFeedRecyclerViewAdapter( datalist, getContext(),MakeShowFragment.this );
            recycler_View_main.setAdapter( adapterFeed );


        } );
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!myPreferences.getAccountBusiness()){
            schedule_show.setVisibility( View.GONE );
        }else{
            schedule_show.setVisibility( View.VISIBLE );
        }
        Log.e("xxvvvvvvccccxxxxx", "onResume: "+myPreferences.getAccountBusiness() );
    }

    @Override
    public void onDoneGettingStreamingLink ( String[] response ) {
        if(ionAlert != null){
            ionAlert.dismiss();
        }
        if(response == null){
            DynamicToast.makeError( getContext(),"Failed to Create Link Please Try again !" ).show();

        }else{
            String gen_key = response[0];
            MEDIA_ID_ROW = response[1];
            startActivity( new Intent( getContext() , VideoStreamActivity.class )
                    .putExtra( "stream","yes" )
                    .putExtra( "id_media",MEDIA_ID_ROW )
                    .putExtra( "gen_key",gen_key )
                    .putExtra("fragment_name" ,Schedule_Show ));
        }
    }
}
