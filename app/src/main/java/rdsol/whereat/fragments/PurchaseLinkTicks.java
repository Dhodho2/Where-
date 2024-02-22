package rdsol.whereat.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.manojbhadane.PaymentCardView;

import rdsol.whereat.R;

import static rdsol.whereat.adapters.recyclerview.HomeFeedRecyclerViewAdapter.SelectedOBJECT_;

public class PurchaseLinkTicks extends Fragment{
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView ( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View root = inflater.inflate( R.layout.fragment_get_credits, container, false );
        TextView showTitle = root.findViewById( R.id.showTitle );
        MaterialButton btnPay = root.findViewById( R.id.btnPay );
        progressDialog = new ProgressDialog( getContext() );

        TextView showDate = root.findViewById( R.id.showDate );
        TextView showCost = root.findViewById( R.id.showCost );

        TextView showOwner = root.findViewById( R.id.showOwner );
        if ( SelectedOBJECT_ != null ) {



            showCost.setText( "R " + SelectedOBJECT_.getPrice() );

            showDate.setText( SelectedOBJECT_.getOndate()  + " @ "+ SelectedOBJECT_.getStart_end_time().replace( "=", " " ));

            showOwner.setText( "(@" +SelectedOBJECT_.getAccount_name() + ") " +SelectedOBJECT_.getAccount_full_name() );

            showTitle.setText( "R " + SelectedOBJECT_.getPrice() + " For "+SelectedOBJECT_.getTitle() );

        }
        btnPay.setOnClickListener( v->{
            showProgressDialog( true );
        } );

        // SelectedOBJECT_
        return root;
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data ) {

        super.onActivityResult( requestCode, resultCode, data );
    }

    private void showProgressDialog ( final boolean isToShow ) {

        if ( isToShow ) {
            if ( !progressDialog.isShowing() ) {
                progressDialog.setMessage( "Processing.Link Tick.." );
                progressDialog.setCancelable( true );
                progressDialog.show();
            }
        } else {
            if ( progressDialog.isShowing() ) {
                progressDialog.dismiss();
            }
        }

    }

}
