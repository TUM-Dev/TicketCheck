package de.tum.in.tca.ticketcheck.component.ticket;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;

/**
 * Mock class, only used to provide static event data for testing purposes
 * TODO: replace this when the actual data is available
 */
public class TicketsController {

    /**
     * Only for testing purposes as server calls are not yet implemented
     * -> TODO: replace with real data
     *
     * @return
     */
    public static List<AdminTicket> getTickets() {
        List<AdminTicket> tickets = new ArrayList<>();
        tickets.add(new AdminTicket("Toni Wampe", "gr234we", 5,
                new DateTime(2018, 7, 20, 13, 14, 0, 0), new DateTime(2018, 7, 22, 13, 14, 0, 0)
        ));
        tickets.add(new AdminTicket("Max Mustermann", "gb123er", 5,
                new DateTime(2018, 8, 2, 13, 14, 0, 0), new DateTime(2018, 8, 4, 13, 14, 0, 0)
        ));
        tickets.add(new AdminTicket("Max Mustermann", "gb123er", 5,
                new DateTime(2018, 8, 2, 13, 14, 0, 0), new DateTime(2018, 8, 4, 13, 14, 0, 0)
        ));
        tickets.add(new AdminTicket("Max Mustermann", "gb123er", 5,
                new DateTime(2018, 8, 2, 13, 14, 0, 0), new DateTime(2018, 8, 4, 13, 14, 0, 0)
        ));
        tickets.add(new AdminTicket("Max Mustermann", "gb123er", 5,
                new DateTime(2018, 8, 2, 13, 14, 0, 0), new DateTime(2018, 8, 4, 13, 14, 0, 0)
        ));
        tickets.add(new AdminTicket("Max Mustermann", "gb123er", 5,
                new DateTime(2018, 8, 2, 13, 14, 0, 0), new DateTime(2018, 8, 4, 13, 14, 0, 0)
        ));
        tickets.add(new AdminTicket("Max Mustermann", "gb123er", 5,
                new DateTime(2018, 8, 2, 13, 14, 0, 0), new DateTime(2018, 8, 4, 13, 14, 0, 0)
        ));
        tickets.add(new AdminTicket("Max Mustermann", "gb123er", 5,
                new DateTime(2018, 8, 2, 13, 14, 0, 0), new DateTime(2018, 8, 4, 13, 14, 0, 0)
        ));
        tickets.add(new AdminTicket("Max Mustermann", "gb123er", 5,
                new DateTime(2018, 8, 2, 13, 14, 0, 0), new DateTime(2018, 8, 4, 13, 14, 0, 0)
        ));
        tickets.add(new AdminTicket("Max Mustermann", "gb123er", 5,
                new DateTime(2018, 8, 2, 13, 14, 0, 0), new DateTime(2018, 8, 4, 13, 14, 0, 0)
        ));
        tickets.add(new AdminTicket("Max Mustermann", "gb123er", 5,
                new DateTime(2018, 8, 2, 13, 14, 0, 0), new DateTime(2018, 8, 4, 13, 14, 0, 0)
        ));

        return tickets;
    }
    public static List<AdminTicket> getrefreshTickets() {
        List<AdminTicket> tickets = new ArrayList<>();
        tickets.add(new AdminTicket("Toni Wampe", "gr234we", 5,
                new DateTime(2018, 7, 20, 13, 14, 0, 0), new DateTime(2018, 7, 22, 13, 14, 0, 0)
        ));
        tickets.add(new AdminTicket("Max Mustermann", "gb123er", 5,
                new DateTime(2018, 8, 2, 13, 14, 0, 0), new DateTime(2018, 8, 4, 13, 14, 0, 0)
        ));

        return tickets;
    }
}


