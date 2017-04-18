package dhu.cst.zjm.encryptmvp.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dhu.cst.zjm.encryptmvp.MyApplication;
import dhu.cst.zjm.encryptmvp.R;
import dhu.cst.zjm.encryptmvp.injector.component.ApplicationComponent;
import dhu.cst.zjm.encryptmvp.injector.component.DaggerApplicationComponent;
import dhu.cst.zjm.encryptmvp.injector.component.DaggerRegisterActivityComponent;
import dhu.cst.zjm.encryptmvp.injector.component.RegisterActivityComponent;
import dhu.cst.zjm.encryptmvp.injector.module.ActivityModule;
import dhu.cst.zjm.encryptmvp.injector.module.RegisterModule;
import dhu.cst.zjm.encryptmvp.mvp.contract.RegisterContract;

/**
 * Created by zjm on 2017/3/1.
 */

public class RegisterActivity extends BaseActivity implements RegisterContract.View {

    @Inject
    RegisterContract.Presenter registerContractPresenter;
    @BindView(R.id.et_register_name)
    EditText et_register_name;
    @BindView(R.id.et_register_password)
    EditText et_register_password;
    @BindView(R.id.et_register_confirmPassword)
    EditText et_register_confirmPassword;
    @BindView(R.id.et_register_verification)
    EditText et_register_verification;
    @BindView(R.id.iv_register_verification)
    ImageView iv_register_verification;
    @BindView(R.id.rl_ui_register)
    RelativeLayout rl_ui_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadBackground();
        registerContractPresenter.generateVerification();
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.ui_register;
    }

    @Override
    protected void injectDependences() {
        ApplicationComponent applicationComponent = ((MyApplication) getApplication()).getApplicationComponent();
        RegisterActivityComponent registerActivityComponent = DaggerRegisterActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(this))
                .registerModule(new RegisterModule())
                .build();
        registerActivityComponent.inject(this);

        registerContractPresenter.attachView(this);
    }

    @Override
    protected void initButterKnife(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @OnClick(R.id.b_register_ok)
    public void registerTry() {
        registerContractPresenter.registerTry(et_register_name.getText().toString() + "", et_register_password.getText().toString() + "",
                et_register_confirmPassword.getText().toString() + "", et_register_verification.getText().toString()+"");
    }

    @Override
    public void confirmError() {
        Toast.makeText(this, "Confirm Error.Please confirm your password!",
                Toast.LENGTH_SHORT).show();
        registerContractPresenter.generateVerification();
    }

    @Override
    public void registerSuccess(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage("Success register : Your id is " + id);
        builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void registerNetworkError() {
        Toast.makeText(this, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
        registerContractPresenter.generateVerification();
    }

    @Override
    public void registerEmptyError() {
        Toast.makeText(this, "Name or Password can not be empty!",
                Toast.LENGTH_SHORT).show();
        registerContractPresenter.generateVerification();
    }

    @Override
    public void registerVerificationError() {
        Toast.makeText(this, "Verification Error.Please confirm again!",
                Toast.LENGTH_SHORT).show();
        registerContractPresenter.generateVerification();
    }

    @Override
    public void setVerificationBitmap(Bitmap bitmap) {
        iv_register_verification.setImageBitmap(bitmap);
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
        BitmapFactory.decodeResource(getResources(), R.drawable.background_register, options);
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
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.background_register, options);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bmp);
        rl_ui_register.setBackground(bitmapDrawable);
    }

    @Override
    public void passwordUnqualified() {
        Toast.makeText(this, "Password at least 8 and must be mixed letter and number!",
                Toast.LENGTH_SHORT).show();
        registerContractPresenter.generateVerification();
    }

    @Override
    public Resources getRes() {
        return getResources();
    }
}
