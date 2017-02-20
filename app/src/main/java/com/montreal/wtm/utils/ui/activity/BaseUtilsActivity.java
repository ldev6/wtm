package com.montreal.wtm.utils.ui.activity;

import android.app.Activity;
import android.app.UiModeManager;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.mattprecious.telescope.EmailDeviceInfoLens;
import com.mattprecious.telescope.TelescopeLayout;
import com.montreal.wtm.R;
import com.montreal.wtm.utils.adapter.MenuDebugAdapter;
import com.montreal.wtm.utils.view.MessageView;
import com.montreal.wtm.utils.view.PermissionsUtils;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;



public class BaseUtilsActivity extends Activity implements MenuDebugAdapter.MenuDebugInterface {

    protected int layoutId;
    protected boolean enableTelescope = false;
    protected String bugSenderTitle;
    protected String email;

    private MessageView messageView;

    private MenuDrawer menuDrawer;
    protected boolean enableDebugView = false;
    private MenuDebugAdapter mAdapter;
    private ListView mList;
    private TelescopeLayout telescope;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiModeManager uiModeManager = (UiModeManager) getSystemService(Activity.UI_MODE_SERVICE);

        if (layoutId != 0) {
            setContentView(R.layout.base_internet_layout);
            FrameLayout frameLayout;
            if (enableDebugView) {
                menuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, Position.END);
                menuDrawer.setContentView(R.layout.base_internet_layout);

                mAdapter = new MenuDebugAdapter(this, this);
                mList = new ListView(this);
                mList.setAdapter(mAdapter);

                menuDrawer.setMenuView(mList);

                frameLayout = (FrameLayout) menuDrawer.findViewById(R.id.frameLayout);
                telescope = (TelescopeLayout) menuDrawer.findViewById(R.id.telescope);
                messageView = (MessageView) menuDrawer.findViewById(R.id.messageView);
            } else {
                setContentView(R.layout.base_internet_layout);
                frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
                messageView = (MessageView) findViewById(R.id.messageView);
                telescope = (TelescopeLayout) findViewById(R.id.telescope);
            }


            View view = getLayoutInflater().inflate(layoutId, null, false);
            frameLayout.addView(view);
            if (enableTelescope) {
                PermissionsUtils.verifyTelescopePermission(this);
                addTelescopeToView();
            }

        }
    }

    private void addTelescopeToView() {
        if (enableTelescope) {
            telescope.setVisibility(View.VISIBLE);
            EmailDeviceInfoLens emailDeviceInfoLens = new EmailDeviceInfoLens(this, bugSenderTitle, email);
            telescope.setLens(emailDeviceInfoLens);
            telescope.setScreenshot(true);
        } else {
            telescope.setVisibility(View.GONE);
            telescope.setScreenshot(false);
        }
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

    protected void hideMessageView() {
        messageView.setVisibility(View.GONE);
    }

    protected void setMessageViewInterface(MessageView.MessageViewInterface messageViewInterface) {
        if (messageView != null) {
            this.messageView.setMessageViewInterface(messageViewInterface);
        }
    }

    protected void setMessageError(String error) {
        if (messageView != null) {
            this.messageView.setMessageError(error);
        }
    }

    protected void hideErrorMessage() {
        if (messageView != null) {
            this.messageView.hideMessageError();
        }
    }

    protected void showProgressBar() {
        if (messageView != null) {
            this.messageView.showProgressBar();
        }
    }

    protected void setMessageInformation(String message) {
        if (messageView != null) {
            this.messageView.setMessageInformation(message);
        }
    }

    protected void setProperty(@ColorRes int infoBoxColor, @ColorRes int backgroundColor, @ColorRes int textColor, @ColorRes int textErrorColor) {
        if (messageView != null) {
            this.messageView.setProperties(infoBoxColor, backgroundColor, textColor, textErrorColor);
        }
    }

    @Override
    public void changeServer(String serverName) {

    }

    @Override
    public void enableTelescope(boolean enable) {
        PermissionsUtils.verifyTelescopePermission(this);
            enableTelescope = enable;
            addTelescopeToView();

    }

    @Override
    public void changeLang(String lang) {

    }



}
