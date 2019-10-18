package com.eshanren.system;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.JFinalConfig;
import com.jfinal.kit.Prop;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * Created by Administrator on 2019/10/12.
 */
public class PluginKit {

    public PluginKit() {
    }

    public PluginKit(Prop prop) {
        this.prop = prop;
    }

    public PluginKit(JFinalConfig jFinalConfig) {
        this.jFinalConfig = jFinalConfig;
    }

    private Prop prop;

    private JFinalConfig jFinalConfig;

    //================================== 插件初始化方法 ==================================

    /**
     * mysql数据库插件
     *
     * @return
     */
    public DruidPlugin mysqlPlugin() {
        String driver = getProperty("jdbcDriverClass"), url = getProperty("jdbcUrl");
        String user = getProperty("user"), password = getProperty("password");

        //密码是否加密
        boolean isDecrypt = getPropertyToBoolean("decrypt", false);
        password = isDecrypt ? decrypt(password) : password;

        DruidPlugin druidPlugin = new DruidPlugin(url, user, password);
        druidPlugin.setDriverClass(driver);
        druidPlugin.addFilter(new StatFilter());
        WallFilter wallFilter = new WallFilter();
        wallFilter.setDbType(JdbcUtils.MYSQL);
        wallFilter.setLogViolation(true);
        WallConfig wallConfig = new WallConfig();
        wallConfig.setConditionAndAlwayFalseAllow(true);
        wallFilter.setConfig(wallConfig);
        druidPlugin.addFilter(wallFilter);

        return druidPlugin;
    }

    //================================== 取值 ==================================

    public String getProperty(String key) {
        if (jFinalConfig != null) {
            return jFinalConfig.getProperty(key);
        }
        return prop.get(key);
    }

    public Boolean getPropertyToBoolean(String key, Boolean defaultValue) {
        if (jFinalConfig != null) {
            return jFinalConfig.getPropertyToBoolean(key, defaultValue);
        }
        return prop.getBoolean(key, defaultValue);
    }

    /**
     * 解密
     *
     * @param password
     * @return
     */
    public String decrypt(String password) {
        try {
            password = ConfigTools.decrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    //================================== getter & setter ==================================

    public Prop getProp() {
        return prop;
    }

    public PluginKit setProp(Prop prop) {
        this.prop = prop;
        return this;
    }

    public JFinalConfig getjFinalConfig() {
        return jFinalConfig;
    }

    public PluginKit setjFinalConfig(JFinalConfig jFinalConfig) {
        this.jFinalConfig = jFinalConfig;
        return this;
    }
}