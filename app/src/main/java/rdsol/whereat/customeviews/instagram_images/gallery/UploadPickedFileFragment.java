package rdsol.whereat.customeviews.instagram_images.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import id.ionbit.ionalert.IonAlert;
import rdsol.whereat.R;
import rdsol.whereat.callbacks.HttpReqCallBack;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.fragments.NotificationsFragment;
import rdsol.whereat.processes.PostUploadProcess;

public class UploadPickedFileFragment extends Fragment implements HttpReqCallBack {
    private MyPreferences myPreferences;
    TextInputEditText postTitle;
    View root;
    IonAlert ionAlert;

    public View onCreateView ( @NonNull LayoutInflater inflater,
                               ViewGroup container, Bundle savedInstanceState ) {
        myPreferences = new MyPreferences( getContext() );

        root = inflater.inflate( R.layout.fragment_upload_gallery_picked_file, container, false );
        CircleImageView circle_previw = root.findViewById( R.id.circle_previw );
        postTitle = root.findViewById( R.id.postTitle );
        MaterialButton uploadpost = root.findViewById( R.id.uploadpost );

        if ( !myPreferences.getLastSelectedIntentFilePath().isEmpty() ) {
            File file = new File( myPreferences.getLastSelectedIntentFilePath() );

            if ( file.getAbsolutePath().endsWith( ".mp4" ) ) {

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource( getContext(), Uri.parse( file.getAbsolutePath() ) );
                Bitmap bitmap = retriever
                        .getFrameAtTime( 100000, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC );
                Glide.with( getContext() )

                        .load( bitmap ).centerCrop()

                        .into( circle_previw );


            } else {

              /*  Picasso.get()
                        .load( Uri.fromFile( file ) )
                        .noFade()
                        .noPlaceholder()
                        .centerCrop()
                        .into( circle_previw );*/

                Glide.with( getContext() ).load( Uri.fromFile( file )  ).centerCrop()
                        .placeholder( R.drawable.ic_main_icon ).fitCenter()
                        .diskCacheStrategy( DiskCacheStrategy.ALL ).into( circle_previw );
            }

        }
        uploadpost.setOnClickListener( ev -> {

            ionAlert = new IonAlert( getContext(), IonAlert.PROGRESS_TYPE );
            ionAlert.setSpinKit( "WanderingCubes" );
            ionAlert.showCancelButton( false );
            ionAlert.show();
            String postTitle_ = postTitle.getText().toString().trim();

            PostUploadProcess uploadProcess = new PostUploadProcess( UploadPickedFileFragment.this );
            uploadProcess.execute( postTitle_, "postDescription_", myPreferences.getLastSelectedIntentFilePath() );

        } );
        return root;
    }

    private Fragment parent;


    @Override
    public void onCompleteResponse ( String response ) {
        if(ionAlert!= null){
            ionAlert.dismiss();
        }
        if ( "connection".equals( response ) ) {
            DynamicToast.makeError( getContext(), "Please check your Connection" ).show();
        } else if ( "fail".equals( response ) ) {
            DynamicToast.makeError( getContext(), "Error Occurred" ).show();
        } else if ( "auth".equals( response ) ) {
            DynamicToast.makeError( getContext(), "Error - Auth Occurred " ).show();
        } else {
            JSONObject respo = null;
            try {
                respo = new JSONObject( response );
                String status = respo.getString( "status" );
                if ( status.equals( "ok" ) ) {
                    DynamicToast.makeSuccess( getContext(), "Post Uploaded" ).show();
                    myPreferences.setLastSelectedIntentFilePath( "" );
                    postTitle.setText( "" );

                    if ( parent instanceof NotificationsFragment ) {
                        NotificationsFragment parennt = ( NotificationsFragment ) parent;
                        parennt.onClickBack();
                        // AndroidUtilities.hideKeyboard(  postCommentBtn,getContext());
                    }
                }
            } catch ( JSONException e ) {
                e.printStackTrace();

                DynamicToast.makeError( getContext(), "Fatal Error - Auth Occurred " ).show();
            }

        }
    }

    @Override
    public void onAttach ( @NonNull Context context ) {
        super.onAttach( context );
        parent = getParentFragment();
    }
}