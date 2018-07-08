package de.tum.in.tumcampusapp.component.ui.overview;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import static de.tum.in.tca.ticketcheck.utils.Const.CARD_POSITION_PREFERENCE_SUFFIX;


/**
 * Card manager, manages inserting, dismissing, updating and displaying of cards
 */
public final class CardManager {

    public static final String SHOW_SUPPORT = "show_support";
    public static final String SHOW_LOGIN = "show_login";
    public static final String SHOW_TOP_NEWS = "show_top_news";


    public static final int CARD_LOGIN = 14;
    public static final int CARD_EVENTS = 17;


    private CardManager() {}


}
