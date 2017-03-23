package com.montreal.wtm.utils.view;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.montreal.wtm.R;


public class MessageView extends RelativeLayout {

    private MessageViewInterface messageViewInterface;

    private ProgressBar progressBar;
    private TextView textViewErrorMessage;
    private TextView textViewInformation;
    private Button btRetry;


    private
    @ColorRes
    int infoBoxColor;
    private
    @ColorRes
    int backgroundColor;
    private
    @ColorRes
    int textColor;
    private
    @ColorRes
    int textErrorColor;

    public interface MessageViewInterface {
        public void retry();
    }

    public MessageView(Context context) {
        super(context);
        init(context);
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MessageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.message_view, this, true);
        progressBar = (ProgressBar) findViewById(R.id.pgLoading);
        textViewErrorMessage = (TextView) findViewById(R.id.tvError);
        btRetry = (Button) findViewById(R.id.btRetry);
        textViewErrorMessage.setVisibility(View.GONE);
        textViewInformation = (TextView) findViewById(R.id.tvInformation);

        infoBoxColor = R.color.colorPrimary;
        backgroundColor = R.color.transGrey;
        textColor = android.R.color.white;
        textErrorColor = android.R.color.white;

        btRetry.setBackgroundColor(getResources().getColor(infoBoxColor));
        this.setBackgroundColor(getResources().getColor(R.color.transGrey));
    }

    public void setProperties(@ColorRes int infoBoxColor, @ColorRes int backgroundColor, @ColorRes int textColor, @ColorRes int textErrorColor) {
        this.infoBoxColor = infoBoxColor;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.textErrorColor = textErrorColor;

        textViewInformation.setTextColor(getResources().getColor(textColor));
        textViewErrorMessage.setTextColor(getResources().getColor(textErrorColor));
        setBackgroundColor(getResources().getColor(backgroundColor));
        btRetry.setBackgroundColor(getResources().getColor(infoBoxColor));
    }

    public void setMessageViewInterface(final MessageViewInterface messageViewInterface) {
        this.messageViewInterface = messageViewInterface;
        btRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                messageViewInterface.retry();
            }
        });
    }

    public void setMessageInformation(String text) {
        this.setVisibility(View.VISIBLE);
        textViewInformation.setVisibility(View.VISIBLE);
        textViewInformation.setText(text);
    }

    public void setMessageError(String text) {
        this.setVisibility(View.VISIBLE);
        hideProgessBar();
        this.textViewInformation.setVisibility(View.GONE);
        textViewErrorMessage.setVisibility(View.VISIBLE);
        textViewErrorMessage.setText(text);
        if (messageViewInterface != null) {
            btRetry.setVisibility(View.VISIBLE);
        }
    }

    public void showProgressBar() {
        hideMessageError();
        progressBar.setVisibility(View.VISIBLE);
        progressBar.animate();
    }

    public void hideMessageError() {
        textViewErrorMessage.setVisibility(View.GONE);
        btRetry.setVisibility(View.GONE);
    }

    public static void hideLoadingView(MessageView messageView) {
        if (messageView != null) {
            messageView.hideProgessBar();
            messageView.setVisibility(View.GONE);
        }
    }

    private void hideProgessBar() {
        progressBar.setVisibility(View.GONE);
        progressBar.clearAnimation();
    }
}