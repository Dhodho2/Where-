package rdsol.whereat.customeviews.instagram_images.photo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import rdsol.whereat.R;
import rdsol.whereat.customeviews.instagram_images.cameraview.CameraView;
import rdsol.whereat.customeviews.instagram_images.models.Session;

/*
import com.octopepper.mediapickerinstagram.R;
import com.octopepper.mediapickerinstagram.commons.cameraview.CameraView;
import com.octopepper.mediapickerinstagram.commons.models.Session;
*/

public class CapturePhotoFragment extends Fragment {

    private static final String TAG = "CapturePhotoFragment";

    private static final Interpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final String DIR_YUMMYPETS = "/yummypets";

    CameraView mCameraPhotoView;

    ImageView mBtnTakePhoto;

    View mShutter;

    ImageView mFlashPhoto;

    ImageView mSwitchCamera;

    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private int mCurrentFlash;
    private Handler mBackgroundHandler;
    private Session mSession = Session.getInstance();




    public static CapturePhotoFragment newInstance() {
        return new CapturePhotoFragment();
    }

    private void initViews() {
        if (mCameraPhotoView != null) {
            mCameraPhotoView.addCallback(mCallback);
        }
    }

    private void animateShutter() {
        mShutter.setVisibility(View.VISIBLE);
        mShutter.setAlpha(0.f);

        ObjectAnimator alphaInAnim = ObjectAnimator.ofFloat(mShutter, "alpha", 0f, 0.8f);
        alphaInAnim.setDuration(100);
        alphaInAnim.setStartDelay(100);
        alphaInAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator alphaOutAnim = ObjectAnimator.ofFloat(mShutter, "alpha", 0.8f, 0f);
        alphaOutAnim.setDuration(200);
        alphaOutAnim.setInterpolator(DECELERATE_INTERPOLATOR);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(alphaInAnim, alphaOutAnim);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mShutter.setVisibility(View.GONE);
            }
        });
        animatorSet.start();
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    File dirDest =
                            new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                    DIR_YUMMYPETS);
                    File file;
                    String fileName = "yummypets_"+
                            TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) +".jpg";
                    if (dirDest.exists()) {
                        Log.d(TAG, "exists " + dirDest.getAbsolutePath());
                        file = new File(dirDest, fileName);
                    } else {
                        if (dirDest.mkdir()) {
                            file = new File(dirDest, fileName);
                        } else {
                            file = null;
                        }
                    }
                    OutputStream os = null;
                    if (file != null) {
                        try {
                            os = new FileOutputStream(file);
                            os.write(data);
                            os.close();
                        } catch (IOException e) {
                            Log.w(TAG, "Cannot write to " + file, e);
                        } finally {
                            if (os != null) {
                                try {
                                    os.close();
                                } catch (IOException e) {
                                    // Ignore
                                }
                            }
                        }
                        mSession.setFileToUpload(file);
                    }
                }
            });
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCameraPhotoView.start();
    }

    @Override
    public void onPause() {
        mCameraPhotoView.stop();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            mBackgroundHandler.getLooper().quitSafely();
            mBackgroundHandler = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.capture_photo_view, container, false);
        mCameraPhotoView = v.findViewById( R.id.mCameraPhotoView );
        mBtnTakePhoto = v.findViewById( R.id.mBtnTakePhoto );
        mCameraPhotoView = v.findViewById( R.id.mCameraPhotoView );
        mCameraPhotoView = v.findViewById( R.id.mCameraPhotoView );
        mShutter = v.findViewById( R.id.mShutter );
        mFlashPhoto = v.findViewById( R.id.mFlashPhoto );
        mSwitchCamera = v.findViewById( R.id.mSwitchCamera );
        initViews();
        mFlashPhoto.setOnClickListener( ev->{
            if (mCameraPhotoView != null) {
                mCurrentFlash = ( mCurrentFlash + 1 ) % FLASH_OPTIONS.length;
                mFlashPhoto.setImageResource( FLASH_ICONS[ mCurrentFlash ] );
                mCameraPhotoView.setFlash( FLASH_OPTIONS[ mCurrentFlash ] );
                mCameraPhotoView.takePicture();
            }
        } );
        mSwitchCamera.setOnClickListener( ev->{
            if (mCameraPhotoView != null) {
                int facing = mCameraPhotoView.getFacing();
                mCameraPhotoView.setFacing(facing == CameraView.FACING_FRONT ?
                        CameraView.FACING_BACK : CameraView.FACING_FRONT);
            }
        } );
        mBtnTakePhoto.setOnClickListener( ev->{
            mCameraPhotoView.takePicture();
            animateShutter();
        } );
        return v;
    }

}
