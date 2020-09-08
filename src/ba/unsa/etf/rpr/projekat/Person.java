package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;

import static ba.unsa.etf.rpr.projekat.Person.Gender.*;

import org.apache.commons.validator.routines.EmailValidator;

public abstract class Person { // some methods may be deleted later
    protected int id;
    protected String firstName, lastName, homeAddress;
    protected LocalDate birthDate;
    protected CitizenNumber citizenNumber;
    protected PhoneNumber phoneNumber;
    protected EmailAddress emailAddress;
    protected Gender gender;
    protected BloodType bloodType;

    public static class CitizenNumber {
        private String citizenNumber;

        public CitizenNumber(String citizenNumber) {
            if (citizenNumber.length() != 13 || !citizenNumber.matches("[0-9]+"))
                throw new InvalidInformationException("Invalid citizen number format");
            this.citizenNumber = citizenNumber;
        }

        public String getCitizenNumber() {
            return citizenNumber;
        }

        @Override
        public String toString() {
            return citizenNumber;
        }
    }

    public static class PhoneNumber {
        private String phoneNumber;

        public PhoneNumber(String phoneNumber) {
            setPhoneNumber(phoneNumber);
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) { // e.g. 0601234567
            if ((phoneNumber.length() != 9 && phoneNumber.length() != 10) || !phoneNumber.matches("[0-9]+"))
                throw new InvalidInformationException("Invalid phone number format");
            int destinationCode = Integer.parseInt(phoneNumber.substring(1, 3));
            Integer[] destinationCodes = {30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 49, 50, 51, 52, 53, 54, 55, 56, 57,
                    58, 59, 60, 61, 62, 63, 64, 65, 66, 67};
            if (!Arrays.asList(destinationCodes).contains(destinationCode))
                throw new InvalidInformationException("Invalid phone number format");
            this.phoneNumber = phoneNumber;
        }

        @Override
        public String toString() { // e.g. +387 60 123 4567
            return "+387 " + phoneNumber.substring(1, 3) + " " + phoneNumber.substring(3, 6) + " " +
                    phoneNumber.substring(6);
        }
    }

    public static class EmailAddress {
        private String emailAddress;

        public EmailAddress(String emailAddress) {
            setEmailAddress(emailAddress);
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            if (!EmailValidator.getInstance().isValid(emailAddress))
                throw new InvalidInformationException("Invalid email address");
            this.emailAddress = emailAddress;
        }

        @Override
        public String toString() {
            return emailAddress;
        }
    }

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

    private boolean isCitizenNumberInvalid(CitizenNumber citizenNumber, LocalDate birthDate, Gender gender) {
        String genderFromCitizenNumber = citizenNumber.citizenNumber.substring(7, 10);
        String birthDateFromCitizenNumber = citizenNumber.citizenNumber.substring(0, 7); // e.g. 3112999 -> 1999-12-31

        birthDateFromCitizenNumber = "X" + birthDateFromCitizenNumber.substring(4, 7) + "-" +
                birthDateFromCitizenNumber.substring(2, 4) + "-" + birthDateFromCitizenNumber.substring(0, 2);
        if (birthDateFromCitizenNumber.charAt(1) == '9')
            birthDateFromCitizenNumber = 1 + birthDateFromCitizenNumber.substring(1);
        else if (birthDateFromCitizenNumber.charAt(1) == '0')
            birthDateFromCitizenNumber = 2 + birthDateFromCitizenNumber.substring(1);

        return !birthDateFromCitizenNumber.equals(birthDate.toString()) ||
                ((Integer.parseInt(genderFromCitizenNumber) >= 500 || gender != MALE) &&
                        (Integer.parseInt(genderFromCitizenNumber) < 500 || gender != FEMALE));
    }

    public Person(int id, String firstName, String lastName, String homeAddress, LocalDate birthDate,
                  CitizenNumber citizenNumber, PhoneNumber phoneNumber, EmailAddress emailAddress, Gender gender,
                  BloodType bloodType) {
        if (isCitizenNumberInvalid(citizenNumber, birthDate, gender))
            throw new InvalidInformationException("Invalid citizen number, birth date or gender");

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.homeAddress = homeAddress;
        this.birthDate = birthDate;
        this.citizenNumber = citizenNumber;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.gender = gender;
        this.bloodType = bloodType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;

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

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public CitizenNumber getCitizenNumber() {
        return citizenNumber;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Gender getGender() {
        return gender;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public void setBirthDateCitizenNumberAndGender(LocalDate birthDate, CitizenNumber citizenNumber, Gender gender) {
        // These three attributes are always connected, thats why this method is needed
        if (isCitizenNumberInvalid(citizenNumber, birthDate, gender))
            throw new InvalidInformationException("Invalid citizen number");
        this.birthDate = birthDate;
        this.citizenNumber = citizenNumber;
        this.gender = gender;
    }
}
