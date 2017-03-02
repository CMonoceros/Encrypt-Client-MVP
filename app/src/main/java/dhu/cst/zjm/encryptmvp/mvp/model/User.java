package dhu.cst.zjm.encryptmvp.mvp.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by zjm on 2017/2/24.
 */

public class User implements Serializable{
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private String password;
    @Expose
    private int isLogin;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public void setIsLogin(int state) {
        isLogin = state;
    }

    public int getIsLogin() {
        return isLogin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
