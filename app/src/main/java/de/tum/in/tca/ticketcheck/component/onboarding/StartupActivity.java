package de.tum.in.tca.ticketcheck.component.onboarding;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import de.tum.in.tca.ticketcheck.R;
import de.tum.in.tca.ticketcheck.component.ticket.activity.EventsActivity;

/**
 * Entrance point of the App.
 */
public class StartupActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        this.startApp();
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
