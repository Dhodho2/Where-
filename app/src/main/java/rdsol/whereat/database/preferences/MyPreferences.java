package rdsol.whereat.database.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    /**
     * <strong>DefaultValue::</strong> Empty
     */

    public String getLastSelectedIntentFilePath() {
        return mSharedPreferences.getString("LastSelectedIntentFilePath", "");
    }

    public void setLastSelectedIntentFilePath(final String value) {
        mEditor.putString("LastSelectedIntentFilePath", value);
        mEditor.commit();
    } /**
     * <strong>DefaultValue::</strong> Empty
     */

    public String getAccountChannel() {
        return mSharedPreferences.getString("account_channel", "");
    }

    public void setAccountChannel(final String value) {
        mEditor.putString("account_channel", value);
        mEditor.commit();
    }

    /**
     * <strong>DefaultValue::</strong> false
     */

    public boolean getAccountBusiness() {
        return mSharedPreferences.getBoolean("AccountBusiness", true);
    }

    public void setAccountBusiness(final boolean value) {
        mEditor.putBoolean("AccountBusiness", value);
        mEditor.commit();
    }



    /**
     * <strong>DefaultValue::</strong> Empty
     */

    public String getMobileNumber() {
        return mSharedPreferences.getString("MobileNumber", "");
    }

    public void setMobileNumber(final String value) {
        mEditor.putString("MobileNumber", value);
        mEditor.commit();
    }


    /**
     * <strong>DefaultValue::</strong> Empty
     */

    public String getPropic() {
        return mSharedPreferences.getString("Propic", "");
    }

    public void setPropic(final String value) {
        mEditor.putString("Propic", value);
        mEditor.commit();
    }


    public String getLastSetPointsBalance() {
        return mSharedPreferences.getString("LastSetPointsBalance", "");
    }

    public void setLastSetPointsBalance(final String value) {
        mEditor.putString("LastSetPointsBalance", value);
        mEditor.commit();
    }

    public String getAPIToken() {
        return mSharedPreferences.getString("APIToken", "");
    }

    public void setAPIToken(final String value) {
        mEditor.putString("APIToken", value);
        mEditor.commit();
    }



    /**
     * <strong>DefaultValue::</strong> false
     */

    public boolean getIsCacheCommentsEnabled() {
        return mSharedPreferences.getBoolean("is_cache_comments", true);
    }

    public void setIsCacheCommentsEnabled(final boolean value) {
        mEditor.putBoolean("is_cache_comments", value);
        mEditor.commit();
    }
/**
     * <strong>DefaultValue::</strong> false
     */

    public boolean getIsFrontCameraMode() {
        return mSharedPreferences.getBoolean("is_frontCameraMode", false);
    }

    public void setIsFrontCameraMode(final boolean value) {
        mEditor.putBoolean("is_frontCameraMode", value);
        mEditor.commit();
    }
    public boolean getIsDarkMode() {
        return mSharedPreferences.getBoolean("dark_mode_check", false);
    }

    public void setIsDarkMode(final boolean value) {
        mEditor.putBoolean("dark_mode_check", value);
        mEditor.commit();
    }

    public boolean isUserLoggedIn(){
        return  mSharedPreferences.getBoolean("is_logged",false);
    }
    public void setUserLoggedIn(final boolean value){
        mEditor.putBoolean("is_logged" , value);
        mEditor.commit();
    }

    public String getEmail(){
        return  mSharedPreferences.getString("Email","Email Address");
    }
    public void setEmail(final String value){
        mEditor.putString("Email" , value);
        mEditor.commit();
    }


    public String getFullName(){
        return  mSharedPreferences.getString("FullName","User Name");
    }


    public void setUserId(final String value){
        mEditor.putString("UserId" , value);
        mEditor.commit();
    }
    public String getUserId(){
        return  mSharedPreferences.getString("UserId","1");
    }


    public void setFullName(final String value){
        mEditor.putString("FullName" , value);
        mEditor.commit();
    }
    public String getUserName(){
        return  mSharedPreferences.getString("username","User Name");
    }



    public void setUserName(final String value){
        mEditor.putString("username" , value);
        mEditor.commit();
    }


    /**
     * <strong>DefaultValue::</strong> Empty
     */

    public String getFCMToken() {
        return mSharedPreferences.getString("FCMToken", "");
    }

    public void setFCMToken(final String value) {
        mEditor.putString("FCMToken", value);
        mEditor.commit();
    }
    /**
     * <strong>DefaultValue::</strong> Empty
     */

    public String getInstallationID() {
        return mSharedPreferences.getString("install_id", "");
    }

    public void setInstallationID(final String value) {
        mEditor.putString("install_id", value);
        mEditor.commit();
    }

    public void LogOut(){
        setUserName( "User Name" );
        setEmail( "Email Address" );
        setIsDarkMode( false );
        setUserLoggedIn( false );
    }



    /**@param context Calling Class context*/
    public  MyPreferences( Context context) {
        mContext = context;
        final String PREF_NAME = "cache";

        /**
         * This will render the access of the preference data private only access by the application
         */
        int PRIVATE_MODE = 0;
        mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
    }
}
