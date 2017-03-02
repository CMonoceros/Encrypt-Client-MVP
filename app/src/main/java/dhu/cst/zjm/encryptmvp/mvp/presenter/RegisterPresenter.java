package dhu.cst.zjm.encryptmvp.mvp.presenter;

import android.util.Log;

import dhu.cst.zjm.encryptmvp.domain.RegisterTryUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.RegisterContract;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zjm on 2017/3/1.
 */

public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterTryUseCase mRegisterTryUseCase;
    private RegisterContract.View mView;
    private CompositeSubscription mCompositeSubscription;

    public RegisterPresenter(RegisterTryUseCase mRegisterTryUseCase) {
        this.mRegisterTryUseCase = mRegisterTryUseCase;
    }

    @Override
    public void attachView(RegisterContract.View BaseView) {
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
    public void registerTry(String name, String password, String confirmPassword) {
        User user;
        if (!password.equals(confirmPassword)) {
            mView.confirmError();
            return;
        }
        try {
            user = new User(name, password);
        } catch (Exception e) {
            e.printStackTrace();
            mView.registerEmptyError();
            return;
        }
        mRegisterTryUseCase.setUser(user);
        Subscription subscription = mRegisterTryUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.registerNetworkError();
                    }

                    @Override
                    public void onNext(User user) {
                        switch (user.getId()) {
                            case -1:
                                mView.registerNetworkError();
                                break;
                            default:
                                mView.registerSuccess(user.getId());
                        }
                    }

                });
        mCompositeSubscription.add(subscription);
    }
}
