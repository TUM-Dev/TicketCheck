package de.tum.in.tca.ticketcheck.component.ticket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;

import java.util.List;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.ticket.model.AdminTicket;

/**
 * Created by YR on 2016/04/12.
 */
public class ticketListAdapter extends BaseAdapter {

    private List<AdminTicket> list = null;

    private Context context = null;

    private LayoutInflater inflater = null;

    public ticketListAdapter(List<AdminTicket> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AdminTicket adminTicket = list.get(position);

        View view = inflater.inflate(R.layout.ticket_list_item, null);

        TextView nameView = view.findViewById(R.id.name);
        TextView lrzIdView =  view.findViewById(R.id.lrz_id);
        TextView ticketTypeView = view.findViewById(R.id.ticket_type);
        TextView purchaseTimeView =  view.findViewById(R.id.purchase);
        TextView redeemTimeView = view.findViewById(R.id.redemption);

        nameView.setText(adminTicket.getName());
        lrzIdView.setText(adminTicket.getLrzId());
        ticketTypeView.setText(adminTicket.getTicketType());
        purchaseTimeView.setText(DateTimeFormat.shortDateTime().print(adminTicket.getPurchaseDate()));
        redeemTimeView.setText(DateTimeFormat.shortDateTime().print(adminTicket.getRedeemDate()));


        return view;
    }
}
