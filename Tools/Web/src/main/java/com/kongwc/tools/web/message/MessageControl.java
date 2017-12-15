package com.kongwc.tools.web.message;

import com.kongwc.tools.common.data.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * 消息发布画面初期化控制类
 */
@Controller
@Slf4j
public class MessageControl {

    @RequestMapping(value = "/message/list", method = RequestMethod.GET)
    public String user(Map<String, Object> model) {
        Message messageInfo = new Message();
        model.put("messageInfo", messageInfo);
        return "message-list";
    }
}
