package dhu.cst.zjm.encryptmvp.mvp.presenter;

import java.util.List;

import dhu.cst.zjm.encryptmvp.domain.GetEncryptTypeUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.FileTypeContract;
import dhu.cst.zjm.encryptmvp.mvp.model.EncryptType;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zjm on 2017/3/3.
 */

public class FileTypePresenter implements FileTypeContract.Presenter{

    private FileTypeContract.View mView;
    private GetEncryptTypeUseCase mGetEncryptTypeUseCase;
    private CompositeSubscription mCompositeSubscription;

    public FileTypePresenter(GetEncryptTypeUseCase getEncryptTypeUseCase){
        this.mGetEncryptTypeUseCase=getEncryptTypeUseCase;
    }

    @Override
    public void attachView(FileTypeContract.View BaseView) {
        this.mView=BaseView;
        mCompositeSubscription=new CompositeSubscription();
    }

    @Override
    public void detachView() {
        if (mCompositeSubscription != null && mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void getEncryptType() {
        Subscription subscription = mGetEncryptTypeUseCase.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<EncryptType>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.getFileTypeNetworkError();
                    }

                    @Override
                    public void onNext(List<EncryptType> list) {
                        mView.updateEncryptType(list);
                    }

                });
        mCompositeSubscription.add(subscription);
    }
}
