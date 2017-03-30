package dhu.cst.zjm.encryptmvp.domain;

import java.util.HashMap;
import java.util.Map;

import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.base.BaseEncryptRelationUseCase;
import dhu.cst.zjm.encryptmvp.mvp.model.EncryptRelation;
import rx.Observable;

/**
 * Created by zjm on 3/23/2017.
 */

public class EncryptRelationUseCase implements BaseEncryptRelationUseCase<EncryptRelation> {
    private BaseRepository mRepository;
    private Map<String, String> params=new HashMap<>();;

    public EncryptRelationUseCase(BaseRepository baseRepository) {
        this.mRepository = baseRepository;
    }

    public void setFileId(String fileId) {
        params.put("file_id", fileId);
    }

    public void setTypeId(String typeId) {
        params.put("type_id", typeId);
    }

    public void setDesKey(String desKey) {
        params.put("des_key", desKey);
    }

    public void setDesLayer(String desLayer) {
        params.put("des_layer", desLayer);
    }

    @Override
    public Observable<EncryptRelation> encryptFile() {
        return mRepository.encryptFile(params);
    }
}
