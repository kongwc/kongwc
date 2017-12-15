package com.kongwc.tools.web.message;

import com.alibaba.fastjson.JSON;
import com.kongwc.tools.common.data.Message;
import com.kongwc.tools.logic.message.MessageDownloadParam;
import com.kongwc.tools.logic.message.MessageGetResult;
import com.kongwc.tools.logic.message.MessageLogic;
import com.kongwc.tools.logic.message.MessageParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 消息发布画面控制类
 */
@RestController
@Slf4j
public class MessageRestControl {

    @Autowired
    private MessageLogic messageLogic;

    @RequestMapping(value = "/message/listData", method = RequestMethod.POST)
    public String messageList(@RequestParam(value = "page") Integer page, @RequestParam(value = "rows") Integer size,
                              @ModelAttribute Message message, HttpServletRequest request) {
        // 调用者信息
        messageLogic.setCaller(request.getRemoteUser());

        MessageParam param = new MessageParam();
        MessageGetResult result = messageLogic.messageGet(param);
        if (result.getMessages() == null) {
            return "{\"total\":0,\"rows\":[]}";
        }

        Iterator<Message> iterator = result.getMessages().iterator();

        while (iterator.hasNext()) {
            Message messageInfo = iterator.next();
            if (!StringUtils.isEmpty(message.getMessageTitle()) && !messageInfo.getMessageTitle().contains(message.getMessageTitle())) {
                iterator.remove();
            }
        }

        List<Message> curPageList;
        int curIndex = (page - 1) * size;
        if (result.getMessages().size() / size >= page) {
            curPageList = result.getMessages().subList(curIndex, page * size);
        } else {
            curPageList = result.getMessages().subList(curIndex, result.getMessages().size());
        }
        String resultJson = "{\"total\":" + result.getMessages().size() + ",\"rows\":" + JSON.toJSONString(curPageList) + "}";
        return resultJson;
    }

    @RequestMapping(value = "/message/add", method = RequestMethod.POST)
    public String add(@ModelAttribute Message message, HttpServletRequest request) {
        // 调用者信息
        messageLogic.setCaller(request.getRemoteUser());

        MessageParam param = new MessageParam();
        param.setMessageId(message.getMessageId());
        param.setMessageTitle(message.getMessageTitle());
        param.setMessage(message.getMessage());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
        param.setPublishTime(sdf.format(new Date()));

        return messageLogic.messageAdd(param).toJson();
    }

    @RequestMapping(value = "/message/edit", method = RequestMethod.POST)
    public String edit(@ModelAttribute Message message, HttpServletRequest request) {
        // 调用者信息
        messageLogic.setCaller(request.getRemoteUser());

        MessageParam param = new MessageParam();
        param.setMessageId(message.getMessageId());
        param.setMessageTitle(message.getMessageTitle());
        param.setMessage(message.getMessage());
        param.setPublishTime(message.getPublishTime());

        return messageLogic.messageUpdate(param).toJson();
    }

    @RequestMapping(value = "/message/delete", method = RequestMethod.POST)
    public String delete(@ModelAttribute Message message, HttpServletRequest request) {
        // 调用者信息
        messageLogic.setCaller(request.getRemoteUser());

        MessageParam param = new MessageParam();
        param.setMessageId(message.getMessageId());

        return messageLogic.messageDelete(param).toJson();
    }

    @RequestMapping(value = "/message/download", method = RequestMethod.POST)
    public String download(@RequestParam("messageId") String messageId, HttpServletRequest request,
                           HttpServletResponse response) {
        // 调用者信息
        messageLogic.setCaller(request.getRemoteUser());

        MessageDownloadParam param = new MessageDownloadParam();
        param.setFileDir(messageId);
        param.setParamHttpServletResponse(response);

        return messageLogic.download(param).toJson();
    }

    @RequestMapping(value = "/message/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam("packageName") String packageName,
                         HttpServletRequest request) {
        // 调用者信息
        messageLogic.setCaller(request.getRemoteUser());

        List<MultipartFile> files = new ArrayList<>();
        files.add(file);

        MessageParam param = new MessageParam();
        param.setMessageId(packageName);
        param.setParamMultipartFiles(files);

        return messageLogic.upload(param).toJson();
    }
}
