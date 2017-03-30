package dhu.cst.zjm.encryptmvp.domain.base;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by zjm on 3/29/2017.
 */

public interface BaseResponseBodyUseCase<ResponseBody> {
    Observable<ResponseBody> downloadFile(String downloadId);
}
