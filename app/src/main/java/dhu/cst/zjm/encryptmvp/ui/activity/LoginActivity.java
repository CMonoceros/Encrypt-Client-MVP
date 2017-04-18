package dhu.cst.zjm.encryptmvp.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dhu.cst.zjm.encryptmvp.MyApplication;
import dhu.cst.zjm.encryptmvp.R;
import dhu.cst.zjm.encryptmvp.injector.component.ApplicationComponent;
import dhu.cst.zjm.encryptmvp.injector.component.DaggerLoginActivityComponent;
import dhu.cst.zjm.encryptmvp.injector.component.LoginActivityComponent;
import dhu.cst.zjm.encryptmvp.injector.module.ActivityModule;
import dhu.cst.zjm.encryptmvp.injector.module.LoginModule;
import dhu.cst.zjm.encryptmvp.mvp.contract.LoginContract;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import dhu.cst.zjm.encryptmvp.util.IntentUtil;

/**
 * Created by zjm on 2017/2/24.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @Inject
    LoginContract.Presenter loginContractPresenter;
    @BindView(R.id.et_login_id)
    public EditText et_login_id;
    @BindView(R.id.et_login_password)
    public EditText et_login_password;
    @BindView(R.id.rl_ui_login)
    public RelativeLayout rl_ui_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadBackground();
        requestPermission();
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.ui_login;
    }

    @Override
    protected void injectDependences() {
        ApplicationComponent applicationComponent = ((MyApplication) getApplication()).getApplicationComponent();
        LoginActivityComponent loginActivityComponent = DaggerLoginActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(this))
                .loginModule(new LoginModule())
                .build();
        loginActivityComponent.inject(this);

        loginContractPresenter.attachView(this);
    }

    @Override
    protected void initButterKnife(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @OnClick(R.id.b_login_internet)
    public void loginInternet() {
        loginContractPresenter.login(et_login_id.getText().toString() + "", et_login_password.getText().toString() + "");
    }

    @OnClick(R.id.b_register)
    public void register() {
        IntentUtil.intentToRegisterActivity(this);
    }

    @Override
    public void getLoginState(User user) {
        if (user != null) {
            IntentUtil.intentToMenuActivity(this, user);
        } else {
            loginPasswordError();
        }
    }

    @Override
    public void loginNetworkError() {
        Toast.makeText(this, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginPasswordError() {
        Toast.makeText(this, "ID or Password Wrong.Please confirm!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginEmptyError() {
        Toast.makeText(this, "ID or Password can not be empty!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadBackground() {
        Point point = new Point();
        WindowManager windowManager = getWindowManager();
        windowManager.getDefaultDisplay().getSize(point);
        int scrWidth = point.x;
        int scrHeight = point.y;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.background_login, options);
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;

        int dx = picWidth / scrWidth;
        int dy = picHeight / scrHeight;
        int scale = 1;
        if (dx >= dy && dy >= 1) {
            scale = dx;
        }
        if (dy >= dx && dx >= 1) {
            scale = dy;
        }

        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.background_login, options);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bmp);
        rl_ui_login.setBackground(bitmapDrawable);
    }

    @Override
    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            final List<String> permissionList = new ArrayList<>();
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                new AlertDialog.Builder(this)
                        .setMessage("Request permission")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(LoginActivity.this, permissionList.toArray(new String[permissionList.size()]), 1);
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(LoginActivity.this, permissionList.toArray(new String[permissionList.size()]), 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.STORAGE)) {
                        Toast.makeText(this, "Permission is denied!It will cause some error!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
