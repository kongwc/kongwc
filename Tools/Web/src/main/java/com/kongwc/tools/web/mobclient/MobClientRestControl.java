package com.kongwc.tools.web.mobclient;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.kongwc.tools.common.data.MobClient;
import com.kongwc.tools.logic.mobclient.ClientGetResult;
import com.kongwc.tools.logic.mobclient.ClientLogic;
import com.kongwc.tools.logic.mobclient.ClientParam;
import com.kongwc.tools.logic.mobclient.MobClientBatchParam;
import com.kongwc.tools.security.SHAencrypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.apache.phoenix.util.StringUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 手持终端管理画面控制类
 */
@RestController
@Slf4j
public class MobClientRestControl {

    @Autowired
    private ClientLogic clientLogic;

    @RequestMapping(value = "/mobClient/listData", method = RequestMethod.POST)
    public String mobClientList(@RequestParam(value = "page") Integer page, @RequestParam(value = "rows") Integer size,
                                @ModelAttribute MobClient mobClient, HttpServletRequest request) {
        // 调用者信息
        clientLogic.setCaller(request.getRemoteUser());

        ClientParam param = new ClientParam();
        ClientGetResult result = clientLogic.clientGet(param);
        if (result.getMobClients() == null) {
            return "{\"total\":0,\"rows\":[]}";
        }

        Iterator<MobClient> iterator = result.getMobClients().iterator();

        while (iterator.hasNext()) {
            MobClient clientInfo = iterator.next();
            if (!StringUtils.isEmpty(mobClient.getImei()) && !clientInfo.getImei().contains(mobClient.getImei())) {
                iterator.remove();
            }
        }

        List<MobClient> curPageList;
        int curIndex = (page - 1) * size;
        if (result.getMobClients().size() / size >= page) {
            curPageList = result.getMobClients().subList(curIndex, page * size);
        } else {
            curPageList = result.getMobClients().subList(curIndex, result.getMobClients().size());
        }
        String resultJson = "{\"total\":" + result.getMobClients().size() + ",\"rows\":" + JSON.toJSONString(curPageList) + "}";
        return resultJson;
    }

    @RequestMapping(value = "/mobClient/add", method = RequestMethod.POST)
    public String add(@ModelAttribute MobClient mobClient, HttpServletRequest request) throws SQLException {
        // 调用者信息
        clientLogic.setCaller(request.getRemoteUser());

        ClientParam param = new ClientParam();
        param.setClientId(mobClient.getClientId());
        param.setImei(mobClient.getImei());
        param.setClientModel(mobClient.getClientModel());
        param.setStatus(mobClient.getStatus());
        param.setDescription(mobClient.getDescription());
        param.setLastOnlineTime(mobClient.getLastOnlineTime());

        if (!StringUtils.isEmpty(mobClient.getImei())) {
            String iMeiSHA = StringUtil.EMPTY_STRING;
            try {
                iMeiSHA = SHAencrypt.encryptSHA(mobClient.getImei());
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
            Blob blob = new SerialBlob(iMeiSHA.getBytes());
            param.setSdFile(blob);
        }
        return clientLogic.clientAdd(param).toJson();
    }

    @RequestMapping(value = "/mobClient/edit", method = RequestMethod.POST)
    public String edit(@ModelAttribute MobClient mobClient, HttpServletRequest request) throws SQLException {
        // 调用者信息
        clientLogic.setCaller(request.getRemoteUser());

        ClientParam param = new ClientParam();
        param.setClientId(mobClient.getClientId());
        param.setImei(mobClient.getImei());
        param.setClientModel(mobClient.getClientModel());
        param.setStatus(mobClient.getStatus());
        param.setDescription(mobClient.getDescription());
        param.setLastOnlineTime(mobClient.getLastOnlineTime());

        if (!StringUtils.isEmpty(mobClient.getImei())) {
            String iMeiSHA = StringUtil.EMPTY_STRING;
            try {
                iMeiSHA = SHAencrypt.encryptSHA(mobClient.getImei());
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
            Blob blob = new SerialBlob(iMeiSHA.getBytes());
            param.setSdFile(blob);
        }
        return clientLogic.clientUpdate(param).toJson();
    }

    @RequestMapping(value = "/mobClient/delete", method = RequestMethod.POST)
    public String delete(@ModelAttribute MobClient mobClient, HttpServletRequest request) {
        // 调用者信息
        clientLogic.setCaller(request.getRemoteUser());

        ClientParam param = new ClientParam();
        param.setClientId(mobClient.getClientId());

        return clientLogic.clientDelete(param).toJson();
    }

    @RequestMapping(value = "/mobClient/batch", method = RequestMethod.POST)
    public String batch(@RequestParam("mobClientFile") MultipartFile file, HttpServletRequest request)
            throws SQLException, InvalidFormatException {
        // 调用者信息
        clientLogic.setCaller("BATCH");

        MobClientBatchParam param = new MobClientBatchParam();

        List<MobClient> mobClients = new ArrayList<>();
        InputStream ins = null;
        try {
            ins = file.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(ins);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
                XSSFRow row = sheet.getRow(i);
                MobClient mobClient = new MobClient();
                for (int j = 0; j < 5; j++) {
                    row.getCell(j).setCellType(CellType.STRING);
                }
                if (StringUtils.isEmpty(row.getCell(0).getStringCellValue())) {
                    break;
                }

                mobClient.setClientId(row.getCell(0).getStringCellValue());

                String iMei = row.getCell(1).getStringCellValue();
                mobClient.setImei(iMei);
                mobClient.setClientModel(row.getCell(2).getStringCellValue());
                mobClient.setDescription(row.getCell(3).getStringCellValue());

                if (!StringUtils.isEmpty(iMei)) {
                    String iMeiSHA = StringUtil.EMPTY_STRING;
                    try {
                        iMeiSHA = SHAencrypt.encryptSHA(iMei);
                    } catch (Exception ex) {
                        log.error(ex.getMessage(), ex);
                    }
                    Blob blob = new SerialBlob(iMeiSHA.getBytes());
                    mobClient.setSdFile(blob);
                }
                mobClient.setBatchAction(Integer.valueOf(row.getCell(4).getStringCellValue()));

                mobClients.add(mobClient);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return "";
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        param.setMobClients(mobClients);
        return clientLogic.batch(param).toJson();
    }

    @RequestMapping(value = "/mobClient/download", method = RequestMethod.POST)
    public void download(@RequestParam("clientId") String clientId, HttpServletRequest request,
                         HttpServletResponse response) throws SQLException {
        // 调用者信息
        clientLogic.setCaller(request.getRemoteUser());

        ClientParam param = new ClientParam();
        param.setClientId(clientId);
        ClientGetResult result = clientLogic.clientGet(param);

        List<MobClient> mobClients = result.getMobClients();

        if (mobClients != null && !mobClients.isEmpty()) {
            MobClient mobClient = mobClients.get(0);

            try {
                response.setContentType(ContentType.APPLICATION_OCTET_STREAM.getMimeType());
                response.setHeader("Content-disposition", "attachment;filename*=UTF-8''" +
                        URLEncoder.encode("sd_cert_" + clientId + ".pem", "UTF-8"));

                OutputStream os = response.getOutputStream();
                Blob sdFile = mobClient.getSdFile();
                if (sdFile != null) {
                    InputStream is = sdFile.getBinaryStream();
                    byte[] buffer = new byte[is.available()];
                    BufferedInputStream bis = new BufferedInputStream(is);
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer);
                        i = bis.read(buffer);
                    }
                    bis.close();
                    is.close();
                }
                response.flushBuffer();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
