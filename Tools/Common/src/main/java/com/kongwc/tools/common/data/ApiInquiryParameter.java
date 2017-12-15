package com.kongwc.tools.common.data;

import com.kongwc.tools.common.exception.WrongParameterException;
import com.kongwc.tools.common.util.StringUtil;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 * API 参数父类
 */
@Data
public class ApiInquiryParameter extends ApiParameter {

    private String startIndex;

    private String limit;
    private String endIndex;

    /**
     * 对limit和分页参数进行检查
     *
     * @param defaultLimit 默认检索limit件数
     */
    public void checkLimitAndPage(long defaultLimit) {

        if (!StringUtils.isEmpty(limit)) {
            if (!StringUtil.checkNumber(limit, "0", String.valueOf(defaultLimit))) {
                throw new WrongParameterException(String.format("\"limit\" must be a number between 0 and %s: [%s]", defaultLimit, limit));
            }
            // 设置默认Limit
            if ("0".equals(limit)) {
                limit = String.valueOf(defaultLimit);
            }
        } else {

            // 当startIndex、endIndex均被指定时
            if (!StringUtils.isEmpty(startIndex) && !StringUtils.isEmpty(endIndex)) {
                if (!StringUtil.checkNumber(endIndex, "0", null)) {
                    throw new WrongParameterException(String.format("\"endIndex\" must be a number more than 0: [%s]", endIndex));
                }
                if (!StringUtil.checkNumber(startIndex, "0", endIndex)) {
                    throw new WrongParameterException(String.format("\"startIndex\" must be a a number between 0 and %s: [%s]", endIndex, startIndex));
                }
                if (new BigDecimal(endIndex).subtract(new BigDecimal(startIndex)).compareTo(new BigDecimal(3000)) > 0) {
                    throw new WrongParameterException(String.format("(\"endIndex\" - \"startIndex\"） must be within %s: ", defaultLimit));
                }
                // 当startIndex、endIndex任一被指定时
            } else if (!StringUtils.isEmpty(startIndex) || !StringUtils.isEmpty(endIndex)) {
                throw new WrongParameterException("\"startIndex\" and \"endIndex\" must be set together.");

                // 当startIndex、endIndex均未被指定时
            } else {
                // 设置默认Limit
                limit = String.valueOf(defaultLimit);
            }
        }
    }
}
