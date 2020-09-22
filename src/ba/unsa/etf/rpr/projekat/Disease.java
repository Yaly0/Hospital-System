package ba.unsa.etf.rpr.projekat;

public class Disease {
    private int id;
    private String diseaseName;
    private MedicalMajor medicalMajor;

    public Disease(int id, String diseaseName, MedicalMajor medicalMajor) {
        this.id = id;
        this.diseaseName = diseaseName;
        this.medicalMajor = medicalMajor;
    }

    public Disease() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public MedicalMajor getMedicalMajor() {
        return medicalMajor;
    }

    public void setMedicalMajor(MedicalMajor medicalMajor) {
        this.medicalMajor = medicalMajor;
    }

    @Override
    public String toString() {
        return diseaseName;
    }
}
