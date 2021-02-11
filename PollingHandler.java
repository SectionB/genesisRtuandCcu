class PollingHandler extends Thread{

    public CCSServer ccsServer;

    public PollingHandler(CCSServer ccsServe){
        this.ccsServer = ccsServe;
    }

    public void run() {
        while (true){

            boolean response = ccsServer.sendPollingMessage();

            if(response){
                System.out.println("RTSUI is connected");
            }

            else {
                System.out.println("RTSUI is not connected");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
