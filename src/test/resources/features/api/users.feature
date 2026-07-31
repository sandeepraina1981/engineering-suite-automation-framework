@sdcb-152 @api
Feature: User Feature
  This is to verify user context with Azure B2C login

  Scenario: Verify list of users with valid authentication
    Given Riley chooses content with flavor as 'magenta' to receive list of organizations
    When Riley receives list of users authenticated through 'autoadmin' of users 'nupano'
    Then Riley looks for the name of the users from the list of users
    | Ashley       |
    | Jan   |
