import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < 1000; i++) {
            executorService.submit(new Thread(() -> {
                String text = generateRoute("RLRFR", 100);
                int count = 0;
                for (int j = 0; j < text.length(); j++) {
                    if (text.charAt(j) == 'R') count++;
                }
                synchronized (sizeToFreq) {
                    if (!sizeToFreq.containsKey(count)) sizeToFreq.put(count, 1);
                    else sizeToFreq.put(count, sizeToFreq.get(count) + 1);
                }

            }));
            while (true) {
                try {
                    if (!executorService.awaitTermination(1, TimeUnit.MILLISECONDS)) break;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        executorService.shutdown();
        int max = 0;
        int keyMax = 0;
        for (Integer cnt : sizeToFreq.keySet()) {
            if (sizeToFreq.get(cnt) > max) {
                max = sizeToFreq.get(cnt);
                keyMax = cnt;
            }
        }
        System.out.println("Самое частое количество повторений " + keyMax + " (встретилось " + max + " раз)");
        System.out.println("Другие размеры:");
        for (Integer cnt : sizeToFreq.keySet()) {
            if (!(sizeToFreq.get(cnt) == max))
                System.out.println("- " + cnt + " ( " + sizeToFreq.get(cnt) + " раз)");

        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}