package com.montreal.wtm.utils.ui.activity;

import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.montreal.wtm.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import java.util.Arrays;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static PublishSubject<Boolean> loginChanged = PublishSubject.create();

    public Observable<Boolean> getLoginChanged() {
       return Observable.just(true).mergeWith(loginChanged).observeOn(AndroidSchedulers.mainThread());
    }

    protected void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

        // Create and launch sign-in intent
        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setLogo(R.drawable.wtm_logo_vertical)      // Set logo drawable
            .setTheme(R.style.AppTheme)
            .setAvailableProviders(providers)
            .build(), RC_SIGN_IN);
    }

    protected void signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(task -> loginChanged.onNext(false));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                loginChanged.onNext(true);
            } else {
                loginChanged.onNext(false);
            }
        }
    }


    public void promptLogin() {
        (new AlertDialog.Builder(this)).setTitle(R.string.login_required)
            .setMessage(R.string.please_login)
            .setCancelable(true)
            .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
            .setPositiveButton(R.string.ok,(dialog, which)->{dialog.dismiss();signIn();})
            .show();
    }
}
