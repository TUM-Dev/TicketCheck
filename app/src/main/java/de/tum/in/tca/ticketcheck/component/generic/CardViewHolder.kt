package de.tum.`in`.tca.ticketcheck.component.generic

import android.app.Activity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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