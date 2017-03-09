package dhu.cst.zjm.encryptmvp.mvp.model;

import com.google.gson.annotations.Expose;

/**
 * Created by zjm on 2017/3/3.
 */

public class EncryptType {

    @Expose
    private String name;
    @Expose
    private int id;
    @Expose
    private String inf;

    public EncryptType(int id, String name, String inf) {
        this.id = id;
        this.name = name;
        this.inf = inf;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setInf(String inf) {
        this.inf = inf;
    }

    public String getInf() {
        return inf;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
