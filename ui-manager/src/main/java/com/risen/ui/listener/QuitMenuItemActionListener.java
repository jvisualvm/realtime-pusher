package com.risen.ui.listener;

import com.risen.common.util.SpringBeanUtil;
import com.risen.realtime.framework.cache.PushUserCache;
import com.risen.realtime.framework.cache.SystemObjectCache;
import com.risen.realtime.framework.cosntant.LoginConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author zhangxin
 * @version 1.0
 * @date 2022/7/23 20:08
 */
public class QuitMenuItemActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        //如果当前用户未登录，直接注销
        if (StringUtils.isEmpty(LoginConstant.CURRENT_LOGIN_USER)) {
            System.exit(0);
            return;
        }
        String password = JOptionPane.showInputDialog(null, "请输入密码");
        SystemObjectCache systemObjectCache = SpringBeanUtil.getBean(SystemObjectCache.class);
        PushUserCache pushUserCache = (PushUserCache) systemObjectCache.get(PushUserCache.class.hashCode());
        if (ObjectUtils.isEmpty(password)) {
            JOptionPane.showMessageDialog(null, "密码不能为空");
        } else {
            String passwordCurrent = pushUserCache.get(LoginConstant.CURRENT_LOGIN_USER);
            if (passwordCurrent.equals(password)) {
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(null, "密码错误");
            }
        }
    }
}
