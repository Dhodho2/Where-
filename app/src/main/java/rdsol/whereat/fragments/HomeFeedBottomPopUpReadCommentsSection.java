package rdsol.whereat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

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
import rdsol.whereat.database.room.entities.DBCommentMessage;
import rdsol.whereat.pojos.CommentsMessagesDataModel;
import rdsol.whereat.pojos.UserType;
import rdsol.whereat.processes.GetCommentsProcess;
import rdsol.whereat.processes.PostCommentProcess;

import static rdsol.whereat.activities.ViewWatchRead.MEDIA_ID_ROW;

public class HomeFeedBottomPopUpReadCommentsSection extends BottomSheetDialogFragment implements HttpReqCallBack , HttpReqCallCommentsBackData {
    private Fragment parent;

    public static HomeFeedBottomPopUpReadCommentsSection newInstance ( int getId_row ) {
        HomeFeedBottomPopUpReadCommentsSection.getId_row = getId_row;
        return new HomeFeedBottomPopUpReadCommentsSection();
    }

    @Override
    public void onAttach ( @NonNull Context context ) {
        super.onAttach( context );
        parent = getParentFragment();

    }

    private EmojiEditText chatEditText1;
   // private EmojiEditText commet;
   // ImageButton postCommentBtn;
    private static int getId_row;
    String comment_text;
    private ArrayList<DBCommentMessage> commentMessages;
    private ImageView enterChatView1;
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
                commentsRecyclerView.scrollToPosition( 0 );
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
    private CommentsRecyclerViewChats Adapter;
    private RecyclerView commentsRecyclerView;

    @Nullable
    @Override
    public View onCreateView ( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        View vw = inflater.inflate( R.layout.fragemnt_pop_bottom_drawer_read_comments, container, false );


        commentMessages = new ArrayList<>();

        commentsRecyclerView = vw.findViewById( R.id.recyclerview );
        commentsRecyclerView.setHasFixedSize( true );

        chatEditText1 = vw.findViewById( R.id.chat_edit_text1 );
        enterChatView1 = ( ImageView ) vw.findViewById( R.id.enter_chat1 );
        ImageView main_dialog_emoji = vw.findViewById( R.id.emojiButton );
       // postCommentBtn = vw.findViewById( R.id.enter_chat1 );



        chatEditText1 = vw.findViewById( R.id.chat_edit_text1 );
        enterChatView1 = vw. findViewById( R.id.enter_chat1 );


        commentsMessagesDataModel = new ViewModelProvider( this ).get( CommentsMessagesDataModel.class );
        commentsRecyclerView.addItemDecoration( new RecyclerViewItemSeparator( 1 ) );
        commentsMessagesDataModel.getLiveData().getAll( Integer.parseInt( MEDIA_ID_ROW ) ).observe( this, datalist -> {

            Adapter = new CommentsRecyclerViewChats( datalist, getContext() );
            commentsRecyclerView.setAdapter( Adapter );
            //LastPosition = datalist.size() - 1;
           // commentsRecyclerView.scrollToPosition( LastPosition );
        } );
        commentsMessagesDataModel.lastComments( Integer.parseInt( MEDIA_ID_ROW ) );



        chatEditText1.setOnKeyListener( keyListener );

        enterChatView1.setOnClickListener( clickListener );

        //chatEditText1.addTextChangedListener( watcher1 );

       // AndroidUtilities.showKeyboard( postCommentBtn, getContext() );
        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView( vw ).build( chatEditText1 );

        main_dialog_emoji.setOnClickListener( ev -> {
            emojiPopup.toggle(); // Toggles visibility of the Popup.

        } );
        /*postCommentBtn.setOnClickListener( ev -> {
            comment_text = commet.getText().toString();
            if ( comment_text.isEmpty() ) {
                DynamicToast.makeError( getContext(), "Cant Post Empty Comment !" ).show();
            } else {
                DynamicToast.make( getContext(), "Posting Comment ..." ).show();
                PostCommentProcess postCommentProcess = new PostCommentProcess( HomeFeedBottomPopUpReadCommentsSection.this );
                postCommentProcess.execute( getId_row + "", comment_text );
                AndroidUtilities.hideKeyboard( postCommentBtn, getContext() );
                dismiss();
            }
            if ( parent instanceof HomeFragment ) {
                HomeFragment HomeFragment = ( HomeFragment ) parent;
                // AndroidUtilities.hideKeyboard(  postCommentBtn,getContext());
            }

        } );*/

        return vw;
    }



    private void sendMessage ( final String messageText, final String userType ) {
        if ( messageText.trim().length() == 0 ) {


            DynamicToast.make( getContext(), "Cant send empty message! " ).show();

            return;
        }



        final ScheduledExecutorService exec = Executors.newScheduledThreadPool( 1 );

        exec.schedule( ( ) -> {


            commentsMessagesDataModel.lastComments( Integer.parseInt( MEDIA_ID_ROW ) );


            PostCommentProcess postCommentProcess = new PostCommentProcess( HomeFeedBottomPopUpReadCommentsSection.this);


            postCommentProcess.execute( MEDIA_ID_ROW, messageText );


            GetCommentsProcess getCommentsProcess = new GetCommentsProcess( HomeFeedBottomPopUpReadCommentsSection.this );
            getCommentsProcess.execute( MEDIA_ID_ROW + "" );


        }, 1, TimeUnit.SECONDS );

    }


    @Override
    public void onCompleteResponse ( String response ) {

    }

    @Override
    public void onCompleteResponse ( List<DBCommentMessage> objects ) {
        for ( DBCommentMessage comment : objects ) {

            RoomDB.getRoomInstance( getContext() ).getTable().save( comment );
        }
    }
}
