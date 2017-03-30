package dhu.cst.zjm.encryptmvp.injector.module;

import dagger.Module;
import dagger.Provides;
import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.UserUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.LoginContract;
import dhu.cst.zjm.encryptmvp.mvp.presenter.LoginPresenter;

/**
 * Created by zjm on 2017/2/24.
 */
@Module
public class LoginModule {
    @Provides
    public UserUseCase provideUserUseCase(BaseRepository baseRepository) {
        return new UserUseCase(baseRepository);
    }

    @Provides
    public LoginContract.Presenter provideLoginPresenter(UserUseCase userUseCase) {
        return new LoginPresenter(userUseCase);
    }
}
