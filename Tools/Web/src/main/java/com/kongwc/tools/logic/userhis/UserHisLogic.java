package com.kongwc.tools.logic.userhis;

import com.kongwc.tools.common.data.UserHis;
import com.kongwc.tools.common.database.accessor.RDBAccessor;
import com.kongwc.tools.common.database.domain.XzUserHis;
import com.kongwc.tools.logic.AbstractLogic;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户登陆履历表相关取得逻辑类
 */
@Service
@Slf4j
public class UserHisLogic extends AbstractLogic {
    /**
     * RDB操作工具
     */
    @Autowired
    private RDBAccessor rdbAccessor;

    /**
     * {@inheritDoc}
     */
    public UserHisGetResult userHisGet(UserHisParam param) {
        // 生成SQL文
        String sql = makeInquirySqlGet(param);
        List<XzUserHis> xzUserHisList = rdbAccessor.query(sql, XzUserHis.class);
        List<UserHis> userHises = new ArrayList<>();
        for (XzUserHis xzUserHis : xzUserHisList) {
            UserHis userHis = new UserHis();
            userHises.add(userHis);

            //用户ID
            userHis.setUserId(xzUserHis.getUserId());
            //履历ID
            userHis.setHisId(xzUserHis.getHisId());
            //手持终端ID
            userHis.setClientId(xzUserHis.getClientId());
            //IP地址
            userHis.setIpAddr(xzUserHis.getIpAddr());
            //登陆时间
            if (xzUserHis.getLoginTime() != null) {
                userHis.setLoginTime(xzUserHis.getLoginTime().toString());
            }
            //登出时间
            if (xzUserHis.getLogoutTime() != null) {
                userHis.setLogoutTime(xzUserHis.getLogoutTime().toString());
            }
            //描述
            userHis.setDescription(xzUserHis.getDescription());
        }

        UserHisGetResult result = new UserHisGetResult();
        result.setUserHises(userHises);
        return result;
    }

    /**
     * 生成查询SQL
     *
     * @param param 查询参数
     * @return 查询结果
     */
    private String makeInquirySqlGet(UserHisParam param) {

        StringBuilder sql = new StringBuilder();
        String table = "XZ_USER_HIS";

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
        return sql.toString();
    }
}
