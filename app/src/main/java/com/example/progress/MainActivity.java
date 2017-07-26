package com.example.progress;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    boolean isContinue;
    WebProgressBarView progressBarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = (WebView) findViewById(R.id.webview);
        progressBarView = (WebProgressBarView) findViewById(R.id.webprogress);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(false);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                //如果进度条隐藏则让它显示
                if (View.GONE == progressBarView.getVisibility()) {
                    progressBarView.setVisibility(View.VISIBLE);
                }

                if (newProgress >= 80) {
                    if (isContinue) {
                        return;
                    }
                    isContinue = true;
                    progressBarView.setCurProgress(1000, new WebProgressBarView.EventEndListener() {
                        @Override
                        public void onEndEvent() {
                            isContinue = false;
                            if (progressBarView.getVisibility() == View.VISIBLE) {
                                hideProgress();
                            }
                        }
                    });
                } else {
                    progressBarView.setNormalProgress(newProgress);
                }
            }
        });

        webView.loadUrl("https://www.baidu.com");
    }

    private void hideProgress() {
        AnimationSet animation = getDismissAnim(MainActivity.this);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                progressBarView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        progressBarView.startAnimation(animation);
    }

    private AnimationSet getDismissAnim(Context context) {
        AnimationSet dismiss = new AnimationSet(context, null);
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
        alpha.setDuration(1000);
        dismiss.addAnimation(alpha);
        return dismiss;
    }
}
