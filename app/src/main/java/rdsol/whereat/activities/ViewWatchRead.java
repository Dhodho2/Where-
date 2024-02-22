package rdsol.whereat.activities;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.material.textfield.TextInputEditText;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import rdsol.whereat.R;
import rdsol.whereat.adapters.recyclerview.CommentsRecyclerViewChats;
import rdsol.whereat.callbacks.HttpReqCallBack;
import rdsol.whereat.callbacks.HttpReqCallCommentsBackData;
import rdsol.whereat.customeviews.RecyclerViewItemSeparator;
import rdsol.whereat.database.RoomDB;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.database.room.entities.DBCommentMessage;
import rdsol.whereat.datamodels.ViewsCounterModel;
import rdsol.whereat.netwox.CacheDataSourceFactory;
import rdsol.whereat.netwox.HandleRequests;
import rdsol.whereat.pojos.CommentsMessagesDataModel;
import rdsol.whereat.pojos.UserType;
import rdsol.whereat.processes.GetCommentsProcess;
import rdsol.whereat.processes.LikeUnLikePostingProcess;
import rdsol.whereat.processes.PostCommentProcess;
import rdsol.whereat.services.others.GetCommentsService;
import rdsol.whereat.services.others.GetStreamViewsDataService;
import rdsol.whereat.utils.AndroidUtilities;
import rdsol.whereat.utils.NotificationCenter;
import rdsol.whereat.utils.VideoPlayerConfig;

import static rdsol.whereat.utils.AndroidUtilities.setDrawable;

public class ViewWatchRead extends BaseActivity implements HttpReqCallBack, HttpReqCallCommentsBackData, Player.EventListener {


    private TextInputEditText chatEditText1;
    private ArrayList<DBCommentMessage> commentMessages;
    private ImageView enterChatView1;

    private MyPreferences dbPreferences;
    PlayerView playerView;
    ProgressBar spinnerVideoDetails;
    String videoUri;
    SimpleExoPlayer player;
    Handler mHandler;
    Runnable mRunnable;
    private int LastPosition = 0;
    private static final String TAG = "ExoPlayerActivity";

    SimpleDateFormat formatDate = new SimpleDateFormat( "dd-MM-yyyy HH:mm" );
    private static final String KEY_VIDEO_URI = "video_uri";
    public static String MEDIA_ID_ROW = "";
    private int liked = 0;
    private int comments = 0;
    String stream = "no";

    private CommentsMessagesDataModel commentsMessagesDataModel;
    private EditText.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey ( View v, int keyCode, KeyEvent event ) {

            // If the event is a key-down event on the "enter" button
            if ( ( event.getAction() == KeyEvent.ACTION_DOWN ) &&
                    ( keyCode == KeyEvent.KEYCODE_ENTER ) ) {
                // Perform action on key press

                TextInputEditText editText = ( TextInputEditText ) v;

                if ( v == chatEditText1 ) {
                    sendMessage( editText.getText().toString(), UserType.OTHER );
                }
                commentsRecyclerView.scrollToPosition( LastPosition );
                chatEditText1.setText( "" );

                return true;
            }
            return false;

        }
    };

    private ImageView.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick ( View v ) {

            if ( v == enterChatView1 ) {
                sendMessage( chatEditText1.getText().toString(), UserType.OTHER );
            }

            chatEditText1.setText( "" );

        }
    };

    private final TextWatcher watcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged ( CharSequence charSequence, int i, int i2, int i3 ) {
        }

        @Override
        public void onTextChanged ( CharSequence charSequence, int i, int i2, int i3 ) {
            if ( chatEditText1.getText().toString().equals( "" ) ) {

            } else {
                enterChatView1.setImageResource( R.drawable.ic_send_white_24dp );

            }
        }

        @Override
        public void afterTextChanged ( Editable editable ) {
            if ( editable.length() == 0 ) {
                enterChatView1.setImageResource( R.drawable.ic_send_white_24dp );
            } else {
                enterChatView1.setImageResource( R.drawable.ic_send_white_24dp );
            }
        }
    };
    private CommentsRecyclerViewChats Adapter;
    private RecyclerView commentsRecyclerView;

    String videURLL = "", dated = "";
    private ProgressDialog progressDialog;
    private ConstraintLayout videPlayerView;
    private LinearLayout toolbar_makeed;
    private ImageView imageViewExitt;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        HandleRequests.LAST_COMMENT_ID = 0;
        dbPreferences = new MyPreferences( this );
        progressDialog = new ProgressDialog( this );
        setTheme( dbPreferences.getIsDarkMode() ? R.style.AppThemeDarkNoActionBar : R.style.AppThemeMaterialNoActionBar );
        super.onCreate( savedInstanceState );
        setContentView( R.layout.content_view_watch_read );
        videPlayerView = findViewById( R.id.videPlayerView );
        imageViewExitt = findViewById( R.id.imageViewExitt );
        toolbar_makeed = findViewById( R.id.toolbar_makeed );
        views_count = findViewById( R.id.views_count );
        linearLayout3 = findViewById( R.id.linearLayout3 );
        if ( getIntent().hasExtra( "video_file" ) ) {
            liked = getIntent().getIntExtra( "liked", 0 );
            comments = getIntent().getIntExtra( "comments", 0 );

            videURLL = getIntent().getStringExtra( "video_file" );
            stream = getIntent().getStringExtra( "stream" );
            dated = getIntent().getStringExtra( "dated" );
            MEDIA_ID_ROW = getIntent().getStringExtra( "id_media" );
        } else {
            // videPlayerView.setVisibility( View.GONE );
            //  toolbar_makeed.setVisibility( View.VISIBLE );
            liked = getIntent().getIntExtra( "liked", 0 );
            comments = getIntent().getIntExtra( "comments", 0 );
            stream = getIntent().getStringExtra( "stream" );
            dated = getIntent().getStringExtra( "dated" );
            MEDIA_ID_ROW = getIntent().getStringExtra( "id_media" );
        }

        spinnerVideoDetails = findViewById( R.id.spinnerVideoDetails );


        LikeButton like = findViewById( R.id.like );
        TextView dated_ = findViewById( R.id.dated );
        TextView comments_count = findViewById( R.id.comments_count );
        comments_count.setText( comments + " Comments" );
        dated_.setText( dated );
        like.setLiked( liked == 1 );

        like.setOnLikeListener( new OnLikeListener() {
            @Override
            public void liked ( LikeButton likeButton ) {
                LikeUnLikePostingProcess likeUnLikePostingProcess = new LikeUnLikePostingProcess();
                likeUnLikePostingProcess.execute( MEDIA_ID_ROW + "", "true" );
            }

            @Override
            public void unLiked ( LikeButton likeButton ) {
                LikeUnLikePostingProcess likeUnLikePostingProcess = new LikeUnLikePostingProcess();
                likeUnLikePostingProcess.execute( MEDIA_ID_ROW + "", "false" );

            }
        } );

        AndroidUtilities.statusBarHeight = getStatusBarHeight();
        playerView = findViewById( R.id.videoFullScreenPlayer );
        ImageView imageViewExit = findViewById( R.id.imageViewExit );
        //getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));

        commentMessages = new ArrayList<>();

        commentsRecyclerView = ( RecyclerView ) findViewById( R.id.chat_list_view );
        commentsRecyclerView.setHasFixedSize( true );

        chatEditText1 = findViewById( R.id.chat_edit_text1 );
        enterChatView1 = ( ImageView ) findViewById( R.id.enter_chat1 );

        // Hide the emoji on click of edit text
        imageViewExit.setOnClickListener( view -> {
            onBackPressed();
        } );
        imageViewExitt.setOnClickListener( view -> {
            onBackPressed();
        } );

        chatEditText1.setOnClickListener( view -> {

        } );

        chatEditText1 = findViewById( R.id.chat_edit_text1 );
        enterChatView1 = ( ImageView ) findViewById( R.id.enter_chat1 );


        commentsMessagesDataModel = new ViewModelProvider( this ).get( CommentsMessagesDataModel.class );
        commentsRecyclerView.addItemDecoration( new RecyclerViewItemSeparator( 1 ) );
        commentsMessagesDataModel.getLiveData().getAll( Integer.parseInt( MEDIA_ID_ROW ) ).observe( this, datalist -> {

            Adapter = new CommentsRecyclerViewChats( datalist, ViewWatchRead.this );
            commentsRecyclerView.setAdapter( Adapter );
            LastPosition = datalist.size() - 1;
            commentsRecyclerView.scrollToPosition( LastPosition );
        } );
        commentsMessagesDataModel.lastComments( Integer.parseInt( MEDIA_ID_ROW ) );



        chatEditText1.setOnKeyListener( keyListener );

        enterChatView1.setOnClickListener( clickListener );

        chatEditText1.addTextChangedListener( watcher1 );

        initializePlayer();

    }


    private void initializePlayer ( ) {
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
                new DefaultRenderersFactory( this ),
                trackSelector, loadControl );

        //  Uri uri = Uri.parse( "rtmp://10.42.0.1/live" /*videURLL*/ );

        if ( stream.equals( "no" ) ) {
            Uri uri = Uri.parse( videURLL );
            ExtractorMediaSource source = new ExtractorMediaSource(
                    uri,
                    //  new DefaultDataSourceFactory( this, "MyExoplayer" ),
                    new CacheDataSourceFactory( this, 100 * 1024 * 1024, 5 * 1024 * 1024 ),
                    new DefaultExtractorsFactory(), null, null

            );
            player.prepare( source );
            linearLayout3.setVisibility( View.GONE );
        } else if ( stream.equals( "yes" ) ) {

            // Log.e( "xxxx", "initializePlayer: " +  HandleRequests.RTMP_URL+"/key"+MEDIA_ID_ROW );
            Uri uri = Uri.parse( HandleRequests.RTMP_URL + "/key_" + MEDIA_ID_ROW );
            MediaSource RMTpsource = new ExtractorMediaSource.Factory(
                    new com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory()
            )
                    .createMediaSource( uri );
            player.prepare( RMTpsource );

            getmHandler = new Handler(  );
            getmHandler.post( getmRunnable );


            ViewsCounterModel counterModel = new ViewModelProvider( this ).get( ViewsCounterModel.class );
            counterModel.getLiveData().getAll().observe( this, datalist -> {
                if ( datalist != null && datalist.size()>0) {
                    views_count.setText( datalist.get( 0 ).getCount()+"" );
                }


            } );
        }

        playerView.setPlayer( player );

        playerView.setResizeMode( AspectRatioFrameLayout.RESIZE_MODE_ZOOM );

        player.setPlayWhenReady( true );
        player.addListener( this );


       /* BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        MediaSource audioSource = new ExtractorMediaSource(Uri.parse(url),
                new CacheDataSourceFactory(this, 100 * 1024 * 1024, 5 * 1024 * 1024), new DefaultExtractorsFactory(), null, null);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.prepare(audioSource);*/

    }
    private Handler getmHandler ;
private Runnable getmRunnable = new Runnable() {
    @Override
    public void run ( ) {
        startBackgroundServices();
        getmHandler.postDelayed(  getmRunnable ,1_000 *10);

    }
};
    private void startBackgroundServices ( ) {
        WorkManager.getInstance( this )
                .enqueue( new PeriodicWorkRequest.Builder( GetStreamViewsDataService.class, 1, TimeUnit.MINUTES )
                        .setConstraints( new Constraints.Builder()
                                .setRequiresCharging( true )
                                .build() ).setInitialDelay( 1, TimeUnit.MINUTES )
                        .build() );


        WorkManager.getInstance( this )
                .enqueue( new PeriodicWorkRequest.Builder( GetCommentsService.class, 4, TimeUnit.MINUTES )
                        .setConstraints( new Constraints.Builder()
                                .setRequiresCharging( true )
                                .build() ).setInitialDelay( 2, TimeUnit.MINUTES )
                        .build() );
    }

    private void pausePlayer ( ) {
        if ( player != null ) {
            player.setPlayWhenReady( false );
            player.getPlaybackState();
        }
    }



    private void sendMessage ( final String messageText, final String userType ) {
        if ( messageText.trim().length() == 0 ) {


            DynamicToast.make( this, "Cant send empty message! ", setDrawable( this, R.drawable.ic_info_white_24dp ) ).show();

            return;
        }



        final ScheduledExecutorService exec = Executors.newScheduledThreadPool( 1 );

        exec.schedule( ( ) -> {


            commentsMessagesDataModel.lastComments( Integer.parseInt( MEDIA_ID_ROW ) );


            PostCommentProcess postCommentProcess = new PostCommentProcess( ViewWatchRead.this );


            postCommentProcess.execute( MEDIA_ID_ROW, messageText );


            GetCommentsProcess getCommentsProcess = new GetCommentsProcess( ViewWatchRead.this );
            getCommentsProcess.execute( MEDIA_ID_ROW + "" );


        }, 1, TimeUnit.SECONDS );

    }



    /**
     * Get the system status bar height
     *
     * @return
     */
    public int getStatusBarHeight ( ) {
        int result = 0;
        int resourceId = getResources().getIdentifier( "status_bar_height", "dimen", "android" );
        if ( resourceId > 0 ) {
            result = getResources().getDimensionPixelSize( resourceId );
        }
        return result;
    }

    @Override
    protected void onPause ( ) {
        super.onPause();
        pausePlayer();
        if ( mRunnable != null ) {
            mHandler.removeCallbacks( mRunnable );
        }

        if ( mRunnable != null ) {
            mHandler.removeCallbacks( mRunnable );
        }
    }

    private void releasePlayer ( ) {
        if ( player != null ) {
            player.release();
            player = null;
        }
    }




    @Override
    public void onDestroy ( ) {
        super.onDestroy();
        releasePlayer();
        NotificationCenter.getInstance().removeObserver( this, NotificationCenter.emojiDidLoaded );
        if ( !dbPreferences.getIsCacheCommentsEnabled() ) {
            // RoomDB.roomConnect().getTable().clearTable();
        }
        WorkManager.getInstance( this ).cancelAllWork();
        if(getmRunnable != null &&  getmHandler != null){
            getmHandler.removeCallbacks(getmRunnable  );
            getmHandler = null;
        }

    }


    @Override
    protected void onRestart ( ) {
        super.onRestart();

    }


    @Override
    public void onCompleteResponse ( String response ) {

    }

    @Override
    public void onCompleteResponse ( List<DBCommentMessage> objects ) {
        for ( DBCommentMessage comment : objects ) {

            RoomDB.getRoomInstance( this ).getTable().save( comment );
        }
    }

    @Override
    public void onTimelineChanged ( Timeline timeline, Object manifest, int reason ) {

    }

    @Override
    public void onTracksChanged ( TrackGroupArray trackGroups, TrackSelectionArray trackSelections ) {

    }

    @Override
    public void onLoadingChanged ( boolean isLoading ) {

    }

    @Override
    public void onPlayerStateChanged ( boolean playWhenReady, int playbackState ) {
        switch ( playbackState ) {

            case Player.STATE_BUFFERING:
                spinnerVideoDetails.setVisibility( View.VISIBLE );
                break;
            case Player.STATE_ENDED:
                // Activate the force enable
                break;
            case Player.STATE_IDLE:

                break;
            case Player.STATE_READY:
                spinnerVideoDetails.setVisibility( View.GONE );

                break;
            default:
                // status = PlaybackStatus.IDLE;
                break;
        }
    }

    @Override
    public void onRepeatModeChanged ( int repeatMode ) {

    }

    @Override
    public void onShuffleModeEnabledChanged ( boolean shuffleModeEnabled ) {

    }

    @Override
    public void onPlayerError ( ExoPlaybackException error ) {

    }

    @Override
    public void onPositionDiscontinuity ( int reason ) {

    }

    @Override
    public void onPlaybackParametersChanged ( PlaybackParameters playbackParameters ) {

    }

    @Override
    public void onSeekProcessed ( ) {

    }

    private TextView views_count;
    private LinearLayout linearLayout3;

}
