package wichura.de.camperapp.mainactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import wichura.de.camperapp.R;
import wichura.de.camperapp.login.LoginActivity;

import static wichura.de.camperapp.mainactivity.Constants.SHARED_PREFS_USER_INFO;

/**
 * Created by ich on 03.06.2016.
 * CamperApp
 */
public class SettingsActivity extends AppCompatActivity {

    private TextView loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        loginInfo = (TextView) findViewById(R.id.login_info_text);
        loginInfo.setText("Logged in as " + getUserName());


        Button logoutButton = initLogoutButton();
    }

    private Button initLogoutButton() {
        Button logoutBtn = (Button) findViewById(R.id.logout_button);
        if (isUserLoggedIn()) {
            logoutBtn.setText("LOGOUT");
        } else {
            logoutBtn.setText("LOGIN");
        }
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserLoggedIn()) {
                    //logout from Facebook
                    LoginManager.getInstance().logOut();
                    //in case email login just delete sharedPrefs
                    updateUserInfo();
                    finish();
                } else {
                    final Intent intent = new Intent(getApplicationContext(),
                            LoginActivity.class);
                    startActivityForResult(intent, Constants.REQUEST_ID_FOR_FACEBOOK_LOGIN);
                }
            }
        });
        return logoutBtn;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_ID_FOR_FACEBOOK_LOGIN) {
            initLogoutButton();
            loginInfo.setText("Logged in as " + getUserName());
        }
    }

    private void updateUserInfo() {
        SharedPreferences settings = getSharedPreferences(SHARED_PREFS_USER_INFO, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.USER_NAME, "");
        editor.putString(Constants.USER_ID, "");
        editor.putString(Constants.USER_TYPE, "");
        editor.apply();
    }

    private String getUserName() {
        SharedPreferences settings = getSharedPreferences(SHARED_PREFS_USER_INFO, 0);
        return settings.getString(Constants.USER_NAME, "");
    }

    private Boolean isUserLoggedIn() {
        SharedPreferences settings = getSharedPreferences(SHARED_PREFS_USER_INFO, 0);
        return (settings.getString(Constants.USER_ID, "").equals("") ? false : true);
    }
}
