package com.risen.ui.swing;

import com.risen.ui.listener.WebBrowerWindowListener;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.util.Set;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/24 18:08
 */
public class WebBrowser {

    public static String webPath = "file:///" + System.getProperty("user.dir") + InitSystem.changePath("\\client\\web\\manager.html");
    public static boolean LIVE_STATUS = true;
    public static int screenWidth = UiManagerUtil.WIDTH / 3 * 2; // 获取屏幕的宽
    public static int screenHeight = UiManagerUtil.HEIGHT / 3 * 2; // 获取屏幕的高
    public static final JFrame managerTab = new JFrame();


    public static JFrame createWebBrowser() {

        final JFXPanel webBrowser = new JFXPanel();
        managerTab.addWindowListener(new WebBrowerWindowListener());

        UiManagerUtil.updateMainJFrame(managerTab, screenWidth, screenHeight, 1, 1);

        initPanelView(webBrowser);

        managerTab.add(webBrowser);

        return managerTab;
    }

    public static void initPanelView(JFXPanel webBrowser) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Group root = new Group();
                Scene scene = new Scene(root, screenWidth, screenHeight);
                webBrowser.setScene(scene);

                WebView view = new WebView();
                view.setMinSize(screenWidth, screenHeight);
                view.setPrefSize(screenWidth, screenHeight);

                view.getChildrenUnmodifiable().addListener(new ListChangeListener<Node>() {
                    @Override
                    public void onChanged(Change<? extends Node> change) {
                        Set<Node> deadSeaScrolls = view.lookupAll(".scroll-bar");
                        for (Node scroll : deadSeaScrolls) {
                            scroll.setVisible(false);
                        }
                    }
                });
                final WebEngine eng = view.getEngine();
                eng.load(webPath);
                root.getChildren().add(view);
            }
        });
    }


}
