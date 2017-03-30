package dhu.cst.zjm.encryptmvp.domain.base;

import dhu.cst.zjm.encryptmvp.mvp.model.EncryptType;
import rx.Observable;

/**
 * Created by zjm on 3/23/2017.
 */

public interface BaseListEncryptTypeUseCase<T> {
    Observable<T> getEncryptType();
}
