package com.kongwc.tools.logic.user;

import com.kongwc.tools.common.consts.Constant;
import com.kongwc.tools.common.data.User;
import com.kongwc.tools.common.database.accessor.RDBAccessor;
import com.kongwc.tools.common.database.domain.XzUser;
import com.kongwc.tools.common.database.tools.FieldNameConverter;
import com.kongwc.tools.common.exception.WrongParameterException;
import com.kongwc.tools.common.util.ReflectionUtil;
import com.kongwc.tools.logic.AbstractLogic;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息表相关取得逻辑类
 */
@Service
@Slf4j
public class UserLogic extends AbstractLogic {

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
    public UserAddResult userAdd(UserParam param) {
        log.debug("parameter == {}", param);

        if (StringUtils.isEmpty(param.getUserId())) {
            throw new WrongParameterException("\"userId\" is necessary.");
        }

        UserAddResult result = new UserAddResult();
        // SQL参数
        List<Object> args = new ArrayList<>();
        // 生成SQL文
        String sql = makeInquirySql(param, args);
        Object[] argsArr = args.toArray(new Object[0]);
        List<XzUser> xzUserList = rdbAccessor.query(sql, XzUser.class, argsArr);

        if (!xzUserList.isEmpty()) {
            result.setReturnCode(Constant.ApiReturnCode.ERROR_PARAM_WRONG);
            result.setErrorMessage(String.format("UserId already exist. %s", param.getUserId()));
            return result;
        }

        XzUser xzUser = new XzUser();
        //用户ID
        xzUser.setUserId(param.getUserId());
        //用户名
        xzUser.setUserName(param.getUserName());
        //用户密码
        xzUser.setPassword(param.getPass());
        //手持终端ID
        xzUser.setClientId(param.getClientId());
        //状态
        xzUser.setStatus(param.getStatus());
        //描述
        xzUser.setDescription(param.getDescription());
        setUserTimeForCreate(xzUser);
        rdbAccessor.insert("XZ_USER", xzUser);

        result.setUserId(param.getUserId());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public UserGetResult userGet(UserParam param) {
        // 生成SQL文
        String sql = makeInquirySqlGet(param);
        List<XzUser> xzUserList = rdbAccessor.query(sql, XzUser.class);
        Long count = rdbAccessor.queryForObject("SELECT FOUND_ROWS()", Long.class);
        List<User> users = new ArrayList<>();
        for (XzUser xzUser : xzUserList) {
            User user = new User();
            users.add(user);

            //用户ID
            user.setUserId(xzUser.getUserId());
            //用户名
            user.setUserName(xzUser.getUserName());
            //用户密码
            user.setPass(xzUser.getPassword());
            //手持终端ID
            user.setClientId(xzUser.getClientId());
            //状态
            user.setStatus(xzUser.getStatus());
            //最后登录时间
            if (xzUser.getLastLoginTime() != null) {
                user.setLastLoginTime(xzUser.getLastLoginTime().toString());
            }
            //描述
            user.setDescription(xzUser.getDescription());
        }

        UserGetResult result = new UserGetResult();
        result.setUsers(users);
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
    public UserUpdateResult userUpdate(UserParam param) {
        log.debug("parameter == {}", param);

        if (param.getUserId() == null) {
            throw new WrongParameterException("\"userId\" is necessary.");
        }

        UserUpdateResult result = new UserUpdateResult();

        XzUser xzUser = new XzUser();
        //用户ID
        xzUser.setUserId(param.getUserId());
        //用户名
        xzUser.setUserName(param.getUserName());
        //用户密码
        xzUser.setPassword(param.getPass());
        //手持终端ID
        xzUser.setClientId(param.getClientId());
        //状态
        xzUser.setStatus(param.getStatus());
        //描述
        xzUser.setDescription(param.getDescription());

        setUserTimeForUpdate(xzUser);

        int count = rdbAccessor.update("XZ_USER", ReflectionUtil.describeBean(xzUser, nameConverter), " USER_ID = ? ", param.getUserId());
        if (count == 0) {
            result.setReturnCode(Constant.ApiReturnCode.ERROR_DATA_NOT_FOUND);
            result.setErrorMessage(String.format("Data not found. %s", param.getUserId()));
            return result;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public UserDeleteResult userDelete(UserParam param) {
        UserDeleteResult result = new UserDeleteResult();
        String userId = param.getUserId();
        int count = rdbAccessor.delete("XZ_USER", " USER_ID = ? ", userId);
        if (count == 0) {
            result.setReturnCode(Constant.ApiReturnCode.ERROR_PARAM_WRONG);
            result.setErrorMessage(String.format("Error param wrong. %s", userId));
            return result;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public UserBatchResult batch(UserBatchParam param) {
        log.debug("parameter == {}", param);

        List<User> users = param.getUsers();
        if (users == null || users.size() == 0) {
            throw new WrongParameterException("\"users\" is necessary.");
        }

        UserBatchResult result = new UserBatchResult();

        List<String> userIds = new ArrayList<>();
        for (User user : param.getUsers()) {
            if (user.getBatchAction() == null) {
                throw new WrongParameterException(Constant.MessageCode.MUST_INPUT, "更新动作");
            }

            if (user.getBatchAction() == 1) {
                UserParam getParam = new UserParam();
                getParam.setUserId(user.getUserId());
                UserGetResult getResult = userGet(getParam);
                if (getResult != null && getResult.getUsers() != null && getResult.getUsers().size() > 0) {

                    UserParam updateParam = new UserParam();
                    ReflectionUtil.copyFields(user, updateParam);
                    UserUpdateResult resultUpdate = userUpdate(updateParam);
                    if (resultUpdate.getReturnCode() != 0) {
                        result.setReturnCode(resultUpdate.getReturnCode());
                        result.setErrorMessage(resultUpdate.getErrorMessage());
                        return result;
                    }
                } else if (getResult != null && getResult.getUsers() != null && getResult.getUsers().size() == 0) {

                    UserParam addParam = new UserParam();
                    ReflectionUtil.copyFields(user, addParam);
                    UserAddResult resultAdd = userAdd(addParam);
                    if (resultAdd.getReturnCode() != 0) {
                        result.setReturnCode(resultAdd.getReturnCode());
                        result.setErrorMessage(resultAdd.getErrorMessage());
                        return result;
                    }
                }
            } else if (user.getBatchAction() == 2) {

                UserParam deleteParam = new UserParam();
                deleteParam.setUserId(user.getUserId());
                UserDeleteResult resultDelete = userDelete(deleteParam);
                if (resultDelete.getReturnCode() != 0) {
                    result.setReturnCode(resultDelete.getReturnCode());
                    result.setErrorMessage(resultDelete.getErrorMessage());
                    return result;
                }
            }

            userIds.add(user.getUserId());
        }

        result.setUserIds(userIds);
        return result;
    }

    /**
     * 生成查询SQL
     *
     * @param param 查询参数
     * @param args  SQL参数
     * @return 查询结果
     */
    private String makeInquirySql(UserParam param, List<Object> args) {

        StringBuilder sql = new StringBuilder();
        String table = "XZ_USER";

        sql.append("select * from ");
        sql.append(table);

        // ID
        if (!StringUtils.isEmpty(param.getUserId())) {
            sql.append(" where ");
            sql.append("USER_ID = ? ");
            args.add(param.getUserId());
        }
        sql.append(" order by USER_ID ");
        return sql.toString();
    }

    /**
     * 生成查询SQL
     *
     * @param param 查询参数
     * @return 查询结果
     */
    private String makeInquirySqlGet(UserParam param) {

        StringBuilder sql = new StringBuilder();
        String table = "XZ_USER";

        sql.append("select SQL_CALC_FOUND_ROWS * from ");
        sql.append(table);
        String[] userIdArray = StringUtils.split(param.getUserId(), ',');
        if (userIdArray != null && userIdArray.length > 0) {
            String strUserId = "";
            for (String userId : userIdArray) {
                strUserId = strUserId.concat(userId).concat("','");
            }
            strUserId = "'".concat(strUserId.substring(0, strUserId.length() - 2));
            param.setUserId(strUserId);
        }

        // ID
        if (!StringUtils.isEmpty(param.getUserId())) {
            sql.append(" where ");
            sql.append(String.format("USER_ID in ( %s )", param.getUserId()));
        }

        sql.append(" order by USER_ID ");
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
}
