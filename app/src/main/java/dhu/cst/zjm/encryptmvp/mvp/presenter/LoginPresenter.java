package dhu.cst.zjm.encryptmvp.mvp.presenter;

import dhu.cst.zjm.encryptmvp.domain.LoginInternetUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.LoginContract;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * Created by zjm on 2017/2/24.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LoginInternetUseCase mLoginInternetUseCase;
    private LoginContract.View mView;
    private CompositeSubscription mCompositeSubscription;

    public LoginPresenter(LoginInternetUseCase loginInternetUseCase) {
        this.mLoginInternetUseCase = loginInternetUseCase;
    }

    @Override
    public void attachView(LoginContract.View BaseView) {
        mView = BaseView;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        if (mCompositeSubscription != null && mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void loginInternet(String id, String password) {
        User user;
        try {
            user = new User(Integer.parseInt(id), password);
        } catch (Exception e) {
            e.printStackTrace();
            mView.loginInfoError();
            return;
        }
        mLoginInternetUseCase.setUser(user);
        Subscription subscription = mLoginInternetUseCase.execute()
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        mView.getLoginState(user.getIsLogin());
                    }
                });
        mCompositeSubscription.add(subscription);

    }
}
