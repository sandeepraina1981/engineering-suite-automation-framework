Feature: DemoTwo

@smoke
Scenario: Scenario 1 : Valid login shows home page
  Given user opens the login page
  When user logs in with username "student" and password "Password123"
  Then home page is displayed
  And user log out from Application

@p1
Scenario: Scenario 2 : Check practice page test
  Given user opens the login page
  When user logs in with username "student" and password "Password123"
  And user clicks on Practice link and then Test Exceptions
  Then Test Exception page should open

