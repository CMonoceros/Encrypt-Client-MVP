package dhu.cst.zjm.encryptmvp.domain;

import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import rx.Observable;

/**
 * Created by zjm on 2017/3/1.
 */

public class RegisterTryUseCase implements UseCase<User> {
    private BaseRepository mRepository;
    private User mUser;

    public RegisterTryUseCase(BaseRepository repository) {
        this.mRepository = repository;
    }

    public void setUser(User user) {
        this.mUser = user;
    }

    @Override
    public Observable<User> execute() {
        return mRepository.registerTry(mUser);
    }
}
