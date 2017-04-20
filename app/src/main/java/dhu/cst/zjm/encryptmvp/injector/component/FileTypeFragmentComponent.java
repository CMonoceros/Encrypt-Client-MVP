package dhu.cst.zjm.encryptmvp.injector.component;

import dagger.Component;
import dhu.cst.zjm.encryptmvp.injector.module.ActivityModule;
import dhu.cst.zjm.encryptmvp.injector.module.FileTypeModule;
import dhu.cst.zjm.encryptmvp.injector.scope.PerActivity;
import dhu.cst.zjm.encryptmvp.mvp.view.ui.fragment.FileTypeFragment;

/**
 * Created by zjm on 2017/3/3.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, FileTypeModule.class})
public interface FileTypeFragmentComponent {
    void inject(FileTypeFragment fileTypeFragment);
}
