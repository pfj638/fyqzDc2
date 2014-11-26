package com.fyqz.dc.common;

import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author 施海洋
 */
public class BaseConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BaseConfiguration.class.getName());
    protected PropertiesConfiguration config;
    private boolean isOutputLog = true;


    protected BaseConfiguration(String fileName) {
        config = new PropertiesConfiguration();
        try {

            config.load(fileName);
        } catch (Exception e) {
            throw new RuntimeException("cannot load configuration file: " + fileName, e);
        }

        config.setThrowExceptionOnMissing(true);
        ConfigFileReloadingStrategy s = new ConfigFileReloadingStrategy(
                this);
        s.setRefreshDelay(60000);
        config.setReloadingStrategy(s);

        if (isOutputLog) {
            log.info("Configuration " + fileName + " is loaded from: "
                    + config.getBasePath() + System.getProperty("line.separator")
                    + this.toString());
        }

        config.getConfigurationListeners();
    }

    protected BaseConfiguration(String fileName, boolean isOutputLog) {
        this(fileName);
        this.isOutputLog = isOutputLog;
    }

    @SuppressWarnings("rawtypes")
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String seperator = System.getProperty("line.separator");
        sb.append(">>>>>>>>>>Start of displaying configuration: ").append(this.getClass().getName()).append(">>>>>>>>>>");
        sb.append(seperator);
        sb.append("Configuration loaded from: ").append(config.getBasePath());
        sb.append(seperator);
        Class k = this.getClass();
        Method[] methods = k.getMethods();

        int tab = 10;
        for (Method m : methods) {
            if (m.getName().length() > tab)
                tab = m.getName().length();
        }

        for (Method m : methods) {
            if (Modifier.isPublic(m.getModifiers()) && !Modifier.isStatic(m.getModifiers())
                    && !Modifier.isFinal(m.getModifiers())
                    && m.getName().startsWith("get")) {
                if (m.getParameterTypes().length > 0) {
                    sb.append("WARN:").append(m.getName()).append(" has paramter(s)").append(seperator);
                    continue;
                }
                StringBuilder temp = new StringBuilder();
                temp.append("\t");
                temp.append(m.getName().substring(3));
                blankFiller(temp, tab - 3);
                temp.append(" = ");
                try {
                    temp.append("").append(m.invoke(this));
                } catch (Exception e) {
                    log.error("FIXME", e);
                }
                temp.append(seperator);
                sb.append(temp.toString());
            }
        }
        sb.append("<<<<<<<<<<End of displaying configuration: ").append(this.getClass().getName()).append("<<<<<<<<<<");
        return sb.toString();
    }

    private void blankFiller(StringBuilder sb, int length) {
        while (sb.length() <= length) {
            sb.append(" ");
        }
    }

    void configurationReloaded(FileConfiguration configuration) {
        log.info("Configuration " + this.getClass().getName() + " is reloaded."
                + System.getProperty("line.separator") + this.toString());
    }

    protected boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    protected double getDouble(String key) {
        return config.getDouble(key);
    }

    protected int getInt(String key) {
        return config.getInt(key);
    }

    protected long getLong(String key) {
        return config.getLong(key);
    }

    protected short getShort(String key) {
        return config.getShort(key);
    }

    protected BigDecimal getBigDecimal(String key) {
        return config.getBigDecimal(key);
    }

    protected BigInteger getBigInteger(String key) {
        return config.getBigInteger(key);
    }

    protected String getString(String key) {
        return config.getString(key);
    }

    protected String[] getStringArray(String key) {
        return config.getStringArray(key);
    }

    protected byte getByte(String key) {
        return config.getByte(key);
    }

    protected float getFloat(String key) {
        return config.getFloat(key);
    }

}
