package fi.vincit.mutrproject.service;

import static fi.vincit.multiusertest.rule.expection.Expectations.valueOf;
import static fi.vincit.multiusertest.util.UserIdentifiers.ifAnyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jayway.restassured.response.Response;

import fi.vincit.multiusertest.annotation.TestUsers;
import fi.vincit.multiusertest.util.LoginRole;
import fi.vincit.mutrproject.configuration.AbstractConfiguredRestAssuredIT;
import fi.vincit.mutrproject.domain.Role;
import fi.vincit.mutrproject.domain.TodoCommand;
import fi.vincit.mutrproject.domain.TodoItemCommand;

/**
 * Example how to use existing users
 */
@TestUsers(
        creators = {"user:admin", "role:ROLE_ADMIN", "role:ROLE_USER"},
        users = {"role:ROLE_ADMIN", "role:ROLE_USER", "user:user1", TestUsers.CREATOR}
)
public class RestAssuredIT extends AbstractConfiguredRestAssuredIT {

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserService userService;

    @Before
    public void initUsers() {
        todoService.clearList();
        userService.createUser("admin", "admin", Role.ROLE_ADMIN);
        userService.createUser("user1", "user1", Role.ROLE_USER);
    }

    @Test
    public void getTodoLists() throws Throwable {
        whenAuthenticated()
                .body(new TodoCommand("Test List")).post("/api/todo/list/private")
                .then().assertThat().statusCode(HttpStatus.SC_OK);
        whenAuthenticated()
                .body(new TodoCommand("Test List")).post("/api/todo/list/public")
                .then().assertThat().statusCode(HttpStatus.SC_OK);

        logInAs(LoginRole.USER);

        Response response = whenAuthenticated().get("/api/todo/lists");

        authorization().expect(valueOf(response::then)
                .toAssert(t -> t.statusCode(HttpStatus.SC_OK)
                                .body("", hasSize(2)),
                        ifAnyOf("role:ROLE_ADMIN", TestUsers.CREATOR))
                .toAssert(t -> t.statusCode(HttpStatus.SC_OK)
                                .body("", hasSize(1)),
                        ifAnyOf("role:ROLE_USER", "user:user1")));
    }

    @Test
    public void getPrivateTodoList() throws Throwable {
        long id = whenAuthenticated()
                .body(new TodoCommand("Test List")).post("/api/todo/list/private")
                .body().as(Long.class);

        logInAs(LoginRole.USER);

        Response response = whenAuthenticated().get("/api/todo/list/" + id);

        authorization().expect(valueOf(response::then)
                .toAssert(t -> t.statusCode(HttpStatus.SC_OK)
                                .assertThat().body("name", equalTo("Test List")),
                        ifAnyOf("role:ROLE_ADMIN", TestUsers.CREATOR))
                .toAssert(t -> t.statusCode(HttpStatus.SC_FORBIDDEN),
                        ifAnyOf("role:ROLE_USER", "user:user1")));
    }

    @Test
    public void addItemToPrivateList() throws Throwable {
        long listId = whenAuthenticated()
                .body(new TodoCommand("Test List")).post("/api/todo/list/private")
                .body().as(Long.class);

        logInAs(LoginRole.USER);

        Response response = whenAuthenticated()
                .body(new TodoItemCommand(listId, "Test List")).post("/api/todo/list/item");

        authorization().expect(valueOf(response::then)
                .toAssert(t -> t.statusCode(HttpStatus.SC_OK),
                        ifAnyOf("role:ROLE_ADMIN", TestUsers.CREATOR))
                .toAssert(t -> t.statusCode(HttpStatus.SC_FORBIDDEN),
                        ifAnyOf("role:ROLE_USER", "user:user1")));
    }

    @Test
    public void addItemToPublicList() throws Throwable {
        long listId = whenAuthenticated()
                .body(new TodoCommand("Test List")).post("/api/todo/list/private")
                .body().as(Long.class);

        logInAs(LoginRole.USER);

        Response response = whenAuthenticated()
                .body(new TodoItemCommand(listId, "Test List")).post("/api/todo/list/item");

        authorization().expect(valueOf(response::then)
                .toAssert(t -> t.statusCode(HttpStatus.SC_OK),
                        ifAnyOf("role:ROLE_ADMIN", TestUsers.CREATOR))
                .toAssert(t -> t.statusCode(HttpStatus.SC_FORBIDDEN),
                        ifAnyOf("role:ROLE_USER", "user:user1")));
    }

    @Test
    public void setPrivateItemAsDone() throws Throwable {
        long listId = whenAuthenticated()
                .body(new TodoCommand("Test List")).post("/api/todo/list/private")
                .body().as(Long.class);

        long itemId = whenAuthenticated()
                .body(new TodoItemCommand(listId, "Test List")).post("/api/todo/list/item")
                .body().as(Long.class);

        logInAs(LoginRole.USER);

        Response response = whenAuthenticated().post(String.format("/api/todo/list/%s/%s/done", listId, itemId));

        authorization().expect(valueOf(response::then)
                .toAssert(t -> t.statusCode(HttpStatus.SC_OK),
                        ifAnyOf("role:ROLE_ADMIN", TestUsers.CREATOR))
                .toAssert(t -> t.statusCode(HttpStatus.SC_FORBIDDEN),
                        ifAnyOf("role:ROLE_USER", "user:user1")));
    }

    @Test
    public void setPublicItemAsDone() throws Throwable {
        long listId = whenAuthenticated()
                .body(new TodoCommand("Test List")).post("/api/todo/list/public")
                .body().as(Long.class);

        long itemId = whenAuthenticated()
                .body(new TodoItemCommand(listId, "Test List")).post("/api/todo/list/item")
                .body().as(Long.class);

        logInAs(LoginRole.USER);

        Response response = whenAuthenticated().post(String.format("/api/todo/list/%s/%s/done", listId, itemId));

        authorization().expect(valueOf(response::then)
                .toAssert(t -> t.statusCode(HttpStatus.SC_OK),
                        ifAnyOf("role:ROLE_ADMIN", TestUsers.CREATOR))
                .toAssert(t -> t.statusCode(HttpStatus.SC_FORBIDDEN),
                        ifAnyOf("role:ROLE_USER", "user:user1")));
    }

}