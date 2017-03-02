package dhu.cst.zjm.encryptmvp.injector.module;

import dagger.Module;
import dagger.Provides;
import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.GetMenuFileListUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.MenuFileListContract;
import dhu.cst.zjm.encryptmvp.mvp.presenter.MenuFileListPresenter;

/**
 * Created by zjm on 3/2/2017.
 */

@Module
public class MenuFileListModule {
    @Provides
    public GetMenuFileListUseCase provideGetMenuFileListUseCase(BaseRepository baseRepository) {
        return new GetMenuFileListUseCase(baseRepository);

    }

    @Provides
    public MenuFileListContract.Presenter provideMenuPresenter(GetMenuFileListUseCase getMenuFileListUseCase) {
        return new MenuFileListPresenter(getMenuFileListUseCase);
    }
}
