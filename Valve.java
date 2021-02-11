public class Valve extends Thread {

    public KKSystem kkSystem;
    public int R;

    public Valve(KKSystem kkSystem, int R){
        this.kkSystem = kkSystem;
        this.R = R;
    }


    public void run() {
        try {
            Thread.sleep(1000*R);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        kkSystem.valveSwitchOff();
    }
}
