package rdsol.whereat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import rdsol.whereat.R;
import rdsol.whereat.callbacks.HttpReqCallBack;
import rdsol.whereat.processes.PostCommentProcess;
import rdsol.whereat.utils.AndroidUtilities;

public class HomeFeedBottomPopUpCommentSection extends BottomSheetDialogFragment implements HttpReqCallBack {
    private Fragment parent;

    public static HomeFeedBottomPopUpCommentSection newInstance ( int getId_row ) {
        HomeFeedBottomPopUpCommentSection.getId_row = getId_row;
        return new HomeFeedBottomPopUpCommentSection();
    }

    @Override
    public void onAttach ( @NonNull Context context ) {
        super.onAttach( context );
        parent = getParentFragment();

    }


    private EmojiEditText commet;
    TextView postCommentBtn;
    private static int getId_row;
    String comment_text;

    @Nullable
    @Override
    public View onCreateView ( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View vw = inflater.inflate( R.layout.bottom_homefeed_comment_box, container, false );
        commet = vw.findViewById( R.id.commet );
        ImageButton main_dialog_emoji = vw.findViewById( R.id.main_dialog_emoji );
        postCommentBtn = vw.findViewById( R.id.postCommentBtn );
        commet.requestFocus();
        AndroidUtilities.showKeyboard( postCommentBtn, getContext() );
        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView( vw ).build( commet );

        main_dialog_emoji.setOnClickListener( ev -> {
            emojiPopup.toggle(); // Toggles visibility of the Popup.
            if ( emojiPopup.isShowing() ) {
                main_dialog_emoji.setImageResource( R.drawable.ic_keyboard_black_24dp );
            } else {
                main_dialog_emoji.setImageResource( R.drawable.emoji_ios_category_smileysandpeople );

            }
        } );
        postCommentBtn.setOnClickListener( ev -> {
            comment_text = commet.getText().toString();
            if ( comment_text.isEmpty() ) {
                DynamicToast.makeError( getContext(), "Cant Post Empty Comment !" ).show();
            } else {
                DynamicToast.make( getContext(), "Posting Comment ..." ).show();
                PostCommentProcess postCommentProcess = new PostCommentProcess( HomeFeedBottomPopUpCommentSection.this );
                postCommentProcess.execute( getId_row + "", comment_text );
                AndroidUtilities.hideKeyboard( postCommentBtn, getContext() );
                dismiss();
            }
            if ( parent instanceof HomeFragment ) {
                HomeFragment HomeFragment = ( HomeFragment ) parent;
                // AndroidUtilities.hideKeyboard(  postCommentBtn,getContext());
            }

        } );

        return vw;
    }


    @Override
    public void onCompleteResponse ( String response ) {

    }
}
