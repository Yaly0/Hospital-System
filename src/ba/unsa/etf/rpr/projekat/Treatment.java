package ba.unsa.etf.rpr.projekat;

import java.util.Objects;

public class Treatment {
    private int id;
    private String treatmentName;

    public Treatment(int id, String treatmentName) {
        this.id = id;
        this.treatmentName = treatmentName;
    }

    public Treatment() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }

    @Override
    public String toString() {
        return treatmentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Treatment treatment = (Treatment) o;
        return id == treatment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
