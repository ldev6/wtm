package com.montreal.wtm.utils.ui.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;
import com.montreal.wtm.R;
import com.montreal.wtm.utils.view.MessageView;

public abstract class BaseFragment extends Fragment implements MessageView.MessageViewInterface {

    protected MessageView mMessageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessageView = (MessageView) getActivity().findViewById(R.id.messageView);
        hideMessageView();
        setMessageViewInterface(this);
    }

    public void hideMessageView() {
        if (mMessageView != null) {
            mMessageView.setVisibility(View.GONE);
        }
    }

    public void setMessageViewInterface(MessageView.MessageViewInterface messageViewInterface) {
        if (mMessageView != null) {
            this.mMessageView.setMessageViewInterface(messageViewInterface);
        }
    }

    public void setMessageError(String error) {
        if (mMessageView != null && isAdded()) {
            this.mMessageView.setMessageError(error);
        }
    }

    private void hideErrorMessage() {
        if (mMessageView != null && isAdded()) {
            this.mMessageView.hideMessageError();
        }
    }

    public void showProgressBar() {
        if (mMessageView != null) {
            hideErrorMessage();
            this.mMessageView.setVisibility(View.VISIBLE);
            this.mMessageView.showProgressBar();
        }
    }

    @Override
    public void retry() {
        showProgressBar();
        retryOnProblem();
    }

    public abstract void retryOnProblem();
}
