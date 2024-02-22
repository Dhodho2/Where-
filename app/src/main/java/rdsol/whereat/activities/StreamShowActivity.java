package rdsol.whereat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.GLException;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.daasuu.camerarecorder.CameraRecordListener;
import com.daasuu.camerarecorder.CameraRecorder;
import com.daasuu.camerarecorder.CameraRecorderBuilder;
import com.daasuu.camerarecorder.LensFacing;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

import rdsol.whereat.R;
import rdsol.whereat.database.preferences.MyPreferences;

import static rdsol.whereat.utils.AndroidUtilities.runOnUIThread;

public class StreamShowActivity extends AppCompatActivity {
    private MaterialButton startButton, endButton;
private Context context = StreamShowActivity.this;
    // CameraRecorder cameraRecorder;
    //GLSurfaceView glSurfaceView;
    private ImageView camera_switch;
    private GLSurfaceView sampleGLView;
    protected CameraRecorder cameraRecorder;
    private String filepath;
    private TextView recordBtn;
    protected LensFacing lensFacing = LensFacing.BACK;
    protected int cameraWidth = 1280;
    protected int cameraHeight = 720;
    protected int videoWidth = 720;
    protected int videoHeight = 720;
    private AlertDialog filterDialog;
    private boolean toggleClick = false;
    private boolean isFront = false;
private TextView timmer_counter;
    private MyPreferences myPreferences;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_stream_show );

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
      

        myPreferences = new MyPreferences( context );
        camera_switch = findViewById( R.id.camera_switch );
        startButton = findViewById( R.id.startButton );
        endButton = findViewById( R.id.endButton );
        timmer_counter = findViewById( R.id.timmer_counter );


        camera_switch.setOnClickListener( ev -> {
            releaseCamera ( );
            if(lensFacing == LensFacing.BACK){
                lensFacing = LensFacing.FRONT;
            }else {
                lensFacing = LensFacing.BACK;
            }
            toggleClick =true;
            /*runOnUIThread( ()->{


                cameraRecorder.release();
                if(cameraRecorder.isStarted()){
                    cameraRecorder.stop();
                }
                cameraRecorder =null;
                if(isFront){
                    isFront = true;
                    setUpCamera();
                }else{
                    isFront = false;
                    setUpFrontViewCamera();
                }
            } );*/

        } );
        startButton.setOnClickListener( ev -> {
            // camera_switch.setVisibility( View.GONE );
            startButton.setVisibility( View.GONE );
            endButton.setVisibility( View.VISIBLE );
            filepath = getVideoFilePath();
            cameraRecorder.start( filepath );
            timmer_counter.setVisibility( View.VISIBLE );
            timmer_counter.setText( "0 00" );
            thread = new Thread(  ){
                @Override
                public void run ( ) {
                    super.run();
                    try {
                        while ( !isInterrupted() ){
                            sleep( 1000 );
                            runOnUIThread( ()->{
                                Calendar calendar = Calendar.getInstance();
                                int hr = calendar.get( Calendar.HOUR_OF_DAY );
                                int min = calendar.get( Calendar.MINUTE );
                                int sec = calendar.get( Calendar.SECOND );
                                String timee = String.format( "%02d : %02d : %02d ",hr,min,sec );
                                int t =0;
                                if(Counter==60){
                                     t = Counter/60;
                                }else
                                if(Counter < 60){

                                 //   timmer_counter.setText( Counter );
                                }else  if(Counter==(60 * 60)){
                                  //  timmer_counter.setText( Counter / (60 * 60) );
                                }else{
                                  //  timmer_counter.setText( Counter );
                                }

                                Counter ++;
                            } );
                        }
                    } catch ( InterruptedException e ) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        } );
        endButton.setOnClickListener( ev -> {
            endButton.setVisibility( View.GONE );
            startButton.setVisibility( View.VISIBLE );
            //  camera_switch.setVisibility( View.VISIBLE );
            cameraRecorder.stop();
            thread.stop();
        } );



    }
    Thread thread ;
    @Override
    public void onPause ( ) {
        super.onPause();
        releaseCamera();
    }

    @Override
    public void onResume ( ) {
        super.onResume();
        //  setUpCamera();
        if(myPreferences.getIsFrontCameraMode()){
            setUpCamera();
        }else{
            setUpFrontViewCamera ();
        }
    }

    private void releaseCamera ( ) {
        if ( sampleGLView != null ) {
            sampleGLView.onPause();
        }

        if ( cameraRecorder != null ) {
            cameraRecorder.stop();
            cameraRecorder.release();
            cameraRecorder = null;
        }

        if ( sampleGLView != null ) {
            // ((FrameLayout) findViewById(R.id.wrap_view)).removeView(sampleGLView);
            sampleGLView = null;
        }
    }

private Timer timer = new Timer(  );
    int Counter = 0;

    private void setUpFrontViewCamera ( ) {
        setUpCameraView();

        cameraRecorder = new CameraRecorderBuilder( this, sampleGLView )
                //.recordNoFilter(true)
                .cameraRecordListener( new CameraRecordListener() {
                    @Override
                    public void onGetFlashSupport ( boolean flashSupport ) {
                        runOnUIThread( ( ) -> {
                            // findViewById(R.id.btn_flash).setEnabled(flashSupport);
                        } );

                    }

                    @Override
                    public void onRecordComplete ( ) {
                        exportMp4ToGallery( context, filepath );
                    }

                    @Override
                    public void onRecordStart ( ) {

                    }

                    @Override
                    public void onError ( Exception exception ) {
                        Log.e( "CameraRecorder", exception.toString() );
                    }

                    @Override
                    public void onCameraThreadFinish ( ) {
                        if ( toggleClick ) {
                            runOnUIThread( ( ) -> setUpCamera() );

                        }
                        toggleClick = false;
                    }
                } )
                .videoSize( videoWidth, videoHeight )
                .cameraSize( cameraWidth, cameraHeight )
                .lensFacing( LensFacing.FRONT )
                .build();


    }

    private void setUpCamera ( ) {
        setUpCameraView();

        cameraRecorder = new CameraRecorderBuilder( this, sampleGLView )
                //.recordNoFilter(true)
                .cameraRecordListener( new CameraRecordListener() {
                    @Override
                    public void onGetFlashSupport ( boolean flashSupport ) {
                        runOnUIThread( ( ) -> {
                            // findViewById(R.id.btn_flash).setEnabled(flashSupport);
                        } );

                    }

                    @Override
                    public void onRecordComplete ( ) {
                        exportMp4ToGallery( context, filepath );
                    }

                    @Override
                    public void onRecordStart ( ) {

                    }

                    @Override
                    public void onError ( Exception exception ) {
                        Log.e( "CameraRecorder", exception.toString() );
                    }

                    @Override
                    public void onCameraThreadFinish ( ) {
                        if ( toggleClick ) {
                            runOnUIThread( ( ) -> setUpCamera() );

                        }
                        toggleClick = false;
                    }
                } )
                .videoSize( videoWidth, videoHeight )
                .cameraSize( cameraWidth, cameraHeight )
                .lensFacing( lensFacing )
                .build();


    }

    @Override
    public void onStop ( ) {
        super.onStop();
        releaseCamera();
    }

    private void setUpCameraView ( ) {
        sampleGLView = findViewById( R.id.flsurface );
//runOnUIThread( ()-> sampleGLView = findViewById( R.id.flsurface ) );
    }


    private interface BitmapReadyCallbacks {
        void onBitmapReady ( Bitmap bitmap );
    }

    private void captureBitmap ( final BitmapReadyCallbacks bitmapReadyCallbacks ) {
        sampleGLView.queueEvent( ( ) -> {
            EGL10 egl = ( EGL10 ) EGLContext.getEGL();
            GL10 gl = ( GL10 ) egl.eglGetCurrentContext().getGL();
            Bitmap snapshotBitmap = createBitmapFromGLSurface( sampleGLView.getMeasuredWidth(), sampleGLView.getMeasuredHeight(), gl );

            runOnUIThread( ( ) -> {
                bitmapReadyCallbacks.onBitmapReady( snapshotBitmap );
            } );
        } );
    }

    private Bitmap createBitmapFromGLSurface ( int w, int h, GL10 gl ) {

        int bitmapBuffer[] = new int[ w * h ];
        int bitmapSource[] = new int[ w * h ];
        IntBuffer intBuffer = IntBuffer.wrap( bitmapBuffer );
        intBuffer.position( 0 );

        try {
            gl.glReadPixels( 0, 0, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer );
            int offset1, offset2, texturePixel, blue, red, pixel;
            for ( int i = 0; i < h; i++ ) {
                offset1 = i * w;
                offset2 = ( h - i - 1 ) * w;
                for ( int j = 0; j < w; j++ ) {
                    texturePixel = bitmapBuffer[ offset1 + j ];
                    blue = ( texturePixel >> 16 ) & 0xff;
                    red = ( texturePixel << 16 ) & 0x00ff0000;
                    pixel = ( texturePixel & 0xff00ff00 ) | red | blue;
                    bitmapSource[ offset2 + j ] = pixel;
                }
            }
        } catch ( GLException e ) {
            Log.e( "CreateBitmap", "createBitmapFromGLSurface: " + e.getMessage(), e );
            return null;
        }

        return Bitmap.createBitmap( bitmapSource, w, h, Bitmap.Config.ARGB_8888 );
    }

    public void saveAsPngImage ( Bitmap bitmap, String filePath ) {
        try {
            File file = new File( filePath );
            FileOutputStream outStream = new FileOutputStream( file );
            bitmap.compress( Bitmap.CompressFormat.PNG, 100, outStream );
            outStream.close();

        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }


    public static void exportMp4ToGallery ( Context context, String filePath ) {
        final ContentValues values = new ContentValues( 2 );
        values.put( MediaStore.Video.Media.MIME_TYPE, "video/mp4" );
        values.put( MediaStore.Video.Media.DATA, filePath );
        context.getContentResolver().insert( MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                values );
        context.sendBroadcast( new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse( "file://" + filePath ) ) );
    }

    public static String getVideoFilePath ( ) {
        return getAndroidMoviesFolder().getAbsolutePath() + "/" + new SimpleDateFormat( "yyyyMM_dd-HHmmss" ).format( new Date() ) + "cameraRecorder.mp4";
    }

    public static File getAndroidMoviesFolder ( ) {
        return Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_MOVIES );
    }

    private static void exportPngToGallery ( Context context, String filePath ) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE );
        File f = new File( filePath );
        Uri contentUri = Uri.fromFile( f );
        mediaScanIntent.setData( contentUri );
        context.sendBroadcast( mediaScanIntent );
    }

    public static String getImageFilePath ( ) {
        return getAndroidImageFolder().getAbsolutePath() + "/" + new SimpleDateFormat( "yyyyMM_dd-HHmmss" ).format( new Date() ) + "cameraRecorder.png";
    }

    public static File getAndroidImageFolder ( ) {
        return Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES );
    }
}
