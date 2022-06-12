import Exceptions.InsufficientFundsException;
import Exceptions.WithdrawalLimitReachedException;

import java.util.*;

public class Main {
    public static HashMap<String, User> users = new HashMap<>();
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Predefined user in system
        users.put("OSL_FEE", new User("OSL_FEE"));
        users.put("Alice", new User("Alice"));
        users.put("Bob", new User("Bob"));

        // Command Line Menu
        while (true) {
            showPreLoginMenu();
        }
    }

    public static void showPreLoginMenu() {
        System.out.println("== BankingSystem ==");
        System.out.println("1. Account creation");
        System.out.println("2. Login");
        System.out.println("3. Exit the program");

        System.out.print("Please Enter option: ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1 -> createAccount();
            case 2 -> loginAccount();
            case 3 -> System.exit(0);
            default -> System.out.println("Invalid input. Please re-enter");
        }
    }

    public static void createAccount() {
        while (true) {
            System.out.print("Enter the new username: ");
            String username = scanner.nextLine();

            if (users.containsKey(username)) {
                System.out.println("Username already exists. Please re-enter.");
            } else {
                // Create new user with inputted username
                users.put(username, new User(username));
                System.out.println("User " + username + " created. ");
                break;
            }
        }
    }

    public static void loginAccount() {
        while (true) {
            System.out.print("Login username: ");
            String username = scanner.nextLine();

            if (users.containsKey(username)) {
                showLoginedMenu(username);
            } else {
                // Create new user with inputted username
                users.put(username, new User(username));
                System.out.println("User " + username + " created. ");
                break;
            }
        }
    }

    public static void showLoginedMenu(String username) {
        while (true) {
            System.out.println("== BankingSystem ==");
            System.out.println("Current User:" + username);
            System.out.println("1. Deposit");
            System.out.println("2. Withdrawal");
            System.out.println("3. Transfer");
            System.out.println("4. List Account Balance");
            System.out.println("5. Display transaction statement");
            System.out.println("6. Logout");

            System.out.print("Please Enter option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> transactionOperation(username, TransactionType.DEPOSIT);
                case 2 -> transactionOperation(username, TransactionType.WITHDRAWAL);
                case 3 -> transactionOperation(username, TransactionType.TRANSFER_OUT);
                case 4 -> listAccountBalance(username);
                case 5 -> displayTransactionStatement(username);
                case 6 -> showPreLoginMenu();
                default -> System.out.println("Invalid input. Please re-enter");
            }
        }
    }

    public static void transactionOperation(String username, TransactionType transactionType) {
        int selected = 0;
        Currency selectedCurrency;
        do {
            try {
                System.out.print("Please enter types of currency to " + transactionType.name().toLowerCase() + " (1 = USD, 2 = HKD, 3 = SGD): ");
                selected = scanner.nextInt();

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please re-enter.");
            }
            scanner.nextLine(); // clears buffer
        } while (selected <= 0 || selected > 3);

        switch (selected) {
            case 1 -> selectedCurrency = Currency.USD;
            case 2 -> selectedCurrency = Currency.HKD;
            case 3 -> selectedCurrency = Currency.SGD;
            default -> throw new IllegalStateException("Unexpected value: " + selected);
        }

        while (true) {
            System.out.print("Please enter amounts to " + transactionType.name().toLowerCase() + ": ");
            String amount = scanner.nextLine();

            // check if inputted string is valid number
            try {
                if (Double.parseDouble(amount) <= 0) {
                    System.out.println("Invalid input of amount. Please enter a valid number.");
                    continue;
                } else {
                    switch (transactionType) {
                        case DEPOSIT -> deposit(username, amount, selectedCurrency);
                        case WITHDRAWAL -> withdrawal(username, amount, selectedCurrency);
                        case TRANSFER_OUT -> transferOut(username, amount, selectedCurrency);
                    }
                    pressEnterKeyToContinue();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input of amount. Please enter a valid number.");
            }
            break;
        }
    }

    public static void deposit(String username, String amount, Currency selectedCurrency) {
        users.get(username).deposit(selectedCurrency, amount);
        System.out.println(selectedCurrency + " " + amount + " has been deposited.");
    }

    public static void withdrawal(String username, String amount, Currency selectedCurrency) {
        try {
            users.get(username).withdrawal(selectedCurrency, amount, users.get("OSL_FEE"));
            System.out.println(selectedCurrency + " " + amount + " has been withdraw.");
            System.out.println("1% handling fee is collected.");
        } catch (WithdrawalLimitReachedException withdrawalLimitReachedException) {
            System.out.println("Withdrawal Limit Reached.");
        } catch (InsufficientFundsException insufficientFundsException) {
            System.out.println("Insufficient Funds.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void transferOut(String username, String amount, Currency selectedCurrency) {
        System.out.print("Enter the username you want to transfer funds to: ");
        String targetUsername = scanner.nextLine();

        if (users.containsKey(targetUsername)) {
            try {
                users.get(username).transferOut(selectedCurrency, amount, users.get(targetUsername), users.get("OSL_FEE"));
                System.out.println(selectedCurrency + " " + amount + " has been transfer to " + targetUsername);
                System.out.println("1% handling fee is collected.");
            } catch (InsufficientFundsException insufficientFundsException) {
                System.out.println("Insufficient Funds.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Username does not exists.");
        }
    }

    public static void listAccountBalance(String username) {
        System.out.println("USD Balance: " + users.get(username).getBalanceByAccountCurrency(Currency.USD));
        System.out.println("HKD Balance: " + users.get(username).getBalanceByAccountCurrency(Currency.HKD));
        System.out.println("SGD Balance: " + users.get(username).getBalanceByAccountCurrency(Currency.SGD));
    }

    public static void displayTransactionStatement(String username) {
        System.out.println("Client Name: " + username);
        System.out.println();
        for (Transaction t : users.get(username).getAllTransactionList()) {
            System.out.println(t);
        }

        pressEnterKeyToContinue();
    }

    public static void pressEnterKeyToContinue() {
        System.out.println();
        System.out.print("Press Enter key to continue...");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }


}