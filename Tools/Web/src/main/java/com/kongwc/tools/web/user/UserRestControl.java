package com.kongwc.tools.web.user;

import com.alibaba.fastjson.JSON;
import com.kongwc.tools.common.data.User;
import com.kongwc.tools.common.data.UserHis;
import com.kongwc.tools.logic.user.UserBatchParam;
import com.kongwc.tools.logic.user.UserGetResult;
import com.kongwc.tools.logic.user.UserLogic;
import com.kongwc.tools.logic.user.UserParam;
import com.kongwc.tools.logic.userhis.UserHisGetResult;
import com.kongwc.tools.logic.userhis.UserHisLogic;
import com.kongwc.tools.logic.userhis.UserHisParam;
import com.kongwc.tools.security.ManageWebAuthenticationProvider;
import com.kongwc.tools.security.ManageWebUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 用户信息管理画面控制类
 */
@RestController
@Slf4j
public class UserRestControl {

    @Autowired
    private ManageWebAuthenticationProvider provider;
    @Autowired
    private ManageWebUserDetailsService userService;

    @Autowired
    private UserLogic userLogic;

    @Autowired
    private UserHisLogic userHisLogic;

    @RequestMapping(value = "/user/chgpw", method = RequestMethod.POST)
    public String chgps(HttpServletRequest request, @ModelAttribute UserModel userModel, Map<String, Object> model) {
        if (!provider.authenticateUser(request.getRemoteUser(), userModel.getPassword())) {
            return "{\"errorMsg\":\"当前密码不正确！\"}";
        }
        if (!userService.chgUserPassword(provider.encryptSHA(userModel.getNewPass()))) {
            return "{\"errorMsg\":\"密码修改失败！\"}";
        }

        return "{\"message\":\"密码修改成功！\"}";
    }

    @RequestMapping(value = "/user/listData", method = RequestMethod.POST)
    public String userList(@RequestParam(value = "page") Integer page, @RequestParam(value = "rows") Integer size,
                           @ModelAttribute User user, HttpServletRequest request) {
        // 调用者信息设为IP地址
        userLogic.setCaller(request.getRemoteAddr());

        UserParam param = new UserParam();
        UserGetResult result = userLogic.userGet(param);
        if (result.getUsers() == null) {
            return "{\"total\":0,\"rows\":[]}";
        }

        Iterator<User> iterator = result.getUsers().iterator();

        while (iterator.hasNext()) {
            User userInfo = iterator.next();
            if (!StringUtils.isEmpty(user.getUserName()) && !userInfo.getUserName().contains(user.getUserName())) {
                iterator.remove();
            }
        }

        List<User> curPageList;
        int curIndex = (page - 1) * size;
        if (result.getUsers().size() / size >= page) {
            curPageList = result.getUsers().subList(curIndex, page * size);
        } else {
            curPageList = result.getUsers().subList(curIndex, result.getUsers().size());
        }
        String resultJson = "{\"total\":" + result.getUsers().size() + ",\"rows\":" + JSON.toJSONString(curPageList) + "}";
        return resultJson;
    }

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public String add(@ModelAttribute User user, HttpServletRequest request) {
        // 调用者信息
        userLogic.setCaller(request.getRemoteUser());

        UserParam param = new UserParam();
        param.setUserId(user.getUserId());
        param.setUserName(user.getUserName());
        param.setPass(user.getPass());
        param.setClientId(user.getClientId());
        param.setStatus(user.getStatus());
        param.setLastLoginTime(user.getLastLoginTime());
        param.setDescription(user.getDescription());

        return userLogic.userAdd(param).toJson();
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public String edit(@ModelAttribute User user, HttpServletRequest request) {
        // 调用者信息
        userLogic.setCaller(request.getRemoteUser());

        UserParam param = new UserParam();
        param.setUserId(user.getUserId());
        param.setUserName(user.getUserName());
        param.setPass(user.getPass());
        param.setClientId(user.getClientId());
        param.setStatus(user.getStatus());
        param.setLastLoginTime(user.getLastLoginTime());
        param.setDescription(user.getDescription());

        return userLogic.userUpdate(param).toJson();
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public String delete(@ModelAttribute User user, HttpServletRequest request) {
        // 调用者信息
        userLogic.setCaller(request.getRemoteUser());

        UserParam param = new UserParam();
        param.setUserId(user.getUserId());

        return userLogic.userDelete(param).toJson();
    }

    @RequestMapping(value = "/user/batch", method = RequestMethod.POST)
    public String batch(@RequestParam("userFile") MultipartFile file) throws InvalidFormatException {
        // 调用者信息
        userLogic.setCaller("BATCH");
        UserBatchParam param = new UserBatchParam();

        List<User> users = new ArrayList<>();
        InputStream ins = null;
        try {
            ins = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(ins);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                XSSFRow row = sheet.getRow(i);
                User user = new User();
                for (int j = 0; j < 6; j++) {
                    row.getCell(j).setCellType(CellType.STRING);
                }
                if (StringUtils.isEmpty(row.getCell(0).getStringCellValue())) {
                    break;
                }

                user.setUserId(row.getCell(0).getStringCellValue());
                user.setUserName(row.getCell(1).getStringCellValue());
                user.setPass(row.getCell(2).getStringCellValue());
                user.setClientId(row.getCell(3).getStringCellValue());
                user.setStatus(2);
                user.setDescription(row.getCell(4).getStringCellValue());
                user.setBatchAction(Integer.valueOf(row.getCell(5).getStringCellValue()));

                users.add(user);
            }
        } catch(IOException e) {
            log.error(e.getMessage(), e);
            return "";
        } finally {
            if (ins != null){
                try {
                    ins.close();
                } catch(IOException e){
                    log.error(e.getMessage(), e);
                }
            }
        }
        param.setUsers(users);
        return userLogic.batch(param).toJson();
    }

    @RequestMapping(value = "/userHis/listData", method = RequestMethod.POST)
    public String userHisList(@RequestParam(value = "page") Integer page, @RequestParam(value = "rows") Integer size,
                              @ModelAttribute UserHis userHis, HttpServletRequest request) {
        // 调用者信息设为IP地址
        userLogic.setCaller(request.getRemoteAddr());

        UserHisParam param = new UserHisParam();
        param.setUserId(userHis.getUserId());
        UserHisGetResult result = userHisLogic.userHisGet(param);
        if (result.getUserHises() == null || result.getUserHises().isEmpty()) {
            return "{\"total\":0,\"rows\":[]}";
        }
        Iterator<UserHis> iterator = result.getUserHises().iterator();

        while (iterator.hasNext()) {
            UserHis userHisInfo = iterator.next();
            if (!StringUtils.isEmpty(userHis.getUserId()) && !userHis.getUserId().equals(userHisInfo.getUserId())) {
                iterator.remove();
            }
        }

        List<UserHis> curPageList;
        int curIndex = (page - 1) * size;
        if (result.getUserHises().size() / size >= page) {
            curPageList = result.getUserHises().subList(curIndex, page * size);
        } else {
            curPageList = result.getUserHises().subList(curIndex, result.getUserHises().size());
        }
        String resultJson = "{\"total\":" + result.getUserHises().size() + ",\"rows\":" + JSON.toJSONString(curPageList) + "}";
        return resultJson;
    }
}
