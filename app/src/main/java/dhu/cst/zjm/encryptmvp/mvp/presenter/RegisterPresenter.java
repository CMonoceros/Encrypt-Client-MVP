package dhu.cst.zjm.encryptmvp.mvp.presenter;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import dhu.cst.zjm.encryptmvp.domain.UserUseCase;
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

    private UserUseCase mUserUseCase;
    private RegisterContract.View mView;
    private CompositeSubscription mCompositeSubscription;

    public RegisterPresenter(UserUseCase userUseCase) {
        this.mUserUseCase = userUseCase;
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
            String md5Password = new String(Hex.encodeHex(DigestUtils.md5(password)));
            user = new User(name, md5Password);
        } catch (Exception e) {
            e.printStackTrace();
            mView.registerEmptyError();
            return;
        }
        mUserUseCase.setUser(user);
        Subscription subscription = mUserUseCase.register()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.registerNetworkError();
                    }

                    @Override
                    public void onNext(User user) {
                        if (user == null) {
                            mView.registerNetworkError();
                        } else {
                            mView.registerSuccess(user.getId());
                        }
                    }

                });
        mCompositeSubscription.add(subscription);
    }
}
