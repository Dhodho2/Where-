package rdsol.whereat.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.manojbhadane.PaymentCardView;
import com.mastercard.gateway.android.sdk.Gateway;
import com.mastercard.gateway.android.sdk.Gateway3DSecureCallback;
import com.mastercard.gateway.android.sdk.GatewayCallback;
import com.mastercard.gateway.android.sdk.GatewayMap;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.pranavpandey.android.dynamic.utils.DynamicTaskUtils;

import java.util.UUID;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import id.ionbit.ionalert.IonAlert;
import rdsol.whereat.R;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.database.room.entities.AccountChannel;
import rdsol.whereat.datamodels.UserCreditDataModel;
import rdsol.whereat.payments.ApiController;
import rdsol.whereat.payments.Config;
import rdsol.whereat.processes.GetTokenShareCreditProcess;
import rdsol.whereat.processes.ShareLinkTicksWithOtherUser;
import rdsol.whereat.utils.TextUtil;

import static rdsol.whereat.activities.GeneralFragmentHandlerActivity.URL_INTENT;
import static rdsol.whereat.netwox.HandleRequests.API_STORAGE;
import static rdsol.whereat.utils.AndroidUtilities.simpleAlertButtonsWithLayout;
import static rdsol.whereat.utils.MediaUtils.accomplishedSoundEffect;

public class CreditPaymentCardPayment extends Fragment implements ShareLinkToDrawerFragment.OnFragmentInteractionListener, ShareLinkTicksWithOtherUser.HttpShareLinkTicksToTothers, GetTokenShareCreditProcess.getSharedCreditLink {
    private ProgressDialog progressDialog;
    private MyPreferences myPreferences;
    static final int REQUEST_CARD_INFO = 100;

    // static for demo
    static final String AMOUNT = "1.00";
    static final String CURRENCY = "USD";
   private IonAlert ionAlert;
    Gateway gateway;
    String sessionId = "", apiVersion, threeDSecureId, orderId, transactionId;
    boolean isGooglePay = false;
    ApiController apiController = ApiController.getInstance();
private GetTokenShareCreditProcess getTokenShareCreditProcess ;
    @Override
    public void onCreate ( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        // init api controller
        apiController.setMerchantServerUrl( Config.MERCHANT_URL.getValue( getContext() ) );

        // init gateway
        gateway = new Gateway();
        gateway.setMerchantId( Config.MERCHANT_ID.getValue( getContext() ) );
        try {
            Gateway.Region region = Gateway.Region.valueOf( Config.REGION.getValue( getContext() ) );
            gateway.setRegion( region );
        } catch ( Exception e ) {
            //  Log.e(ProcessPaymentActivity.class.getSimpleName(), "Invalid Gateway region value provided", e);
        }

        // random order/txn IDs for example purposes
        orderId = UUID.randomUUID().toString();
        orderId = orderId.substring( 0, orderId.indexOf( '-' ) );
        transactionId = UUID.randomUUID().toString();
        transactionId = transactionId.substring( 0, transactionId.indexOf( '-' ) );

    }

    void setSelectectedShar ( AccountChannel selected ) {
        if ( selected == null ) {
            return;
        }

        NumberPicker picker;
        MaterialNumberPicker.Builder numberPickerBuilder = new MaterialNumberPicker.Builder( getContext() );
        numberPickerBuilder
                .minValue( 1 )
                .maxValue( 20 )
                .defaultValue( 2 )
                .separatorColor( ContextCompat.getColor( getContext(), R.color.colorAccent ) )
                .textColor( ContextCompat.getColor( getContext(), R.color.colorPrimary ) )
                .textSize( 18 )
                .formatter( value -> value + " Link Ticks " );

        picker = numberPickerBuilder.build();
        simpleAlertButtonsWithLayout( getContext(), "Select Number of Ticks", "", ( dialog, which ) -> {
            IonAlert h = new IonAlert( getContext(), IonAlert.NORMAL_TYPE );
            h.setTitleText( "Share Link Tick" );
            h.setContentText( "You wish to share " + picker.getValue() + " Link Ticks To " + selected.getFull_name() );
            h.setConfirmText( "Yes,Share!" );

            h.setConfirmClickListener( sDialog -> {
                h.dismiss();
                ionAlert = new IonAlert( getContext(), IonAlert.PROGRESS_TYPE );
                ionAlert.setSpinKit( "WanderingCubes" );
                ionAlert.showCancelButton( false );
                ionAlert.show();
                ShareLinkTicksWithOtherUser shareLinkTicksWithOtherUser = new ShareLinkTicksWithOtherUser( CreditPaymentCardPayment.this );
                shareLinkTicksWithOtherUser.execute( picker.getValue() + "", selected.getUser_id() );
            } );
            // .setConfirmTextSize(12)
            h.showCancelButton( true );
            h.show();
        }, null, "Use", "cancel", picker );


        /* */


    }

    private TextView linkTicks;
    private ShareLinkToDrawerFragment mBottomNavigationDrawerFragment;

    @Nullable
    @Override
    public View onCreateView ( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View root = inflater.inflate( R.layout.fragment_creditcard_payment, container, false );
        TextView showTitle = root.findViewById( R.id.showTitle );
        linkTicks = root.findViewById( R.id.linkTicks );
        PaymentCardView btnPay = root.findViewById( R.id.btnPay );
        MaterialButton shareBtn = root.findViewById( R.id.shareBtn );
        MaterialButton shareBtnLinkText = root.findViewById( R.id.shareBtnLinkText );
        progressDialog = new ProgressDialog( getContext() );
        myPreferences = new MyPreferences( getContext() );
        linkTicks.setText( myPreferences.getLastSetPointsBalance() );
        mBottomNavigationDrawerFragment = ShareLinkToDrawerFragment.newInstance();


        shareBtnLinkText.setOnClickListener( ev -> {
            NumberPicker picker;
            MaterialNumberPicker.Builder numberPickerBuilder = new MaterialNumberPicker.Builder( getContext() );
            numberPickerBuilder
                    .minValue( 1 )
                    .maxValue( 20 )
                    .defaultValue( 2 )
                    .separatorColor( ContextCompat.getColor( getContext(), R.color.colorAccent ) )
                    .textColor( ContextCompat.getColor( getContext(), R.color.colorPrimary ) )
                    .textSize( 18 )
                    .formatter( value -> value + " Link Ticks " );

            picker = numberPickerBuilder.build();
            simpleAlertButtonsWithLayout( getContext(), "Select Number of Ticks", "", ( dialog, which ) -> {
                IonAlert h = new IonAlert( getContext(), IonAlert.NORMAL_TYPE );
                h.setTitleText( "Share Link Tick" );
                h.setContentText( "You wish to share " + picker.getValue() + " Link Ticks To Via a Link ?"  );
                h.setConfirmText( "Yes,Share!" );

                h.setConfirmClickListener( sDialog -> {
                    h.dismiss();
                    ionAlert = new IonAlert( getContext(), IonAlert.PROGRESS_TYPE );
                    ionAlert.setSpinKit( "WanderingCubes" );
                    ionAlert.showCancelButton( false );
                    ionAlert.show();
                    getTokenShareCreditProcess = new GetTokenShareCreditProcess( CreditPaymentCardPayment.this );
                    getTokenShareCreditProcess.execute( picker.getValue()+"" );
                } );
                // .setConfirmTextSize(12)
                h.showCancelButton( true );
                h.show();
            }, null, "Yes,Proceed", "cancel", picker );

        } );
        shareBtn.setOnClickListener( ev -> {
            mBottomNavigationDrawerFragment = ShareLinkToDrawerFragment.newInstance();
            mBottomNavigationDrawerFragment.show( getChildFragmentManager(), "tag" );

        } );



        CreditTicksgetBankingData();
        btnPay.setOnPaymentCardEventListener( new PaymentCardView.OnPaymentCardEventListener() {
            @Override
            public void onCardDetailsSubmit ( String cardExpiryMM, String cardExpiryYY, String cardNumber, String cardCvv ) {
                showProgressDialog( true );
                getBankingData();

                check3dsEnrollment();
            }

            @Override
            public void onError ( String error ) {

            }

            @Override
            public void onCancelClick ( ) {

            }
        } );
        // SelectedOBJECT_
        if(!URL_INTENT.isEmpty()){
            ionAlert = new IonAlert( getContext(), IonAlert.PROGRESS_TYPE );
            ionAlert.setSpinKit( "WanderingCubes" );
            ionAlert.showCancelButton( false );
            ionAlert.show();
        }
        return root;
    }

    @Override
    public void onDestroy ( ) {
        super.onDestroy();
        if(getTokenShareCreditProcess != null){
            DynamicTaskUtils.cancelTask(getTokenShareCreditProcess  );
        }
    }

    void collectCardInfo ( ) {
        // binding.collectCardInfoProgress.setVisibility(View.VISIBLE);

       /* Intent i = new Intent(this, CollectCardInfoActivity.class);
        i.putExtra(CollectCardInfoActivity.EXTRA_GOOGLE_PAY_TXN_AMOUNT, AMOUNT);
        i.putExtra(CollectCardInfoActivity.EXTRA_GOOGLE_PAY_TXN_CURRENCY, CURRENCY);

        startActivityForResult(i, REQUEST_CARD_INFO);*/
    }

    void processPayment ( ) {
        // binding.processPaymentProgress.setVisibility(View.VISIBLE);

        apiController.completeSession( sessionId, orderId, transactionId, AMOUNT, CURRENCY, threeDSecureId, isGooglePay, new CompleteSessionCallback() );
    }

    void showResult ( @DrawableRes int iconId, @StringRes int messageId ) {
        //binding.resultIcon.setImageResource(iconId);
        //binding.resultText.setText(messageId);

        //  binding.groupConfirm.setVisibility(View.GONE);
        //  binding.groupResult.setVisibility(View.VISIBLE);
    }

    void check3dsEnrollment ( ) {
        //   binding.check3dsProgress.setVisibility(View.VISIBLE);
        //   binding.confirmButton.setEnabled(false);

        // generate a random 3DSecureId for testing
        String threeDSId = UUID.randomUUID().toString();
        threeDSId = threeDSId.substring( 0, threeDSId.indexOf( '-' ) );

        apiController.check3DSecureEnrollment( sessionId, AMOUNT, CURRENCY, threeDSId, new Check3DSecureEnrollmentCallback() );
    }

    void updateSession ( String name, String number, String expiryMonth, String expiryYear, String cvv ) {
        //  binding.updateSessionProgress.setVisibility(View.VISIBLE);

        // build the gateway request
        GatewayMap request = new GatewayMap()
                .set( "sourceOfFunds.provided.card.nameOnCard", name )
                .set( "sourceOfFunds.provided.card.number", number )
                .set( "sourceOfFunds.provided.card.securityCode", cvv )
                .set( "sourceOfFunds.provided.card.expiry.month", expiryMonth )
                .set( "sourceOfFunds.provided.card.expiry.year", expiryYear );

        gateway.updateSession( sessionId, apiVersion, request, new UpdateSessionCallback() );
    }

    void updateSession ( String paymentToken ) {
        //binding.updateSessionProgress.setVisibility(View.VISIBLE);

        GatewayMap request = new GatewayMap()
                .set( "sourceOfFunds.provided.card.devicePayment.paymentToken", paymentToken );

        gateway.updateSession( sessionId, apiVersion, request, new UpdateSessionCallback() );
    }

    void createSession ( ) {


        apiController.createSession( new CreateSessionCallback() );
    }

    void getBankingData ( ) {
        UserCreditDataModel creditDataModel;
        creditDataModel = new ViewModelProvider( this ).get( UserCreditDataModel.class );
        creditDataModel.getLiveData().getAll().observe( this, datalist -> {
            if ( datalist != null && datalist.size() > 0 ) {

                myPreferences.setLastSetPointsBalance( datalist.get( 0 ).getPoints() );


            }


        } );
    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data ) {

        super.onActivityResult( requestCode, resultCode, data );
    }

    private void showProgressDialog ( final boolean isToShow ) {

        if ( isToShow ) {
            if ( !progressDialog.isShowing() ) {
                progressDialog.setMessage( "Processing.Link Ticks.." );
                progressDialog.setCancelable( true );
                progressDialog.show();
            }
        } else {
            if ( progressDialog.isShowing() ) {
                progressDialog.dismiss();
            }
        }

    }

    @Override
    public void setDataSeleted ( AccountChannel selectedItemUsers ) {
        Log.e( "xxxx", "setDataSeleted:  oooooo" );
    }

    @Override
    public void onDoneSharingCredit ( String response ) {
        if ( ionAlert != null ) {
            ionAlert.dismiss();
        }
        if ( response.equals( "broke" ) ) {
            IonAlert h = new IonAlert( getContext(), IonAlert.ERROR_TYPE );

            h.setContentText( " " );

            // .setConfirmTextSize(12)
            h.showCancelButton( false );
            h.show();
        } else if ( response.equals( "error" ) ) {
            IonAlert h = new IonAlert( getContext(), IonAlert.ERROR_TYPE );
            h.setTitleText( "Failed to share,Please reselect again to share !" );


            // .setConfirmTextSize(12)
            h.showCancelButton( false );
            h.show();
        } else if ( response.equals( "ok" ) ) {
            CreditTicksgetBankingData();
            accomplishedSoundEffect(getContext());
            IonAlert h = new IonAlert( getContext(), IonAlert.SUCCESS_TYPE );
            h.setTitleText( "You have Shared !" );


            // .setConfirmTextSize(12)
            h.showCancelButton( false );
            h.show();
            DynamicToast.makeSuccess( getContext(), "Updating Your Link Ticks Balance" ).show();
        }
    }

    private UserCreditDataModel creditDataModel;

    void CreditTicksgetBankingData ( ) {
        creditDataModel = new ViewModelProvider( this ).get( UserCreditDataModel.class );
        creditDataModel.getLiveData().getAll().observe( getViewLifecycleOwner(), datalist -> {
            if ( datalist != null && datalist.size() > 0 ) {

                myPreferences.setLastSetPointsBalance( datalist.get( 0 ).getPoints() );
                linkTicks.setText( myPreferences.getLastSetPointsBalance() );

            }


        } );
    }

    @Override
    public void onDoneSharebleLink ( String link ) {
        if(ionAlert != null){
            ionAlert.dismiss();
        }
        if(link == null){
            DynamicToast.makeError( getContext() , "Failed to generate Link to share " ).show();
        }else{

            TextUtil.share( getContext(),API_STORAGE+"justify-share?credit="+link.replace( "\"","" ),"Generated Tick Link" );
        }
    }

    class CreateSessionCallback implements ApiController.CreateSessionCallback {
        @Override
        public void onSuccess ( String sessionId, String apiVersion ) {
            Log.i( "CreateSessionTask", "Session established" );
            //binding.createSessionProgress.setVisibility(View.GONE);
            //  binding.createSessionSuccess.setVisibility(View.VISIBLE);

            CreditPaymentCardPayment.this.sessionId = sessionId;
            CreditPaymentCardPayment.this.apiVersion = apiVersion;

            collectCardInfo();
        }

        @Override
        public void onError ( Throwable throwable ) {
            Log.e( CreditPaymentCardPayment.class.getSimpleName(), throwable.getMessage(), throwable );

            //    binding.createSessionProgress.setVisibility(View.GONE);
            //   binding.createSessionError.setVisibility(View.VISIBLE);

            //showResult(R.drawable.failed, R.string.pay_error_unable_to_create_session);
        }
    }

    class UpdateSessionCallback implements GatewayCallback {
        @Override
        public void onSuccess ( GatewayMap response ) {
            Log.i( CreditPaymentCardPayment.class.getSimpleName(), "Successfully updated session" );
          /*  binding.updateSessionProgress.setVisibility(View.GONE);
            binding.updateSessionSuccess.setVisibility(View.VISIBLE);

            binding.startButton.setVisibility(View.GONE);
            binding.groupConfirm.setVisibility(View.VISIBLE);*/
        }

        @Override
        public void onError ( Throwable throwable ) {
            Log.e( CreditPaymentCardPayment.class.getSimpleName(), throwable.getMessage(), throwable );

           /* binding.updateSessionProgress.setVisibility(View.GONE);
            binding.updateSessionError.setVisibility(View.VISIBLE);

            showResult(R.drawable.failed, R.string.pay_error_unable_to_update_session);*/
        }
    }

    class Check3DSecureEnrollmentCallback implements ApiController.Check3DSecureEnrollmentCallback {
        @Override
        public void onSuccess ( GatewayMap response ) {
            int apiVersionInt = Integer.valueOf( apiVersion );
            String threeDSecureId = ( String ) response.get( "gatewayResponse.3DSecureID" );

            String html = null;
            if ( response.containsKey( "gatewayResponse.3DSecure.authenticationRedirect.simple.htmlBodyContent" ) ) {
                html = ( String ) response.get( "gatewayResponse.3DSecure.authenticationRedirect.simple.htmlBodyContent" );
            }

            // for API versions <= 46, you must use the summary status field to determine next steps for 3DS
            if ( apiVersionInt <= 46 ) {
                String summaryStatus = ( String ) response.get( "gatewayResponse.3DSecure.summaryStatus" );

                if ( "CARD_ENROLLED".equalsIgnoreCase( summaryStatus ) ) {
                    Gateway.start3DSecureActivity( getActivity(), html );
                    return;
                }

             /*   binding.check3dsProgress.setVisibility(View.GONE);
                binding.check3dsSuccess.setVisibility(View.VISIBLE);*/
                CreditPaymentCardPayment.this.threeDSecureId = null;

                // for these 2 cases, you still provide the 3DSecureId with the pay operation
                if ( "CARD_NOT_ENROLLED".equalsIgnoreCase( summaryStatus ) || "AUTHENTICATION_NOT_AVAILABLE".equalsIgnoreCase( summaryStatus ) ) {
                    CreditPaymentCardPayment.this.threeDSecureId = threeDSecureId;
                }

                processPayment();
            }

            // for API versions >= 47, you must look to the gateway recommendation and the presence of 3DS info in the payload
            else {
                String gatewayRecommendation = ( String ) response.get( "gatewayResponse.response.gatewayRecommendation" );

                // if DO_NOT_PROCEED returned in recommendation, should stop transaction
                if ( "DO_NOT_PROCEED".equalsIgnoreCase( gatewayRecommendation ) ) {
                  /*  binding.check3dsProgress.setVisibility(View.GONE);
                    binding.check3dsError.setVisibility(View.VISIBLE);*/

                    // showResult(R.drawable.failed, R.string.pay_error_3ds_authentication_failed);
                    return;
                }

                // if PROCEED in recommendation, and we have HTML for 3ds, perform 3DS
                if ( html != null ) {
                    Gateway.start3DSecureActivity( getActivity(), html );
                    return;
                }

                CreditPaymentCardPayment.this.threeDSecureId = threeDSecureId;

                processPayment();
            }
        }

        @Override
        public void onError ( Throwable throwable ) {
            Log.e( CreditPaymentCardPayment.class.getSimpleName(), throwable.getMessage(), throwable );

           /* binding.check3dsProgress.setVisibility(View.GONE);
            binding.check3dsError.setVisibility(View.VISIBLE);*/

            //   showResult(R.drawable.failed, R.string.pay_error_3ds_authentication_failed);
        }
    }

    class ThreeDSecureCallback implements Gateway3DSecureCallback {
        @Override
        public void on3DSecureCancel ( ) {
            showError();
        }

        @Override
        public void on3DSecureComplete ( GatewayMap result ) {
            int apiVersionInt = Integer.valueOf( apiVersion );

            if ( apiVersionInt <= 46 ) {
                if ( "AUTHENTICATION_FAILED".equalsIgnoreCase( ( String ) result.get( "3DSecure.summaryStatus" ) ) ) {
                    showError();
                    return;
                }
            } else { // version >= 47
                if ( "DO_NOT_PROCEED".equalsIgnoreCase( ( String ) result.get( "response.gatewayRecommendation" ) ) ) {
                    showError();
                    return;
                }
            }

          /*  binding.check3dsProgress.setVisibility(View.GONE);
            binding.check3dsSuccess.setVisibility(View.VISIBLE);*/

            CreditPaymentCardPayment.this.threeDSecureId = threeDSecureId;

            processPayment();
        }

        void showError ( ) {
            /*binding.check3dsProgress.setVisibility(View.GONE);
            binding.check3dsError.setVisibility(View.VISIBLE);*/

            //  showResult(R.drawable.failed, R.string.pay_error_3ds_authentication_failed);
        }
    }

    class CompleteSessionCallback implements ApiController.CompleteSessionCallback {
        @Override
        public void onSuccess ( String result ) {
           /* binding.processPaymentProgress.setVisibility(View.GONE);
            binding.processPaymentSuccess.setVisibility(View.VISIBLE);

            showResult(R.drawable.success, R.string.pay_you_payment_was_successful);*/
        }

        @Override
        public void onError ( Throwable throwable ) {
            Log.e( CreditPaymentCardPayment.class.getSimpleName(), throwable.getMessage(), throwable );

           /* binding.processPaymentProgress.setVisibility(View.GONE);
            binding.processPaymentError.setVisibility(View.VISIBLE);

            showResult(R.drawable.failed, R.string.pay_error_processing_your_payment);*/
        }
    }
}
