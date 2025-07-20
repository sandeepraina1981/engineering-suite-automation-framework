@demo
Feature: Organizations Feature
  This is to verify organization context with Azure B2C login

  Scenario: Verify list of organizations with valid authentication
    Given Riley chooses content with flavor as 'magenta' to receive list of organizations
    When Riley receives list of organizations authenticated through 'suite-admin' of organization 'nupano'
    Then Riley looks for the name of the organizations from the list of organizations
    | nupano.com       |
    | nupano-dev.com   |

  Scenario: Verify list of organizations with invalid authentication
    Given Riley chooses content with flavor as 'magenta' to receive list of organizations
    When Riley sees access denied to receive list of organizations authenticated through 'org-admin' of organization 'nupano'