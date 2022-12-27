import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import java.io.IOException;
public class Game {

    private Card[] deck;
    private Player player;
    private Player computer;
    private Card[] ground;
    private int currentCard;
    private int totalCards = 52;
    private Random r = new Random();
    private boolean isPlayerTakeGround;
    public boolean gameOver;
    public Game ()
    {
        InitGame();
    }
    public Card[] GetDeck()
    {
        return deck;
    }

    public void ShuffleDeck()
    {
        System.out.println("Deste karıştırılıyor.");
        for (int j=0;j<100;j++)
        {
            int i = r.nextInt(deck.length);
            int k = r.nextInt(deck.length);
            Card temp = deck[k];
            deck[k] = deck[i];
            deck[i] = temp;
        }
    }
    public void CutDeck()
    {
        System.out.println("Deste kesiliyor.");
        int k = 20;
        int counter1 =0;
        Card[] newDeck = new Card[deck.length];
        for (int i=k;i<deck.length;i++)
        {
            newDeck[i-k] = deck[i];
            counter1++;
        }
        for (int i=0;i<k;i++)
        {
            newDeck[counter1] = deck[i];
            counter1++;
        }
        deck = newDeck;
    }
    public Card RemoveCard()
    {
        Card temp = deck[0];
        Card[] newDeck = new Card[deck.length-1];
        for(int i=1;i<deck.length;i++)
        {
            newDeck[i-1] = deck[i];
        }
        deck = newDeck;
        return temp;
    }
    public void PullCard(boolean isFirstRound)
    {
        if (isFirstRound)
        {
            for(int i=3;i>=0;i--)
            {
                Card removedCard = RemoveCard();
                ground[i] = removedCard;
            }
        }
        Card[] playerHand = new Card[4];
        for(int i=0;i<4;i++)
        {
            Card removedCard = RemoveCard();
            playerHand[i] = removedCard;
        }
        player.SetHand(playerHand);
        Card[] computerHand = new Card[4];
        for(int i=0;i<4;i++)
        {
            Card removedCard = RemoveCard();
            computerHand[i] = removedCard;
        }
        computer.SetHand(computerHand);
    }

    public Player getPlayer() {
        return player;
    }
    public Player getComputer() {
        return computer;
    }
    public Card[] getGround() {
        return ground;
    }
    public void PrintDeck() {
        for (int i = 0; i < 52; i++) {
            System.out.println(deck[i].getRank() + " " + deck[i].getSuit());
        }
    }
    public void AddGround(Card c,boolean isPlayer) {
        boolean isWonRound = (ground.length>0 && (c.getRank() == ground[0].getRank() || c.getRank() == "Jacks"));
        boolean isPisti = (ground.length == 1 && c.getRank() == ground[0].getRank());
        Card[] newGround = new Card[ground.length+1];
        newGround[0] = c;
        if(ground.length>0) {
            for (int i = 1; i < newGround.length; i++) {
                newGround[i] = ground[i - 1];
            }
        }
        ground = newGround;
        if (isWonRound)
        {
            CalculateScore(isPlayer,isPisti);
            newGround = new Card[0];
            ground = newGround;
        }
    }
    public void ThrowCardForPlayer(int cardNum) {
        Card[] newHand = new Card[player.GetHand().length-1];
        int counter=0;
        for(int i=0;i<player.GetHand().length;i++)
        {
            if(i==cardNum-1)
            {
                AddGround(player.GetHand()[i],true);
            }
            else
            {
                newHand[counter] = player.GetHand()[i];
                counter++;
            }
        }
        player.SetHand(newHand);
        ThrowCardForComputer();
    }
    public void ThrowCardForComputer() {
        Card[] newHand = new Card[computer.GetHand().length-1];
        int counter =0;
        boolean isCardThrown = false;
        int thrownCardIndex =r.nextInt(0,computer.GetHand().length);
        for(int i=0;i<computer.GetHand().length;i++)
        {
            if(ground.length>0)
            {
                if(ground[0].getRank() == computer.GetHand()[i].getRank())
                {
                    AddGround(computer.GetHand()[i],false);
                    isCardThrown = true;
                    thrownCardIndex = i;
                    break;
                }
            }
        }
        if(!isCardThrown)
        {
            AddGround(computer.GetHand()[thrownCardIndex],false);
        }
        for(int i=0;i<computer.GetHand().length;i++)
        {
            if(i != thrownCardIndex)
            {
                newHand[counter] = computer.GetHand()[i];
                counter++;
            }
        }
        computer.SetHand(newHand);
        if(computer.GetHand().length == 0)
        {
            if(deck.length == 0)
            {
                if(ground.length>0)
                {
                    CalculateScore(isPlayerTakeGround,false);
                }
                System.out.println("Oyun bitti.");
                System.out.println("Player score: " + player.GetScore() + " | Computer score: " + computer.GetScore());
                if(player.GetScore() > computer.GetScore())
                {
                    System.out.println("Tebrikler, kazandınız.");
                }
                else {
                    System.out.println("Kaybettin.");
                }
                gameOver = true;
            }
            else
            {
                PullCard(false);
            }
        }
    }
    public void CalculateScore(boolean isPlayer,boolean isPisti)
    {
        int score = 0;
        if(isPisti)
        {
            score += 10;
        }
        else
        {
            for(int i=0;i< ground.length;i++)
            {
                score += ground[i].getValueOfCard();
            }
        }
        if(isPlayer)
        {
            player.SetScore(score);
            isPlayerTakeGround = true;
        }
        else
        {
            computer.SetScore(score);
            isPlayerTakeGround = false;
        }
    }
    public void WriteInfos()
    {
        String groundInfo = "Yerdeki kart: ";
        if(ground.length>0) {
            groundInfo += ground[0].getName();
        }
        else {
            groundInfo += "kart yok";
        }
        System.out.println(groundInfo + " | Yerdeki kart sayısı: " + ground.length);
        String computerHand = "bilgisayar: ";
        String playerHand = "oyuncu: ";
        for(int i=0;i<computer.GetHand().length;i++)
        {
            computerHand += (i+1) + ".card:" + computer.GetHand()[i].getName() + " | ";
        }
        computerHand += " score: " + computer.GetScore();
        System.out.println(computerHand);
        for(int i=0;i<player.GetHand().length;i++)
        {
            playerHand += (i+1) + ".card:" + player.GetHand()[i].getName() + " | ";
        }
        playerHand += "score:  " + player.GetScore();
        System.out.println(playerHand);
    }
    public void InitGame() {
        String ranks[] = {"Ace","2","3","4","5","6","7","8","9","10","Jacks","Queen","King"};
        String suits[] = {"♠","♣","♥","♦"};
        deck = new Card[totalCards];

        for(int i=0;i<13;i++)
        {
            for(int j=0;j<4;j++)
            {
                var newCard = new Card();
                newCard.setRank(ranks[i]);
                newCard.setSuit(suits[j]);
                deck[currentCard] = newCard;
                currentCard++;
            }
        }
        gameOver = false;
        player = new Player();
        computer = new Player();
        ground = new Card[4];
        currentCard = 0;
        isPlayerTakeGround = false;
    }
    public void AddToLeaderboard()
    {
        Scanner sc = new Scanner(System.in);
        String name;

    }
    public void PrintLeaderboard() throws IOException {
        Scanner reader = null;
        String[] tag = { "Name: ", "Score: "};
        try
        {
            var path = Paths.get("leaderboard.txt");
            reader = new Scanner(Paths.get("leaderboard.txt"));
            while(reader.hasNextLine())
            {
                String[] info = reader.nextLine().split(",");
                for(int i=0;i<info.length;i++)
                {
                    System.out.println(tag[i] + info[i]);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
