package de.tum.`in`.tca.ticketcheck.component.generic.drawer

import android.content.Context
import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.view.Menu
import android.view.MenuItem
import de.tum.`in`.tca.ticketcheck.R
import de.tum.`in`.tca.ticketcheck.component.settings.UserPreferencesActivity
import de.tum.`in`.tca.ticketcheck.component.ticket.activity.EventsActivity
import de.tum.`in`.tca.ticketcheck.component.ticket.activity.TicketScanActivity

class DrawerMenuHelper(private val mContext: Context, private val mDrawerLayout: DrawerLayout) : NavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        mDrawerLayout.closeDrawers()
        val intent = menuItem.intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        mContext.startActivity(intent)
        return true
    }

    fun populateMenu(navigationMenu: Menu) {
        // General information which mostly can be used without a TUMonline token
        val commonTumMenu = navigationMenu.addSubMenu(R.string.common_info)
        for (item in COMMON_TUM) {
            commonTumMenu.add(item.titleRes)
                    .setIcon(item.iconRes).intent = Intent(mContext, item.activity)
        }
    }

    companion object {
        private val COMMON_TUM = arrayOf(
                SideNavigationItem(R.string.events_tickets, R.drawable.ic_events, EventsActivity::class.java),
                SideNavigationItem(R.string.scanner, R.drawable.ic_events, TicketScanActivity::class.java),
                SideNavigationItem(R.string.settings, R.drawable.ic_action_settings, UserPreferencesActivity::class.java)

        )

    }
}
