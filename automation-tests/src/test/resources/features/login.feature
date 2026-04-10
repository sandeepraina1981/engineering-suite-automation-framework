Feature: Login to Engineering Suite using Personas

  @UI @XRAY-123
  Scenario: Successful login with Alex the Admin on MSEdge
    Given Alex is on the login page
    When Alex logs in with valid credentials
    Then Alex should see the dashboard

  @UI @XRAY-124
  Scenario: Successful login with Chris the Admin on Chrome
    Given Chris is on the login page
    When Chris logs in with valid credentials
    Then Chris should see the dashboard

  @API @XRAY-125
  Scenario: Admin can access restricted API
    Given Charlie is on the login page
    When Charlie logs in with valid credentials
    # Then Charlie can call the Admin API endpoint
