package dhu.cst.zjm.encryptmvp.domain;

import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import rx.Observable;

/**
 * Created by zjm on 2017/2/24.
 */

public class LoginInternetUseCase implements UseCase<User> {

    private BaseRepository mRepository;
    private User mUser;

    public LoginInternetUseCase(BaseRepository repository){
        this.mRepository=repository;
    }

    public void setUser(User user){
        this.mUser=user;
    }

    @Override
    public Observable<User> execute() {
        return mRepository.loginInternet(mUser);
    }
}
