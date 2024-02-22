package rdsol.whereat.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rdsol.whereat.R;
import rdsol.whereat.adapters.recyclerview.HomeFeedRecyclerViewAccountsAdapter;
import rdsol.whereat.adapters.recyclerview.HomeFeedRecyclerViewAdapter;
import rdsol.whereat.callbacks.HttpGetAccountHomeFeedData;
import rdsol.whereat.customeviews.RecyclerViewHorizontalItemSeparator;
import rdsol.whereat.customeviews.RecyclerViewItemSeparator;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.database.room.entities.AccountChannel;
import rdsol.whereat.database.room.entities.DBHomeFeed;
import rdsol.whereat.datamodels.HomeViewModel;
import rdsol.whereat.processes.HomeAccountProcess;

public class HomeFragment extends Fragment implements HttpGetAccountHomeFeedData, SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout swiperootView;
    private AAH_CustomRecyclerView recycler_View_main;
    private RecyclerView recycler_View_accounts;
    private HomeFeedRecyclerViewAdapter adapterFeed;
    private HomeFeedRecyclerViewAccountsAdapter adapterAccFeed;
    private HomeAccountProcess homeAccountProcess;
    private List<DBHomeFeed> items = new ArrayList<>();
    private SearchView searchView;
    private MyPreferences myPreferences;
    private HomeViewModel homeViewModel;
    private SearchView.OnQueryTextListener onQueryTextListener;

    public static HomeFragment getInstance ( ) {

        return new HomeFragment();
    }

    @Override
    public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        setHasOptionsMenu( true );
        myPreferences = new MyPreferences( getContext() );
    }

    private List<AccountChannel> dataAcc = new ArrayList<>();

    public View onCreateView ( @NonNull LayoutInflater inflater,
                               ViewGroup container, Bundle savedInstanceState ) {

        View root = inflater.inflate( R.layout.fragment_home, container, false );

        swiperootView = root.findViewById( R.id.swiperootView );
        recycler_View_accounts = root.findViewById( R.id.recycler_View_accounts );
        recycler_View_main = root.findViewById( R.id.recycler_View_main );
        recycler_View_accounts.setHasFixedSize( true );
        recycler_View_main.setHasFixedSize( true );

        adapterAccFeed = new HomeFeedRecyclerViewAccountsAdapter( dataAcc, getContext() );
        recycler_View_accounts.setAdapter( adapterAccFeed );
        recycler_View_main.addItemDecoration( new RecyclerViewItemSeparator( 15 ) );
        recycler_View_accounts.addItemDecoration( new RecyclerViewHorizontalItemSeparator( 7 ) );

        swiperootView.setRefreshing( true );
        swiperootView.setColorSchemeColors( DynamicColorUtils.getRandomColor(), DynamicColorUtils.getRandomColor(), DynamicColorUtils.getRandomColor(), DynamicColorUtils.getRandomColor() );
        swiperootView.setOnRefreshListener( this );
        recycler_View_main.setActivity( getActivity() );
        recycler_View_main.setDownloadVideos( true );
        recycler_View_main.setDownloadPath( Environment.getExternalStorageDirectory()+"/where-at-app" );
        homeViewModel = new ViewModelProvider( this ).get( HomeViewModel.class );
        homeViewModel.getLiveData().getAll().observe( getViewLifecycleOwner(), datalist -> {
            items = datalist;
            List<String > urls = new ArrayList<>(  );
            for ( DBHomeFeed feed:datalist ){
                if(feed.getVideo_url().endsWith( ".mp4" )){
                    urls.add( feed.getVideo_url() );
                }

            }
            adapterFeed = new HomeFeedRecyclerViewAdapter( datalist, getContext(),HomeFragment.this );
            recycler_View_main.setAdapter( adapterFeed );
            recycler_View_main.preDownload( urls );

        } );
       // toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        onRefresh();

        return root;
    }

    @Override
    public void onPause ( ) {
        super.onPause();
        recycler_View_main.stopVideos();
    }

    @Override
    public void onCompleteResponse ( List<AccountChannel> objects ) {
        dataAcc = objects;

        adapterAccFeed = new HomeFeedRecyclerViewAccountsAdapter( dataAcc, getContext() );
        recycler_View_accounts.setAdapter( adapterAccFeed );
        recycler_View_accounts.setVisibility( View.VISIBLE );
        swiperootView.setRefreshing( false );
        for ( int i = 0; i < objects.size(); i++ ) {
            AccountChannel it = objects.get( i );

            if(it.getUser_id() .equals( myPreferences.getUserId() )){
                Collections.swap( dataAcc,i,0 );
                Collections.swap( objects,i,0 );
                adapterAccFeed.notifyItemMoved( i,0 );
                break;
            }
        }

    }

    @Override
    public void onRefresh ( ) {
        swiperootView.setRefreshing( true );

        homeViewModel.getHomeFeedData();
        homeAccountProcess = new HomeAccountProcess( HomeFragment.this );
        homeAccountProcess.execute();
    }

    @Override
    public void onCreateOptionsMenu ( @NonNull Menu menu, @NonNull MenuInflater inflater ) {
        super.onCreateOptionsMenu( menu, inflater );

        MenuItem searchItem = menu.findItem( R.id.action_search );
        SearchManager searchManager = ( SearchManager ) getActivity().getSystemService( Context.SEARCH_SERVICE );
        if ( searchItem != null ) {
            searchView = ( SearchView ) searchItem.getActionView();

        }
        if ( searchView != null ) {

            searchView.setSearchableInfo( searchManager.getSearchableInfo( getActivity().getComponentName() ) );

            onQueryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit ( String query ) {
                    //  Log.e( "xxxx", "onQueryTextSubmit: " +query  );
                    return true;
                }

                @Override
                public boolean onQueryTextChange ( String newText ) {
                    if ( newText.isEmpty() ) {
                        adapterFeed = new HomeFeedRecyclerViewAdapter( items, getContext() ,HomeFragment.this);
                        recycler_View_main.setAdapter( adapterFeed );

                        return false;
                    }
                    List<DBHomeFeed> search = new ArrayList<>();
                    Log.e( "xxxx", "onQueryTextChange: " + newText );
                    swiperootView.setRefreshing( true );
                    for ( DBHomeFeed item :
                            items ) {
                        if ( item.getTitle().contains( newText ) ) {
                            search.add( item );
                        }
                    }
                    adapterFeed = new HomeFeedRecyclerViewAdapter( search, getContext() ,HomeFragment.this);
                    recycler_View_main.setAdapter( adapterFeed );
                    swiperootView.setRefreshing( false );
                    return false;
                }
            };
            searchView.setOnQueryTextListener( onQueryTextListener );
        }
        super.onCreateOptionsMenu( menu, inflater );
    }

    @Override
    public boolean onOptionsItemSelected ( @NonNull MenuItem item ) {
        if ( R.id.action_search == item.getItemId() ) {

        }
        searchView.setOnQueryTextListener( onQueryTextListener );
        return super.onOptionsItemSelected( item );
    }


}
