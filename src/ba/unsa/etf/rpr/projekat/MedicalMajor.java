package ba.unsa.etf.rpr.projekat;

public class MedicalMajor {
    private int id;
    private String medicalMajorName;

    public MedicalMajor(int id, String medicalMajorName) {
        this.id = id;
        this.medicalMajorName = medicalMajorName;
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
