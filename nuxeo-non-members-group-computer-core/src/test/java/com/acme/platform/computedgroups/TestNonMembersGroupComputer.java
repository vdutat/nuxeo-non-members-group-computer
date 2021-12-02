package com.acme.platform.computedgroups;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.platform.computedgroups.UserManagerWithComputedGroups;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class })
@Deploy("org.nuxeo.ecm.platform.usermanager.api")
@Deploy("org.nuxeo.ecm.platform.usermanager")
@Deploy("com.acme.platform.computedgroups.nuxeo-non-members-group-computer-core")
public class TestNonMembersGroupComputer {

    @Inject
    protected UserManager um;

    @Test
    public void testUserManager() {
        boolean isUserManagerWithComputedGroups = false;
        if (um instanceof UserManagerWithComputedGroups) {
            isUserManagerWithComputedGroups = true;
        }
        assertTrue(isUserManagerWithComputedGroups);
    }

    @Test
    public void testComputer() {
        // Create Users
        DocumentModel userModel = um.getBareUserModel();
        userModel.setProperty("user", "username", "User1");
        um.createUser(userModel);
        userModel.setProperty("user", "username", "User2");
        um.createUser(userModel);
        // Create group
        DocumentModel groupModel = um.getBareGroupModel();
        groupModel.setProperty("group", "groupname", NonMembersGroupComputer.NON_MEMBERS_GROUP);
        um.createGroup(groupModel);
        // Assign group to user
        List<String> staticGroups = new ArrayList<>();
        staticGroups.add(NonMembersGroupComputer.NON_MEMBERS_GROUP);
        userModel = um.getUserModel("User2");
        userModel.setProperty("user", "groups", staticGroups);
        um.updateUser(userModel);
        
        NuxeoPrincipal principal = (NuxeoPrincipal) um.getPrincipal("User1");
        assertNotNull(principal);
        assertTrue(principal.getGroups().contains("members"));
        
        principal = (NuxeoPrincipal) um.getPrincipal("User2");
        assertNotNull(principal);
        assertFalse(principal.getGroups().contains("members"));
        assertTrue(principal.getGroups().contains(NonMembersGroupComputer.NON_MEMBERS_GROUP));
    }

}
