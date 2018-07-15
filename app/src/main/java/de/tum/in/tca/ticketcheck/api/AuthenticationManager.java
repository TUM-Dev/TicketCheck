package de.tum.in.tca.ticketcheck.api;

import android.content.Context;
import android.util.Base64;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.UUID;

import de.tum.in.tca.ticketcheck.api.exception.NoPrivateKey;
import de.tum.in.tca.ticketcheck.api.exception.NoPublicKey;
import de.tum.in.tca.ticketcheck.utils.Const;
import de.tum.in.tca.ticketcheck.utils.RSASigner;
import de.tum.in.tca.ticketcheck.utils.Utils;

/**
 * This provides methods to authenticate this app installation with the tumcabe server and other instances requiring a pki.
 */
public class AuthenticationManager {
    private final static String ALGORITHM = "RSA";
    private final static int RSA_KEY_SIZE = 1024;
    private static String uniqueID;
    private final Context mContext;

    public AuthenticationManager(Context c) {
        mContext = c;
    }

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

    public static KeyPairGenerator getKeyPairGeneratorInstance() {
        try {
            return KeyPairGenerator.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            // We don't support platforms without RSA
            throw new AssertionError(e);
        }
    }

    public static KeyFactory getKeyFactoryInstance() {
        try {
            return KeyFactory.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            // We don't support platforms without RSA
            throw new AssertionError(e);
        }
    }

    /**
     * Get the private key as string.
     *
     * @return
     * @throws NoPrivateKey
     */
    private String getPrivateKeyString() throws NoPrivateKey {
        String key = Utils.getSetting(mContext, Const.PRIVATE_KEY, "");
        if (key.isEmpty()) {
            throw new NoPrivateKey();
        }
        return key;
    }

    /**
     * Loads the private key as an object.
     *
     * @return The private key object
     */
    private PrivateKey getPrivateKey() throws NoPrivateKey {
        byte[] privateKeyBytes = Base64.decode(this.getPrivateKeyString(), Base64.DEFAULT);
        try {
            return getKeyFactoryInstance().generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        } catch (InvalidKeySpecException e) {
            Utils.log(e);
        }
        return null;
    }

    /**
     * Sign a message with the currently stored private key.
     *
     * @param data String to be signed
     * @return signature used to verify this request
     * @throws NoPrivateKey
     */
    public String sign(String data) throws NoPrivateKey {
        RSASigner signer = new RSASigner(this.getPrivateKey());
        return signer.sign(data);
    }


}
