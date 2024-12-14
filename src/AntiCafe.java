
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

class Table {
    private int id;
    private boolean occupied;
    private Instant startTime;

    public Table(int id) {
        this.id = id;
        this.occupied = false;
    }

    public int getId() {
        return id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void occupy() {
        this.occupied = true;
        this.startTime = Instant.now();
    }

    public void free() {
        this.occupied = false;
    }

    public int getDuration() {
        return (int) Duration.between(startTime, Instant.now()).toMinutes();
    }

    public double calculateCost() {
        Duration elapsedTime = Duration.between(startTime, Instant.now());
        long elapsedMinutes = elapsedTime.toMinutes();
        double cost = 0;

        cost = elapsedMinutes * CafeManager.PRICE_PER_MINUTE_1;

        return cost;
    }
}

class Statistics {
    private double totalEarnings;
    private Map<Integer, Integer> tableUsageCounts;
    private Map<Integer, Double> averageTimes;
    private Map<Integer, Double> earningsByTable;

    public Statistics() {
        this.totalEarnings = 0;
        this.tableUsageCounts = new HashMap<>();
        this.averageTimes = new HashMap<>();
        this.earningsByTable = new HashMap<>();
    }

    public void recordOccupation(Table table) {
        int id = table.getId();
        tableUsageCounts.put(id, tableUsageCounts.getOrDefault(id, 0) + 1);
        averageTimes.put(id, (double) table.getDuration());
    }

    public void recordEarnings(Table table) {
        int id = table.getId();
        double cost = table.calculateCost();
        earningsByTable.put(id, earningsByTable.getOrDefault(id, 0.0) + cost);
        totalEarnings += cost;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public double getAverageTimeForTable(int id) {
        return averageTimes.getOrDefault(id, 0.0);
    }

    public int getMostUsedTable() {
        try {
            return tableUsageCounts.entrySet().stream()
                    .max(Map.Entry.comparingByValue()).orElseThrow().getKey();
        } catch (NoSuchElementException e) {
            System.out.println("Статистика отсутствует");
            return 0;
        }
    }

    public int getHighestEarningTable() {
        try {
            return earningsByTable.entrySet().stream()
                    .max(Map.Entry.comparingByValue()).orElseThrow().getKey();
        } catch (NoSuchElementException e) {
            System.out.println("Статистика отсутствует");
            return 0;
        }
    }
}

