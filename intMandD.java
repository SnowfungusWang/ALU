public class intMandD {
    private static int getLowerN(int num,int n){
        int helper=0xFFFFFFFF>>>(32-n);
        return num&helper;
    }
    private static int getHighN(int num,int n){
        int helper=0xFFFFFFFF<<(32-n);
        return num&helper;
    }
    private static int negation(int num,int n){
        return ((~num)+1)&(0xFFFFFFFF>>>(32-n));
    }
    private static int getSign(int num, int n){return num&(1<<(n-1));}
    private static int setPos(int num,int n){ return   (0xFFFFFFFF>>>(32-n))&num;}
    private static int setNeg(int num, int n){return (0xFFFFFFFF<<(n))|num;}
    //暂时不用写取反
    //还是要写的兄弟
    private static void printBinary(int num,int n){
        int hepler=1<<n-1;
        for (int i = 0; i < n; i++) {
            if((num&hepler)==0) System.out.print(0);
            else System.out.print(1);
            hepler=hepler>>>1;
        }
        System.out.println();
    }

    private static void printBinary(int num){
        int hepler=0x80000000;
        for (int i = 0; i < 32; i++) {
            if((num&hepler)==0) System.out.print(0);
            else System.out.print(1);
            hepler=hepler>>>1;
        }
        System.out.println();
    }

    private static void cutOff(){
        System.out.println("*");
    }
    private static int simMul(int x,int y,int n){
        //判断是否有0
        if (x==0) return 0;
        if (y==0) return 0;
        int helper=1;
        for (int i =0;i<n;i++){
            if ((y&helper)!=0) {
                y = y + (x<<(n));
            }
            y=y>>>1;
        }
        //判断溢出
        //等等普通乘法真的会上溢吗
        //不会，所以下面的没啥用
        if (getHighN(y, 32-2*n)!=0){
            System.out.println("overflow");
            return -1;
        }
        //没用的结束了
        return y;
    }

    private static int BoothMul(int x, int y, int n){
        //判断是否有0
        if (x==0) return 0;
        if (y==0) return 0;


        int judgeHelper=3;
        int signJudge=1<<(2*n);
        int setNe=0xFFFFFFFF<<(n*2+1);
        int setPo=0xFFFFFFFF>>>(32-n*2-1);
        int neX=intMandD.negation(x,n);

        //设置初始储存结果区
        int result=y<<1;
        if ((result&(signJudge>>n))!=0){
            result=result&(setPo>>n);
        }



         for (int i =0;i<n;i++){
            if ((result&judgeHelper)==1){//Pi=0,pi-1=1
                result = result + (x<<(n+1));
            }
            if ((result&judgeHelper)==2) {//Pi=0,pi-1=1
                result = result + (neX<<(n+1));
            }

            if ((result&signJudge)!=0){
                result = result|setNe;
            }
            else {
                result=result&setPo;
            }
            result=result>>1;
        }
        result=result>>1;
        return result;
    }

    private static String division(int x, int y, int n){
        int mixRes=recoverReDiv(x,y,n);
        int remainder,quotient;
        //得到商
        if (getSign(mixRes,n)==0){
            quotient=setPos(mixRes,n);
        }
        else quotient=setNeg(mixRes,n);

        //得到余数
        if (getSign(mixRes,2*n)==0){
            remainder=setPos(mixRes>>n,n);
        }
        else remainder=setNeg(mixRes>>n,n);
        return String.valueOf(quotient)+"@"+String.valueOf(remainder);
    }

    private static String division2(int x, int y, int n){
        int mixRes=notRecoverRe(x,y,n);
        int remainder,quotient;
        //得到商
        if (getSign(mixRes,n)==0){
            quotient=setPos(mixRes,n);
        }
        else quotient=setNeg(mixRes,n);

        //得到余数
        if (getSign(mixRes,2*n+1)==0){
            remainder=setPos(mixRes>>n+1,n);
        }
        else remainder=setNeg(mixRes>>n+1,n);
        return String.valueOf(quotient)+"@"+String.valueOf(remainder);
    }

    private static int recoverReDiv(int x,int y, int n){
        if (x==0){
            return 0;
        }
        if (y==0){
            System.out.println("error");
            return -1;
        }
        boolean enoughForMinus;
        //结果存放在x之中

        for (int i=0;i<n;i++){
            x=x<<1;
            if (getSign(y,n)==getSign(x>>>n,n)){
                    x=x-(y<<n);
                    if (getSign(y,n)==getSign(x>>>n,n))
                        enoughForMinus=true;
                    else {
                        enoughForMinus = false;
                        x=x+(y<<n);
                        printBinary(x,32);
                    }
                }
            else {
                x=x+(setPos(y<<n,2*n));
                if (getSign(y,n)!=getSign(x>>>n,n))
                    enoughForMinus=true;
                else {
                    enoughForMinus = false;
                    x=x-(setPos(y<<n,2*n));
                }
            }
            //判断结束;
            if (enoughForMinus){
                x++;
            }
        }
        if (getSign(y,n)!=getSign(x,2*n)){
            x=(setPos((~(setPos(x,n))+1),n))+(x&setNeg(0,n));
        }

        return x;
    }

    private static int notRecoverRe (int x, int y, int n){
        if (x==0){
            return 0;
        }
        if (y==0){
            System.out.println("error");
            return -1;
        }
        boolean enoughForMinus;
        int ySign=getSign(y,n)<<n;
        int xSign=(getSign(x,2*n))<<1;//用于最后一步的结果处理

        //结果存放在x之中
        //第一次，不左移
        if (ySign==xSign>>>1)
            x=setPos(x-setPos(y<<n,2*n),2*n);
        else
            x=setPos(x+(setPos(y<<n,2*n)),2*n);
        enoughForMinus=(ySign==getSign(x,n*2));
        //printBinary(x);
        for (int i=0;i<n;i++){
            //left shift(*2)
            //+/-(according to last step's e
            //judge new e
            cutOff();
            x=x<<1;
            if (enoughForMinus){
                x=setPos(x-setPos(y<<n,2*n),2*n)+1;
            }
            else {
                x=setPos(x+(setPos(y<<n,2*n)),2*n);
            }
            enoughForMinus=(ySign==getSign(x,n*2));
            printBinary(x);
            //判断结束;
        }
        x=x<<1;
        if (enoughForMinus)x++;
        //最后一次完成；
        //cutOff();
        //printBinary(x);

        //修正商
        if((getSign(y,n)<<n+1)!=(xSign)){
            x=x+1;
        }
        //System.out.println("a");
        //printBinary(x);

        //修正余数
        if(getSign(x,2*n+1)!=xSign){
            if ((getSign(y,n)<<n+1)==xSign){
                x=(((x>>n+1)+y)<<n+1)|(setPos(x,n+1));
            }
            else x=(((x>>n+1)-y)<<n+1)|(setPos(x,n+1));
        }



        return x;
    }
    public static void main(String args[]){
        int x=8;
        int y=-1;
        int n=5;

        System.out.print("x=");
        intMandD.printBinary(x,n);
        System.out.print("y=");
        intMandD.printBinary(y,n);
        //System.out.println(intMandD.BoothMul(x,y,n));
        System.out.println(division2(x,y,n));
    }

}
