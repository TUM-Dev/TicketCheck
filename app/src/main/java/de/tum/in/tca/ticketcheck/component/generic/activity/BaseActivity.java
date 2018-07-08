package de.tum.in.tca.ticketcheck.component.generic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.generic.drawer.DrawerMenuHelper;
import de.tum.in.tca.ticketcheck.component.ticket.EventsActivity;
import de.tum.in.tca.ticketcheck.utils.Const;
import de.tum.in.tca.ticketcheck.utils.Utils;

/**
 * Takes care of the navigation drawer which might be attached to the activity and also handles up navigation
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Default layouts for user interaction
     */
    private final int mLayoutId;

    protected DrawerLayout mDrawerLayout;
    protected NavigationView mDrawerList;
    protected View headerView;

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
        setUpDrawer();
        setUpToolbar();
    }

    public void setUpLayout() {
        setContentView(mLayoutId);
    }

    public void setUpDrawer() {
        // Get handles to navigation drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);

        // Setup the navigation drawer if present in the layout
        if (mDrawerList != null && mDrawerLayout != null) {
            // Set personalization in the navdrawer
            headerView = mDrawerList.inflateHeaderView(R.layout.drawer_header);
            TextView nameText = headerView.findViewById(R.id.nameTextView);
            TextView emailText = headerView.findViewById(R.id.emailTextView);

            nameText.setText(Utils.getSetting(this, Const.CHAT_ROOM_DISPLAY_NAME,
                                              getString(R.string.token_not_enabled)));

            StringBuffer email = new StringBuffer(Utils.getSetting(this, Const.LRZ_ID, ""));
            if (email.toString().isEmpty()) {
                emailText.setVisibility(View.GONE);
            } else {
                email.append("@mytum.de");
            }
            emailText.setText(email);

            DrawerMenuHelper helper = new DrawerMenuHelper(this, mDrawerLayout);
            helper.populateMenu(mDrawerList.getMenu());

            // Set the NavigationDrawer's click listener
            mDrawerList.setNavigationItemSelectedListener(helper);

            if (Utils.getSettingBool(this, Const.RAINBOW_MODE, false)) {
                headerView.setBackgroundResource(R.drawable.drawer_header_rainbow);
            } else {
                headerView.setBackgroundResource(R.drawable.wear_tuition_fee);
            }
        }
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
