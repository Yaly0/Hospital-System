package ba.unsa.etf.rpr.projekat;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disease disease = (Disease) o;
        return id == disease.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
