package co.id.datascrip.mo_sam_div4_dts1.activity;

import android.Manifest;
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

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.SessionManager;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackLogin;
import co.id.datascrip.mo_sam_div4_dts1.custom.TimeOutDialog;
import co.id.datascrip.mo_sam_div4_dts1.object.BEX;
import co.id.datascrip.mo_sam_div4_dts1.process.Process_Login;

public class Login_Activity extends AppCompatActivity {

    private EditText tuser, tpass;
    private Button btLogin;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tuser = (EditText) findViewById(R.id.username);
        tpass = (EditText) findViewById(R.id.password);
        btLogin = (Button) findViewById(R.id.btLogin);
        TextView txVersion = (TextView) findViewById(R.id.txtVersion);

        session = new SessionManager(this);

        txVersion.setText(Global.getVersionText(this));

        btLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                final TedPermission tedPermission = new TedPermission(Login_Activity.this);
                tedPermission.setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                tedPermission.setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Login();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        tedPermission.setGotoSettingButton(true)
                                .setGotoSettingButtonText("Buka Setting")
                                .setRationaleMessage("Aplikasi membutuhkan permission untuk dapat melanjutkan.")
                                .setRationaleConfirmText("OK")
                                .setDeniedMessage("Aplikasi tidak dapat berjalan tanpa permission.\nSilakan aktifkan permission di menu Setting -> Apps -> Mo-SAM DTS1 -> Permission.")
                                .setDeniedCloseButtonText("Batal")
                                .check();
                    }
                });
                tedPermission.check();
            }
        });
    }

    public void Login() {
        tpass.requestFocus();
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(tpass.getWindowToken(), 0);

        final String usr = tuser.getText().toString();
        String pas = tpass.getText().toString();

        if (usr.isEmpty() && pas.isEmpty())
            Toast.makeText(Login_Activity.this, "Masukkan username & password",
                    Toast.LENGTH_SHORT).show();
        else {
            new Process_Login(Login_Activity.this).Login(usr, pas, new CallbackLogin.CBLogin() {
                @Override
                public void onCB(BEX bex, String datetime) {
                    Next(bex, datetime);
                }
            });
        }
    }

    private void Next(BEX bex, String datetime) {
        if (bex != null) {
            if (!bex.getInitial().equals("null")) {
                if (isDateMatch(datetime)) {
                    session.createLoginSession(bex);
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
