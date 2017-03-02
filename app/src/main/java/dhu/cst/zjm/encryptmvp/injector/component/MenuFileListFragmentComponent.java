package dhu.cst.zjm.encryptmvp.injector.component;

import dagger.Component;
import dhu.cst.zjm.encryptmvp.injector.module.ActivityModule;
import dhu.cst.zjm.encryptmvp.injector.module.MenuFileListModule;
import dhu.cst.zjm.encryptmvp.injector.scope.PerActivity;
import dhu.cst.zjm.encryptmvp.ui.activity.MenuActivity;
import dhu.cst.zjm.encryptmvp.ui.fragment.MenuFileListFragment;

/**
 * Created by zjm on 3/2/2017.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class ,modules = {ActivityModule.class, MenuFileListModule.class})
public interface MenuFileListFragmentComponent {
    void inject(MenuFileListFragment menuFileListFragment);
}
