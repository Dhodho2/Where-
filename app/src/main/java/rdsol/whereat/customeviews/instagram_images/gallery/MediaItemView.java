package rdsol.whereat.customeviews.instagram_images.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;

import rdsol.whereat.R;
import rdsol.whereat.customeviews.instagram_images.modules.ReboundModule;
import rdsol.whereat.customeviews.instagram_images.modules.ReboundModuleDelegate;

/*
import com.octopepper.mediapickerinstagram.R;
import com.octopepper.mediapickerinstagram.commons.modules.ReboundModule;
import com.octopepper.mediapickerinstagram.commons.modules.ReboundModuleDelegate;*/

public class MediaItemView extends RelativeLayout implements ReboundModuleDelegate {


    ImageView mMediaThumb;

    private File mCurrentFile;
    private ReboundModule mReboundModule = ReboundModule.getInstance(this);
    private WeakReference<MediaItemViewListener> mWrListener;

    void setListener(MediaItemViewListener listener) {
        this.mWrListener = new WeakReference<>(listener);
    }

    public MediaItemView(Context context) {
        super(context);
        View v = View.inflate(context, R.layout.media_item_view, this);
        mMediaThumb = v.findViewById( R.id.mMediaThumb );
       // ButterKnife.bind(this, v);
    }

    public void bind(File file) {
        mCurrentFile = file;
        mReboundModule.init(mMediaThumb);
       // Log.e( "xxxx", "bind: " + file.getAbsolutePath());
      //  Log.e( "xxxx", "bind222222: " + file.getAbsoluteFile());

        if(file.getAbsolutePath().endsWith( ".mp4" )){

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getContext(), Uri.parse( file.getAbsolutePath() ) );
            Bitmap bitmap = retriever
                    .getFrameAtTime(100000,MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
            Glide.with( getContext() )

                    .load( bitmap ).centerCrop()
                    .thumbnail(Glide.with(getContext()).load(bitmap))
                    .into( mMediaThumb );
        }else{
        Picasso.get()
                .load(Uri.fromFile(file))
                .resize(350, 350)
                .centerCrop()
                .placeholder(R.drawable.ic_main_icon)
                .error(R.drawable.ic_main_icon)
                .noFade()
                .into(mMediaThumb);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    public void onTouchActionUp() {
        mWrListener.get().onClickItem(mCurrentFile);
    }
}
