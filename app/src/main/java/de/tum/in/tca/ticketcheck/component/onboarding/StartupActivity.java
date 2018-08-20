package de.tum.in.tca.ticketcheck.component.onboarding;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.ticket.activity.EventsActivity;
import de.tum.in.tca.ticketcheck.utils.Const;
import de.tum.in.tca.ticketcheck.utils.Utils;

/**
 * Entrance point of the App.
 */
public class StartupActivity extends AppCompatActivity {

    private int tapCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Show a loading screen during boot
        setContentView(R.layout.activity_startup);

        // init easter egg (logo)
        ImageView tumLogo = findViewById(R.id.startup_tum_logo);
        if (Utils.getSettingBool(this, Const.RAINBOW_MODE, false)) {
            tumLogo.setImageResource(R.drawable.tum_logo_rainbow);
        } else {
            tumLogo.setImageResource(R.drawable.tum_logo);
        }

        tapCounter = 0;
        View background = findViewById(R.id.startup_background);
        background.setOnClickListener(view -> {
            tapCounter++;
            if (tapCounter % 3 == 0) {
                tapCounter = 0;

                // use the other logo and invert the setting
                boolean rainbowEnabled = Utils.getSettingBool(this, Const.RAINBOW_MODE, false);
                if (rainbowEnabled) {
                    tumLogo.setImageResource(R.drawable.tum_logo);
                } else {
                    tumLogo.setImageResource(R.drawable.tum_logo_rainbow);
                }
                Utils.setSetting(this, Const.RAINBOW_MODE, !rainbowEnabled);
            }
        });
        background.setSoundEffectsEnabled(false);

        this.init();
    }

    private void init() {

        //Check that we have a private key setup in order to authenticate this device
        //AuthenticationManager am = new AuthenticationManager(this);
        //am.generatePrivateKey(null);

        startApp();
    }

    private void startApp() {
        // Get views to be moved
        final View background = findViewById(R.id.startup_background);
        final ImageView tumLogo = findViewById(R.id.startup_tum_logo);
        final TextView loadingText = findViewById(R.id.startup_loading);

        // Make some position calculations
        final int actionBarHeight = getActionBarHeight();
        final float screenHeight = background.getHeight();

        // Setup animation
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(background, "translationY", background.getTranslationX(), actionBarHeight - screenHeight),
                ObjectAnimator.ofFloat(tumLogo, "alpha", 1, 0, 0),
                ObjectAnimator.ofFloat(loadingText, "alpha", 1, 0, 0),
                ObjectAnimator.ofFloat(tumLogo, "translationY", 0, -screenHeight / 3),
                ObjectAnimator.ofFloat(loadingText, "translationY", 0, -screenHeight / 3)
        );
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // NOOP
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Start the demo Activity if demo mode is set
                Intent intent = new Intent(StartupActivity.this, EventsActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // NOOP
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // NOOP
            }
        });
        set.start();
    }

    /**
     * Gets the height of the actionbar
     *
     * @return Actionbar height
     */
    protected int getActionBarHeight() {
        int[] attrs = {R.attr.actionBarSize};
        TypedArray values = obtainStyledAttributes(attrs);
        try {
            return values.getDimensionPixelSize(0, 0);
        } finally {
            values.recycle();
        }
    }
}
