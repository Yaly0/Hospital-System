package ba.unsa.etf.rpr.projekat;

public class Treatment {
    private int id;
    private String treatmentName;
    private MedicalMajor medicalMajor;

    public Treatment(int id, String treatmentName, MedicalMajor medicalMajor) {
        this.id = id;
        this.treatmentName = treatmentName;
        this.medicalMajor = medicalMajor;
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

    public MedicalMajor getMedicalMajor() {
        return medicalMajor;
    }

    public void setMedicalMajor(MedicalMajor medicalMajor) {
        this.medicalMajor = medicalMajor;
    }

    @Override
    public String toString() {
        return treatmentName;
    }
}
