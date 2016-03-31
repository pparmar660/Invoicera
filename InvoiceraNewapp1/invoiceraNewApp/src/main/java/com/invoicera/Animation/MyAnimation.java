package com.invoicera.Animation;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;

import static android.view.animation.Animation.AnimationListener;

/**
 * Created by Parvesh on 15/6/15.
 */
public class MyAnimation {
    Context ctx;

    public MyAnimation(Context ctx) {

        this.ctx = ctx;
    }

    public void amimation(TableLayout layout, int animationId) {

        android.view.animation.Animation animationInfo;

        animationInfo = AnimationUtils.loadAnimation(ctx,
                animationId);

        animationInfo.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        layout.startAnimation(animationInfo);



    }

/*    public void SlideToAbove(final TableLayout rl_footer) {
        Animation slide = null;
        slide = new TranslateAnimation(RELATIVE_TO_SELF, 0.0f,
                RELATIVE_TO_SELF, 0.0f, RELATIVE_TO_SELF,
                0.0f, RELATIVE_TO_SELF, -5.0f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        rl_footer.startAnimation(slide);

        slide.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {



            }

        });

    }

    public void SlideToDown(final TableLayout rl_footer) {
        Animation slide = null;
        slide = new TranslateAnimation(RELATIVE_TO_SELF, 0.0f,
                RELATIVE_TO_SELF, 0.0f, RELATIVE_TO_SELF,
                0.0f, RELATIVE_TO_SELF, 5.2f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        rl_footer.startAnimation(slide);

        slide.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                rl_footer.clearAnimation();

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        rl_footer.getWidth(), rl_footer.getHeight());
                lp.setMargins(0, rl_footer.getWidth(), 0, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                rl_footer.setLayoutParams(lp);

            }

        });

    }*/


}
