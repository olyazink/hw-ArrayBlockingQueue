import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static BlockingQueue<String> queA = new ArrayBlockingQueue<>(10_000);
    public static BlockingQueue<String> queB = new ArrayBlockingQueue<>(10_000);
    public static BlockingQueue<String> queC = new ArrayBlockingQueue<>(10_000);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Расчет запущен...");
        Runnable thrAdd = () -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    queA.put(generateText("abc", 100_000));
                    queB.put(generateText("abc", 100_000));
                    queC.put(generateText("abc", 100_000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread thread = new Thread(thrAdd);
        thread.start();

        final int[] maxA = {0, 0, 0};
        final String[] strMax = {"", "", ""};

        Runnable thrA = () -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    String str = queA.take();
                    int countA = str.length() - str.replace("a", "").length();
                    if (countA > maxA[0]) {
                        maxA[0] = countA;
                        strMax[0] = str;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
            Thread threadA = new Thread(thrA);
            threadA.start();

        Runnable thrB = () -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    String str = queB.take();
                    int countB = str.length() - str.replace("b", "").length();
                    if (countB > maxA[1]) {
                        maxA[1] = countB;
                        strMax[1] = str;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread threadB = new Thread(thrB);
        threadB.start();

        Runnable thrC = () -> {
            for (int i = 0; i < 10_000; i++) {
                try {
                    String str = queC.take();
                    int countC = str.length() - str.replace("c", "").length();
                    if (countC > maxA[2]) {
                        maxA[2] = countC;
                        strMax[2] = str;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread threadC = new Thread(thrC);
        threadC.start();

        thread.join();
        threadA.join();
        threadB.join();
        threadC.join();


            System.out.println("Текст из строк с максимальным количеством a, b, c :");
            System.out.println(strMax[0]);
            System.out.println(strMax[1]);
            System.out.println(strMax[2]);

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}


