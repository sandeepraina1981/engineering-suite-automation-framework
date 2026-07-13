Feature: login

@smoke
Scenario: Scenario 2 : Check practice page test
  Given user opens the login page
  When user logs in with username "student" and password "Password123"
  And user clicks on Practice link and then Test Exceptions
  Then Test Exception page should open
  And check "TC5" is visible
  And edit value in the "txtbx_Row1" textbox

@p1
Scenario: Scenario 1 : Valid login shows home page
  Given user opens the login page
  When user logs in with username "student" and password "Password123"
  Then home page is displayed
  And user log out from Application

@p1
Scenario: Scenario 3 : Valid login shows home page
  Given user opens the login page
  When user logs in with username "student" and password "Password123"
  Then home page is displayed
  And user log out from Application

#@p2
#Scenario: Scenario 4 : Check practice page test
#  Given user opens the login page
#  When user logs in with username "student" and password "Password123"
#  And user clicks on Practice link and then Test Exceptions
#  Then Test Exception page should open
#  And check "TC5" is visible
#  And edit value in the "txtbx_Row1" textbox

#@p2
#Scenario: Scenario 5 : Valid login shows home page
#  Given user opens the login page
#  When user logs in with username "student" and password "Password123"
#  Then home page is displayed
#  And user log out from Application

#@p3
#Scenario: Scenario 6 : Check practice page test
#  Given user opens the login page
#  When user logs in with username "student" and password "Password123"
#  And user clicks on Practice link and then Test Exceptions
#  Then Test Exception page should open
#  And check "TC5" is visible
#  And edit value in the "txtbx_Row1" textbox

@p3
Scenario: Scenario 7 : Check practice page test
  Given user opens the login page
  When user logs in with username "student" and password "Password123"
  And user clicks on Practice link and then Test Exceptions
  Then Test Exception page should open
  And user click on "btn_edit" and "btn_add" check if second "txtbx_row2" is visible
  And check "all_row" count is 3

