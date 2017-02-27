package dhu.cst.zjm.encryptmvp.mvp.model;

import com.google.gson.annotations.Expose;

/**
 * Created by zjm on 2017/2/24.
 */

public class User {
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private String password;
    @Expose
    private boolean isLogin;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setIsLogin(boolean state) {
        isLogin = state;
    }

    public boolean getIsLogin() {
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
