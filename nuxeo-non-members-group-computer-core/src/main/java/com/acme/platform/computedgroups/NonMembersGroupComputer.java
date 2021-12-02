package com.acme.platform.computedgroups;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.platform.computedgroups.AbstractGroupComputer;
import org.nuxeo.ecm.platform.usermanager.NuxeoPrincipalImpl;

public class NonMembersGroupComputer extends AbstractGroupComputer {

    public static final String NON_MEMBERS_GROUP = "non-members";
    
    private static final String MEMBERS_GROUP = "members";

    private static final Log log = LogFactory.getLog(NonMembersGroupComputer.class);

    @Override
    public List<String> getGroupsForUser(NuxeoPrincipalImpl nuxeoPrincipal) {
        List<String> groups = new ArrayList<String>();
        if (!nuxeoPrincipal.getGroups().contains(NON_MEMBERS_GROUP)) {
            groups.add(MEMBERS_GROUP);
        }
        if (log.isDebugEnabled()) {
            log.debug("<getGroupsForUser> returns " + groups.stream().map(Object::toString).collect(Collectors.joining(", ")));
        }
        return groups;
    }

    @Override
    public List<String> getAllGroupIds() {
        return null;
    }

    @Override
    public List<String> getGroupMembers(String groupName) {
        // Can get a performance issue here, be careful if you change
        return null;
    }

    @Override
    public List<String> getParentsGroupNames(String groupName) {
        return null;
    }

    @Override
    public List<String> getSubGroupsNames(String groupName) {
        return null;
    }

}
