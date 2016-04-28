package com.qa.framework.cache;

import com.qa.framework.library.admin.AdminAccount;
import com.qa.framework.library.admin.BaseAdminBean;
import com.qa.framework.library.admin.XmlToBean;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AdminCache {
    protected static Logger logger = Logger.getLogger(AdminCache.class);
    private static ThreadLocal<AdminAccount> accountThreadLocal = new ThreadLocal<AdminAccount>();
    private static List<AdminAccount> currentUsingAdmin = new ArrayList<>();
    private static List<AdminAccount> allAdmin = new ArrayList<>();

    static {
        initList();
    }

    public static void initList(AdminAccount adminAccount) {
        allAdmin.add(adminAccount);
    }

    public static void initList() {
        BaseAdminBean baseAdminBean = XmlToBean.getBaseAdminBean();
        for (int i = 0; i < baseAdminBean.getAdminList().size(); i++) {
            AdminAccount adminAccount = baseAdminBean.getAdminList().get(i);
            initList(adminAccount);
        }
    }

    private static AdminAccount getUnUsedAdmin() {
        for (AdminAccount adminAccount : allAdmin) {
            if (!currentUsingAdmin.contains(adminAccount)) {
                return adminAccount;
            }
        }
        throw new RuntimeException("对不起, 无可用的后台账号");
    }

    public static AdminAccount getAdmin() {
        if (currentUsingAdmin.size() == allAdmin.size()) {
            throw new RuntimeException("对不起, 无可用的后台账号");
        }
        AdminAccount adminAccount = accountThreadLocal.get();
        if (adminAccount != null) {
            logger.info("AdminCache use exist " + adminAccount.getName());
            return adminAccount;
        } else {
            adminAccount = getUnUsedAdmin();
            logger.info("AdminCache use " + adminAccount.getName());
            accountThreadLocal.set(adminAccount);
            currentUsingAdmin.add(adminAccount);
            return adminAccount;
        }
    }

    public static void recoverAdmin() {
        AdminAccount adminAccount = accountThreadLocal.get();
        if (adminAccount != null) {
            logger.info("AdminCache recover account " + adminAccount.getName());
            accountThreadLocal.set(null);
            currentUsingAdmin.remove(adminAccount);
        }
    }


}
