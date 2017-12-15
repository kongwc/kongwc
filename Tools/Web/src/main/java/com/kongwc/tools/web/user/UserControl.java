package com.kongwc.tools.web.user;

import com.kongwc.tools.common.data.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户信息管理画面初期化控制类
 */
@Controller
@Slf4j
public class UserControl {

    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public String user(HttpServletRequest request, Map<String, Object> model) {
        UserModel userModel = new UserModel();
        userModel.setUsername(request.getRemoteUser());
        model.put("deviceWebUserModel", userModel);
        User userInfo = new User();
        userInfo.setUserName(request.getRemoteUser());
        model.put("userInfo", userInfo);
        return "user-list";
    }
}
