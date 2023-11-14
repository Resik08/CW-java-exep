import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserInputProcessor userInputProcessor = new UserInputProcessor();

        boolean inputProcessed = false;

        while (!inputProcessed) {
            try {
                System.out.println("Введите данные в произвольном порядке (фамилия имя отчество дата рождения номер телефона пол):");
                String input = scanner.nextLine();
                userInputProcessor.processInput(input);
                System.out.println("Данные успешно обработаны и записаны в файл.");
                inputProcessed = true;
            } catch (InputFormatException | IOException e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }

        scanner.close();
    }
}

class UserInputProcessor {
    public void processInput(String input) throws InputFormatException, IOException {
        UserData userData = UserDataParser.parseInput(input);
        userData.validateAndWriteToFile();
    }
}

class UserData {
    private String lastName;
    private String firstName;
    private String middleName;
    private String birthDate;
    private String phoneNumber;
    private String gender;

    public UserData(String lastName, String firstName, String middleName, String birthDate, String phoneNumber, String gender) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public void validateAndWriteToFile() throws InputFormatException, IOException {
        if (!birthDate.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
            throw new InputFormatException("Неверный формат даты рождения. Используйте dd.mm.yyyy.");
        }

        if (!"f".equals(gender) && !"m".equals(gender)) {
            throw new InputFormatException("Используйте для указания пола f/m");
        }

        if (!phoneNumber.matches("\\d+")) {
            throw new InputFormatException("Неверный формат номера телефона. Используйте только цифры.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(lastName + ".txt", true))) {
            writer.write(lastName + " " + firstName + " " + middleName + " " + birthDate + " " + phoneNumber + " " + gender);
            writer.newLine();
        }
    }
}

class UserDataParser {
    public static UserData parseInput(String input) throws InputFormatException {
        String[] data = input.split("\\s+");

        if (data.length != 6) {
            throw new InputFormatException("Неверное количество данных. Введите фамилию, имя, отчество, дату рождения, номер телефона и пол.");
        }

        return new UserData(data[0], data[1], data[2], data[3], data[4], data[5]);
    }
}

class InputFormatException extends Exception {
    public InputFormatException(String message) {
        super(message);
    }
}
