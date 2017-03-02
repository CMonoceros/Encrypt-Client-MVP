package dhu.cst.zjm.encryptmvp.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
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
        Log.i(et_register_name.getText().toString() + "", et_register_password.getText().toString() + " " + et_register_confirmPassword.getText().toString() + "");
        registerContractPresenter.registerTry(et_register_name.getText().toString() + "", et_register_password.getText().toString() + "", et_register_confirmPassword.getText().toString() + "");
    }

    @Override
    public void confirmError() {
        Toast.makeText(this, "Confirm Error.Please confirm your password!",
                Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void registerEmptyError() {
        Toast.makeText(this, "Name or Password can not be empty!",
                Toast.LENGTH_SHORT).show();
    }
}
