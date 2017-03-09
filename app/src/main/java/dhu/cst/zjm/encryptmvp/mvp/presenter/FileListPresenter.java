package dhu.cst.zjm.encryptmvp.mvp.presenter;

import java.util.List;

import dhu.cst.zjm.encryptmvp.domain.GetFileListUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.FileListContract;
import dhu.cst.zjm.encryptmvp.mvp.model.ServerFile;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zjm on 3/2/2017.
 */

public class FileListPresenter implements FileListContract.Presenter {
    private GetFileListUseCase mGetFileListUseCase;
    private FileListContract.View mView;
    private CompositeSubscription mCompositeSubscription;

    public FileListPresenter(GetFileListUseCase getFileListUseCase) {
        this.mGetFileListUseCase = getFileListUseCase;
    }


    @Override
    public void attachView(FileListContract.View BaseView) {
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
        mGetFileListUseCase.setId(id+"");
        Subscription subscription = mGetFileListUseCase.execute()
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
