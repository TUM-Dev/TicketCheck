package de.tum.in.tca.ticketcheck.api;

import android.content.Context;

import java.util.UUID;

import de.tum.in.tca.ticketcheck.utils.Const;
import de.tum.in.tca.ticketcheck.utils.Utils;

/**
 * This provides methods to authenticate this app installation with the tumcabe server and other instances requiring a pki.
 */
public class AuthenticationManager {
    private static String uniqueID;

    /**
     * Gets an unique id that identifies this device.
     * Should only reset after a reinstall or wiping of the settingsPrefix.
     *
     * @return Unique device id
     */
    public static synchronized String getDeviceID(Context context) {
        if (uniqueID == null) {
            uniqueID = Utils.getSetting(context, Const.PREF_UNIQUE_ID, "");
            if ("".equals(uniqueID)) {
                uniqueID = UUID.randomUUID()
                               .toString();
                Utils.setSetting(context, Const.PREF_UNIQUE_ID, uniqueID);
            }
        }
        return uniqueID;
    }

}
