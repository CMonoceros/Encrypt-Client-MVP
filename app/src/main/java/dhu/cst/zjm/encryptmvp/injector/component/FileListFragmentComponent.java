package dhu.cst.zjm.encryptmvp.injector.component;

import dagger.Component;
import dhu.cst.zjm.encryptmvp.injector.module.ActivityModule;
import dhu.cst.zjm.encryptmvp.injector.module.FileListModule;
import dhu.cst.zjm.encryptmvp.injector.scope.PerActivity;
import dhu.cst.zjm.encryptmvp.mvp.view.ui.fragment.FileListFragment;

/**
 * Created by zjm on 3/2/2017.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class ,modules = {ActivityModule.class, FileListModule.class})
public interface FileListFragmentComponent {
    void inject(FileListFragment fileListFragment);
}
