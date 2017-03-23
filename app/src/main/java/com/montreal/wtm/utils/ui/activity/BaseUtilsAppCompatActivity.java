package com.montreal.wtm.utils.ui.activity;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.montreal.wtm.R;
import com.montreal.wtm.utils.view.MessageView;

public class BaseUtilsAppCompatActivity extends AppCompatActivity {


    private MessageView messageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageView = (MessageView) findViewById(R.id.messageView);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    protected void loadServiceWeb() {
        messageView.setVisibility(View.VISIBLE);
    }

    public void hideMessageView() {
        messageView.setVisibility(View.GONE);
    }

    public void setMessageViewInterface(MessageView.MessageViewInterface messageViewInterface) {
        if (messageView != null) {
            this.messageView.setMessageViewInterface(messageViewInterface);
        }
    }

    public void setMessageError(String error) {
        if (messageView != null) {
            this.messageView.setMessageError(error);
        }
    }

    private void hideErrorMessage() {
        if (messageView != null) {
            this.messageView.hideMessageError();
        }
    }

    public void showProgressBar() {
        if (messageView != null) {
            hideErrorMessage();
            this.messageView.setVisibility(View.VISIBLE);
            this.messageView.showProgressBar();
        }
    }

    public void setMessageInformation(String message) {
        if (messageView != null) {
            this.messageView.setMessageInformation(message);
        }
    }

    protected void setProperty(@ColorRes int infoBoxColor, @ColorRes int backgroundColor, @ColorRes int textColor, @ColorRes int textErrorColor) {
        if (messageView != null) {
            this.messageView.setProperties(infoBoxColor, backgroundColor, textColor, textErrorColor);
        }
    }

}
