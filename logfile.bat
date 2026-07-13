@echo off
echo ===========================================
echo Opening Allure Report...
echo ===========================================

REM Navigate to your project directory where allure-results exists
cd /d "C:\Santosh\Tech_data\Playwright\Playwright_Cucumber_POC"

REM Serve the report
allure serve allure-results

pause
