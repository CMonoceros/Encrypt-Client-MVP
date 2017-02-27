package dhu.cst.zjm.encryptmvp.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

/**
 * Created by zjm on 2017/2/24.
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @Inject
    LoginContract.Presenter loginContractPresenter;
    @BindView(R.id.et_login_id)
    EditText et_login_id;
    @BindView(R.id.et_login_password)
    EditText et_login_password;


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
    private void loginInternet() {
        loginContractPresenter.loginInternet(et_login_id.getText().toString() + "", et_login_password.getText().toString() + "");
    }

    @OnClick(R.id.b_register)
    private void register() {

    }

    @Override
    public void getLoginState(boolean state) {

    }

    @Override
    public void loginInfoError() {
        Toast.makeText(this, "ID or Password can not be empty!",
                Toast.LENGTH_SHORT).show();
    }
}
