package com.tokentm.sdk.components.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 属性动画工具类
 */
public class PropertyAnimUtils {


    /**
     * 执行盖章动画
     *
     * @param imprintView 印记view
     * @param sealView    印章view
     */
    public static void startStampAnim(View imprintView, View sealView,AnimatorListenerAdapter animatorListenerAdapter) {

        //印章的缩小然后放大,模拟落章的效果
        ObjectAnimator animator = ObjectAnimator.ofFloat(sealView, "scaleY", 1f, 0.7f, 1f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(sealView, "scaleX", 1f, 0.7f, 1f);
        AnimatorSet animSet1 = new AnimatorSet();
        animSet1.play(animator).with(animator1);
        animSet1.setDuration(900);
        animSet1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 落完章
                // 隐藏印章
                sealView.setVisibility(View.INVISIBLE);
                // 显示印记
                imprintView.setVisibility(View.VISIBLE);
                animatorListenerAdapter.onAnimationEnd(animation);
            }
        });

        //模拟印章移动效果
        ObjectAnimator anim = ObjectAnimator
                .ofFloat(sealView, "translationX", 0, -(sealView.getLeft() - imprintView.getLeft()));
        ObjectAnimator anim1 = ObjectAnimator
                .ofFloat(sealView, "translationY", 0, -(sealView.getTop() - imprintView.getTop()));

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim).with(anim1);
        animSet.setInterpolator(new AccelerateInterpolator());
        animSet.setDuration(900);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //开启落章动画
                animSet1.start();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                sealView.setVisibility(View.VISIBLE);
            }
        });
        //开启印章移动动画
        animSet.start();
    }
}
