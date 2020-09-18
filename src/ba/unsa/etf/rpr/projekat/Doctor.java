package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;
import java.time.LocalTime;

public class Doctor extends Person {  // some methods may be deleted later
    private MedicalMajor medicalMajor;
    private ShiftHours shiftHours;
    private int numberOfVisits, sumOfRatings;

    public static class ShiftHours {
        LocalTime startTime, endTime, breakStartTime, breakEndTime;

        private void ValidateShiftHours(LocalTime startTime, LocalTime endTime, LocalTime breakStartTime, LocalTime breakEndTime) {
            if (startTime.compareTo(breakStartTime) >= 0 || breakStartTime.compareTo(breakEndTime) > 0 ||
                    breakEndTime.compareTo(endTime) >= 0)
                throw new InvalidInformationException("Invalid shift hours");
        }

        public ShiftHours(LocalTime startTime, LocalTime endTime, LocalTime breakStartTime, LocalTime breakEndTime) {
            ValidateShiftHours(startTime, endTime, breakStartTime, breakEndTime);
            this.startTime = LocalTime.of(startTime.getHour(), startTime.getMinute());
            this.endTime = LocalTime.of(endTime.getHour(), endTime.getMinute());
            this.breakStartTime = LocalTime.of(breakStartTime.getHour(), breakStartTime.getMinute());
            this.breakEndTime = LocalTime.of(breakEndTime.getHour(), breakEndTime.getMinute());
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalTime startTime) {
            ValidateShiftHours(startTime, endTime, breakStartTime, breakEndTime);
            this.startTime = startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalTime endTime) {
            ValidateShiftHours(startTime, endTime, breakStartTime, breakEndTime);
            this.endTime = endTime;
        }

        public LocalTime getBreakStartTime() {
            return breakStartTime;
        }

        public void setBreakStartTime(LocalTime breakStartTime) {
            ValidateShiftHours(startTime, endTime, breakStartTime, breakEndTime);
            this.breakStartTime = breakStartTime;
        }

        public LocalTime getBreakEndTime() {
            return breakEndTime;
        }

        public void setBreakEndTime(LocalTime breakEndTime) {
            ValidateShiftHours(startTime, endTime, breakStartTime, breakEndTime);
            this.breakEndTime = breakEndTime;
        }

        @Override
        public String toString() { // 8:00-16:00 / {12:00-12:30}
            return startTime + "-" + endTime +
                    (breakStartTime.equals(breakEndTime) ? "" : " / {" + breakStartTime + "-" + breakEndTime + "}");
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

    public double getRating() {
        return Math.round(sumOfRatings * 10.0 / numberOfVisits) / 10.0;
    }

    public void addRating(Rating rating) {
        numberOfVisits++;
        sumOfRatings += rating.getRatingValue();
    }

    @Override
    public String toString() {
        return "Doctor{" + "\n" +
                " medicalMajor = " + medicalMajor + "\n" +
                " shiftHours = " + shiftHours + "\n" +
                " sumOfRatings = " + sumOfRatings + "\n" +
                " totalRating = " + getRating() + "\n" +
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
