package marc.Class;

import javafx.beans.property.*;

public class Test {
    private final IntegerProperty testId = new SimpleIntegerProperty();
    private final StringProperty testName = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();

    // Default Constructor
    public Test() {}

    // Constructor with all fields
    public Test(int testId, String testName, String description) {
        this.testId.set(testId);
        this.testName.set(testName);
        this.description.set(description);
    }

    // Constructor with testId and testName only
    public Test(int testId, String testName) {
        this.testId.set(testId);
        this.testName.set(testName);
        this.description.set(""); // Default description
    }

    // Getter and setter for testId
    public int getTestId() {
        return testId.get();
    }

    public void setTestId(int testId) {
        this.testId.set(testId);
    }

    public IntegerProperty testIdProperty() {
        return testId;
    }

    // Getter and setter for testName
    public String getTestName() {
        return testName.get();
    }

    public void setTestName(String testName) {
        this.testName.set(testName);
    }

    public StringProperty testNameProperty() {
        return testName;
    }

    // Getter and setter for description
    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
