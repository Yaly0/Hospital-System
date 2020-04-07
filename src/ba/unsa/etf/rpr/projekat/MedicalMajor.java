package ba.unsa.etf.rpr.projekat;

public class MedicalMajor {
    private String medicalMajorName;
    private int id;

    public MedicalMajor(String medicalMajorName, int id) {
        this.medicalMajorName = medicalMajorName;
        this.id = id;
    }

    public String getMedicalMajorName() {
        return medicalMajorName;
    }

    public void setMedicalMajorName(String medicalMajorName) {
        this.medicalMajorName = medicalMajorName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return medicalMajorName;
    }
}
