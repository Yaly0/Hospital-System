package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;
import java.time.Period;

public abstract class Person {
    protected String firstName, lastName, email, citizenNumber, phoneNumber, address; // some may be converted to separate classes later
    protected int id;
    protected LocalDate birthDate;
    protected Gender gender;
    protected BloodType bloodType;

    public enum Gender {
        MALE {
            @Override
            public String toString() {
                return "Male";
            }
        },
        FEMALE {
            @Override
            public String toString() {
                return "Female";
            }
        }
    }

    public enum BloodType {
        O_POZITIVE("O+"),
        O_NEGATIVE("O-"),
        A_POZITIVE("A+"),
        A_NEGATIVE("A-"),
        B_POZITIVE("B+"),
        B_NEGATIVE("B-"),
        AB_POZITIVE("AB+"),
        AB_NEGATIVE("AB-");
        private final String typeName;

        BloodType(String typeName) {
            this.typeName = typeName;
        }

        @Override
        public String toString() {
            return typeName;
        }
    }

    public Person(String firstName, String lastName, String citizenNumber, String phoneNumber, int id, LocalDate birthDate, Gender gender, BloodType bloodType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.citizenNumber = citizenNumber;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.birthDate = birthDate;
        this.gender = gender;
        this.bloodType = bloodType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCitizenNumber() {
        return citizenNumber;
    }

    public void setCitizenNumber(String citizenNumber) {
        this.citizenNumber = citizenNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }
}
