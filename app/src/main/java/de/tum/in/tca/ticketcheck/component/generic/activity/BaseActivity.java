package de.tum.in.tca.ticketcheck.component.generic.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.ticket.activity.EventsActivity;

/**
 * Takes care of the navigation drawer which might be attached to the activity and also handles up navigation
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Default layouts for user interaction
     */
    private final int mLayoutId;

    /**
     * Standard constructor for BaseActivity.
     * The given layout might include a DrawerLayout.
     *
     * @param layoutId Resource id of the xml layout that should be used to inflate the activity
     */
    public BaseActivity(int layoutId) {
        mLayoutId = layoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpLayout();
        setUpToolbar();
    }

    public void setUpLayout() {
        setContentView(mLayoutId);
    }

    public void setUpToolbar() {
        String parent = NavUtils.getParentActivityName(this);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null && (parent != null || this instanceof EventsActivity)) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStackImmediate();
                return true;
            }
            // Respond to the action bar's Up/Home button
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if(upIntent == null) {
                return false;
            }

            upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                // This activity is NOT part of this apps task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(this)
                                .addNextIntentWithParentStack(upIntent)
                                .startActivities();
            } else {
                // This activity is part of this apps task, so simply
                // navigate up to the logical parent activity.
                NavUtils.navigateUpTo(this, upIntent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
