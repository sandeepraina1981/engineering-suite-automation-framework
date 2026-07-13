package demo.reporting;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;


public class StepStatusLoggerPlugin implements EventListener {
    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::onScenarioStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::onStepFinished);
        publisher.registerHandlerFor(TestCaseFinished.class, this::onScenarioFinished);
    }


    private void onScenarioFinished(TestCaseFinished event) {
        TestCase testCase = event.getTestCase();
        Result result = event.getResult();
        String name = testCase.getName();
        Status status = result.getStatus();
        // Your logging/Extent call
        ExtentCucumberAdapter.addTestStepLog("🔵 Scenario finished: " + name + " → " + status);
    }


    private void onScenarioStarted(TestCaseStarted event) {
        try {
            TestCase testCase = event.getTestCase();
            String name = testCase.getName();
            String feature = testCase.getUri().getPath();  // or toString()
            // Extent example:
            ExtentCucumberAdapter.addTestStepLog("🟢 Scenario started: " + name);
        } catch (Exception e) {
            //throw new RuntimeException(e);
        }

    }

    private void onStepFinished(TestStepFinished event) {
        TestStep step = event.getTestStep();
        if (step instanceof PickleStepTestStep) {
            PickleStepTestStep ps = (PickleStepTestStep) step;
            String text = ps.getStep().getText(); //ps.getStep().getKeyword() +
            Status status = event.getResult().getStatus();
            switch (status) {
                case PASSED:
                    ExtentCucumberAdapter.addTestStepLog("✅ PASSED: " + text);
                    break;
                case FAILED:
                    ExtentCucumberAdapter.addTestStepLog("❌ FAILED: " + text);
                    break;
                case SKIPPED:
                    ExtentCucumberAdapter.addTestStepLog("⏭️ SKIPPED: " + text);
                    break;
                case PENDING:
                case UNDEFINED:
                    ExtentCucumberAdapter.addTestStepLog("🟨 " + status + ": " + text);
                    break;
                default:
                    ExtentCucumberAdapter.addTestStepLog("ℹ️ " + status + ": " + text);
            }
        }
    }
}
