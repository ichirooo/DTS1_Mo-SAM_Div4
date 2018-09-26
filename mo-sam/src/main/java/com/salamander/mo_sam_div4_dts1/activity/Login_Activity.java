package com.salamander.mo_sam_div4_dts1.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.SessionManager;
import com.salamander.mo_sam_div4_dts1.custom.TimeOutDialog;
import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Login;
import com.salamander.mo_sam_div4_dts1.proses.callback.CallbackLogin;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_logger.LoggingExceptionHandler;
import com.salamander.salamander_network.RetroStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Login_Activity extends AppCompatActivity {

    private EditText tx_initial, tx_password;
    private Button bt_login;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new LoggingExceptionHandler(this));
        setContentView(R.layout.activity_login);

        tx_initial = findViewById(R.id.tx_initial);
        tx_password = findViewById(R.id.tx_password);
        bt_login = findViewById(R.id.bt_login);
        TextView tx_version = findViewById(R.id.tx_version);

        session = new SessionManager(this);

        tx_version.setText(App.getVersionText(this));

        bt_login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Login();
            }
        });
    }

    public void Login() {
        tx_initial.requestFocus();
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert keyboard != null;
        keyboard.hideSoftInputFromWindow(tx_initial.getWindowToken(), 0);

        final String usr = tx_initial.getText().toString();
        String pas = tx_password.getText().toString();

        if (usr.isEmpty() && pas.isEmpty())
            Toast.makeText(Login_Activity.this, "Masukkan username & password",
                    Toast.LENGTH_SHORT).show();
        else {
            new Proses_Login(Login_Activity.this).Login(usr, pas, new CallbackLogin.CBLogin() {
                @Override
                public void onCB(RetroStatus retroStatus, User user, String datetime) {
                    if (retroStatus.isSuccess())
                        Next(user, datetime);
                    else
                        DialogUtils.showErrorMessage(Login_Activity.this, retroStatus.getMessage(), false);
                }
            });
        }
    }

    private void Next(User user, String datetime) {
        if (user != null) {
            if (!user.getInitial().equals("null")) {
                if (isDateMatch(datetime)) {
                    session.createLoginSession(user);
                    session.checkLogin();
                    finish();
                } else {
                    new AlertDialog.Builder(Login_Activity.this)
                            .setMessage("Tanggal/jam tidak valid.")
                            .setNegativeButton("Exit", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    finish();
                                }
                            })
                            .show();
                }
            } else
                Toast.makeText(Login_Activity.this, "username/password salah", Toast.LENGTH_LONG).show();
        } else {
            new TimeOutDialog(Login_Activity.this).show();
        }
    }

    private boolean isDateMatch(String datetime) {
        if (datetime == null)
            return false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = new Date();
            Date dateServer = format.parse(datetime);
            long selisih;
            if (date.getTime() > dateServer.getTime())
                selisih = date.getTime() - dateServer.getTime();
            else
                selisih = dateServer.getTime() - date.getTime();

            long tolerance = 10 * 60 * 1000;
            return selisih < tolerance;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return true;
        }
    }
}
