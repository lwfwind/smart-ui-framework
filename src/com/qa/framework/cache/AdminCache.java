package com.qa.framework.cache;

import com.qa.framework.library.admin.AdminAccount;
import com.qa.framework.library.admin.BaseAdminBean;
import com.qa.framework.library.admin.XmlToBean;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.ArrayQueue;

import java.util.Queue;

public class AdminCache {
    protected static Logger logger = Logger.getLogger(AdminCache.class);
    private static ThreadLocal<AdminAccount> accountThreadLocal = new ThreadLocal<AdminAccount>();
    private static Queue<AdminAccount> unUsedAdmin = new ArrayQueue<>();

    static {
        initList();
    }

    public static void initList(AdminAccount adminAccount) {
        unUsedAdmin.offer(adminAccount);
    }

    public static void initList() {
        BaseAdminBean baseAdminBean = XmlToBean.getBaseAdminBean();
        for (int i = 0; i < baseAdminBean.getAdminList().size(); i++) {
            AdminAccount adminAccount = baseAdminBean.getAdminList().get(i);
            initList(adminAccount);
        }
    }

    public static AdminAccount getAdmin() {
        if (unUsedAdmin.size() == 0) {
            throw new RuntimeException("对不起, 无可用的后台账号");
        }
        AdminAccount adminAccount = accountThreadLocal.get();
        if (adminAccount != null) {
            logger.info("AdminCache use exist " + adminAccount.getName());
            return adminAccount;
        } else {
            adminAccount = unUsedAdmin.poll();
            logger.info("AdminCache use " + adminAccount.getName());
            accountThreadLocal.set(adminAccount);
            return adminAccount;
        }
    }

    public static void recoverAdmin(){
        AdminAccount adminAccount = accountThreadLocal.get();
        if (adminAccount != null) {
            logger.info("AdminCache use exist " + adminAccount.getName());
            unUsedAdmin.offer(adminAccount);
        }
    }


}
