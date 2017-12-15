package com.kongwc.tools.logic.user;

import com.kongwc.tools.common.data.ApiParameter;
import com.kongwc.tools.common.data.User;
import lombok.Data;

import java.util.List;

/**
 * 用户信息参数
 */
@Data
public class UserBatchParam extends ApiParameter {

    private List<User> users;
}
