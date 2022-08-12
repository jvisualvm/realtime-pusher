package com.risen.ui.swing;

import com.risen.ui.listener.QuitMenuItemActionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/21 10:57
 */
public class SystemTrayUtil {

    public static void addTray() {
        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            // 创建弹出菜单
            PopupMenu popup = new PopupMenu();
            //主界面选项
            MenuItem mainFrameItem = new MenuItem("主页");
            mainFrameItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!WebBrowser.LIVE_STATUS) {
                        WebBrowser.managerTab.setVisible(true);
                        WebBrowser.LIVE_STATUS = true;
                    }
                }
            });

            //退出程序选项
            MenuItem exitItem = new MenuItem("退出");
            exitItem.addActionListener(new QuitMenuItemActionListener());

            popup.add(mainFrameItem);
            popup.add(exitItem);
            trayIcon = new TrayIcon(UiManagerUtil.iconStart, "数采工具", popup);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        if (!WebBrowser.LIVE_STATUS) {
                            WebBrowser.managerTab.setVisible(true);
                            WebBrowser.LIVE_STATUS = true;
                        }
                    }
                }
            });
            try {
                trayIcon.setImageAutoSize(true);
                tray.add(trayIcon);
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
        }

    }


}
