package rdsol.whereat.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.button.MaterialButton;
import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import net.ossrs.rtmp.ConnectCheckerRtmp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rdsol.whereat.R;
import rdsol.whereat.adapters.recyclerview.CommentsRecyclerViewChats;
import rdsol.whereat.customeviews.RecyclerViewItemSeparator;
import rdsol.whereat.database.room.entities.DBCommentMessage;
import rdsol.whereat.datamodels.ViewsCounterModel;
import rdsol.whereat.netwox.HandleRequests;
import rdsol.whereat.pojos.CommentsMessagesDataModel;
import rdsol.whereat.processes.CheckIfStreamIsStillLiveProcess;
import rdsol.whereat.services.others.GetStreamViewsDataService;

import static rdsol.whereat.activities.ViewWatchRead.MEDIA_ID_ROW;

public class VideoStreamActivity extends AppCompatActivity implements ConnectCheckerRtmp, View.OnClickListener, SurfaceHolder.Callback, CheckIfStreamIsStillLiveProcess.HttpCheckIfStreamIsSTillLive {

    private RtmpCamera1 rtmpCamera1;
    private MaterialButton button;
    private MaterialButton bRecord;
    private EditText etUrl; TextView clock_timer ,views_count;
    private String Value_of_Timmer;
    private String currentDateAndTime = "";
    private String StreamingUrl = "";
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private String stream = "";
    private String id_media = "" ,Timeed;
    private Handler customHandler = new Handler(),handler=new Handler();
    private File folder = new File( Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/where-at-app");
    private TextView caption ;
    private CommentsMessagesDataModel commentsMessagesDataModel;
    private CommentsRecyclerViewChats Adapter;
    private RecyclerView commentsRecyclerView;
    private ArrayList<DBCommentMessage> commentMessages;
    private Runnable updateTimerThread = new Runnable() {
        @SuppressLint ("SetTextI18n")
        @Override
        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;


            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);

            int mins;

            mins=    secs    /   60;

            secs = secs % 60;

            Value_of_Timmer = "" + mins + ":" + String.format("%02d", secs);

            clock_timer.setText( Value_of_Timmer);


            customHandler.postDelayed(this, 0);
        }
    };
    private TextView liveText;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_video_stream );
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        liveText = findViewById(R.id.liveText);
        views_count = findViewById(R.id.views_count);
        caption = findViewById(R.id.caption);
        clock_timer = findViewById(R.id.clock_timer);
        button = findViewById(R.id.b_start_stop);
        button.setOnClickListener(this);
        bRecord = findViewById(R.id.b_record);
        bRecord.setOnClickListener(this);
        ImageView switchCamera = findViewById(R.id.switch_camera);
        switchCamera.setOnClickListener(this);
        etUrl = findViewById(R.id.et_rtp_url);
        etUrl.setHint(R.string.hint_rtmp);
        rtmpCamera1 = new RtmpCamera1(surfaceView, this);
        rtmpCamera1.setReTries(10);
        surfaceView.getHolder().addCallback(this);
        StreamingUrl  = HandleRequests.RTMP_URL;
        if(getIntent().hasExtra( "stream" )){

            stream = getIntent().getStringExtra( "stream" );
            id_media = getIntent().getStringExtra( "id_media" );
            if(getIntent().hasExtra( "timmed" )) {
                Timeed = getIntent().getStringExtra( "timmed" );
                caption.setText( "Ready For Scheduled Stream ("+Timeed.replace( "=","-" )+")"  );
            }else{
                caption.setText( "Ready For Stream " );
            }

            StreamingUrl  += "/key_" +id_media;
            MEDIA_ID_ROW = id_media;


            Log.e( "xxxx", "onCreate:StreamingUrl " + StreamingUrl);
        }


        commentMessages = new ArrayList<>();

        commentsRecyclerView = findViewById( R.id.chat_list_view );
        commentsRecyclerView.setHasFixedSize( true );

        commentsMessagesDataModel = new ViewModelProvider( this ).get( CommentsMessagesDataModel.class );
        commentsRecyclerView.addItemDecoration( new RecyclerViewItemSeparator( 1 ) );
        commentsMessagesDataModel.getLiveData().getAll( Integer.parseInt( MEDIA_ID_ROW ) ).observe( this, datalist -> {

            Adapter = new CommentsRecyclerViewChats( datalist, VideoStreamActivity.this );
            commentsRecyclerView.setAdapter( Adapter );
            commentsRecyclerView.scrollToPosition( datalist.size()-1 );

        } );
        commentsMessagesDataModel.lastComments( Integer.parseInt( MEDIA_ID_ROW ) );

       /* checkIfStreamIsStillLiveProcess = new CheckIfStreamIsStillLiveProcess( VideoStreamActivity.this );
        checkIfStreamIsStillLiveProcess.execute( MEDIA_ID_ROW );*/
    }
private CheckIfStreamIsStillLiveProcess checkIfStreamIsStillLiveProcess;
    @Override
    protected void onStart ( ) {
        super.onStart();
       // button.performClick();
    }

    private void StartTimmer() {
        caption.setText( ""  );
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    private void Stop_Timmer() {
        if(!stream.isEmpty()){
            if(Timeed !=null){
                caption.setText( "Ready For Scheduled Stream ("+Timeed.replace( "=","-" )+")"  );
            }

        }

        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }

    @Override
    protected void onStop ( ) {
        super.onStop();
        Stop_Timmer();
        WorkManager.getInstance( this ).cancelAllWork();

        if(getmHandler != null && getmRunnable != null){
            getmHandler.removeCallbacks(getmRunnable  );
            getmHandler = null;
        }
    }
        void startGettingData(){
            checkIfStreamIsStillLiveProcess = new CheckIfStreamIsStillLiveProcess( VideoStreamActivity.this );
            checkIfStreamIsStillLiveProcess.execute( MEDIA_ID_ROW );
            WorkManager.getInstance( this )
                    .enqueue( new PeriodicWorkRequest.Builder( GetStreamViewsDataService.class, 5, TimeUnit.SECONDS )
                            .setConstraints( new Constraints.Builder()
                                    .setRequiresCharging( true )
                                    .build() )
                            .build() );


            ViewsCounterModel counterModel = new ViewModelProvider( this ).get( ViewsCounterModel.class );
            counterModel.getLiveData().getAll().observe( this, datalist -> {
                if ( datalist != null && datalist.size()>0) {
                    views_count.setText( datalist.get( 0 ).getCount()+"" );
                }


            } );
        }
    private Handler getmHandler ;
    private Runnable getmRunnable = new Runnable() {
        @Override
        public void run ( ) {
            startGettingData();
           // Log.e( "xxxx", "run: HHHHHHHHHHHHHHHHH"  );
            commentsMessagesDataModel.lastComments( Integer.parseInt( MEDIA_ID_ROW ) );
            getmHandler.postDelayed(  getmRunnable ,1_000 *3);
        }
    };
    @Override
    public void onConnectionSuccessRtmp() {
        runOnUiThread( ( ) -> {
            StartTimmer();
            DynamicToast.makeSuccess(VideoStreamActivity.this, "Connection success").show();

            getmHandler = new Handler(  );
            getmHandler.post( getmRunnable );

        } );
    }
    @Override
    public void onConnectionFailedRtmp(final String reason) {
        runOnUiThread( ( ) -> {
            if (rtmpCamera1.reTry(5000, reason)) {
                DynamicToast.make(VideoStreamActivity.this, "Retry Again ,Connection Failed")
                        .show();
            } else {
                DynamicToast.makeError(VideoStreamActivity.this, "Connection Failed. " + reason)
                        .show();
                rtmpCamera1.stopStream();
                button.setText(R.string.start_button);
            }
        } );
    }

    @Override
    public void onNewBitrateRtmp(long bitrate) {

    }

    @Override
    public void onDisconnectRtmp() {
        runOnUiThread( ( ) -> DynamicToast.makeWarning(VideoStreamActivity.this, "Disconnected").show() );
        liveText.setVisibility( View.INVISIBLE );
        if(getmRunnable != null){
            getmHandler.removeCallbacks(getmRunnable  );
            getmHandler = null;
        }
    }

    @Override
    public void onAuthErrorRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DynamicToast.makeError(VideoStreamActivity.this, "Auth error").show();
            }
        });
    }

    @Override
    public void onAuthSuccessRtmp() {
        runOnUiThread( ( ) -> DynamicToast.makeError(VideoStreamActivity.this, "Auth success").show() );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_start_stop:
                if (!rtmpCamera1.isStreaming()) {
                    if (rtmpCamera1.isRecording()
                            || rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
                        button.setText(R.string.stop_button);
                        rtmpCamera1.startStream( StreamingUrl);
                    } else {

                        DynamicToast.makeError(this, "Error preparing stream, This device cant do it").show();
                    }
                } else {
                    button.setText(R.string.start_button);
                    rtmpCamera1.stopStream();
                    Stop_Timmer();
                }
                break;
            case R.id.switch_camera:
                try {
                    rtmpCamera1.switchCamera();
                } catch ( CameraOpenException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.b_record:
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (!rtmpCamera1.isRecording()) {
                        try {
                            if (!folder.exists()) {
                                folder.mkdir();
                            }
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                            currentDateAndTime = sdf.format(new Date());
                            if (!rtmpCamera1.isStreaming()) {
                                if (rtmpCamera1.prepareAudio() && rtmpCamera1.prepareVideo()) {
                                    rtmpCamera1.startRecord(
                                            folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
                                    bRecord.setText(R.string.stop_record);
                                    DynamicToast.makeSuccess(this, "Recording... ").show();
                                } else {
                                    DynamicToast.makeError(this, "Error preparing stream, This device cant do it").show();
                                }
                            } else {
                                rtmpCamera1.startRecord(
                                        folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
                                bRecord.setText(R.string.stop_record);
                                DynamicToast.make(this, "Recording... ").show();
                            }
                        } catch ( IOException e) {
                            rtmpCamera1.stopRecord();
                            bRecord.setText(R.string.start_record);
                            DynamicToast.make(this, e.getMessage()).show();
                        }
                    } else {
                        rtmpCamera1.stopRecord();
                        bRecord.setText(R.string.start_record);
                        DynamicToast.make(this,
                                "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath()).show();
                        currentDateAndTime = "";
                    }
                } else {
                    DynamicToast.makeError(this, "You need min JELLY_BEAN_MR2(API 18) for do it...").show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        rtmpCamera1.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && rtmpCamera1.isRecording()) {
            rtmpCamera1.stopRecord();
            bRecord.setText(R.string.start_record);
            DynamicToast.make(this,
                    "file " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath()).show();
            currentDateAndTime = "";
        }
        if (rtmpCamera1.isStreaming()) {
            rtmpCamera1.stopStream();
            button.setText(getResources().getString(R.string.start_button));
        }
        rtmpCamera1.stopPreview();
    }

    @Override
    public void onDoneCheckingStreamLivenessResult ( String resposne ) {
        if(resposne == null || resposne.isEmpty()){
            DynamicToast.makeError( this,"PLease check your connection" ).show();
            liveText.setVisibility( View.INVISIBLE );
        }else{
            liveText.setVisibility( View.VISIBLE );
        }
    }
}
