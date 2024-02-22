package rdsol.whereat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import rdsol.whereat.R;
import rdsol.whereat.adapters.recyclerview.HomeFeedRecyclerViewAccountsAdapter;
import rdsol.whereat.adapters.recyclerview.HomeFeedRecyclerViewAdapter;
import rdsol.whereat.adapters.recyclerview.ReyclerViewBillboard;
import rdsol.whereat.customeviews.RecyclerViewItemSeparator;
import rdsol.whereat.database.room.entities.AccountChannel;
import rdsol.whereat.pojos.PojoBillBoardData;

public class FragmentBillBoards extends Fragment {
    private RecyclerView recycler_View;
    private ReyclerViewBillboard adapterFeed;
    private List<PojoBillBoardData> dataFeed = new ArrayList<>();
    public static FragmentBillBoards getInstance ( ) {

        return new FragmentBillBoards();
    }

    public View onCreateView (@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState ) {
        View root = inflater.inflate( R.layout.fragment_billboard, container, false );

        recycler_View = root.findViewById( R.id.recycler_View );
        recycler_View.setHasFixedSize( true );
        recycler_View.addItemDecoration( new RecyclerViewItemSeparator( 15 ) );
        adapterFeed = new ReyclerViewBillboard( dataFeed, getContext() );
        recycler_View.setAdapter(adapterFeed);
        return root;
    }
}
