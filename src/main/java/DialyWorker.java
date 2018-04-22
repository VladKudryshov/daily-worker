public class DialyWorker {
    public static void main(String[] args) {
        while (true) {
            try {
                Thread.sleep(1000*60);
            } catch (InterruptedException e) {
            }
            System.out.println("Worker process woke up");
        }
    }
}
