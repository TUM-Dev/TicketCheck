package de.tum.in.tca.ticketcheck.component.ticket.model;

import java.util.List;

public interface AdminTicketRefreshCallback {
    void handle(List<AdminTicket> list);
}
