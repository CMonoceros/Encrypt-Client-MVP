package dhu.cst.zjm.encryptmvp.injector.module;

import dagger.Module;
import dagger.Provides;
import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.LoginInternetUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.LoginContract;
import dhu.cst.zjm.encryptmvp.mvp.presenter.LoginPresenter;

/**
 * Created by zjm on 2017/2/24.
 */
@Module
public class LoginModule {
    @Provides
    public LoginInternetUseCase provideLoginInternetUseCase(BaseRepository baseRepository) {
        return new LoginInternetUseCase(baseRepository);
    }

    @Provides
    public LoginContract.Presenter provideLoginPresenter(LoginInternetUseCase loginInternetUseCase) {
        return new LoginPresenter(loginInternetUseCase);
    }
}
