package dhu.cst.zjm.encryptmvp.injector.module;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.RegisterTryUseCase;
import dhu.cst.zjm.encryptmvp.injector.scope.PerActivity;
import dhu.cst.zjm.encryptmvp.mvp.contract.RegisterContract;
import dhu.cst.zjm.encryptmvp.mvp.presenter.RegisterPresenter;

/**
 * Created by zjm on 2017/3/1.
 */

@Module
public class RegisterModule {
    @Provides
    public RegisterTryUseCase provideLoginInternetUseCase(BaseRepository baseRepository) {
        return new RegisterTryUseCase(baseRepository);
    }

    @Provides
    public RegisterContract.Presenter provideLoginPresenter(RegisterTryUseCase registerTryUseCase) {
        return new RegisterPresenter(registerTryUseCase);
    }
}
