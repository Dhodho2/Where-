package rdsol.whereat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import rdsol.whereat.R;
import rdsol.whereat.adapters.recyclerview.OtherUsersRecyclerViewAdapter;
import rdsol.whereat.customeviews.GridSpacingItemDecoration;
import rdsol.whereat.customeviews.MyRecyclerItemClickListener;
import rdsol.whereat.database.room.entities.AccountChannel;
import rdsol.whereat.processes.GetOtherUsersProcess;

public class ShareLinkToDrawerFragment extends BottomSheetDialogFragment implements GetOtherUsersProcess.HttpReqGetOtherUsers {
    private RecyclerView recyclerview;
    private GetOtherUsersProcess getOtherUsersProcess;
    private List<AccountChannel> data = new ArrayList<>();
    private List<AccountChannel> searchArr = new ArrayList<>();
    private View vw;
    private OtherUsersRecyclerViewAdapter adapter;
    public static AccountChannel SELECTED_USER_OBJECT = null;
    private Fragment parent;
    public interface OnFragmentInteractionListener {
        void setDataSeleted ( AccountChannel selectedItemUsers );
    }
    private OnFragmentInteractionListener onFragmentInteractionListener;

    public static ShareLinkToDrawerFragment newInstance ( ) {
        return new ShareLinkToDrawerFragment();
    }
    @Override
    public void onAttach ( @NonNull Context context ) {
        super.onAttach( context );
         parent = getParentFragment();

    }
    @Override
    public void onCreate ( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        getOtherUsersProcess = new GetOtherUsersProcess( ShareLinkToDrawerFragment.this );

    }

    private EditText search_;

    @Nullable
    @Override
    public View onCreateView ( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        vw = inflater.inflate( R.layout.bottom_navigation_drawer, container, false );
        recyclerview = vw.findViewById( R.id.recyclerview );
        recyclerview.setVisibility( View.VISIBLE );
        recyclerview.setHasFixedSize( true );
       EditText search = vw.findViewById( R.id.search );
        search.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged ( CharSequence charSequence, int i, int i2, int i3 ) {
            }

            @Override
            public void onTextChanged ( CharSequence charSequence, int i, int i2, int i3 ) {

                if ( search.getText().toString().isEmpty() ) {return;}
                searchArr.clear();
                    for ( AccountChannel channel : data  ) {

                        if ( !search.getText().toString().isEmpty() ) {
                            if ( channel.getFull_name().contains( search.getText().toString()   )  ||channel.getUsername().contains( search.getText().toString()   )  ) {
                                searchArr.add( channel );
                            }
                        }
                    }

                    adapter = new OtherUsersRecyclerViewAdapter( searchArr, getContext() );

                    recyclerview.setAdapter( adapter );

            }

            @Override
            public void afterTextChanged ( Editable editable ) {

            }
        } );


        adapter = new OtherUsersRecyclerViewAdapter( data, getContext() );
        int spanCount = 4; // columns
        int spacing = 20; // px
        boolean includeEdge = true;
        recyclerview.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerview.setAdapter( adapter );

        getOtherUsersProcess.execute();

        recyclerview.addOnItemTouchListener( new MyRecyclerItemClickListener( getContext(), ( view, position ) -> {

dismiss();

            SELECTED_USER_OBJECT = search.getText().toString().isEmpty() ?  data.get( position ): searchArr.get( position );
           // onFragmentInteractionListener.setDataSeleted( SELECTED_USER_OBJECT );
            if ( parent instanceof CreditPaymentCardPayment ) {
                CreditPaymentCardPayment creditPaymentCardPayment = (CreditPaymentCardPayment) parent;
                creditPaymentCardPayment.setSelectectedShar(SELECTED_USER_OBJECT);
            }
        } ) );
        return vw;
    }

    @Override
    public void onFinnishedFoundResults ( List<AccountChannel> list ) {
       /// Log.e( "xxxx", "onFinnishedFoundResults:gggggg " + list );
        data = list;

        adapter = new OtherUsersRecyclerViewAdapter( data, getContext() );

        recyclerview.setAdapter( adapter );
    }
}
