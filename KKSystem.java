public class KKSystem {

    public boolean v1 = false;
    public boolean v2 = false;
    public boolean v3 = false;
    public boolean p1 = false;
    public boolean p2 = false;
    public boolean m1 = false;
    public boolean m2 = false;
    public boolean lastBit = false;

    public int N = 5;
    public int R = 15;

    public boolean[] getSystemStatus(){
        boolean[] status = {v1, v2 ,v3 , p1,p2 ,m1, m2};
        return status;
    }

    public void setSystemStates(byte data){
        this.v1= checkBit(data,0);
        this.v2= checkBit(data,1);
        this.v3= checkBit(data,2);
        this.p1= checkBit(data,3);
        this.p2= checkBit(data,4);
        this.m1= checkBit(data,5);
        this.m2= checkBit(data,6);
        this.lastBit= checkBit(data,7);
    }

    public void checkSystemUpdate(byte data) {


        if(!(this.v3 | this.p2 | this.m1 | this.m2)){
                this.v1= checkBit(data,0);
        }
        else {
            this.v1= false;
        }

        if(!(this.v3 | this.p1 | this.m1 | this.m2)){
                this.v2= checkBit(data,1);
        }
        else{
            this.v2= false;
        }

        if(checkBit(data,2)){
            if(!checkBit(data,0) & !checkBit(data,1) &
                    !checkBit(data,5) & !checkBit(data,6)
                    & !this.v1 & !this.v2 & !this.m1 & !this.m2){
                v3= checkBit(data,2);
            }
        }

        if(checkBit(data,3)){
            if(checkBit(data,0) & !checkBit(data,1) & this.v1 & !this.v2){
                this.p1= checkBit(data,3);
            }
        }
        else{
            this.p1= checkBit(data,3);
        }

        if(checkBit(data,4)){
            if(!checkBit(data,0) & checkBit(data,1) & !this.v1 & this.v2){
                this.p2= checkBit(data,4);
            }
        }
        else {
            this.p2= checkBit(data,4);
        }

        if(checkBit(data,5) |checkBit(data,6)){
            if(!checkBit(data,0) & !checkBit(data,1) & !checkBit(data,2)
                    & !this.v1 & !this.v2 & !this.v3){
                m1= checkBit(data,5);
                m2= checkBit(data,6);
            }
        }

        if(m1|m2){
            mixerOperate();
        }
        if(v3){
            valveOperate();
        }

    }

    public void mixerOperate() {
        Motor motor = new Motor(this,N);
        motor.start();
    }
    public void valveOperate(){
        Valve valve = new Valve(this,R);
        valve.start();
    }

    public byte getResponce(){
        byte responce= (byte)((lastBit?1<<7:0) +
                (m2?1<<6:0) + (m1?1<<5:0) +
                (p2?1<<4:0) + (p1?1<<3:0) +
                (v3?1<<2:0) + (v2?1<<1:0) + (v1?1:0));
        return responce;
    }

    static boolean checkBit(byte byteK,int position)
    {
        return ((byteK >> position) & 1)==1;
    }

    public void motorSwitchOff(){
        m1 = false;
        m2 = false;
    }
    public void valveSwitchOff(){
        v3 = false;
    }



}
