package rdsol.whereat.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import java.util.Collections;

import rdsol.whereat.R;
import rdsol.whereat.adapters.recyclerview.ChatListViewRecyclerAdapter;
import rdsol.whereat.customeviews.RecyclerViewItemSeparator;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.datamodels.DirectMesagesModel;

import static rdsol.whereat.netwox.HandleRequests.saveMessage;

public class FragementChatView extends Fragment {


    private MyPreferences myPreferences;
    private ChatListViewRecyclerAdapter adapter;
    private View root;

    public View onCreateView ( @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        String userChatID = getActivity().getIntent().getStringExtra( "user_chats" );
        getActivity().getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.background_mainn));
        root = inflater.inflate( R.layout.fragment_chat_messages, container, false );
        myPreferences = new MyPreferences( getContext() );
        ImageView emojiButton = root.findViewById( R.id.emojiButton );
        ImageButton enter_chat1 = root.findViewById( R.id.enter_chat1 );
        EmojiEditText chat_edit_text1 = root.findViewById( R.id.chat_edit_text1 );
        RecyclerView recyclerView = root.findViewById( R.id.recyclerview );
        recyclerView.addItemDecoration( new RecyclerViewItemSeparator( 3 ) );
        final EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView( root ).build( chat_edit_text1 );
        emojiButton.setOnClickListener( ev -> {
            emojiPopup.toggle(); // Toggles visibility of the Popup.

        } );
        DirectMesagesModel hwModel = new ViewModelProvider( this ).get( DirectMesagesModel.class );
        hwModel.getLiveData().getAllChatsFor(userChatID ,myPreferences.getUserId()).observe( getViewLifecycleOwner(), datalist -> {
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ) {
                datalist.sort( ( o1, o2 ) -> o1.getId_rows() - o2.getId_rows() );
            }else{
                Collections.sort( datalist, ( o1, o2 ) -> o1.getId_rows() - o2.getId_rows() );
            }
            adapter = new ChatListViewRecyclerAdapter( datalist, getContext() );
            recyclerView.setAdapter( adapter );
            recyclerView.scrollToPosition(  datalist.size()-1);

        } );
        // DynamicToast.make( getContext(),"chat id::" + userChatID ).show();
        enter_chat1.setOnClickListener( ev->{
            String message = chat_edit_text1.getText().toString();
            if(message.isEmpty()){
                DynamicToast.makeWarning( getContext(),"Message cant be empty" ).show();;
                return;
            }
            chat_edit_text1.setText( "" );
            new AsyncTask<Void,Void,String>(){
                @SuppressLint ("StaticFieldLeak")
                @Override
                protected String doInBackground ( Void... voids ) {
                    return saveMessage(message ,userChatID);
                }

                @Override
                protected void onPostExecute ( String s ) {
                    super.onPostExecute( s );
                    if(s==null){
                        DynamicToast.makeError( getContext(),"Failed to send Message"  ).show();
                    }else{

                        if(s.equals( "ok" )){
                            hwModel.getData();
                        }
                    }
                }
            }.execute(  );

        } );


        return root;
    }
}
