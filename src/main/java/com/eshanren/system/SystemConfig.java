package com.eshanren.system;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.wall.WallFilter;
import com.eshanren.handle.SafeHandler;
import com.eshanren.redis.RedisPlugin;
import com.eshanren.route.AdminRoute;
import com.eshanren.route.IndexRoute;
import com.jfinal.config.*;
import com.jfinal.ext.handler.FakeStaticHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

/**
 * Created by Administrator on 2019/10/12.
 */
public class SystemConfig extends JFinalConfig {

    public static void main(String[] args) {
        UndertowServer.start(SystemConfig.class);
    }


    /**
     * 是否开发中
     * @return
     */
    public static boolean isDev(){return true;}

    @Override
    public void configConstant(Constants constants) {
        loadPropertyFile("databaseConfig.properties");
        constants.setDevMode(getPropertyToBoolean("devMode",true));
        constants.setMaxPostSize(100*1024*1024);
        constants.setViewType(ViewType.JFINAL_TEMPLATE);

        constants.setEncoding("UTF-8");

        constants.setError401View("/WEB-INF/error.html");
        constants.setError403View("/WEB-INF/error.html");
        constants.setError404View("/WEB-INF/error.html");
        constants.setError500View("/WEB-INF/error.html");

    }

    @Override
    public void configRoute(Routes routes) {
        routes.add(new IndexRoute());
        routes.add(new AdminRoute());
    }

    @Override
    public void configEngine(Engine engine) {
        engine.setDevMode(getPropertyToBoolean("devMode",true));

        //常量配置

        //角色权限管理

        //扩展方法配置

        engine.getEngineConfig().setEncoding("UTF-8");

    }

    @Override
    public void configPlugin(Plugins plugins) {
        //数据库连接池插件
        PluginKit pluginKit = new PluginKit(this);
        DruidPlugin druidPlugin =  pluginKit.mysqlPlugin();

        ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
        activeRecordPlugin.setDialect(new MysqlDialect());
        activeRecordPlugin.addSqlTemplate("/sqls/all.sql");

//        _MappingKit.mapping(activeRecordPlugin);

        plugins.add(druidPlugin);
        plugins.add(activeRecordPlugin);

        plugins.add(new RedisPlugin());

    }

    @Override
    public void configInterceptor(Interceptors interceptors) {

    }

    @Override
    public void configHandler(Handlers handlers) {
        handlers.add(new SafeHandler());
        handlers.add(new FakeStaticHandler());
    }
}
