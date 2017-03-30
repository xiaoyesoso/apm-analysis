package com.hcloud.apm.analysis.common;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by 陈志民 on 2016/11/25.
 */
public class Configuration {

    public static final Logger LOGGER = LogManager.getLogger(Configuration.class);

    public static final String CONFIG_PROPERTIES = "application.properties";

    public static final String PROJECT_JAR_ADDRESS ;

    public static final String SPARK_MASTER_HOST ;

    public static final String SPARK_MASTER_PORT ;

    public static final String HDFS_PATH;

    public static final String DB_URL;

    public static final String DB_USERNAME ;

    public static final String DB_PASSWORD ;

    public static final String DB_DRIVER ;

    public static final String HDFS_DATA_PREFIX;

    public static final String  SPARK_CORES_MAX;

    public static final String SPARK_EXECUTOR_MEMORY;

    protected Configuration() {
        throw new UnsupportedOperationException();
    }
    static {


        LOGGER.info("Reading configuration:" + CONFIG_PROPERTIES);
        InputStream in = Configuration.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES);
        Properties props = new Properties();
        try {
            props.load(in);
        } catch (IOException ex) {
            LOGGER.error("Init Config failed.", ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        SPARK_MASTER_HOST = props.getProperty("spark.master.host", "cdh03");
        SPARK_MASTER_PORT = props.getProperty("spark.master.port", "7077");
        PROJECT_JAR_ADDRESS = props.getProperty("project.jar.address", "hdfs://cdh01:8020/apm/test/ApmAnalytics.war");
        HDFS_PATH = props.getProperty("hdfs.path", "hdfs://cdh01:8020/apm/");

        DB_URL = props.getProperty("spring.datasource.url", "jdbc:mysql://localhost:3306/label_management?useUnicode=true&characterEncoding=UTF-8");
        DB_USERNAME = props.getProperty("spring.datasource.username", "root");
        DB_PASSWORD = props.getProperty("spring.datasource.password", "123456");
        DB_DRIVER = props.getProperty("spring.datasource.driverClassName", "com.mysql.jdbc.Driver");
        HDFS_DATA_PREFIX = props.getProperty("hdfs.data.Prefix", "hdfs://cdh01:8020/");


        SPARK_EXECUTOR_MEMORY = props.getProperty("spark.executor.memory", "8G");
        SPARK_CORES_MAX = props.getProperty("spark.cores.max", "4");
    }


}
