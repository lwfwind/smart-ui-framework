package com.qa.framework.library.jndi;

import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Ldap service.
 */
public class LDAPService extends JNDIService {

    /**
     * Instantiates a new Ldap service.
     *
     * @param context the context
     */
    protected LDAPService(DirContext context) {
        super(context);
    }

    /**
     * get the all person information by User ID
     *
     * @param uesrId the uesr id
     */
    public void getPersonAllInfo(String uesrId) {
        NamingEnumeration<?> results = null;
        try {
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String filter = "(&(objectClass=person)(sAMAccountName=" + uesrId
                    + "))";
            results = dirContext.search("", filter, controls);
            while (results.hasMore()) {
                SearchResult searchResult = (SearchResult) results.next();
                Attributes attributes = searchResult.getAttributes();
                for (NamingEnumeration<?> e = (attributes.getIDs()); e
                        .hasMore(); ) {
                    Object attrID = e.next();
                    if (attrID != null) {
                        logger.info(attrID.toString() + " = "
                                + attributes.get((String) attrID).get());
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }

    }

    /**
     * get the specific person information by User ID
     *
     * @param uesrId   the uesr id
     * @param property the property
     * @return person info
     */
    public String getPersonInfo(String uesrId, String property) {
        NamingEnumeration<?> results = null;
        try {
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String filter = "(&(objectClass=person)(sAMAccountName=" + uesrId
                    + "))";
            results = dirContext.search("", filter, controls);
            while (results.hasMore()) {
                SearchResult searchResult = (SearchResult) results.next();
                Attributes attributes = searchResult.getAttributes();
                Attribute att = attributes.get(property);
                String retValue = (String) att.get();
                return retValue;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return null;
    }

    /**
     * get the direct reports, according to the specified manager
     *
     * @param managerFilter manager's DN value like:                      {@code SQA_MANAGER = "(manager=CN=Jin\\\\, Yiru,OU=Users and Groups,OU=SSTZ,OU=Asia,DC=corp,DC=statestr,DC=com)";}
     * @return direct reports by
     */
    public Map<String, String> getDirectReportsBy(String managerFilter) {
        Map<String, String> directReports = new HashMap<String, String>();
        NamingEnumeration<?> results = null;
        try {
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String filter = "(&(objectClass=person)";
            filter += managerFilter;
            filter += ")";

            results = dirContext.search("", filter, controls);
            while (results.hasMore()) {
                SearchResult searchResult = (SearchResult) results.next();
                Attributes attributes = searchResult.getAttributes();
                Attribute eid = attributes.get("sAMAccountName");
                Attribute fullName = attributes.get("cn");
                directReports.put((String) eid.get(), (String) fullName.get());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return directReports;
    }

    /**
     * get the direct reports, according to the uesrId of the specified manager
     *
     * @param uesrId the uesr id
     * @return direct reports
     */
    public Map<String, String> getDirectReports(String uesrId) {
        Map<String, String> directReports = new HashMap<String, String>();
        NamingEnumeration<?> results = null;
        String managerFilter = "(manager="
                + getPersonInfo(uesrId, "distinguishedName").replace("\\",
                "\\\\") + ")";
        try {
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String filter = "(&(objectClass=person)";
            filter += managerFilter;
            filter += ")";

            results = dirContext.search("", filter, controls);
            while (results.hasMore()) {
                SearchResult searchResult = (SearchResult) results.next();
                Attributes attributes = searchResult.getAttributes();
                Attribute eid = attributes.get("sAMAccountName");
                Attribute fullName = attributes.get("cn");
                directReports.put((String) eid.get(), (String) fullName.get());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return directReports;
    }
}
