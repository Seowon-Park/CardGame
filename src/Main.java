import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int playerNum = GamePlay.setPlayerNum(); //참가인원 받아오기
        Player[] player = new Player[playerNum]; //참가인원 만큼의 객체 배열

        GamePlay.setPlayer(player, 0); //플레이어 세팅
        GamePlay.showPlayer(player); //플레이어 쇼잉

        System.out.println();
        GamePlay.play(player, 100); //100번의 게임 라운드

        GamePlay.showResult(player);
    }
}

//--- 게임 플레이(setPlayerNum , setPlayer)
class GamePlay{
    static int setPlayerNum() throws InputMismatchException{
        Scanner in = new Scanner(System.in);
        int input = 0;
        do {
            System.out.print("참가 인원을 입력해주세요(최대 4명): ");
            try {
                input = in.nextInt();
            }catch(InputMismatchException e){ //
                System.out.println("잘못입력하셨습니다. 참가 인원을 2-4사이의 정수로 입력해주세요.");
                return GamePlay.setPlayerNum();
            }
            if (2 <= input && input <= 4) {
               break;
            } else {
                System.out.println("잘못입력하셨습니다.참가 인원은 최소 2명, 최대 4명입니다.");
            }
        }while(true);
        return input;
    }
    static void setPlayer(Player[] player, int replayer)throws ArrayIndexOutOfBoundsException{
        String nickname="";
        int i = replayer;

        for(i=replayer; i<player.length; i++){
            Scanner in = new Scanner(System.in);
            System.out.printf("%d번째 플레이어의 이름을 입력해주세요(최대 20자): ",i+1);
            nickname = in.nextLine();
            try {
                if(!(nickname.isEmpty())){ player[i] = new Player(nickname);}
                else{player[i]=new Player();}
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.println("이름은 최대 20자입니다,");
                GamePlay.setPlayer(player,i);
                break;
            }
        }
    }
    static void showPlayer(Player[] player){
        System.out.print("| ");
        for(int j=0; j<player.length; j++){
            System.out.print(player[j].nickName);
            System.out.print(" | ");
        }
        System.out.printf("%n*게임에 참가했습니다.*%n");
    }
    static void play(Player[] player, int round){
        int i = 1;
        Deck deck = new Deck();
        Master master = new Master();

        while(i<=round){
            System.out.printf("%n-----라운드 %d-----%n",i);
            master.share(player, deck);
            master.setGrade(player);
            master.setWinner(player);
            i++;
        }
    }
    static void showResult(Player[] player){
        System.out.println();
        System.out.println("========최종결과===========");
        Player[] pc = new Player[player.length];
        pc = player;
        Player swp;

        for(int i=0;i< player.length;i++){
            for(int j=i+1;j<player.length;j++) {
                if(pc[i].win<pc[j].win){
                    swp = pc[j];
                    pc[j] = pc[i];
                    pc[i] = swp;
                }
            }
        }

        for(int i=0; i<player.length; i++){
            System.out.printf("%d위 ",i+1);
            System.out.print(pc[i].nickName);
            System.out.printf("|금액:%10d | 승:%5d | 패:%5d%n",pc[i].getmoney(),pc[i].win,pc[i].lose);
        }
    }
}
//--- 딜러 객체
class Master{
    void share(Player[] player, Deck deck){
        deck.shuffle();
        for(int i=0; i< player.length; i++){
            for(int j=0; j<5; j++){
                player[i].Pcard[j]=deck.cardArr[j+5*i];
            }
            player[i].setCard();
        }
    }
    void setGrade(Player[] player){
        for(int playNum=0 ; playNum<player.length; playNum++){
            int flush=0;
            int fourCard=0;
            int treeCard=0;
            int twoCard=0;
            int straightCheck=1;

            int[] kind= new int[4];
            int[] num= new int[13];

            player[playNum].roundGrade=0;

            for(int i=0;i<5;i++){
                if(player[playNum].Pcard[i].kind== Card.CLOVER){kind[0]++;}
                if(player[playNum].Pcard[i].kind== Card.HEART){kind[1]++;}
                if(player[playNum].Pcard[i].kind== Card.DIAMOND){kind[2]++;}
                if(player[playNum].Pcard[i].kind== Card.SPADE){kind[3]++;}
                num[player[playNum].Pcard[i].number-1]++;
            }

            for(int i=0;i<13;i++) {
                if(num[i]==4){fourCard++;}
                if(num[i]==3){treeCard++;}
                if(num[i]==2){twoCard++;}
            }
            for(int i=0;i<12;i++) {
                if(num[i]==1&&num[i+1]==1){straightCheck++;}
            }
            for(int check: kind){
                if(check==5){flush++; break;}
            }

            System.out.print(player[playNum].nickName);
            System.out.print(" 결과: ");
            if(straightCheck==5&&flush==1&&(num[0]==1&&num[9]==1&&num[10]==1&&num[11]==1&&num[12]==1))
            {System.out.println("ROYAL FlUSH(90)"); player[playNum].roundGrade=90;}
            else if(straightCheck==5&&flush==1){System.out.println("STRAIGHT FlUSH(80)"); player[playNum].roundGrade=80;}
            else if(fourCard==1){System.out.println("FOUR CARD(70)");; player[playNum].roundGrade=70;}
            else if(treeCard==1&&twoCard==1){System.out.println("FULL HOUSE(60)"); player[playNum].roundGrade=60;}
            else if(flush==1){System.out.println("FLUSH(50)"); player[playNum].roundGrade=50;}
            else if(straightCheck==5){System.out.println("STRAIGHT(40)"); player[playNum].roundGrade=40;}
            else if(treeCard==1){System.out.println("THREE CARD(30)"); player[playNum].roundGrade=30;}
            else if(twoCard==2){System.out.println("TWO PAIR(20)"); player[playNum].roundGrade=20;}
            else if(twoCard==1){System.out.println("ONE PAIR(10)"); player[playNum].roundGrade=10;}
            else{System.out.println("NO RANK(0)"); player[playNum].roundGrade=0;}

            for(int k=0;k<player[playNum].Pcard.length;k++) {
                System.out.println(player[playNum].Pcard[k]);
            }
        //    System.out.println(Arrays.toString(kind)+","+Arrays.toString(num)); //kind배열 num배열 확인
        //    System.out.println("S:"+straightCheck+"/4:"+fourCard+"/3:"+treeCard+"/2:"+twoCard+"/F:"+flush); //카운터 잘 작동하는지 확인
        }
    }
    void setWinner(Player[] player){
        Player[] pc = new Player[player.length];
        pc = player;
        Player swp = pc[0];

        for(int i=0;i< player.length;i++){
            for(int j=i+1;j<player.length;j++){
                if(pc[i].roundGrade==pc[j].roundGrade) {//둘이 동점
                    for(int k=0; k<5; k++) {
                        if (pc[i].Pcard[k].number < pc[j].Pcard[k].number) {
                            pc[j].roundGrade += 5; break;
                        } else if (pc[i].Pcard[k].number > pc[j].Pcard[k].number) {
                            pc[i].roundGrade += 5; break;
                        }
                        //숫자도 전부 똑같을 경우?
                    }
                }
                if(pc[i].roundGrade<pc[j].roundGrade){
                    swp=pc[i];
                    pc[i]=pc[j];
                    pc[j]=swp;
                }
            }
        }
        pc[0].win += 1;
        pc[0].setmoney(100);
        for(int i=1 ; i<pc.length; i++){
            pc[i].lose += 1;
            pc[i].setmoney(0);
        }
        System.out.print("승자는...............................");
        System.out.println(pc[0].nickName);

    }
}
//--- 플레이어 객체
class Player{
    static int Pnum = 1;
    char[] nickName={' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '};
    private int money = 10000; //money는 함부로 건들여지면 안되니까..private
    int win=0;
    int lose=0;
    int roundGrade;
    Card[] Pcard = new Card[5];


    Player(){
        this("익명" + Pnum++);
    }
    Player(String nn){
        for(int i=0; i<nn.length(); i++){
            nickName[i]= nn.charAt(i);
        }
    }
    int getmoney(){
        return money;
    }
    void setmoney(int reward){
        money += reward;
    }
    void setCard(){ //5장의 카드 오름차순으로 정렬
        int swp=0;
        for(int i=0 ; i<5 ; i++){
            for(int j=i+1; j<5; j++){
                if(Pcard[i].number<Pcard[j].number){
                    swp = Pcard[i].number;
                    Pcard[i].number = Pcard[j].number;
                    Pcard[i].number = swp;
                }
            }
        }
    }
}

//--- 카드 객체, 각드 덱
class Card{
    static final int KIND_MAX = 3;
    static final int NUM_MAX = 13;

    static final int SPADE = 3;
    static final int DIAMOND = 2;
    static final int HEART = 1;
    static final int CLOVER = 0;

    int kind;
    int number;

    Card(){this(SPADE,1);}
    Card(int kind, int number){this.kind=kind; this.number=number;}
    Card(Card c){
        this.kind = c.kind;
        this.number = c.number;
    }

    //
    public String toString(){
        String[] kinds = {"CLOVER","HEART","DIAMOND","SPADE"};
        String numbers = "0123456789XJQK";

        return "kind: "+kinds[this.kind]+", number: "+numbers.charAt(this.number);
    }
    //
}

class Deck{
    final int CARD_NUM =52;
    Card[] cardArr = new Card[CARD_NUM];

    Deck(){
        int i=0;

        for(int k=Card.KIND_MAX ; k>=0 ; k--)
            for(int n=0 ; n<Card.NUM_MAX ; n++)
                cardArr[i++] = new Card(k,n+1);
    }

    Card pick(){
        int index = (int)(Math.random()*CARD_NUM);
        return pick(index);
    }
    Card pick(int index){
        return cardArr[index];
    }

    void shuffle(){
        for (int i = 0; i < cardArr.length; i++) {
            int r = (int) (Math.random() * CARD_NUM);

            Card temp = cardArr[i];
            cardArr[i] = cardArr[r];
            cardArr[r] = temp;
        }
    }
}