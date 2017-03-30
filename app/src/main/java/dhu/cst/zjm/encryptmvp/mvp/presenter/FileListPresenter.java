package dhu.cst.zjm.encryptmvp.mvp.presenter;

import java.util.List;

import dhu.cst.zjm.encryptmvp.domain.ListFileUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.FileListContract;
import dhu.cst.zjm.encryptmvp.mvp.model.File;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zjm on 3/2/2017.
 */

public class FileListPresenter implements FileListContract.Presenter {
    private ListFileUseCase mListFileUseCase;
    private FileListContract.View mView;
    private CompositeSubscription mCompositeSubscription;

    public FileListPresenter(ListFileUseCase listFileUseCase) {
        this.mListFileUseCase = listFileUseCase;
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
        mListFileUseCase.setOwner(id + "");
        Subscription subscription = mListFileUseCase.getFileList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<File>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.getFileListNetworkError();
                    }

                    @Override
                    public void onNext(List<File> list) {
                        mView.updateSourceList(list);
                    }

                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void getMenuFileListByPaper(int id, int rows, int paper) {
        mListFileUseCase.setOwner(id + "");
        mListFileUseCase.setRows(rows + "");
        mListFileUseCase.setPaper(paper + "");
        Subscription subscription = mListFileUseCase.getFileListByPaper()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<File>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.getFileListNetworkError();
                    }

                    @Override
                    public void onNext(List<File> list) {
                        mView.updateSourceList(list);
                    }

                });
        mCompositeSubscription.add(subscription);
    }
}
