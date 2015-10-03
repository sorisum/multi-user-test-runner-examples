package fi.vincit.mutrproject.configuration;


import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import fi.vincit.multiusertest.annotation.MultiUserTestConfig;
import fi.vincit.multiusertest.runner.junit.MultiUserTestRunner;
import fi.vincit.multiusertest.runner.junit.framework.SpringMultiUserTestClassRunner;
import fi.vincit.multiusertest.test.AbstractUserRoleIT;
import fi.vincit.multiusertest.util.LoginRole;
import fi.vincit.mutrproject.Application;
import fi.vincit.mutrproject.config.SecurityConfig;
import fi.vincit.mutrproject.service.todo.TodoService;
import fi.vincit.mutrproject.service.user.UserService;
import fi.vincit.mutrproject.service.user.model.Role;
import fi.vincit.mutrproject.service.user.model.User;

/**
 * Example configuration that uses role aliases. Coverts role definitions
 * to roles used by the system under test. See {@link #stringToRole(String)}
 * method.
 */
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@MultiUserTestConfig(
        runner = SpringMultiUserTestClassRunner.class,
        defaultException = AccessDeniedException.class)
@ContextConfiguration(classes = {Application.class, SecurityConfig.class})
@RunWith(MultiUserTestRunner.class)
public abstract class AbstractConfiguredRoleAliasIT extends AbstractUserRoleIT<User, Role> {

    @After
    public void clear() {
        todoService.clearList();
        userService.clearUsers();
    }

    @Autowired
    private UserService userService;
    @Autowired
    private TodoService todoService;

    @Override
    protected void loginWithUser(User user) {
        userService.loginUser(user);
    }

    @Override
    protected User createUser(String username, String firstName, String lastName, Role userRole, LoginRole loginRole) {
        return userService.createUser(username, username, userRole);
    }

    @Override
    protected Role stringToRole(String role) {
        if (role.equals("REGULAR")) {
            return Role.ROLE_USER;
        } else {
            return Role.valueOf("ROLE_" + role);
        }
    }

    @Override
    protected User getUserByUsername(String username) {
        return userService.loadUserByUsername(username);
    }
}
