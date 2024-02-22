package rdsol.whereat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import java.util.HashSet;

import rdsol.whereat.R;
import rdsol.whereat.customeviews.instagram_images.gallery.GalleryPickerFragment;
import rdsol.whereat.customeviews.instagram_images.gallery.UploadPickedFileFragment;
import rdsol.whereat.customeviews.instagram_images.models.Session;
import rdsol.whereat.customeviews.instagram_images.models.enums.SourceType;
import rdsol.whereat.customeviews.instagram_images.modules.PermissionModule;
import rdsol.whereat.customeviews.instagram_images.ui.ToolbarView;
import rdsol.whereat.database.preferences.MyPreferences;

public class NotificationsFragment extends Fragment implements ToolbarView.OnClickTitleListener,ToolbarView.OnClickNextListener, ToolbarView.OnClickBackListener {

    private TabLayout mMainTabLayout;
    private ToolbarView mToolbar;
    private FragmentTransaction transaction;
    private MyPreferences myPreferences;
    private Session mSession = Session.getInstance();
    private HashSet<SourceType> mSourceTypeSet = new HashSet<>();

    private void initViews ( ) {
        PermissionModule permissionModule = new PermissionModule( getContext() );
        permissionModule.checkPermissions();

        mToolbar.setOnClickBackMenuListener( this )
                .setOnClickTitleListener( this )
                .setOnClickNextListener( this );

        transaction = getChildFragmentManager().beginTransaction();
        transaction.replace( R.id.mMainViewPager, galleryPickerFragment );
        transaction.commit();
    }



    public static NotificationsFragment getInstance ( ) {

        return new NotificationsFragment();
    }

    @Override
    public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );
        setHasOptionsMenu( false );
    }

    View root;

    public View onCreateView ( @NonNull LayoutInflater inflater,
                               ViewGroup container, Bundle savedInstanceState ) {
        myPreferences = new MyPreferences( getContext() );

        root = inflater.inflate( R.layout.fragment_notifications, container, false );
        mMainTabLayout = root.findViewById( R.id.mMainTabLayout );
        FrameLayout mMainViewPager = root.findViewById( R.id.mMainViewPager );
        mToolbar = root.findViewById( R.id.mToolbar );


        initViews();

        return root;
    }


    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
    }

    @Override
    public void onClickBack ( ) {
        transaction = getChildFragmentManager().beginTransaction();
        transaction.replace( R.id.mMainViewPager, galleryPickerFragment );
        transaction.commit();
        mToolbar.getBackIcon().setVisibility( View.GONE );
        mToolbar.showNext();
    }

    Fragment galleryPickerFragment = new GalleryPickerFragment();
    Fragment uploadPickedFileFragment = new UploadPickedFileFragment();

    @Override
    public void onClickNext ( ) {
        // Fetch file to upload
        mSession.getFileToUpload();
        //  DynamicToast.makeError( getContext(),"Nextt" ).show();
        transaction = getChildFragmentManager().beginTransaction();
        transaction.replace( R.id.mMainViewPager, uploadPickedFileFragment );
        transaction.commit();

        // mToolbar.getmIconNext().setVisibility( View.VISIBLE );
        mToolbar.hideNext();
        mToolbar.getBackIcon().setVisibility( View.VISIBLE );
    }

    @Override
    public void onClickTitle ( ) {

    }
}
