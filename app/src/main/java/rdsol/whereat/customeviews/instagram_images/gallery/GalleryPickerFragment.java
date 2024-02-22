package rdsol.whereat.customeviews.instagram_images.gallery;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rdsol.whereat.R;
import rdsol.whereat.customeviews.instagram_images.models.Session;
import rdsol.whereat.customeviews.instagram_images.modules.LoadMoreModule;
import rdsol.whereat.customeviews.instagram_images.modules.LoadMoreModuleDelegate;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.utils.VideoPlayerConfig;

/*
import com.octopepper.mediapickerinstagram.R;
import com.octopepper.mediapickerinstagram.commons.models.Session;
import com.octopepper.mediapickerinstagram.commons.modules.LoadMoreModule;
import com.octopepper.mediapickerinstagram.commons.modules.LoadMoreModuleDelegate;*/

/*
 * Created by Guillaume on 17/11/2016.
 */

public class GalleryPickerFragment extends Fragment implements GridAdapterListener, LoadMoreModuleDelegate {


   private RecyclerView mGalleryRecyclerView;

  private   ImageView mPreview;

   private AppBarLayout mAppBarContainer;
private MyPreferences myPreferences;
    private static final String EXTENSION_JPG = ".jpg";
    private static final String EXTENSION_JPEG = ".jpeg";
    private static final String EXTENSION_PNG = ".png";
    private static final String EXTENSION_MP4 = ".mp4";
    private static final int PREVIEW_SIZE = 800;
    private static final int MARGING_GRID = 2;
    private static final int RANGE = 20;

    private Session mSession = Session.getInstance();
    private LoadMoreModule mLoadMoreModule = new LoadMoreModule();
    private GridAdapter mGridAdapter;
    private ArrayList<File> mFiles;
    private boolean isLoading = false;
    private int mOffset;
    private boolean isFirstLoad = true;

    public static GalleryPickerFragment newInstance() {
        return new GalleryPickerFragment();
    }

    private void initViews() {
        if (isFirstLoad) {
            mGridAdapter = new GridAdapter(getContext());
        }
        mGridAdapter.setListener(this);
        mGalleryRecyclerView.setAdapter(mGridAdapter);
        mGalleryRecyclerView.setHasFixedSize(true);
        mGalleryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mGalleryRecyclerView.addItemDecoration(addItemDecoration());
        mLoadMoreModule.LoadMoreUtils(mGalleryRecyclerView, this, getContext());
        mOffset = 0;
        // exoplayer inits
        LoadControl loadControl = new DefaultLoadControl(
                new DefaultAllocator( true, 16 ),
                VideoPlayerConfig.MIN_BUFFER_DURATION,
                VideoPlayerConfig.MAX_BUFFER_DURATION,
                VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true );

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory( bandwidthMeter );
        TrackSelector trackSelector =
                new DefaultTrackSelector( videoTrackSelectionFactory );
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory( getContext() ),
                trackSelector, loadControl );

        fetchMedia();
    }

    private RecyclerView.ItemDecoration addItemDecoration() {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view,
                                       RecyclerView parent, RecyclerView.State state) {
                outRect.left = MARGING_GRID;
                outRect.right = MARGING_GRID;
                outRect.bottom = MARGING_GRID;
                if (parent.getChildLayoutPosition(view) >= 0 && parent.getChildLayoutPosition(view) <= 3) {
                    outRect.top = MARGING_GRID;
                }
            }
        };
    }

    private void fetchMedia() {
        mFiles = new ArrayList<>();
        File dirDownloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        parseDir(dirDownloads);

        File dirDcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        parseDir(dirDcim);
        File moviesDcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        parseDir(moviesDcim);

        File dirPictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        parseDir(dirPictures);
        File dirDocuments = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        parseDir(dirDocuments);

        if (mFiles.size() > 0) {
            displayPreview(mFiles.get(0));
            mGridAdapter.setItems(getRangePets());
        }
        isFirstLoad = false;
    }

    private List<File> getRangePets() {
        if (mOffset < mFiles.size()) {
            if ((mOffset + RANGE) < mFiles.size()) {
                return mFiles.subList(mOffset, mOffset + RANGE);
            } else if ((mOffset + RANGE) >= mFiles.size()) {
                return mFiles.subList(mOffset, mFiles.size());
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    private void parseDir(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            parseFileList(files);
        }
    }

    private void parseFileList(File[] files) {
        HashSet<File> mFileset = new HashSet<>(  );
        for (File file : files) {
            if (file.isDirectory()) {
                if (!file.getName().toLowerCase().startsWith(".")) {
                    parseDir(file);
                }
            } else {
                if (file.getName().toLowerCase().endsWith(EXTENSION_JPG)
                        || file.getName().toLowerCase().endsWith(EXTENSION_JPEG)
                        || file.getName().toLowerCase().endsWith(EXTENSION_PNG)
                        || file.getName().toLowerCase().endsWith(EXTENSION_MP4)
                ) {
                    mFiles.add(file);
                    //mFileset.add( file );
                }
            }
        }

       // mFiles.addAll( mFileset );
    }

    private void loadNext() {
        if (!isLoading) {
            isLoading = true;
            mOffset += RANGE;
            List<File> files = new ArrayList<>();
            files.addAll(getRangePets());
            if (files.size() > 0) {
                mGridAdapter.addItems(files, mGridAdapter.getItemCount());
            }
            isLoading = false;
        }
    }
    PlayerView playerView;
    ProgressBar spinnerVideoDetails;
    String videoUri;
    SimpleExoPlayer player;
    private void displayPreview(File file) {
        myPreferences.setLastSelectedIntentFilePath( file.getAbsolutePath() );
        if(file.getAbsolutePath().endsWith( EXTENSION_MP4 )){

           /* MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getContext(), Uri.parse( file.getAbsolutePath() ) );
            Bitmap bitmap = retriever
                    .getFrameAtTime(100000,MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
            Glide.with( getContext() )

                    .load( bitmap ).centerCrop()

                    .into( mPreview );*/
            mPreview.setVisibility( View.GONE );
            playerView.setVisibility( View.VISIBLE );
            player.setPlayWhenReady( false );
            initializePlayer(file);

        }else{
            player.stop();
            mPreview.setVisibility( View.VISIBLE );
            playerView.setVisibility( View.GONE );
            Picasso.get()
                    .load(Uri.fromFile(file))
                    .noFade()
                    .noPlaceholder()
                    .resize(PREVIEW_SIZE, PREVIEW_SIZE)
                    .centerCrop()
                    .into(mPreview);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPreferences = new MyPreferences( getContext() );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gallery_picker_view, container, false);
       //// ButterKnife.bind(this, v);
        mGalleryRecyclerView = v.findViewById( R.id.mGalleryRecyclerView );
        mPreview = v.findViewById( R.id.mPreview );
       // videPlayerView = v.findViewById( R.id.videPlayerView );
        playerView =v. findViewById( R.id.videoFullScreenPlayer );
        mAppBarContainer =v.findViewById( R.id.mAppBarContainer );
        initViews();

        playerView.setPlayer( player );

        playerView.setResizeMode( AspectRatioFrameLayout.RESIZE_MODE_ZOOM );


        return v;
    }

    private void initializePlayer ( File file) {


            Uri uri = Uri.parse( file.getAbsolutePath() );
            ExtractorMediaSource source = new ExtractorMediaSource(
                    uri,
                      new DefaultDataSourceFactory( getContext(), "MyExoplayer" ),
                   // new CacheDataSourceFactory( getContext(), 100 * 1024 * 1024, 5 * 1024 * 1024 ),
                    new DefaultExtractorsFactory(), null, null

            );
            player.prepare( source );



        playerView.setPlayer( player );

        playerView.setResizeMode( AspectRatioFrameLayout.RESIZE_MODE_ZOOM );

    }
    private Fragment parent;

    @Override
    public void onAttach ( @NonNull Context context ) {
        super.onAttach( context );
        parent = getParentFragment();

    }

    @Override
    public void onPause() {
        super.onPause();
        player.stop();
        Picasso.get().cancelRequest(mPreview);
    }

    @Override
    public void onClickMediaItem(File file) {
        displayPreview(file);
        mSession.setFileToUpload(file);
        mAppBarContainer.setExpanded(true, true);
    }

    @Override
    public void shouldLoadMore() {
        loadNext();
    }
}
