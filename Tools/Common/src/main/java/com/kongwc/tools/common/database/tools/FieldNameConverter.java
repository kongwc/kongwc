package com.kongwc.tools.common.database.tools;

/**
 * DB字段名和Java名的相互转换
 */
public interface FieldNameConverter {

    public String toDbName(String javaField);
    public String toJavaName(String dbField);

}
