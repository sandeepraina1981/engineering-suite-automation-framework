Feature: Api Test Feature

@api @p1
Scenario: Scenario 1: Check E-Commerce end to end flow
  When user create product with "addProductAPI"
  And product created successfully
  Then user create order for the product with "createOrderAPI"
  And product should be deleted successfully with "deleteProductAPI"

@api
Scenario: Scenario 2: Check E-Commerce end to end flow
  When user create product with "addProductAPI"
  And product created successfully
  Then user create order for the product with "createOrderAPI"
  And product should be deleted successfully with "deleteProductAPI"

@api
Scenario: Scenario 3: check API Endpoints
  When All id's are available
  Then All Endpoints are created with "organization"