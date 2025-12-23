package marc.Class;

import javafx.beans.property.*;

public class LabTest extends Test {
    private final IntegerProperty labTestID;  // Primary key (Auto-increment)
    private final StringProperty createdOn;   // Created date (auto-managed in the database)
    private final StringProperty modifiedOn;  // Modified date (auto-managed in the database)

    // Constructor with all fields (LabTestID, TestID, TestName, CreatedOn, ModifiedOn)
    public LabTest(int labTestID, int testID, String testName, String createdOn, String modifiedOn) {
        super(testID, testName); // Call parent constructor to initialize testID and testName
        this.labTestID = new SimpleIntegerProperty(labTestID);
        this.createdOn = new SimpleStringProperty(createdOn);
        this.modifiedOn = new SimpleStringProperty(modifiedOn);
    }

    // Constructor with only labTestID and testID (for adding new LabTest, setting createdOn and modifiedOn to empty)
    public LabTest(int labTestID, int testID) {
        super(testID, "");  // Call parent constructor, but set testName to an empty string
        this.labTestID = new SimpleIntegerProperty(labTestID);
        this.createdOn = new SimpleStringProperty("");  // Set empty default createdOn
        this.modifiedOn = new SimpleStringProperty(""); // Set empty default modifiedOn
    }

    // Getters and Setters for properties
    public IntegerProperty labTestIDProperty() {
        return labTestID;
    }

    public StringProperty createdOnProperty() {
        return createdOn;
    }

    public StringProperty modifiedOnProperty() {
        return modifiedOn;
    }

    // Getters for raw values
    public int getLabTestID() {
        return labTestID.get();
    }

    public String getCreatedOn() {
        return createdOn.get();
    }

    public String getModifiedOn() {
        return modifiedOn.get();
    }

    // Setters for raw values
    public void setLabTestID(int labTestID) {
        this.labTestID.set(labTestID);
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn.set(createdOn);
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn.set(modifiedOn);
    }

}
