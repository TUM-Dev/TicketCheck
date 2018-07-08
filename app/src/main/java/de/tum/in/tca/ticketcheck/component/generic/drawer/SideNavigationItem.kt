package de.tum.`in`.tca.ticketcheck.component.generic.drawer

import android.app.Activity

data class SideNavigationItem(val titleRes: Int,
                              val iconRes: Int,
                              val activity: Class<out Activity>? = null)
