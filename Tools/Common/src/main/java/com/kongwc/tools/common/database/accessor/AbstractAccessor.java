package com.kongwc.tools.common.database.accessor;

import com.kongwc.tools.common.application.AppContextSupport;
import com.kongwc.tools.common.database.tools.FieldNameConverter;
import com.kongwc.tools.common.database.datasource.CommonDataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 数据库访问器父类
 */
public abstract class AbstractAccessor {

    /**
     * 字段名转换器
     */
    @Autowired
    private FieldNameConverter nameConverter;

    /**
     * 内部访问器
     */
    private AbstractDaoSupport innerAccessor;

    /**
     * 构造函数
     *
     * @param dataSource 数据源
     */
    public AbstractAccessor(CommonDataSource dataSource) {
        this.innerAccessor = createAccessor(dataSource);
    }

    /**
     * 获得内部访问器
     *
     * @return 访问器
     */
    protected AbstractDaoSupport getAccessor() {
        return innerAccessor;
    }

    /**
     * 创建访问器
     *
     * @param dataSource 数据源
     * @return 访问器
     */
    private AbstractDaoSupport createAccessor(CommonDataSource dataSource) {

        String dbType = dataSource.getDbType();
        String className = getClass().getPackage().getName().concat(".impl.").concat(StringUtils.capitalize(dbType.toLowerCase()));
        return AppContextSupport.getBean(className, true, BeanDefinition.SCOPE_PROTOTYPE, dataSource);
    }

    /**
     * 查询
     *
     * @param sql       sql文
     * @param classType 检索结果类型
     * @param args      参数
     * @param <T>       检索结果类型
     * @return 检索结果List
     */
    public <T> List<T> query(String sql, Class<T> classType, Object... args) {
        return getAccessor().query(sql, classType, args);
    }

    /**
     * 查询
     *
     * @param sql       sql文
     * @param classType 检索结果类型
     * @param args      参数
     * @param <T>       检索结果类型
     * @return 检索结果List
     */
    public <T> List<T> querySingle(String sql, Class<T> classType, Object... args) {
        return getAccessor().querySingle(sql, classType, args);
    }

    /**
     * 查询
     *
     * @param sql          sql文
     * @param requiredType 检索结果类型
     * @param args         参数
     * @param <T>          检索结果类型
     * @return 检索结果
     */
    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) {
        return getAccessor().queryForObject(sql, requiredType, args);
    }


    /**
     * 查询Blob数据
     *
     * @param sql          sql文
     * @param args         参数
     * @return 检索结果
     */
    public InputStream queryForBlob(String sql, Object... args) {
        return getAccessor().queryForBlob(sql, args);
    }

    /**
     * 查询
     *
     * @param sql  sql文
     * @param args 参数
     * @return 检索结果
     */
    public Map<String, Object> queryForMap(String sql, Object... args) {
        return getAccessor().queryForMap(sql, args);
    }

    /**
     * 查询
     *
     * @param table     表
     * @param columns   检索对象
     * @param where     检索条件
     * @param classType 检索结果类型
     * @param args      参数
     * @param <T>       检索结果类型
     * @return 检索结果
     */
    public <T> List<T> query(String table, String[] columns, String where, Class<T> classType, Object... args) {
        return getAccessor().query(table, columns, where, classType, args);
    }

    /**
     * 插入
     *
     * @param table        表
     * @param columnValues 键值对
     * @return 插入件数
     */
    public int insert(String table, Map<String, Object> columnValues) {
        return getAccessor().insert(table, columnValues);
    }

    /**
     * 插入
     *
     * @param table         表
     * @param bean          键值对
     * @return 插入件数
     */
    public <T> int insert(String table, T bean) {
        return getAccessor().insert(table, bean, nameConverter);
    }

    /**
     * 更新
     *
     * @param table        表
     * @param columnValues 键值对
     * @param where        更新条件
     * @param args         更新参数
     * @return 插入件数
     */
    public int update(String table, Map<String, Object> columnValues, String where, Object... args) {
        return getAccessor().update(table, columnValues, where, args);
    }

    /**
     * 删除
     *
     * @param table 表
     * @param where 删除条件
     * @param args  更新参数
     * @return 删除件数
     */
    public int delete(String table, String where, Object... args) {
        return getAccessor().delete(table, where, args);
    }

    /**
     * 执行
     *
     * @param sql sql文
     * @param args  参数
     */
    public void execute(String sql, Object... args) {
        getAccessor().execute(sql, args);
    }


}
