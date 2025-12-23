package marc.Class;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Disease {
    private IntegerProperty diseaseID;
    private StringProperty diseaseName;

    public Disease() {
        this.diseaseID = new SimpleIntegerProperty();
        this.diseaseName = new SimpleStringProperty();
    }

    public int getDiseaseID() {
        return diseaseID.get();
    }

    public void setDiseaseID(int diseaseID) {
        this.diseaseID.set(diseaseID);
    }

    public IntegerProperty diseaseIDProperty() {
        return diseaseID;
    }

    public String getDiseaseName() {
        return diseaseName.get();
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName.set(diseaseName);
    }

    public StringProperty diseaseNameProperty() {
        return diseaseName;
    }
}
