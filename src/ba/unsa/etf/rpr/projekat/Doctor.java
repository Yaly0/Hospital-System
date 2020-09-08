package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;
import java.time.LocalTime;

public class Doctor extends Person {  // some methods may be deleted later
    private MedicalMajor medicalMajor;
    private ShiftHours shiftHours;
    private double sumOfRatings, totalRating;
    private int numberOfVisits;

    public static class ShiftHours {
        LocalTime startTime, endTime, breakStartTime, breakEndTime;

        private void throwException() {
            throw new InvalidInformationException("Invalid shift hours");
        }

        public ShiftHours(LocalTime startTime, LocalTime endTime, LocalTime breakStartTime, LocalTime breakEndTime) {

            if (startTime.compareTo(breakStartTime) >= 0 || breakStartTime.compareTo(breakEndTime) > 0 ||
                    breakEndTime.compareTo(endTime) >= 0)
                throwException();
            this.startTime = LocalTime.of(startTime.getHour(), startTime.getMinute());
            this.endTime = LocalTime.of(endTime.getHour(), endTime.getMinute());
            this.breakStartTime = LocalTime.of(breakStartTime.getHour(), breakStartTime.getMinute());
            this.breakEndTime = LocalTime.of(breakEndTime.getHour(), breakEndTime.getMinute());
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalTime startTime) {
            if (startTime.compareTo(breakStartTime) >= 0) throwException();
            this.startTime = startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalTime endTime) {
            if (breakEndTime.compareTo(endTime) >= 0) throwException();
            this.endTime = endTime;
        }

        public LocalTime getBreakStartTime() {
            return breakStartTime;
        }

        public void setBreakStartTime(LocalTime breakStartTime) {
            if (startTime.compareTo(breakStartTime) >= 0 || breakStartTime.compareTo(breakEndTime) > 0)
                throwException();
            this.breakStartTime = breakStartTime;
        }

        public LocalTime getBreakEndTime() {
            return breakEndTime;
        }

        public void setBreakEndTime(LocalTime breakEndTime) {
            if (breakStartTime.compareTo(breakEndTime) > 0 || breakEndTime.compareTo(endTime) >= 0) throwException();
            this.breakEndTime = breakEndTime;
        }

        @Override
        public String toString() { // 8:00-16:00 / {12:00-12:30}
            return startTime + "-" + endTime +
                    (breakStartTime.equals(breakEndTime) ? "" : " / {" + breakStartTime + "-" + breakEndTime + "}");
        }
    }

    public enum Rating {
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5);

        private final int ratingValue;

        Rating(int ratingValue) {
            this.ratingValue = ratingValue;
        }

        @Override
        public String toString() {
            return String.valueOf(ratingValue);
        }
    }

    public Doctor(int id, String firstName, String lastName, String homeAddress, LocalDate birthDate,
                  CitizenNumber citizenNumber, PhoneNumber phoneNumber, EmailAddress emailAddress, Gender gender,
                  BloodType bloodType, MedicalMajor medicalMajor, ShiftHours shiftHours) {
        super(id, firstName, lastName, homeAddress, birthDate, citizenNumber, phoneNumber, emailAddress, gender,
                bloodType);
        this.medicalMajor = medicalMajor;
        this.shiftHours = shiftHours;
        this.sumOfRatings = 0;
        this.numberOfVisits = 0;
    }

    public MedicalMajor getMedicalMajor() {
        return medicalMajor;
    }

    public void setMedicalMajor(MedicalMajor medicalMajor) {
        this.medicalMajor = medicalMajor;
    }

    public ShiftHours getShiftHours() {
        return shiftHours;
    }

    public void setShiftHours(ShiftHours shiftHours) {
        this.shiftHours = shiftHours;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public void addRating(Rating rating) {
        numberOfVisits++;
        sumOfRatings += rating.ratingValue;
        this.totalRating = Math.round(sumOfRatings / numberOfVisits * 10) / 10.0;
    }

    @Override
    public String toString() {
        return "Doctor{" + "\n" +
                " medicalMajor = " + medicalMajor + "\n" +
                " shiftHours = " + shiftHours + "\n" +
                " sumOfRatings = " + sumOfRatings + "\n" +
                " totalRating = " + totalRating + "\n" +
                " numberOfVisits = " + numberOfVisits + "\n" +
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
