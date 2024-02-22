package rdsol.whereat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;


public abstract class BaseActivity extends AppCompatActivity {
    /**
     * This seeks to allow all the objects that are to be used in an activity .
     * Also this is usually for global data reference initialisation .
     */
    protected void initObjects ( ) {
    }

    ;

    /**
     * This is meant to initialise all the views that are required at runtime of the application
     * This also seeks to remove all the clutter in the onCreate() ;
     * But will highly discourage any state retainment
     */
    protected void initViews ( ) {
    }


    /**
     * Will handle all the events for example all the view click listeners
     */
    protected void initListeners ( ) {
    }

    @Override
    public void startActivity ( Intent intent ) {
        super.startActivity( intent );
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter ( ) {
        // overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onBackPressed ( ) {
        super.onBackPressed();
        overridePendingTransitionExit();
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit ( ) {
        //  overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    protected void clearAllActivitiesToOpenNewActivity ( Context context, Class clazz ) {
        Intent intent = new Intent( context, clazz );
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity( intent );
        finish();
    }

    @Override
    public boolean onOptionsItemSelected ( MenuItem item ) {
        if ( item.getItemId() == android.R.id.home ) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
}
