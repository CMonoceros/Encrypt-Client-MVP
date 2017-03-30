package dhu.cst.zjm.encryptmvp.injector.module;

import dagger.Module;
import dagger.Provides;
import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.UserUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.RegisterContract;
import dhu.cst.zjm.encryptmvp.mvp.presenter.RegisterPresenter;

/**
 * Created by zjm on 2017/3/1.
 */

@Module
public class RegisterModule {
    @Provides
    public UserUseCase provideUserUseCase(BaseRepository baseRepository) {
        return new UserUseCase(baseRepository);
    }

    @Provides
    public RegisterContract.Presenter provideRegisterPresenter(UserUseCase userUseCase) {
        return new RegisterPresenter(userUseCase);
    }
}
