package de.tum.in.tca.ticketcheck.component.ticket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.tum.in.tca.ticketcheck.component.ticket.model.Event;
import de.tum.in.tca.ticketcheck.utils.Const;

/**
 * TODO: combine this with KinoAdapter
 */
public class AdminDetailsAdapter extends FragmentStatePagerAdapter {

    private final int count; //number of pages
    private final List<String> titles = new ArrayList<>();  // titles shown in the pagerStrip

    public AdminDetailsAdapter(FragmentManager fm, Collection<Event> events) {
        super(fm);
        count = events.size();

        // get all titles
        for (Event event : events) {
            titles.add(event.getTitle());
        }

        this.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new AdminDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(Const.POSITION, position);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // returns the titles for the pagerStrip
        String title = titles.get(position);
        // TODO: Why is there a substring substring needed? (Copied from KinoAdapter...)
        return title.substring(title.indexOf(':') + 1)
                .trim();
    }
}