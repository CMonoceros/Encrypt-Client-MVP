package dhu.cst.zjm.encryptmvp.injector.component;

import dagger.Component;
import dhu.cst.zjm.encryptmvp.injector.module.ActivityModule;
import dhu.cst.zjm.encryptmvp.injector.module.RegisterModule;
import dhu.cst.zjm.encryptmvp.injector.scope.PerActivity;
import dhu.cst.zjm.encryptmvp.ui.activity.RegisterActivity;

/**
 * Created by zjm on 2017/3/1.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, RegisterModule.class})
public interface RegisterActivityComponent {
    void inject(RegisterActivity registerActivity);
}
