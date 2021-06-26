package com.drop.leaves.datasource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mbs on 2021-06-26 13:35
 */
@Slf4j
public class DynamicDataSourceHolder {

    private DynamicDataSourceHolder() {
    }

    private static final ThreadLocal<DbType> DB_TYPE_HOLDER = ThreadLocal.withInitial(() -> DbType.MASTER);

    public static DbType getDbType() {
        return DB_TYPE_HOLDER.get();
    }

    public static void setDBType(DbType dbType) {
        log.info("当前设置数据源为" + dbType);
        DB_TYPE_HOLDER.set(dbType);
    }
}
