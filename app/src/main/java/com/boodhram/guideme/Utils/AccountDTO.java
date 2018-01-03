package com.boodhram.guideme.Utils;

import java.io.Serializable;

/**
 * Created by shaulkory on 1/3/2018.
 */

public class AccountDTO implements Serializable {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
