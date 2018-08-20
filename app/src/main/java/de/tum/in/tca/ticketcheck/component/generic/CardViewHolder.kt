package de.tum.`in`.tca.ticketcheck.component.generic

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View

open class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var currentCard: Card? = null
    private val activity = itemView.context as Activity

    init {
        itemView.setOnClickListener {
            val intent = currentCard?.getIntent() ?: return@setOnClickListener
            ContextCompat.startActivity(activity, intent, null)
        }
    }

}