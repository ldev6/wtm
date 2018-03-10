package com.montreal.wtm.utils.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.montreal.wtm.R;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import java.util.Arrays;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static PublishSubject<Boolean> changeLogin = PublishSubject.create();

    protected Observable<Boolean> getChangeLogin() {
       return Observable.just(true)
            .mergeWith(changeLogin);
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
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            public void onComplete(@NonNull Task<Void> task) {
                changeLogin.onNext(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                changeLogin.onNext(true);
            } else {
                changeLogin.onNext(false);
            }
        }
    }
}
