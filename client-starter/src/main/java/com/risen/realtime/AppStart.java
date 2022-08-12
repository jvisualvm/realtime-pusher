package com.risen.realtime;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.risen.realtime.load.LoadJarUtil;
import com.risen.ui.swing.InitSystem;
import com.risen.ui.swing.SystemTrayUtil;
import com.risen.ui.swing.WebBrowser;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.risen.*"})
@MapperScan(value = {"com.risen.*"})
@EnableKnife4j
@EnableSwagger2
@EnableTransactionManagement
@ComponentScan({"com.risen.*"})
public class AppStart {

    public static void main(String[] args) {
        LoadJarUtil.load(LoadJarUtil.DEV_LIB_PATH, LoadJarUtil.PROD_LIB_PATH, LoadJarUtil.DEV_CORE_LIB_PATH, LoadJarUtil.PROD_CORE_LIB_PATH);
        boolean canRun = InitSystem.initSystem();
        if (canRun) {
            WebBrowser.createWebBrowser();
            SystemTrayUtil.addTray();
            SpringApplication.run(AppStart.class, args);
        }
    }

}

