package com.kongwc.tools.logic.message;

import com.kongwc.tools.common.config.LocalConfig;
import com.kongwc.tools.common.consts.Constant;
import com.kongwc.tools.common.data.Message;
import com.kongwc.tools.common.database.accessor.RDBAccessor;
import com.kongwc.tools.common.database.domain.XzMessage;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 广播信息表相关操作逻辑类
 */
@Service
@Slf4j
public class MessageLogic extends AbstractLogic {
    /**
     * RDB操作工具
     */
    @Autowired
    private RDBAccessor rdbAccessor;

    /**
     * 本主机配置文件
     */
    @Autowired
    private LocalConfig localConfig;

    /**
     * 字段名转换器
     */
    @Autowired
    private FieldNameConverter nameConverter;

    /**
     * {@inheritDoc}
     */
    public MessageGetResult messageGet(MessageParam param) {
        // 生成SQL文
        String sql = makeInquirySqlGet(param);
        List<XzMessage> xzMessageList = rdbAccessor.query(sql, XzMessage.class);
        Long count = rdbAccessor.queryForObject("SELECT FOUND_ROWS()", Long.class);
        List<Message> messages = new ArrayList<>();
        for (XzMessage xzMessage : xzMessageList) {
            Message message = new Message();
            messages.add(message);

            //信息ID
            message.setMessageId(xzMessage.getMessageId());
            //信息
            message.setMessage(xzMessage.getMessage());
            //信息标题
            message.setMessageTitle(xzMessage.getMessageTitle());
            //状态
            message.setStatus(xzMessage.getStatus());
            //发布时间
            if (xzMessage.getPublishTime() != null) {
                message.setPublishTime(xzMessage.getPublishTime().toString());
            }

            File uploadFile = new File(localConfig.getFileStorageDir() + xzMessage.getMessageId());
            if (uploadFile.exists()) {
                File[] files = uploadFile.listFiles();
                if (files != null && files.length > 0) {
                    message.setAttachment(1);
                } else {
                    message.setAttachment(0);
                }
            } else {
                message.setAttachment(0);
            }
        }

        MessageGetResult result = new MessageGetResult();
        result.setMessages(messages);
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
    public MessageAddResult messageAdd(MessageParam param) {
        log.debug("parameter == {}", param);

        if (StringUtils.isEmpty(param.getMessageId())) {
            throw new WrongParameterException("\"messageId\" is necessary.");
        }

        MessageAddResult result = new MessageAddResult();
        // SQL参数
        List<Object> args = new ArrayList<>();
        // 生成SQL文
        String sql = makeInquirySql(param, args);
        Object[] argsArr = args.toArray(new Object[0]);
        List<XzMessage> xzMessageList = rdbAccessor.query(sql, XzMessage.class, argsArr);

        if (!xzMessageList.isEmpty()) {
            result.setReturnCode(Constant.ApiReturnCode.ERROR_PARAM_WRONG);
            result.setErrorMessage(String.format("MessageId already exist. %s", param.getMessageId()));
            return result;
        }

        XzMessage xzMessage = new XzMessage();
        //消息ID
        xzMessage.setMessageId(param.getMessageId());
        //标题
        xzMessage.setMessageTitle(param.getMessageTitle());
        //消息
        xzMessage.setMessage(param.getMessage());
        //状态
        xzMessage.setStatus(1);
        //发布时间
        if (!StringUtils.isEmpty(param.getPublishTime())) {
            xzMessage.setPublishTime(DateUtil.getDateFromDefaultFormatBySecond(param.getPublishTime()
                    .replaceAll(":", StringUtil.EMPTY_STRING)
                    .replaceAll("-", StringUtil.EMPTY_STRING)
                    .replaceAll(" ", StringUtil.EMPTY_STRING)));
        }
        setUserTimeForCreate(xzMessage);
        rdbAccessor.insert("XZ_MESSAGE", xzMessage);

        result.setMessageId(param.getMessageId());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public MessageResult messageUpdate(MessageParam param) {
        log.debug("parameter == {}", param);

        if (param.getMessageId() == null) {
            throw new WrongParameterException("\"messageId\" is necessary.");
        }

        MessageResult result = new MessageResult();

        XzMessage xzMessage = new XzMessage();
        //消息ID
        xzMessage.setMessageId(param.getMessageId());
        //标题
        xzMessage.setMessageTitle(param.getMessageTitle());
        //消息
        xzMessage.setMessage(param.getMessage());
        //发布时间
        if (!StringUtils.isEmpty(param.getPublishTime())) {
            xzMessage.setPublishTime(DateUtil.getDateFromDefaultFormatBySecond(param.getPublishTime()
                    .replaceAll(":", StringUtil.EMPTY_STRING)
                    .replaceAll("-", StringUtil.EMPTY_STRING)
                    .replaceAll(" ", StringUtil.EMPTY_STRING)));
        }

        setUserTimeForUpdate(xzMessage);

        int count = rdbAccessor.update("XZ_MESSAGE", ReflectionUtil.describeBean(xzMessage, nameConverter), " MESSAGE_ID = ? ", param.getMessageId());
        if (count == 0) {
            result.setReturnCode(Constant.ApiReturnCode.ERROR_DATA_NOT_FOUND);
            result.setErrorMessage(String.format("Data not found. %s", param.getMessageId()));
            return result;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public MessageResult messageDelete(MessageParam param) {
        log.debug("parameter == {}", param);

        if (param.getMessageId() == null) {
            throw new WrongParameterException("\"messageId\" is necessary.");
        }

        XzMessage xzMessage = new XzMessage();
        //状态
        xzMessage.setStatus(2);

        MessageResult result = new MessageResult();

        setUserTimeForUpdate(xzMessage);

        int count = rdbAccessor.update("XZ_MESSAGE", ReflectionUtil.describeBean(xzMessage, nameConverter), " MESSAGE_ID = ? ", param.getMessageId());
        if (count == 0) {
            result.setReturnCode(Constant.ApiReturnCode.ERROR_DATA_NOT_FOUND);
            result.setErrorMessage(String.format("Data not found. %s", param.getMessageId()));
            return result;
        }

        File uploadFile = new File(localConfig.getFileStorageDir() + param.getMessageId());
        if (uploadFile.exists()) {
            File[] files = uploadFile.listFiles();
            if (files != null) {
                for (File childFile : files) {
                    childFile.delete();
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public MessageResult download(MessageDownloadParam param) {

        int downloadFlg = downloadFile(param.getFileDir(), StringUtil.EMPTY_STRING, param.getParamHttpServletResponse());

        MessageResult result = new MessageResult();

        if (downloadFlg != 0) {
            result.setReturnCode(Constant.ApiReturnCode.ERROR_PARAM_WRONG);
            result.setErrorMessage(String.format("指定路径下文件不存在！<br /> %s", localConfig.getFileStorageDir() + param.getFileDir()));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public MessageResult upload(MessageParam param) {

        List<MultipartFile> files = param.getParamMultipartFiles();
        if ((files == null || files.size() == 0)) {
            throw new WrongParameterException(Constant.MessageCode.MUST_INPUT, "文件内容信息");
        }
        int uploadFlg = uploadFile(files.get(0), param.getMessageId(), true);

        MessageResult result = new MessageResult();

        if (uploadFlg != 0) {
            result.setReturnCode(Constant.ApiReturnCode.ERROR_SYSTEM);
            result.setErrorMessage("文件上传失败！请稍后再试！");
        }

        return result;
    }

    /**
     * 生成查询SQL
     *
     * @param param 查询参数
     * @return 查询结果
     */
    private String makeInquirySqlGet(MessageParam param) {

        StringBuilder sql = new StringBuilder();
        String table = "XZ_MESSAGE";

        sql.append("select SQL_CALC_FOUND_ROWS * from ");
        sql.append(table);
        String[] messageIdArray = StringUtils.split(param.getMessageId(), ',');
        if (messageIdArray != null && messageIdArray.length > 0) {
            String strMessageId = "";
            for (String messageId : messageIdArray) {
                strMessageId = strMessageId.concat(messageId).concat("','");
            }
            strMessageId = "'".concat(strMessageId.substring(0, strMessageId.length() - 2));
            param.setMessageId(strMessageId);
        }

        // ID
        if (!StringUtils.isEmpty(param.getMessageId())) {
            sql.append(" where ");
            sql.append(String.format("MESSAGE_ID in ( %s )", param.getMessageId()));
            sql.append(" and STATUS = 1");
        } else {
            sql.append(" where ");
            sql.append(" STATUS = 1");
        }

        sql.append(" order by MESSAGE_ID ");
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
    private String makeInquirySql(MessageParam param, List<Object> args) {

        StringBuilder sql = new StringBuilder();
        String table = "XZ_MESSAGE";

        sql.append("select * from ");
        sql.append(table);

        // ID
        if (!StringUtils.isEmpty(param.getMessageId())) {
            sql.append(" where ");
            sql.append("MESSAGE_ID = ? ");
            args.add(param.getMessageId());
        }
        sql.append("and STATUS = 1");
        sql.append(" order by MESSAGE_ID ");
        return sql.toString();
    }
}
