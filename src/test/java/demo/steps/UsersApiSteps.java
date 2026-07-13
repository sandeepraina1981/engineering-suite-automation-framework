package demo.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.FilePayload;
import demo.api.APIResponseWrapper;
import demo.api.ApiClient;
import demo.hooks.PlaywrightHooks;
import demo.playwright.PwSession;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pojo.LoginReq;
import pojo.OrderDetail;
import pojo.Orders;
import resources.APIResources;
import resources.OrgPaths;
import resources.PathBuilder;
import resources.PathCreator;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UsersApiSteps {

  //  private final Page page;

    private APIResponseWrapper lastResponse;
    private APIResponse response;
    private final ObjectMapper mapper = new ObjectMapper();
    ApiClient apiClient = new ApiClient();
    LoginReq loginReq = new LoginReq();
    String tokenID;
    String prodcutID;

    private String basePath = "/organizations";
    private String orgId;
    private String userId;
    private String roleName;


    public UsersApiSteps() {
       // this.page = PlaywrightHooks.page;
    }

    @Given("I fetch the users list")
    public void i_fetch_the_users_list() {
        // path only; baseURL is already set in ApiHooks
        String path = "public/v2/users";
        lastResponse = PlaywrightHooks.api().get(path);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer expectedStatus) {
        assertNotNull(lastResponse, "No response captured.");
        assertEquals(expectedStatus.intValue(), lastResponse.status(),
                "Unexpected HTTP status. Body:\n" + lastResponse.bodyAsString());
    }

    @Then("the response should be a non-empty JSON array")
    public void the_response_should_be_a_non_empty_json_array() throws Exception {
        String body = lastResponse.bodyAsString();
        JsonNode node = mapper.readTree(body);
        assertTrue(node.isArray(), "Expected JSON array, got: " + node.getNodeType());
        assertTrue(node.size() > 0, "Expected non-empty array.");
    }

    @Given("Login to ECOM API with {string} and {string}")
    public void login_to_ecom_api(String uname, String pword) throws JsonProcessingException {


//        JsonNode json = generateToken(uname, pword);
//        tokenID = json.get("token").asText();
//        userId = json.get("userId").asText();
//        assertNotNull(tokenID, "Token ID is not null");
    }

    @When("user create product with {string}")
    public void user_create_product_with_image(String createProductPath) throws JsonProcessingException {
        APIResources resources = APIResources.valueOf(createProductPath);
        String path = resources.getResource();
        String imagePath = "C:\\Users\\shindes\\Downloads\\laptop.jpg";
        // Authorization header per request (context was created before login)
        Map<String, Object> multipart = new HashMap<>();
        multipart.put("productName", System.getProperty("PRODUCT_NAME", "Laptop"));
        multipart.put("productAddedBy", ApiClient.userID);
        multipart.put("productCategory", System.getProperty("PRODUCT_CATEGORY", "Gadget"));
        multipart.put("productSubCategory", System.getProperty("PRODUCT_SUBCATEGORY", "Electronics"));
        multipart.put("productPrice", System.getProperty("PRODUCT_PRICE", "41500"));
        multipart.put("productDescription", System.getProperty("PRODUCT_DESCRIPTION", "Lenova"));
        multipart.put("productFor", System.getProperty("PRODUCT_FOR", "men"));
        FilePayload imagePayload = ApiClient.filePayload(imagePath, "image/jpeg");
        multipart.put("productImage", imagePayload);

        response =PlaywrightHooks.api().postMultipart(path, multipart);
        JsonNode json = mapper.readTree(response.text());
        prodcutID = json.get("productId").asText();
    }

    @When("product created successfully")
    public void product_created_successfully() throws JsonProcessingException {

    }

    @Then("user create order for the product with {string}")
    public void user_create_order_for_the_product(String createOrderPath) throws JsonProcessingException {
        //creating one set on data
        APIResources resources = APIResources.valueOf(createOrderPath);
        String path = resources.getResource();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCountry("India");
        orderDetail.setProductOrderedId(prodcutID);
        //add above data in list
        List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
        orderDetailList.add(orderDetail);
        //adding this list into the base class(Orders)
        Orders orders = new Orders();
        orders.setOrders(orderDetailList);
        response = PlaywrightHooks.api().postJson(path, orders);
        JsonNode json = mapper.readTree(response.text());
    }

    @Then("product should be deleted successfully with {string}")
    public void product_should_be_deleted_successfully(String deleteOrderPath) throws JsonProcessingException {
        APIResources resources = APIResources.valueOf(deleteOrderPath);
        String path = resources.getResource()+"/"+prodcutID;
        response = PlaywrightHooks.api().deleteJson(path, "");
        JsonNode json = mapper.readTree(response.text());
    }


    @When("All id's are available")
    public void all_ids_are_available() {
        orgId = "12345";
        userId = "67890";
        roleName = "admin";
    }


    @Then("All Endpoints are created with {string}")
    public void all_endpoints_are_created(String basePath) {
//        String readOrganization = PathCreator.readOrganization(basePath, orgId);
//        String listOrgUsers = PathCreator.listOrgUsers(basePath, orgId);
//        String listOrgadmin = PathCreator.listOrgadmin(basePath, orgId);
//        String readOrgsUserPermissions = PathCreator.readOrgsUserPermissions(basePath, orgId, userId);
//        String readOrgsUserRoles = PathCreator.readOrgsUserRoles(basePath, orgId, userId);
//        String grantRevokeRole = PathCreator.grantRevokeRole(basePath, orgId, userId, roleName);

        APIResources resources = APIResources.valueOf(basePath);
        String readOrg = OrgPaths.readOrganization(resources, orgId);
        String list_OrgUsers = OrgPaths.listOrgUsers(resources,orgId);
        String listOrgadmin = OrgPaths.listOrgAdmins(resources, orgId);
        String readOrgsUserPermissions = OrgPaths.readOrgsUserPermissions(resources, orgId, userId);
        String readOrgsUserRoles = OrgPaths.readOrgsUserRoles(resources, orgId, userId);
        String grantRevokeRole = OrgPaths.grantRevokeRole(resources, orgId, userId, roleName);
    }
}
