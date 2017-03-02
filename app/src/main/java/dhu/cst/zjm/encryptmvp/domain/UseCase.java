package dhu.cst.zjm.encryptmvp.domain;


import rx.Observable;

/**
 * Created by zjm on 2017/2/24.
 */

public interface UseCase<T> {
    Observable<T> execute();
}
