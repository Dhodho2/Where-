package rdsol.whereat.adapters.recyclerview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.allattentionhere.autoplayvideos.AAH_VideoImage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputEditText;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.skybase.humanizer.DateHumanizer;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.ionbit.ionalert.IonAlert;
import rdsol.whereat.R;
import rdsol.whereat.activities.AccountChannelProfile;
import rdsol.whereat.activities.GeneralFragmentHandlerActivity;
import rdsol.whereat.activities.VideoStreamActivity;
import rdsol.whereat.activities.ViewWatchRead;
import rdsol.whereat.callbacks.HttpReqCallBack;
import rdsol.whereat.callbacks.HttpReqCallBackData;
import rdsol.whereat.customeviews.TriangleLabelView;
import rdsol.whereat.database.RoomDB;
import rdsol.whereat.database.preferences.MyPreferences;
import rdsol.whereat.database.room.entities.DBHomeFeed;
import rdsol.whereat.fragments.HomeFeedBottomPopUpCommentSection;
import rdsol.whereat.fragments.HomeFeedBottomPopUpReadCommentsSection;
import rdsol.whereat.processes.CheckIfUserHasPaidForShowProcess;
import rdsol.whereat.processes.DeleteMediaItemProcess;
import rdsol.whereat.processes.FollowingProcess;
import rdsol.whereat.processes.GetHomeFeedProcess;
import rdsol.whereat.processes.LikeUnLikePostingProcess;
import rdsol.whereat.processes.PostCommentProcess;
import rdsol.whereat.processes.UnFollowingProcess;
import rdsol.whereat.processes.UserReedToPayWithCreditProcess;
import rdsol.whereat.utils.AndroidUtilities;
import rdsol.whereat.utils.DateUtils;

import static rdsol.whereat.activities.ViewWatchRead.MEDIA_ID_ROW;
import static rdsol.whereat.utils.NotificationsUtils.setReminder;

public class HomeFeedRecyclerViewAdapter extends com.allattentionhere.autoplayvideos.AAH_VideosAdapter implements HttpReqCallBack, HttpReqCallBackData, CheckIfUserHasPaidForShowProcess.HttpGetShowPaymentCheck, UserReedToPayWithCreditProcess.HttpMakePaymentReq {
    private List<DBHomeFeed> items;
    private Context context;
    private TextInputEditText comment_home_feed;
    public static DBHomeFeed SelectedOBJECT_ = null;
    private IonAlert ionAlert;
    private Fragment fragment;
    private String ShowDateOn = "";
    private MyPreferences myPreferences;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public HomeFeedRecyclerViewAdapter(List<DBHomeFeed> items, Context context, Fragment fragment) {
        this.items = items;
        this.context = context;

        myPreferences = new MyPreferences(context);
        this.fragment = fragment;
    }


    @Override
    public void onBindViewHolder(com.allattentionhere.autoplayvideos.AAH_CustomViewHolder holder, int position) {
        DBHomeFeed feeditem = items.get(position);

        if (feeditem.getVideo_url().endsWith(".mp4")) {
            // Log.e( "xxxx", "onBindViewHolder:|||| " + feeditem.getVideo_url());
            ((CustomeViewHolder) holder).llimages.setVisibility(View.GONE);
            ((CustomeViewHolder) holder).llVideo.setVisibility(View.VISIBLE);
            ((CustomeViewHolder) holder).setImageUrl(feeditem.getVideo_url());
            ((CustomeViewHolder) holder).setVideoUrl(feeditem.getVideo_url());
            //  ( (CustomeViewHolder)holder).img_playback.getCustomVideoView().setSource( Uri.parse( feeditem.getVideo_url() ) );
        }

        ((CustomeViewHolder) holder).titleOfCardAccount.setText("@" + feeditem.getAccount_name());

        String fDate = "", tobeDate = "";
        try {

            //Log.e( "xxxx", "onBindViewHolder:|||| " + feeditem.getDated());
            Date date = format.parse(feeditem.getDated());

            fDate = (DateHumanizer.humanize(date, DateHumanizer.TYPE_PRETTY_EVERYTHING));


        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((CustomeViewHolder) holder).dated.setText(fDate);
        ((CustomeViewHolder) holder).dated.setVisibility(View.VISIBLE);
        ((CustomeViewHolder) holder).comments_count.setText(feeditem.getLikes_count() + " Likes ," + feeditem.getComments_count() + " Comments");

        if (feeditem.getIs_schedule() == 1) {
            String startTime = feeditem.getStart_end_time().split("=")[0];
            String endTime = feeditem.getStart_end_time().split("=")[1];
            try {


                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = format.parse(feeditem.getOndate() + " " + startTime);
                tobeDate = (DateHumanizer.humanize(date, DateHumanizer.TYPE_PRETTY_EVERYTHING));
                //  Log.e( "xxxx", "onBindViewHolder:]]]]] " + tobeDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ((CustomeViewHolder) holder).ribbonView2.setVisibility(View.VISIBLE);
            String tickCount = Integer.parseInt( feeditem.getRating()) > 1 ? " Ticks." : " Tick.";
            ((CustomeViewHolder) holder).ribbonView2.setText(tobeDate + " \n" + feeditem.getRating() + tickCount);
            ((CustomeViewHolder) holder).description.setText(/*"Show,"  +*/ ""
                    + startTime + " to " + endTime
            );
/*String data = DateHumanizer.humanize(feeditem.getOndate(),DateHumanizer.TYPE_PRETTY_EVERYTHING);
            Log.e( "xxxx", "onBindViewHolder: Humansor" + data);*/
            try {
                Date date = format.parse(feeditem.getOndate());
                if (DateUtils.isToday(date)) {
                    ((CustomeViewHolder) holder).labelTriangle.setSecondaryText("Today ");
                    ((CustomeViewHolder) holder).labelTriangle.setPrimaryText(feeditem.getRating() + " ");
                } else {
                    ((CustomeViewHolder) holder).labelTriangle.setPrimaryText(feeditem.getRating() + " ");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            ((CustomeViewHolder) holder).labelTriangle.setVisibility(View.GONE);
            ((CustomeViewHolder) holder).description.setText(feeditem.getTitle());


        }

        Uri videoURI = Uri.parse(feeditem.getVideo_url());


        Glide.with(context).load(videoURI).placeholder(R.drawable.ic_main_icon).fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(((CustomeViewHolder) holder).image_thumbnail);
        Glide.with(context).load(videoURI).placeholder(R.drawable.ic_main_icon).fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(((CustomeViewHolder) holder).image_thumbnail_two);

        Glide.with(context)
                .load(feeditem.getOwner_avatar())
                .placeholder(R.drawable.ic_account_circle_deep_purple_600_24dp).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((CustomeViewHolder) holder).avatar);


        ((CustomeViewHolder) holder).avatar.setOnClickListener(ev -> {
            context.startActivity(new Intent(context, AccountChannelProfile.class)
                    .putExtra("account", feeditem.getOwner() + "")
            );
        });
        if (feeditem.getIs_image() == 0) {
            ((CustomeViewHolder) holder).image_thumbnail.setOnClickListener(ev -> {
                context.startActivity(new Intent(context, ViewWatchRead.class)
                        .putExtra("id_media", feeditem.getId_row() + "")
                        .putExtra("video_file", feeditem.getVideo_url())
                        .putExtra("file", feeditem.getId_row())
                        .putExtra("stream", "no")
                        .putExtra("dated", feeditem.getDated())
                        .putExtra("liked", feeditem.getLiked())
                        .putExtra("comments", feeditem.getComments_count())
                );
            });

        } else if (!feeditem.getOwner().equals(myPreferences.getUserId())) {

            ((CustomeViewHolder) holder).image_thumbnail.setOnClickListener(ev -> {
                /*context.startActivity( new Intent( context, ViewWatchRead.class )
                        .putExtra( "id_media", feeditem.getId_row() + "" )
                        .putExtra( "stream", "yes" )
                        .putExtra( "file", feeditem.getId_row() )

                        .putExtra( "dated", feeditem.getDated() )
                        .putExtra( "liked", feeditem.getLiked() )
                        .putExtra( "comments", feeditem.getComments_count() )
                );*/
                SelectedOBJECT_ = feeditem;

                CheckIfUserHasPaidForShowProcess ifUserHasPaidForShowProcess = new CheckIfUserHasPaidForShowProcess(HomeFeedRecyclerViewAdapter.this);
                ifUserHasPaidForShowProcess.execute(feeditem.getId_row() + "");
                ionAlert = new IonAlert(context, IonAlert.PROGRESS_TYPE);
                ionAlert.setSpinKit("WanderingCubes");
                ionAlert.setIcon(R.drawable.ic_main_icon);

                ionAlert.showCancelButton(false);
                ionAlert.show();


            });
        }

        if (feeditem.getOwner().equals(myPreferences.getUserId()) && feeditem.getIs_schedule() == 1) {
            ((CustomeViewHolder) holder).image_thumbnail.setOnClickListener(ev -> {
                context.startActivity(new Intent(context, VideoStreamActivity.class)
                        .putExtra("id_media", feeditem.getId_row() + "")


                        .putExtra("stream", "owner")

                        .putExtra("timmed", feeditem.getStart_end_time()
                        )
                );
            });
        }

        ((CustomeViewHolder) holder).like.setLiked(feeditem.getLiked() == 1);
        ((CustomeViewHolder) holder).comments_count.setOnClickListener(ev -> {
            HomeFeedBottomPopUpReadCommentsSection mBottomNavigationDrawerFragment = HomeFeedBottomPopUpReadCommentsSection.newInstance(feeditem.getId_row());
            MEDIA_ID_ROW = feeditem.getId_row() + "";
            mBottomNavigationDrawerFragment.show(fragment.getChildFragmentManager(), "hhh");


        });
        ((CustomeViewHolder) holder).commentPlaceHolder.setOnClickListener(ev -> {
            HomeFeedBottomPopUpCommentSection mBottomNavigationDrawerFragment = HomeFeedBottomPopUpCommentSection.newInstance(feeditem.getId_row());

            mBottomNavigationDrawerFragment.show(fragment.getChildFragmentManager(), "vvv");

            if (!mBottomNavigationDrawerFragment.isHidden()) {
                AndroidUtilities.showKeyboard(((CustomeViewHolder) holder).commentPlaceHolder, context);
            }

            AndroidUtilities.showKeyboard(((CustomeViewHolder) holder).commentPlaceHolder, context);

        });
        TextInputEditText.OnKeyListener keyListener = (v, keyCode, event) -> {

            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                // Perform action on key press

                TextInputEditText editText = (TextInputEditText) v;

                if (v == ((CustomeViewHolder) holder).comment_home_feed) {
                    if (editText.getText().toString().isEmpty()) {
                        DynamicToast.makeError(context, "Cant Post Empty Comment !").show();
                    } else {
                        DynamicToast.make(context, "Posting Comment ...").show();
                        PostCommentProcess postCommentProcess = new PostCommentProcess(HomeFeedRecyclerViewAdapter.this);
                        postCommentProcess.execute(feeditem.getId_row() + "", editText.getText().toString());
                    }


                }
                if (((CustomeViewHolder) holder).comment_home_feed != null)
                    ((CustomeViewHolder) holder).comment_home_feed.setText("");

                return true;
            }
            return false;

        };
        ((CustomeViewHolder) holder).comment_home_feed.setOnKeyListener(keyListener);
        // PowerMenu powerMenu;
        // holder.more.setVisibility( View.INVISIBLE );
        ((CustomeViewHolder) holder).more.setOnClickListener(v -> {
// holder.onMenuItemClick( new ActionMenuItem( context,0,1,1,1,"HHH" ) )
            //  v.showContextMenu();
            List<PowerMenuItem> menuItems = new ArrayList<>();

            String payForShow = "Pay For Show";
            String streamShow = "Stream Show";
            String RemindMe = "Remind Me";
            if (feeditem.getOwner().equals(myPreferences.getUserId())) {
                menuItems.add(new PowerMenuItem("Delete"));
                //  menuItems.add( new PowerMenuItem( streamShow ) );

            } else {
                /*Log.e( "xxxx", "onBindViewHolder:VVVVV " + feeditem.getCheck_if_following() );
                Log.e( "xxxx", "onBindViewHolder:VVVVV " + feeditem.getAccount_name() );*/
                if (feeditem.getCheck_if_following() == 1) {
                    menuItems.add(new PowerMenuItem("UnFollow"));
                } else {
                    menuItems.add(new PowerMenuItem("Follow"));
                }
            }

            if (feeditem.getIs_schedule() == 1) {
                ShowDateOn = feeditem.getOndate() + " " + feeditem.getStart_end_time().split("=")[0];
                // Log.e( "xxxx", "onBindViewHolder: " + ShowDateOn);
                try {

                    Date date = format.parse(feeditem.getOndate() + " " + feeditem.getStart_end_time().split("=")[0]);
                    // Date date = format.parse("2020-05-27 09:15"  );
                    if (!DateUtils.isAfterDay(new Date(), date)) {
                        menuItems.add(new PowerMenuItem(RemindMe));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //  menuItems.add( new PowerMenuItem( payForShow ) );
            }


            PowerMenu powerMenuu = new PowerMenu.Builder(context)
                    .addItemList(menuItems) // list has "Novel", "Poerty", "Art"

                    .setAnimation(MenuAnimation.DROP_DOWN) // Animation start point (TOP | LEFT).
                    .setMenuRadius(10f) // sets the corner radius.
                    .setMenuShadow(10f) // sets the shadow.

                    .setTextGravity(Gravity.CENTER)
                    .setBackgroundColor(myPreferences.getIsDarkMode() ?
                            context.getResources().getColor(R.color.cardview_dark_background) : context.getResources().getColor(R.color.cardview_light_background)
                    )
                    .setMenuColor(myPreferences.getIsDarkMode() ?
                            context.getResources().getColor(R.color.white) : context.getResources().getColor(R.color.black)
                    ).setTextColor(myPreferences.getIsDarkMode() ?
                            context.getResources().getColor(R.color.black) : context.getResources().getColor(R.color.white)
                    ).setAutoDismiss(true)
                    .setSelectedMenuColor(context.getResources().getColor(R.color.colorPrimary))
                    .setOnMenuItemClickListener((position1, item) -> {

                        if (item.getTitle().equals(RemindMe)) {
                            // remember this date time is det to this fomart , 2020-12-30 09:53;
                            setReminder(context, ViewWatchRead.class, ShowDateOn);

                        }
                        if (item.getTitle().equals("Delete")) {
                            DynamicToast.make(context, "Deleting Post ...").show();
                            notifyItemRemoved(position);
                            DeleteMediaItemProcess deleteMediaItemProcess = new DeleteMediaItemProcess(HomeFeedRecyclerViewAdapter.this);
                            deleteMediaItemProcess.execute(feeditem.getId_row() + "");
                        }
                        if (item.getTitle().equals("Enter Showroom")) {
                            DynamicToast.make(context, "Following " + feeditem.getAccount_name() + " ...").show();
                            FollowingProcess mFollowingProcess = new FollowingProcess(HomeFeedRecyclerViewAdapter.this);
                            mFollowingProcess.execute(feeditem.getOwner() + "");
                        }
                        if (item.getTitle().equals("UnFollow")) {
                            DynamicToast.make(context, "UnFollowing " + feeditem.getAccount_name() + " ...").show();
                            UnFollowingProcess unFollowingProcess = new UnFollowingProcess(HomeFeedRecyclerViewAdapter.this);
                            unFollowingProcess.execute(feeditem.getOwner() + "");
                        }
                        if (item.getTitle().equals(payForShow)) {
                            SelectedOBJECT_ = feeditem;
                            context.startActivity(new Intent(context, GeneralFragmentHandlerActivity.class)
                                    .putExtra("show_id", feeditem.getId_row() + "")

                                    .putExtra("fragment_name", "Purchase Link Tickets"));
                        }
                        if (item.getTitle().equals(streamShow)) {
                            context.startActivity(new Intent(context, VideoStreamActivity.class)
                                    .putExtra("id_media", feeditem.getId_row() + "")


                                    .putExtra("stream", "owner")

                                    .putExtra("timmed", feeditem.getStart_end_time()
                                    )
                            );
                        }


                        // }
                    })
                    .build();
            //finalPowerMenu = powerMenu;
            powerMenuu.showAsAnchorCenter(((CustomeViewHolder) holder).more);
        });

        ((CustomeViewHolder) holder).like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                LikeUnLikePostingProcess likeUnLikePostingProcess = new LikeUnLikePostingProcess();
                likeUnLikePostingProcess.execute(feeditem.getId_row() + "", "true");
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                LikeUnLikePostingProcess likeUnLikePostingProcess = new LikeUnLikePostingProcess();
                likeUnLikePostingProcess.execute(feeditem.getId_row() + "", "false");

            }
        });

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public com.allattentionhere.autoplayvideos.AAH_CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item, parent, false);
        return new CustomeViewHolder(view);
    }

    @Override
    public void onCompleteResponse(String response) {
        if (response.equals("ok")) {
            GetHomeFeedProcess getHomeFeedProcess = new GetHomeFeedProcess(HomeFeedRecyclerViewAdapter.this);
            getHomeFeedProcess.execute();
        }
    }

    @Override
    public void onCompleteResponse(List<DBHomeFeed> objects) {
        RoomDB roomDB = RoomDB.getRoomInstance(context);
        if (objects != null && objects.size() > 0) {
            roomDB.getTables().clearTable();
        }
        for (DBHomeFeed items : objects) {
            roomDB.getTables().save(items);
        }
    }

    @Override
    public void onResultFinnished(String booleanString) {
        if (ionAlert != null) {
            ionAlert.dismiss();
        }
        if (!booleanString.equals("true")) {


            IonAlert r = new IonAlert(context, IonAlert.NORMAL_TYPE);
            r.setTitleText("Send Ticks to watch the show");
            r.setContentText(" " + myPreferences.getLastSetPointsBalance() + "  Link-Ticks for " + SelectedOBJECT_.getRating());
            r.setCancelText("No!");
            r.setConfirmText("Yes, please!");
            r.setConfirmTextSize(12);
            r.showCancelButton(false);
            r.setCanceledOnTouchOutside(true);
            r.setCancelClickListener(sDialog -> {
                r.dismiss();
                sDialog.setTitleText("Cancelled!")

                        .setContentText("Since You have declined to pay you cant watch the Show !")
                        .setConfirmText("OK")
                        .showCancelButton(false)
                        .setCancelClickListener(null)
                        .setConfirmClickListener(null)
                        .changeAlertType(IonAlert.ERROR_TYPE);
            })
                    .setConfirmClickListener(sDialog -> {
                        r.dismiss();
                        UserReedToPayWithCreditProcess userReedToPayWithCreditProcess = new UserReedToPayWithCreditProcess(HomeFeedRecyclerViewAdapter.this);
                        userReedToPayWithCreditProcess.execute(SelectedOBJECT_.getId_row() + "");

                        ionAlert = new IonAlert(context, IonAlert.PROGRESS_TYPE);
                        ionAlert.setSpinKit("WanderingCubes");
                        ionAlert.showCancelButton(false);
                        ionAlert.show();
                    });
            r.show();
        } else {
            context.startActivity(new Intent(context, ViewWatchRead.class)
                    .putExtra("id_media", SelectedOBJECT_.getId_row() + "")
                    .putExtra("stream", "yes")
                    .putExtra("file", SelectedOBJECT_.getId_row())

                    .putExtra("dated", SelectedOBJECT_.getDated())
                    .putExtra("liked", SelectedOBJECT_.getLiked())
                    .putExtra("comments", SelectedOBJECT_.getComments_count()));
        }

    }

    @Override
    public void onDoneRequest(String response) {
        if (ionAlert != null) {
            ionAlert.dismiss();
        }
        ionAlert = null;
        if (response.equals("broke")) {
            IonAlert q = new IonAlert(context, IonAlert.ERROR_TYPE);
            q.setTitleText("Response");
            q.setContentText("You do not have enough Link-Ticks to Watch the show!");
            q.setCanceledOnTouchOutside(true);
            // .setConfirmTextSize(12)
            q.showCancelButton(true);
            q.show();
        } else if (response.equals("paid")) {
            IonAlert h = new IonAlert(context, IonAlert.SUCCESS_TYPE);
            h.setTitleText("Response");
            h.setContentText("You have already Redeemed for this stream !");
            h.setConfirmText("Yes,Watch!");

            h.setConfirmClickListener(sDialog -> {
                h.dismiss();
                context.startActivity(new Intent(context, ViewWatchRead.class)
                        .putExtra("id_media", SelectedOBJECT_.getId_row() + "")
                        .putExtra("stream", "yes")
                        .putExtra("file", SelectedOBJECT_.getId_row())

                        .putExtra("dated", SelectedOBJECT_.getDated())
                        .putExtra("liked", SelectedOBJECT_.getLiked())
                        .putExtra("comments", SelectedOBJECT_.getComments_count()));
            });
            // .setConfirmTextSize(12)
            h.showCancelButton(true);
            h.show();
        } else if (response.equals("error_p")) {
            IonAlert r = new IonAlert(context, IonAlert.ERROR_TYPE);
            r.setTitleText("Response");
            ;
            r.setContentText("Please Try again ,Something went Wrong !");
            r.setConfirmClickListener(Dialog::dismiss);
            r.setCanceledOnTouchOutside(true);
            r.show();
        } else if (response.equals("ok")) {
            IonAlert r = new IonAlert(context, IonAlert.SUCCESS_TYPE);
            r.setTitleText("Response");
            r.setContentText("You have Redeemed !");
            r.setConfirmText("Yes,Watch!");

            r.setConfirmClickListener(sDialog -> {
                r.dismiss();
                context.startActivity(new Intent(context, ViewWatchRead.class)
                        .putExtra("id_media", SelectedOBJECT_.getId_row() + "")
                        .putExtra("stream", "yes")
                        .putExtra("file", SelectedOBJECT_.getId_row())

                        .putExtra("dated", SelectedOBJECT_.getDated())
                        .putExtra("liked", SelectedOBJECT_.getLiked())
                        .putExtra("comments", SelectedOBJECT_.getComments_count()));
            })
                    // .setConfirmTextSize(12)
                    .showCancelButton(true)
                    .show();
        }
    }


    public class CustomeViewHolder extends com.allattentionhere.autoplayvideos.AAH_CustomViewHolder {
        public TextView dated;
        public TextView description;
        public TextView comments_count;
        RelativeLayout llimages;
        LinearLayout llVideo;
        private com.mohan.ribbonview.RibbonView ribbonView2;
        public TextView titleOfCardAccount;
        public TextView commentPlaceHolder;
        public CircleImageView avatar;
        public LinearLayout llroot;
        private AAH_VideoImage img_playback;
        public LikeButton like;
        public ImageView image_thumbnail, image_thumbnail_two, more;
        public EditText comment_home_feed;
        public TriangleLabelView labelTriangle;

        CustomeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.labelTriangle = itemView.findViewById(R.id.labelTriangle);
            this.like = itemView.findViewById(R.id.like);
            this.llroot = itemView.findViewById(R.id.llroot);
            this.img_playback = itemView.findViewById(R.id.img_playback);
            this.comments_count = itemView.findViewById(R.id.comments_count);
            this.llimages = itemView.findViewById(R.id.llimages);
            this.llVideo = itemView.findViewById(R.id.llVideo);
            this.ribbonView2 = itemView.findViewById(R.id.ribbonView2);
            this.image_thumbnail_two = itemView.findViewById(R.id.image_thumbnail_two);

            this.titleOfCardAccount = itemView.findViewById(R.id.titleOfCardAccount);
            this.dated = itemView.findViewById(R.id.dated);
            this.description = itemView.findViewById(R.id.description);
            this.avatar = itemView.findViewById(R.id.avatar);
            this.image_thumbnail = itemView.findViewById(R.id.image_thumbnail);
            this.comment_home_feed = itemView.findViewById(R.id.comment_home_feed);
            this.more = itemView.findViewById(R.id.more);
            this.commentPlaceHolder = itemView.findViewById(R.id.commentPlaceHolder);

        }

        @Override
        public void videoStarted() {
            super.videoStarted();


            this.image_thumbnail_two.setVisibility(View.GONE);

        }
    }

}
