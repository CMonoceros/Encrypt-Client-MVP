package dhu.cst.zjm.encryptmvp.mvp.presenter;

import android.util.Log;

import java.util.List;

import dhu.cst.zjm.encryptmvp.domain.GetMenuFileListUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.MenuFileListContract;
import dhu.cst.zjm.encryptmvp.mvp.model.ServerFile;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zjm on 3/2/2017.
 */

public class MenuFileListPresenter implements MenuFileListContract.Presenter {
    private GetMenuFileListUseCase mGetMenuFileListUseCase;
    private MenuFileListContract.View mView;
    private CompositeSubscription mCompositeSubscription;

    public MenuFileListPresenter(GetMenuFileListUseCase getMenuFileListUseCase) {
        this.mGetMenuFileListUseCase = getMenuFileListUseCase;
    }


    @Override
    public void attachView(MenuFileListContract.View BaseView) {
        this.mView = BaseView;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        if (mCompositeSubscription != null && mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void getMenuFileList(int id) {
        mGetMenuFileListUseCase.setId(id+"");
        Subscription subscription = mGetMenuFileListUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ServerFile>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.getFileListNetworkError();
                    }

                    @Override
                    public void onNext(List<ServerFile> list) {
                        mView.updateSourceList(list);
                    }

                });
        mCompositeSubscription.add(subscription);
    }
}
