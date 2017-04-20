package dhu.cst.zjm.encryptmvp.injector.component;

import dagger.Component;
import dhu.cst.zjm.encryptmvp.injector.module.ActivityModule;
import dhu.cst.zjm.encryptmvp.injector.module.MenuModule;
import dhu.cst.zjm.encryptmvp.injector.scope.PerActivity;
import dhu.cst.zjm.encryptmvp.mvp.view.ui.activity.MenuActivity;

/**
 * Created by zjm on 2/9/2017.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, MenuModule.class})
public interface MenuActivityComponent {
    void inject(MenuActivity menuActivity);
}
