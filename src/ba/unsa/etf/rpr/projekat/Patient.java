package ba.unsa.etf.rpr.projekat;

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
        private int weight;

        public Weight(int weight) {
            if (weight < 5 || weight > 500) throw new InvalidInformationException("Invalid weight");
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            if (weight < 5 || weight > 500) throw new InvalidInformationException("Invalid weight");
            this.weight = weight;
        }

        @Override
        public String toString() {
            return weight + "kg";
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

    public Patient() {
        super();
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
        return firstName + " " + lastName;
    }
}
