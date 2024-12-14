import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CafeManager {
    public static final int PRICE_PER_MINUTE_1 = 50;
    private Scanner scanner;
    private List<Table> tables;
    private Statistics stats;

    public CafeManager() {
        scanner = new Scanner(System.in);
        tables = new ArrayList<>();
        stats = new Statistics();

        for (int i = 1; i <= 10; i++) {
            tables.add(new Table(i));
        }
    }

    public void run() {
        while (true) {
            printMenu();
            int choice = readIntFromConsole("Ваш выбор: ");

            switch (choice) {
                case 1:
                    reserveTable();
                    break;
                case 2:
                    cancelReservation();
                    break;
                case 3:
                    releaseTable();
                    break;
                case 4:
                    displayCurrentStats();
                    break;
                case 5:
                    displayArchivedStats();
                    break;
                case 6:
                    displayAllTablesStatus();
                    break;
                case 7:
                    displayPricingInfo();
                    break;
                case 8:
                    exitProgram();
                    break;
                default:
                    System.out.println("Неправильный ввод. Попробуйте снова.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nМеню:");
        System.out.println("1. Выбрать столик");
        System.out.println("2. Освободить стол без оплаты");
        System.out.println("3. Освободить столик");
        System.out.println("4. Текущие статусы столиков");
        System.out.println("5. Архивные данные");
        System.out.println("6. Просмотреть все столы");
        System.out.println("7. Информация о ценах");
        System.out.println("8. Выход");
    }

    private void reserveTable() {
        int tableNumber = readIntFromConsole("Введите номер столика: ");
        if (tableNumber < 1 || tableNumber > 10) {
            System.out.println("Такого столика нет. Попробуйте снова.");
            return;
        }

        Table selectedTable = tables.get(tableNumber - 1);
        if (selectedTable.isOccupied()) {
            System.out.println("Данный столик уже занят. Выберите другой.");
            return;
        }

        selectedTable.occupy();
        stats.recordOccupation(selectedTable);
        System.out.println("Столик успешно занят.");
    }

    private void cancelReservation() {
        int tableNumber = readIntFromConsole("Введите номер столика: ");
        if (tableNumber < 1 || tableNumber > 10) {
            System.out.println("Такого столика нет. Попробуйте снова.");
            return;
        }

        Table selectedTable = tables.get(tableNumber - 1);
        if (!selectedTable.isOccupied()) {
            System.out.println("Данный столик уже свободен.");
            return;
        }

        selectedTable.free();
        System.out.println("Столик освобожден без оплаты.");
    }

    private void releaseTable() {
        int tableNumber = readIntFromConsole("Введите номер столика: ");
        if (tableNumber < 1 || tableNumber > 10) {
            System.out.println("Такого столика нет. Попробуйте снова.");
            return;
        }

        Table selectedTable = tables.get(tableNumber - 1);
        if (!selectedTable.isOccupied()) {
            System.out.println("Данный столик уже свободен.");
            return;
        }

        selectedTable.free();
        stats.recordEarnings(selectedTable);
        System.out.println("Столик освобожден.");
        System.out.printf("Время пребывания: %d минут.%n", selectedTable.getDuration());
        System.out.printf("К оплате: %.2f рублей.%n", selectedTable.calculateCost());
    }

    private void displayCurrentStats() {
        double sum = 0;
        System.out.println("\nТекущий статус столиков:");
        for (Table table : tables) {
            String status = table.isOccupied() ? "Занято" : "Свободно";
            System.out.printf("Столик %d: %s%n", table.getId(), status);
            if (table.isOccupied()) {
                System.out.printf("Время пребывания: %d минут.%n", table.getDuration());
                System.out.printf("Стоимость: %.2f рублей.%n", table.calculateCost());
                sum += table.calculateCost();
            }
        }
        System.out.printf("Стоимость всех столов: %.2f рублей.%n", sum);
    }

    private void displayArchivedStats() {
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("\nАрхив статистики:");
        System.out.printf("Общий доход: %s рублей.%n", df.format(stats.getTotalEarnings()));
        System.out.printf("Самый популярный столик: %d%n", stats.getMostUsedTable());
        System.out.printf("Столик, приносящий наибольший доход: %d%n", stats.getHighestEarningTable());
        System.out.println("\nСредняя занятость:");
        for (Table table : tables) {
            System.out.printf("Столик %d: %.0f минут %n", table.getId(), stats.getAverageTimeForTable(table.getId()));
        }
    }

    private void displayAllTablesStatus() {
        System.out.println("\nВсе столы:");
        for (Table table : tables) {
            System.out.printf("Столик %d: %s%n", table.getId(), table.isOccupied() ? "Занято" : "Свободно");
        }
    }

    private void displayPricingInfo() {
        System.out.println("\nИнформация о цене:");
        System.out.printf("%d рублей/минуту%n", PRICE_PER_MINUTE_1);
    }

    private void exitProgram() {
        System.out.println("Завершение работы...");
        System.exit(0);
    }

    private int readIntFromConsole(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Пожалуйста, введите целое число.");
            scanner.next();
            System.out.print(prompt);
        }
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        CafeManager cafeManager = new CafeManager();
        cafeManager.run();
    }
}
