package com.kongwc.tools.logic.mobclient;

import com.kongwc.tools.common.consts.Constant;
import com.kongwc.tools.common.data.MobClient;
import com.kongwc.tools.common.database.accessor.RDBAccessor;
import com.kongwc.tools.common.database.domain.XzMobClient;
import com.kongwc.tools.common.database.tools.FieldNameConverter;
import com.kongwc.tools.common.exception.WrongParameterException;
import com.kongwc.tools.common.util.DateUtil;
import com.kongwc.tools.common.util.ReflectionUtil;
import com.kongwc.tools.logic.AbstractLogic;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.phoenix.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 手持终端信息表相关操作逻辑类
 */
@Service
@Slf4j
public class ClientLogic extends AbstractLogic {
    /**
     * RDB操作工具
     */
    @Autowired
    private RDBAccessor rdbAccessor;

    /**
     * 字段名转换器
     */
    @Autowired
    private FieldNameConverter nameConverter;

    /**
     * {@inheritDoc}
     */
    public ClientGetResult clientGet(ClientParam param) {
        // 生成SQL文
        String sql = makeInquirySqlGet(param);
        List<XzMobClient> xzMobClientList = rdbAccessor.query(sql, XzMobClient.class);
        Long count = rdbAccessor.queryForObject("SELECT FOUND_ROWS()", Long.class);
        List<MobClient> mobClients = new ArrayList<>();
        for (XzMobClient xzMobClient : xzMobClientList) {
            MobClient mobClient = new MobClient();
            mobClients.add(mobClient);

            //终端ID
            mobClient.setClientId(xzMobClient.getClientId());
            //终端IMEI
            mobClient.setImei(xzMobClient.getImei());
            //终端型号
            mobClient.setClientModel(xzMobClient.getClientModel());
            //状态
            mobClient.setStatus(xzMobClient.getStatus());
            //SD卡证书文件
            mobClient.setSdFile(xzMobClient.getSdFile());
            //最后联机时刻
            if (xzMobClient.getLastOnlineTime() != null) {
                mobClient.setLastOnlineTime(xzMobClient.getLastOnlineTime().toString());
            }
            //描述
            mobClient.setDescription(xzMobClient.getDescription());
        }

        ClientGetResult result = new ClientGetResult();
        result.setMobClients(mobClients);
        result.setDataCount(count);
        if (param.getStartIndex() != null) {
            result.setStartIndex(Long.parseLong(param.getStartIndex()));
        } else {
            result.setStartIndex(0L);
        }
        if (param.getEndIndex() != null) {
            result.setEndIndex(Long.parseLong(param.getEndIndex()));
        } else {
            result.setEndIndex(0L);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public MobClientAddResult clientAdd(ClientParam param) {
        log.debug("parameter == {}", param);

        if (StringUtils.isEmpty(param.getClientId())) {
            throw new WrongParameterException("\"clientId\" is necessary.");
        }

        MobClientAddResult result = new MobClientAddResult();
        // SQL参数
        List<Object> args = new ArrayList<>();
        // 生成SQL文
        String sql = makeInquirySql(param, args);
        Object[] argsArr = args.toArray(new Object[0]);
        List<XzMobClient> xzMobClientList = rdbAccessor.query(sql, XzMobClient.class, argsArr);

        if (!xzMobClientList.isEmpty()) {
            result.setReturnCode(Constant.ApiReturnCode.ERROR_PARAM_WRONG);
            result.setErrorMessage(String.format("ClientId already exist. %s", param.getClientId()));
            return result;
        }

        XzMobClient xzMobClient = new XzMobClient();
        //终端ID
        xzMobClient.setClientId(param.getClientId());
        //终端IMEI
        xzMobClient.setImei(param.getImei());
        //终端类型
        xzMobClient.setClientModel(param.getClientModel());
        //状态
        xzMobClient.setStatus(param.getStatus());
        //SD卡证书文件
        xzMobClient.setSdFile(param.getSdFile());
        //描述
        xzMobClient.setDescription(param.getDescription());
        //最后联机时间
        if (!StringUtils.isEmpty(param.getLastOnlineTime())) {
            xzMobClient.setLastOnlineTime(DateUtil.getDateFromDefaultFormatBySecond(param.getLastOnlineTime()
                    .replaceAll(":", StringUtil.EMPTY_STRING)
                    .replaceAll("-", StringUtil.EMPTY_STRING)
                    .replaceAll(" ", StringUtil.EMPTY_STRING)));
        }
        setUserTimeForCreate(xzMobClient);
        rdbAccessor.insert("XZ_MOB_CLIENT", xzMobClient);

        result.setClientId(param.getClientId());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public MobClientUpdateResult clientUpdate(ClientParam param) {
        log.debug("parameter == {}", param);

        if (param.getClientId() == null) {
            throw new WrongParameterException("\"clientId\" is necessary.");
        }

        MobClientUpdateResult result = new MobClientUpdateResult();

        XzMobClient xzMobClient = new XzMobClient();
        //终端ID
        xzMobClient.setClientId(param.getClientId());
        //终端IMEI
        xzMobClient.setImei(param.getImei());
        //终端类型
        xzMobClient.setClientModel(param.getClientModel());
        //状态
        xzMobClient.setStatus(param.getStatus());
        //SD卡证书文件
        xzMobClient.setSdFile(param.getSdFile());
        //描述
        xzMobClient.setDescription(param.getDescription());
        //最后联机时间
        if (!StringUtils.isEmpty(param.getLastOnlineTime())) {
            xzMobClient.setLastOnlineTime(DateUtil.getDateFromDefaultFormatBySecond(param.getLastOnlineTime()
                    .replaceAll(":", StringUtil.EMPTY_STRING)
                    .replaceAll("-", StringUtil.EMPTY_STRING)
                    .replaceAll(" ", StringUtil.EMPTY_STRING)));
        }

        setUserTimeForUpdate(xzMobClient);

        int count = rdbAccessor.update("XZ_MOB_CLIENT", ReflectionUtil.describeBean(xzMobClient, nameConverter), " CLIENT_ID = ? ", param.getClientId());
        if (count == 0) {
            result.setReturnCode(Constant.ApiReturnCode.ERROR_DATA_NOT_FOUND);
            result.setErrorMessage(String.format("Data not found. %s", param.getClientId()));
            return result;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public MobClientDeleteResult clientDelete(ClientParam param) {
        MobClientDeleteResult result = new MobClientDeleteResult();
        String clientId = param.getClientId();
        int count = rdbAccessor.delete("XZ_MOB_CLIENT", " CLIENT_ID = ? ", clientId);
        if (count == 0) {
            result.setReturnCode(Constant.ApiReturnCode.ERROR_PARAM_WRONG);
            result.setErrorMessage(String.format("Error param wrong. %s", clientId));
            return result;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public MobClientBatchResult batch(MobClientBatchParam param) {
        log.debug("parameter == {}", param);

        List<MobClient> mobClients = param.getMobClients();
        if (mobClients == null || mobClients.size() == 0) {
            throw new WrongParameterException("\"mobClients\" is necessary.");
        }

        MobClientBatchResult result = new MobClientBatchResult();

        List<String> clientIds = new ArrayList<>();
        for (MobClient mobClient : param.getMobClients()) {
            if (mobClient.getBatchAction() == null) {
                throw new WrongParameterException(Constant.MessageCode.MUST_INPUT, "更新动作");
            }

            if (mobClient.getBatchAction() == 1) {
                ClientParam getParam = new ClientParam();
                getParam.setClientId(mobClient.getClientId());
                ClientGetResult getResult = clientGet(getParam);
                if (getResult != null && getResult.getMobClients() != null && getResult.getMobClients().size() > 0) {

                    ClientParam updateParam = new ClientParam();
                    ReflectionUtil.copyFields(mobClient, updateParam);
                    setCaller(getCaller());
                    MobClientUpdateResult resultUpdate = clientUpdate(updateParam);
                    if (resultUpdate.getReturnCode() != 0) {
                        result.setReturnCode(resultUpdate.getReturnCode());
                        result.setErrorMessage(resultUpdate.getErrorMessage());
                        return result;
                    }
                } else if (getResult != null && getResult.getMobClients() != null && getResult.getMobClients().size() >= 0) {

                    ClientParam addParam = new ClientParam();
                    ReflectionUtil.copyFields(mobClient, addParam);
                    setCaller(getCaller());
                    MobClientAddResult resultAdd = clientAdd(addParam);
                    if (resultAdd.getReturnCode() != 0) {
                        result.setReturnCode(resultAdd.getReturnCode());
                        result.setErrorMessage(resultAdd.getErrorMessage());
                        return result;
                    }
                }
            } else if (mobClient.getBatchAction() == 2) {

                ClientParam deleteParam = new ClientParam();
                deleteParam.setClientId(mobClient.getClientId());
                setCaller(getCaller());
                MobClientDeleteResult resultDelete = clientDelete(deleteParam);
                if (resultDelete.getReturnCode() != 0) {
                    result.setReturnCode(resultDelete.getReturnCode());
                    result.setErrorMessage(resultDelete.getErrorMessage());
                    return result;
                }
            }

            clientIds.add(mobClient.getClientId());
        }

        result.setClientIds(clientIds);
        return result;
    }

    /**
     * 生成查询SQL
     *
     * @param param 查询参数
     * @return 查询结果
     */
    private String makeInquirySqlGet(ClientParam param) {

        StringBuilder sql = new StringBuilder();
        String table = "XZ_MOB_CLIENT";

        sql.append("select SQL_CALC_FOUND_ROWS * from ");
        sql.append(table);
        String[] clientIdArray = StringUtils.split(param.getClientId(), ',');
        if (clientIdArray != null && clientIdArray.length > 0) {
            String strClientId = "";
            for (String clientId : clientIdArray) {
                strClientId = strClientId.concat(clientId).concat("','");
            }
            strClientId = "'".concat(strClientId.substring(0, strClientId.length() - 2));
            param.setClientId(strClientId);
        }

        // ID
        if (!StringUtils.isEmpty(param.getClientId())) {
            sql.append(" where ");
            sql.append(String.format("CLIENT_ID in ( %s )", param.getClientId()));
        }

        sql.append(" order by CLIENT_ID ");
        String limit = param.getLimit();
        String startIndex = param.getStartIndex();
        String endIndex = param.getEndIndex();
        // 件数限制
        if (!StringUtils.isEmpty(limit)) {
            sql.append(" limit ");
            sql.append(param.getLimit());
        } else {
            // 分页检索
            if (!StringUtils.isEmpty(startIndex) && !StringUtils.isEmpty(endIndex)) {
                BigDecimal start = new BigDecimal(param.getStartIndex());
                BigDecimal end = new BigDecimal(param.getEndIndex());
                sql.append(" limit ".concat(start.subtract(BigDecimal.ONE).toString()).concat(",").concat(end.subtract(start).add(BigDecimal.ONE).toString()));
            }
        }
        return sql.toString();
    }

    /**
     * 生成查询SQL
     *
     * @param param 查询参数
     * @param args  SQL参数
     * @return 查询结果
     */
    private String makeInquirySql(ClientParam param, List<Object> args) {

        StringBuilder sql = new StringBuilder();
        String table = "XZ_MOB_CLIENT";

        sql.append("select * from ");
        sql.append(table);

        // ID
        if (!StringUtils.isEmpty(param.getClientId())) {
            sql.append(" where ");
            sql.append("CLIENT_ID = ? ");
            args.add(param.getClientId());
        }
        sql.append(" order by CLIENT_ID ");
        return sql.toString();
    }
}
