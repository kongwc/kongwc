package com.kongwc.tools.web.mobclient;

import com.kongwc.tools.common.data.MobClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * 手持终端管理画面初始化控制类
 */
@Controller
@Slf4j
public class MobClientControl {

    @RequestMapping(value = "/mobClient/list", method = RequestMethod.GET)
    public String mobClient(Map<String, Object> model) {
        MobClient clientInfo = new MobClient();
        model.put("clientInfo", clientInfo);
        return "mobClient-list";
    }
}
