package com.risen.ui.swing;

import javax.swing.*;
import java.awt.*;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/21 17:38
 */
public class UiManagerUtil {

    public static int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static StringBuilder SYSTEM_START_PATH = new StringBuilder();

    public static Image iconStart = null;


    static {
        SYSTEM_START_PATH.append(System.getProperty("user.dir")).append(InitSystem.changePath("\\client\\icon\\upload.png"));
        iconStart = UiManagerUtil.getImage(UiManagerUtil.SYSTEM_START_PATH.toString());
    }


    public static Image getImage(String path) {
        return Toolkit.getDefaultToolkit().getImage(path);
    }

    public static void updateMainJFrame(JFrame jFrame, int width, int height, int rows, int cols) {
        //构建容器
        jFrame.setTitle("数据采集工具V1.0");
        jFrame.setIconImage(UiManagerUtil.iconStart);
        jFrame.setLayout(new GridLayout(rows, cols));
        jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jFrame.setSize(width, height);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
    }
}
