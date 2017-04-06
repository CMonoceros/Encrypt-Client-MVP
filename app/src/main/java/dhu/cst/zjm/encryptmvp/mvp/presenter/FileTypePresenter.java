package dhu.cst.zjm.encryptmvp.mvp.presenter;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import dhu.cst.zjm.encryptmvp.domain.EncryptRelationUseCase;
import dhu.cst.zjm.encryptmvp.domain.ListEncryptTypeUseCase;
import dhu.cst.zjm.encryptmvp.domain.ResponseBodyUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.FileTypeContract;
import dhu.cst.zjm.encryptmvp.mvp.model.EncryptRelation;
import dhu.cst.zjm.encryptmvp.mvp.model.EncryptType;
import dhu.cst.zjm.encryptmvp.util.FileUtil;
import dhu.cst.zjm.encryptmvp.util.ZipUtil;
import dhu.cst.zjm.encryptmvp.util.algorithm.des.DesUtil;
import dhu.cst.zjm.encryptmvp.util.algorithm.md5.Md5Util;
import dhu.cst.zjm.encryptmvp.util.algorithm.rsa.RSASignature;
import dhu.cst.zjm.encryptmvp.util.web.DownloadFileResponseBody;
import dhu.cst.zjm.encryptmvp.util.web.ProgressListener;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zjm on 2017/3/3.
 */

public class FileTypePresenter implements FileTypeContract.Presenter {

    private FileTypeContract.View mView;
    private ListEncryptTypeUseCase mListEncryptTypeUseCase;
    private ResponseBodyUseCase mResponseBodyUseCase;
    private EncryptRelationUseCase mEncryptRelationUseCase;
    private CompositeSubscription mCompositeSubscription;
    private String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Encrypt/";

    public FileTypePresenter(ListEncryptTypeUseCase listEncryptTypeUseCase, ResponseBodyUseCase responseBodyUseCase, EncryptRelationUseCase encryptRelationUseCase) {
        this.mListEncryptTypeUseCase = listEncryptTypeUseCase;
        this.mResponseBodyUseCase = responseBodyUseCase;
        this.mEncryptRelationUseCase = encryptRelationUseCase;
    }

    @Override
    public void attachView(FileTypeContract.View BaseView) {
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
    public void getEncryptType() {
        Subscription subscription = mListEncryptTypeUseCase.getEncryptType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<EncryptType>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.getFileTypeNetworkError();
                    }

                    @Override
                    public void onNext(List<EncryptType> list) {
                        mView.updateEncryptType(list);
                    }

                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void encryptFile(EncryptRelation encryptRelation) {
        switch (encryptRelation.getTypeId()) {
            case 1:
                mView.setDesKey(encryptRelation);
                break;
        }
    }

    @Override
    public void encryptBaseType(EncryptRelation encryptRelation, String desKey, String desLayer) {
        mEncryptRelationUseCase.setFileId(encryptRelation.getFileId() + "");
        mEncryptRelationUseCase.setTypeId(encryptRelation.getTypeId() + "");
        mEncryptRelationUseCase.setDesKey(desKey);
        mEncryptRelationUseCase.setDesLayer(desLayer);
        Subscription subscription = mEncryptRelationUseCase.encryptFile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EncryptRelation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.encryptBaseTypeNetworkError();
                    }

                    @Override
                    public void onNext(EncryptRelation encryptRelation) {
                        mView.encryptBaseTypeEncryptSuccess();
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void downloadFile(final dhu.cst.zjm.encryptmvp.mvp.model.File file, final ProgressListener listener) {
        String[] s = file.getName().split("\\.");
        final String realName = s[0];
        Subscription subscription = mResponseBodyUseCase.downloadFile(file.getId() + "")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Func1<ResponseBody, DownloadFileResponseBody>() {
                    @Override
                    public DownloadFileResponseBody call(ResponseBody responseBody) {
                        return new DownloadFileResponseBody(responseBody, listener);
                    }
                })
                .observeOn(Schedulers.computation())
                .map(new Func1<DownloadFileResponseBody, InputStream>() {
                    @Override
                    public InputStream call(DownloadFileResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())
                .subscribe(new Observer<InputStream>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.downloadFileNetworkError();
                    }

                    @Override
                    public void onNext(InputStream inputStream) {
                        String dir = FileUtil.createDir(downloadPath + file.getOwner() + "/Save/");
                        File out = new File(dir + File.separator + realName + ".zip");
                        if (!out.exists()) {
                            try {
                                out.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            OutputStream outputStream = null;
                            byte[] fileReader = new byte[4096];
                            long fileSizeDownloaded = 0;
                            outputStream = new FileOutputStream(out);
                            while (true) {
                                int read = inputStream.read(fileReader);
                                if (read == -1) {
                                    break;
                                }
                                outputStream.write(fileReader, 0, read);
                                fileSizeDownloaded += read;
                                Log.i("Download Success ", "file download: " + fileSizeDownloaded);
                            }
                            outputStream.flush();
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void decryptFile(EncryptRelation encryptRelation, dhu.cst.zjm.encryptmvp.mvp.model.File file) {
        String[] s = file.getName().split("\\.");
        String realName = s[0];
        String downloadDir = FileUtil.createDir(downloadPath + file.getOwner() + "/Save/");
        File out = new File(downloadDir + File.separator + realName + ".zip");
        if (!out.exists()) {
            mView.decryptFileExistError();
            return;
        }
        switch (encryptRelation.getTypeId()) {
            case 1:
                mView.confirmDesKey(encryptRelation);
                break;
        }

    }

    @Override
    public void decryptBaseType(final dhu.cst.zjm.encryptmvp.mvp.model.File file, final String desKey) {
        final String[] s = file.getName().split("\\.");
        final String realName = s[0];
        final String downloadDir = FileUtil.createDir(downloadPath + file.getOwner() + "/Save/");
        final String zipDir = FileUtil.createDir(downloadPath + file.getOwner() + "/Zip/");
        final String decryptDir = FileUtil.createDir(downloadPath + file.getOwner() + "/Decrypt");
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    String publicKey, sign, hashSign;
                    ZipUtil.ZipDecrypt(downloadDir, realName + ".zip", zipDir);
                    publicKey = FileUtil.File2String(new File(zipDir + "public.key"));
                    sign = FileUtil.File2String(new File(zipDir + "sign.sign"));
                    byte[] decrypt = DesUtil.decrypt(FileUtil.File2byte(zipDir + s[0] + ".encrypt"), desKey.getBytes());
                    FileUtil.byte2File(decrypt, decryptDir, file.getName());
                    hashSign = Md5Util.getMd5ByFile(new File(decryptDir, file.getName()));
                    boolean result = RSASignature.doCheck(hashSign, sign, publicKey);
                    subscriber.onNext(result);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.decryptBaseTypeDecryptError();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mView.decryptBaseTypeDecryptSuccess();
                        } else {
                            mView.decryptBaseTypeDecryptFailed();
                        }
                    }
                });
        mCompositeSubscription.add(subscription);
    }


}
