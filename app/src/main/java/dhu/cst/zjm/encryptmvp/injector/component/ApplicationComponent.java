package dhu.cst.zjm.encryptmvp.injector.component;

import android.app.Application;

import dhu.cst.zjm.encryptmvp.MyApplication;
import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.injector.module.ApplicationModule;
import dhu.cst.zjm.encryptmvp.injector.module.NetworkModule;
import dhu.cst.zjm.encryptmvp.injector.scope.PerApplication;
import dagger.Component;

/**
 * Created by zjm on 2017/2/23.
 */

@PerApplication
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {

    Application application();

    MyApplication myApplication();

    BaseRepository baseApi();
}
