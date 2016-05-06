package com.qa.framework.library.database;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Db pool factory.
 */
public class DBPoolFactory {

    private final static Logger logger = Logger
            .getLogger(DBPoolFactory.class);

    private static String dbJdbc = null;
    private static String dbUser = null;
    private static String dbPwd = null;
    private static int max;
    private static long wait;
    private static String driver = null;
    @SuppressWarnings("rawtypes")
    private static Class driverClass = null;
    private static ObjectPool connectionPool = null;
    private static Map<String, Boolean> poolNameMap = new HashMap<>();

    /**
     * Initial DataSource
     */
    private static synchronized void initDataSource() {
        // drive data source
        if (driverClass == null) {
            try {
                driverClass = Class.forName(driver);
            } catch (ClassNotFoundException e) {
                logger.error(e.toString());
            }
        }
    }

    /**
     * Start Pool
     *
     * @param poolname the poolname
     * @param dbJdbc   the db jdbc
     * @param dbUser   the db user
     * @param dbPwd    the db pwd
     * @param max      the max
     * @param wait     the wait
     */
    public static synchronized void startPool(String poolname, String dbJdbc,
                                              String dbUser, String dbPwd, int max, long wait) {
        // Initial DataSource
        initDataSource();

        if (connectionPool != null) {
            shutdownPool();
        }
        try {
            connectionPool = new GenericObjectPool(null, max, (byte) 1, wait);
            ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                    dbJdbc, dbUser, dbPwd);
            new PoolableConnectionFactory(connectionFactory, connectionPool,
                    null, null, false, true);
            Class.forName("org.apache.commons.dbcp.PoolingDriver");
            PoolingDriver driver = (PoolingDriver) DriverManager
                    .getDriver("jdbc:apache:commons:dbcp:");
            driver.registerPool(poolname, connectionPool);
            logger.info("Create " + poolname
                    + " for Database Connection Succees.");
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    /**
     * Release Pool
     */
    public static void shutdownPool() {
        try {
            PoolingDriver driver = (PoolingDriver) DriverManager
                    .getDriver("jdbc:apache:commons:dbcp:");
            driver.closePool("dbpool");
        } catch (SQLException e) {
            logger.error(e.toString());
        }
    }

    /**
     * Get Pool Status
     *
     * @param poolname the poolname
     * @return pool stats
     */
    public static String getPoolStats(String poolname) {

        StringBuilder stat = new StringBuilder();
        try {
            PoolingDriver driver = (PoolingDriver) DriverManager
                    .getDriver("jdbc:apache:commons:dbcp:");
            ObjectPool connectionPool = driver.getConnectionPool(poolname);

            stat.append("-- Active Connection: ");
            stat.append(connectionPool.getNumActive());
            stat.append(" ,");
            stat.append("Free Connection: ");
            stat.append(connectionPool.getNumIdle());
            stat.append(" . --");
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return stat.toString();
    }

    /**
     * Get connection in pool
     *
     * @param poolname the poolname
     * @return Connection db connection
     * @throws SQLException the sql exception
     */
    public synchronized static Connection getDbConnection(String poolname) throws SQLException {
        if (poolNameMap.get(poolname) == null) {
            init(poolname);
            startPool(poolname, dbJdbc, dbUser, dbPwd, max, wait);
            poolNameMap.put(poolname, true);
        }
        return DriverManager.getConnection("jdbc:apache:commons:dbcp:"
                + poolname);
    }

    /**
     * Initial pool
     *
     * @param poolname
     */
    private static void init(String poolname) {

        List<BaseConnBean> pools = XmlToBean.read();
        for (BaseConnBean baseConnBean : pools) {
            if (baseConnBean.getName().equals(poolname)) {
                dbJdbc = baseConnBean.getJdbcurl();
                dbUser = baseConnBean.getUsername();
                dbPwd = baseConnBean.getPassword();
                max = baseConnBean.getMax();
                wait = baseConnBean.getWait();
                driver = baseConnBean.getDriver();
            }

        }
    }

    /**
     * Close the connection in pool
     *
     * @param c the c
     */
    public static void close(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        }
    }

}
