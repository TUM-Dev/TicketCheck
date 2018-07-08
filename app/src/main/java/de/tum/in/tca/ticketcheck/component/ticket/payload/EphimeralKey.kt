package de.tum.`in`.tca.ticketcheck.component.ticket.payload

import com.google.gson.annotations.SerializedName


data class EphimeralKey(@SerializedName("customer_mail")
                        var customerMail: String = "",
                        @SerializedName("api_version")
                        var apiVersion: String = "")