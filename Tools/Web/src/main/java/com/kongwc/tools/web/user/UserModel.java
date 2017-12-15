package com.kongwc.tools.web.user;

import lombok.Data;

/**
 * Created by zhanghx on 2017/09/26.
 */
@Data
public class UserModel {

    private String username;
    private String password;
    private String newPass;
    private String confirmPass;
}
