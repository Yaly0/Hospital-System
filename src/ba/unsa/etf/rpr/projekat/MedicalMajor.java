package ba.unsa.etf.rpr.projekat;

import java.util.Objects;

public class MedicalMajor {
    private int id;
    private String medicalMajorName;

    public MedicalMajor() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalMajor that = (MedicalMajor) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
