package com.montreal.wtm.utils.ui.activity;

import android.app.UiModeManager;
import android.content.res.Configuration;
import android.support.annotation.ColorRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
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




public class BaseUtilsActionBarActivity extends ActionBarActivity implements MenuDebugAdapter.MenuDebugInterface {

    private static String TAG = BaseUtilsActionBarActivity.class.getName();

    protected boolean needDisplayHome = false;
    protected boolean enableTelescope = false;
    protected int layoutId;
    protected MessageView messageView;
    protected String bugSenderTitle;
    protected String email;
    private boolean backButton;
    protected boolean isInTVMode;

    private MenuDrawer menuDrawer;
    protected boolean enableDebugView = false;
    private MenuDebugAdapter mAdapter;
    private ListView mList;


    private TelescopeLayout telescope;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (layoutId != 0) {

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
                telescope = (TelescopeLayout)findViewById(R.id.telescope);
            }


            View view = getLayoutInflater().inflate(layoutId, null, false);
            frameLayout.addView(view);
            if(enableTelescope){
                PermissionsUtils.verifyTelescopePermission(this);
                addTelescopeToView();

            }

        }
        if (needDisplayHome) {
            backButton = true;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
            isInTVMode = true;
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        } else {
            isInTVMode = false;
        }
    }

    protected void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title.toUpperCase());
    }


    private void addTelescopeToView() {
        if (enableTelescope) {
            telescope.setVisibility(View.VISIBLE);
            EmailDeviceInfoLens emailDeviceInfoLens = new EmailDeviceInfoLens(this, bugSenderTitle, email);
            telescope.setLens(emailDeviceInfoLens);
            telescope.setScreenshot(true);
        }else{
            telescope.setVisibility(View.GONE);
            telescope.setScreenshot(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (backButton) {
                    onBackPressed();
                    return true;
                }
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadServiceWeb() {
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
