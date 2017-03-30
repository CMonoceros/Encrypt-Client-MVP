package dhu.cst.zjm.encryptmvp.domain;

import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.base.BaseUserUseCase;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import rx.Observable;
/**
 * Created by zjm on 2017/2/24.
 */

public class UserUseCase implements BaseUserUseCase<User> {

    private BaseRepository mRepository;
    private User mUser;

    public UserUseCase(BaseRepository repository){
        this.mRepository=repository;
    }

    public void setUser(User user){
        this.mUser=user;
    }

    @Override
    public Observable<User> login() {
        return mRepository.login(mUser);
    }

    @Override
    public Observable<User> register() {
        return mRepository.register(mUser);
    }
}
