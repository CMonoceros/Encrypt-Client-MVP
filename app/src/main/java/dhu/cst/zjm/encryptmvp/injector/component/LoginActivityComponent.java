package dhu.cst.zjm.encryptmvp.injector.component;

import dagger.Component;
import dhu.cst.zjm.encryptmvp.injector.module.ActivityModule;
import dhu.cst.zjm.encryptmvp.injector.module.LoginModule;
import dhu.cst.zjm.encryptmvp.injector.scope.PerActivity;
import dhu.cst.zjm.encryptmvp.ui.activity.LoginActivity;

/**
 * Created by zjm on 2017/2/24.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, LoginModule.class})
public interface LoginActivityComponent {
    void inject(LoginActivity loginActivity);
}
