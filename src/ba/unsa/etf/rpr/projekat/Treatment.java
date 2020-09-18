package ba.unsa.etf.rpr.projekat;

public class Treatment {
    private int id;
    private String treatmentName;

    public Treatment(int id, String treatmentName) {
        this.id = id;
        this.treatmentName = treatmentName;
    }

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
}
