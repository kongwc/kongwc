package com.kongwc.tools.web.login;

import com.kongwc.tools.common.data.User;
import com.kongwc.tools.web.user.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by litong on 2017/06/27.
 */
@Controller
@Slf4j
public class LoginControl {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String index(Map<String, Object> model) {
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(Map<String, Object> model) {
        return "login";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String defaultMenu(HttpServletRequest request, Map<String, Object> model) {
        UserModel userModel = new UserModel();
        userModel.setUsername(request.getRemoteUser());
        model.put("deviceWebUserModel", userModel);
        User userInfo = new User();
        userInfo.setUserName(request.getRemoteUser());
        model.put("userInfo", userInfo);
        return "user-list";
    }
}
