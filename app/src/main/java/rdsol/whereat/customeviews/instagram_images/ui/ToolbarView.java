package rdsol.whereat.customeviews.instagram_images.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

import rdsol.whereat.R;


public class ToolbarView extends RelativeLayout {


    CustomTextView mTitle;

    CustomTextView mIconBack;

    CustomTextView mIconNext;

    private WeakReference<OnClickBackListener> mWrBackMenuListener;
    private WeakReference<OnClickNextListener> mWrNextListener;
    private WeakReference<OnClickTitleListener> mWrTitleListener;


    private void init(Context context) {
        View view = View.inflate(context, R.layout.toolbar_view, this);
        mTitle = view.findViewById( R.id.mTitle );
        mTitle.setText( "Gallery" );
        mIconBack = view.findViewById( R.id.mIconBack );
        mIconNext = view.findViewById( R.id.mIconNext );
        mIconBack.setVisibility( INVISIBLE );
        mIconBack.setOnClickListener( ev->{
            mWrBackMenuListener.get().onClickBack();
        } );

        mTitle.setOnClickListener( ev->{
            mWrTitleListener.get().onClickTitle();
        } );

        mIconNext.setOnClickListener( ev->{
            mWrNextListener.get().onClickNext();
        } );


    }
public CustomTextView getBackIcon(){
        return mIconBack;
}
public CustomTextView getmIconNext(){
        return mIconNext;
}
    public ToolbarView(Context context) {
        super(context);
        init(context);
    }

    public ToolbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ToolbarView setOnClickBackMenuListener(OnClickBackListener listener) {
        this.mWrBackMenuListener = new WeakReference<>(listener);
        return this;
    }

    public ToolbarView setOnClickTitleListener(OnClickTitleListener listener) {
        this.mWrTitleListener = new WeakReference<>(listener);
        return this;
    }

    public ToolbarView setOnClickNextListener(OnClickNextListener listener) {
        this.mWrNextListener = new WeakReference<>(listener);
        return this;
    }

    public ToolbarView setTitle(String title) {
        mTitle.setText(title);
        return this;
    }

    public ToolbarView hideNext() {
        mIconNext.setVisibility(GONE);
        return this;
    }

    public ToolbarView showNext() {
        mIconNext.setVisibility(VISIBLE);
        return this;
    }

    public interface OnClickBackListener {
        void onClickBack ( );
    }

    public interface OnClickNextListener {
        void onClickNext ( );
    }

    public interface OnClickTitleListener {
        void onClickTitle ( );
    }

}
