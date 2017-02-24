package dhu.cst.zjm.encryptmvp.ui.activity;

import android.os.Bundle;

import butterknife.ButterKnife;
import dhu.cst.zjm.encryptmvp.MyApplication;
import dhu.cst.zjm.encryptmvp.R;
import dhu.cst.zjm.encryptmvp.injector.component.ApplicationComponent;
import dhu.cst.zjm.encryptmvp.injector.component.DaggerLoginActivityComponent;
import dhu.cst.zjm.encryptmvp.injector.component.LoginActivityComponent;
import dhu.cst.zjm.encryptmvp.injector.module.ActivityModule;
import dhu.cst.zjm.encryptmvp.injector.module.LoginModule;

/**
 * Created by zjm on 2017/2/24.
 */

public class LoginActivity extends BaseActivity{



    @Override
    protected int getContentViewLayoutId() {
        return R.layout.ui_login;
    }

    @Override
    protected void injectDependences() {
        ApplicationComponent applicationComponent=((MyApplication)getApplication()).getApplicationComponent();
        LoginActivityComponent loginActivityComponent= DaggerLoginActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(this))
                .loginModule(new LoginModule())
                .build();
        loginActivityComponent.inject(this);

    }

    @Override
    protected void initButterKnife(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }
}
