package ba.unsa.etf.rpr.projekat;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class Patient extends Person { // some methods may be deleted later
    private Height height;
    private Weight weight;

    public static class Height {
        private int height;

        public Height(int height) {
            if (height < 50 || height > 250) throw new InvalidInformationException("Invalid height");
            this.height = height;
        }

        public Height(double height) {
            if (height < 0.5 || height > 2.5) throw new InvalidInformationException("Invalid height");
            this.height = (int) (100 * height);
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            if (height < 0.5 || height > 2.5) throw new InvalidInformationException("Invalid height");
            this.height = height;
        }

        @Override
        public String toString() {
            return height + "cm";
        }
    }

    public static class Weight {
        private double weight;

        public Weight(double weight) {
            if (weight < 5 || weight > 500) throw new InvalidInformationException("Invalid weight");
            this.weight = weight;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            if (weight < 5 || weight > 500) throw new InvalidInformationException("Invalid weight");
            this.weight = weight;
        }

        @Override
        public String toString() {
            return new DecimalFormat("#.#").format(weight) + "kg";
        }
    }

    public Patient(int id, String firstName, String lastName, String homeAddress, LocalDate birthDate,
                   CitizenNumber citizenNumber, PhoneNumber phoneNumber, EmailAddress emailAddress, Gender gender,
                   BloodType bloodType, Height height, Weight weight) {

        super(id, firstName, lastName, homeAddress, birthDate, citizenNumber, phoneNumber, emailAddress, gender,
                bloodType);
        this.height = height;
        this.weight = weight;
    }

    public Height getHeight() {
        return height;
    }

    public void setHeight(Height height) {
        this.height = height;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public double getBMI() {
        return Math.round(weight.weight / (height.height * height.height / 10000.0) * 10) / 10.0;
    }

    @Override
    public String toString() {
        return "Patient{" + "\n" +
                " height = " + height + "\n" +
                " weight = " + weight + "\n" +
                " BMI = " + getBMI() + "\n" +
                " id = " + id + "\n" +
                " firstName = '" + firstName + '\'' + "\n" +
                " lastName = '" + lastName + '\'' + "\n" +
                " homeAddress = '" + homeAddress + '\'' + "\n" +
                " birthDate = " + birthDate + "\n" +
                " citizenNumber = " + citizenNumber + "\n" +
                " phoneNumber = " + phoneNumber + "\n" +
                " emailAddress = " + emailAddress + "\n" +
                " gender = " + gender + "\n" +
                " bloodType = " + bloodType + "\n" +
                '}' + "\n";
    }
}
