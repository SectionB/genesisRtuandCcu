public class Motor extends Thread {


    public KKSystem kkSystem;
    public int N;

    public Motor(KKSystem kkSystem, int N){
        this.kkSystem = kkSystem;
        this.N = N;
    }


    public void run() {
        try {
            Thread.sleep(1000*N);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        kkSystem.motorSwitchOff();
    }
}
