@sdcb-152 @api
Feature: User List Feature
  This is to verify user context with Azure B2C login

  Scenario: Verify list of users with valid authentication
    Given Riley chooses content with flavor as 'magenta' to receive list of organizations
    When Riley receives list of users authenticated through 'autoadmin' of users 'nupano'
    And Riley confirms that the list of users is not empty
    Then Riley verifies the user list fields from the list of users
      | id        |
      | firstName |
      | lastName  |
      | email     |
