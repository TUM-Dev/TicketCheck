package de.tum.`in`.tca.ticketcheck.component.ticket.model

data class Customer(var lrzId: String = "",
                    var name: String = "",
                    var nrOfTickets: Int = 0) {

    fun filter(query: String?): Boolean {
        query?.let {
            return name.contains(it, true) || lrzId.contains(it, true)
        }

        return true
    }
}