package com.qa.framework.library.database;

import com.qa.framework.config.PropConfig;
import com.qa.framework.library.base.CollectionHelper;
import com.qa.framework.library.base.StringHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The type Db DBHelper.
 */
public class DBHelper {

    private final static Logger logger = Logger.getLogger(DBHelper.class);

    private static final ThreadLocal<Connection> connThreadLocal = new ThreadLocal<Connection>();
    private static final QueryRunner queryRunner = new QueryRunner();
    private static String poolName;

    static {
        if (!(PropConfig.getCoreType().equalsIgnoreCase("ANDROIDAPP") || PropConfig.getCoreType().equalsIgnoreCase("IOSAPP"))) {
            String dbPoolName = PropConfig.getDbPoolName();
            if(dbPoolName == null) {
                String webPath = PropConfig.getWebPath();
                if (StringHelper.startsWithIgnoreCase(webPath, "http://")) {
                    if (webPath.contains("/")) {
                        poolName = StringHelper.getTokensList(webPath.substring(7), "/").get(0);
                    } else {
                        poolName = webPath.substring(7);
                    }
                }
            }
            else
            {
                poolName = dbPoolName;
            }
        } else {
            poolName = PropConfig.getDbPoolName();
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection(String poolname) {
        Connection conn = connThreadLocal.get();
        if (conn == null) {
            try {
                conn = DBPoolFactory.getDbConnection(poolname);
            } catch (SQLException e) {
                logger.error("get connection failure", e);
                throw new RuntimeException(e);
            } finally {
                connThreadLocal.set(conn);
            }
        }
        return conn;
    }

    public static Connection getConnection() {
        return getConnection(poolName);
    }

    /**
     * 执行查询语句
     */
    public static List<Map<String, Object>> queryRows(String sql, Object... params) {
        List<Map<String, Object>> result;
        try {
            Connection conn = getConnection();
            result = queryRunner.query(conn, sql, new MapListHandler(), params);
        } catch (Exception e) {
            logger.error("execute query failure", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public static Map<String, Object> queryOneRow(String sql, Object... params) {
        List<Map<String, Object>> records = queryRows(sql, params);
        if (records == null || records.size() == 0) {
            return null;
        }
        Random random = new Random();
        return records.get(random.nextInt(records.size()));
    }

    public static String queryField(String sql, String columnName, Object... params) {
        Map<String, Object> recordInfo = queryOneRow(sql, params);
        if (recordInfo != null) {
            return recordInfo.get(columnName).toString();
        }
        return null;
    }

    public static boolean hasRecord(String sql, Object... params) {
        List<Map<String, Object>> records = queryRows(sql, params);
        return records.size() > 0;
    }

    /**
     * 执行更新语句（包括：update、insert、delete）
     */
    public static int executeUpdate(String sql, Object... params) {
        int rows = 0;
        try {
            Connection conn = getConnection();
            rows = queryRunner.update(conn, sql, params);
        } catch (SQLException e) {
            logger.error("execute update failure", e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    /**
     * 查询实体列表
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = queryRunner.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            logger.error("query entity list failure", e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    /**
     * 查询实体
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity;
        try {
            Connection conn = getConnection();
            entity = queryRunner.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            logger.error("query entity failure", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * 插入实体
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (CollectionHelper.isEmpty(fieldMap)) {
            logger.error("can not insert entity: fieldMap is empty");
            return false;
        }

        String sql = "INSERT INTO " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += columns + " VALUES " + values;

        Object[] params = fieldMap.values().toArray();

        return executeUpdate(sql, params) == 1;
    }

    /**
     * 更新实体
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (CollectionHelper.isEmpty(fieldMap)) {
            logger.error("can not update entity: fieldMap is empty");
            return false;
        }

        String sql = "UPDATE " + getTableName(entityClass) + " SET ";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(" = ?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id = ?";

        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();

        return executeUpdate(sql, params) == 1;
    }

    /**
     * 删除实体
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
        String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id = ?";
        return executeUpdate(sql, id) == 1;
    }

    /**
     * 执行 SQL 文件
     */
    public static void executeSqlFile(String filePath) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String sql;
            while ((sql = reader.readLine()) != null) {
                executeUpdate(sql);
            }
        } catch (Exception e) {
            logger.error("execute sql file failure", e);
            throw new RuntimeException(e);
        }
    }

    private static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }

}
