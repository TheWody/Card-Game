import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Formatter;
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
    private String playerScores[][];
    private String name = "";

    public Game() {
        InitGame();
    }

    public void ShuffleDeck() {
        System.out.println("Deste karıştırılıyor.");
        for (int j = 0; j < 100; j++) {
            int i = r.nextInt(deck.length);
            int k = r.nextInt(deck.length);
            Card temp = deck[k];
            deck[k] = deck[i];
            deck[i] = temp;
        }
    }

    public void CutDeck() {
        System.out.println("Deste kesiliyor.");
        int k = r.nextInt(1, 51);
        int counter1 = 0;
        Card[] newDeck = new Card[deck.length];
        for (int i = k; i < deck.length; i++) {
            newDeck[i - k] = deck[i];
            counter1++;
        }
        for (int i = 0; i < k; i++) {
            newDeck[counter1] = deck[i];
            counter1++;
        }
        deck = newDeck;
    }

    public Card RemoveCard() {
        Card temp = deck[0];
        Card[] newDeck = new Card[deck.length - 1];
        for (int i = 1; i < deck.length; i++) {
            newDeck[i - 1] = deck[i];
        }
        deck = newDeck;
        return temp;
    }

    public void PullCard(boolean isFirstRound) {
        Card[] playerHand = new Card[4];
        for (int i = 0; i < 4; i++) {
            Card removedCard = RemoveCard();
            playerHand[i] = removedCard;
        }
        player.SetHand(playerHand);
        Card[] computerHand = new Card[4];
        for (int i = 0; i < 4; i++) {
            Card removedCard = RemoveCard();
            computerHand[i] = removedCard;
        }
        computer.SetHand(computerHand);
        if (isFirstRound) {
            for (int i = 3; i >= 0; i--) {
                Card removedCard = RemoveCard();
                ground[i] = removedCard;
            }
        }
    }

    public void AddGround(Card c, boolean isPlayer) {
        boolean isWonRound = (ground.length > 0 && (c.getRank() == ground[0].getRank() || c.getRank() == "Jacks"));
        boolean isPisti = (ground.length == 1 && c.getRank() == ground[0].getRank());
        Card[] newGround = new Card[ground.length + 1];
        newGround[0] = c;
        if (ground.length > 0) {
            for (int i = 1; i < newGround.length; i++) {
                newGround[i] = ground[i - 1];
            }
        }
        ground = newGround;
        if (isWonRound) {
            CalculateScore(isPlayer, isPisti);
            newGround = new Card[0];
            ground = newGround;
        }
    }

    public void ThrowCardForPlayer(int cardNum) throws IOException {
        Card[] newHand = new Card[player.GetHand().length - 1];
        int counter = 0;
        for (int i = 0; i < player.GetHand().length; i++) {
            if (i == cardNum - 1) {
                AddGround(player.GetHand()[i], true);
            } else {
                newHand[counter] = player.GetHand()[i];
                counter++;
            }
        }
        player.SetHand(newHand);
        ThrowCardForComputer();
    }

    public void ThrowCardForComputer() throws IOException {
        Card[] newHand = new Card[computer.GetHand().length - 1];
        int counter = 0;
        boolean isCardThrown = false;
        int thrownCardIndex = r.nextInt(0, computer.GetHand().length);
        for (int i = 0; i < computer.GetHand().length; i++) {
            if (ground.length > 0) {
                if (ground[0].getRank() == computer.GetHand()[i].getRank()) {
                    AddGround(computer.GetHand()[i], false);
                    isCardThrown = true;
                    thrownCardIndex = i;
                    break;
                }
            }
        }
        if (!isCardThrown) {
            AddGround(computer.GetHand()[thrownCardIndex], false);
        }
        for (int i = 0; i < computer.GetHand().length; i++) {
            if (i != thrownCardIndex) {
                newHand[counter] = computer.GetHand()[i];
                counter++;
            }
        }
        computer.SetHand(newHand);
        if (computer.GetHand().length == 0) {
            if (deck.length == 0) {
                if (ground.length > 0) {
                    CalculateScore(isPlayerTakeGround, false);
                }
                System.out.println("Game Over.");
                System.out.println("Player score: " + player.GetScore() + " | Computer score: " + computer.GetScore());
                if (player.GetScore() > computer.GetScore()) {
                    System.out.println("Congratulations, You Won!");
                    CreateLeaderboard();
                } else {
                    System.out.println("You Lost.");
                }
                gameOver = true;
            } else {
                PullCard(false);
            }
        }
    }

    public void CalculateScore(boolean isPlayer, boolean isPisti) {
        int score = 0;
        if (isPisti) {
            score += 10;
        } else {
            for (int i = 0; i < ground.length; i++) {
                score += ground[i].getValueOfCard();
            }
        }
        if (isPlayer) {
            player.SetScore(score);
            isPlayerTakeGround = true;
        } else {
            computer.SetScore(score);
            isPlayerTakeGround = false;
        }
    }

    public void WriteInfos() {
        System.out.println("------------------------------");
        String groundInfo = "Yerdeki kart: ";
        if (ground.length > 0) {
            groundInfo += ground[0].getName();
        } else {
            groundInfo += "kart yok";
        }
        System.out.println(groundInfo + " | Yerdeki kart sayısı: " + ground.length);
        String computerHand = "bilgisayar: ";
        String playerHand = "oyuncu: ";
        for (int i = 0; i < computer.GetHand().length; i++) {
            computerHand += (i + 1) + ".card:" + computer.GetHand()[i].getName() + " | ";
        }
        computerHand += " score: " + computer.GetScore();
        System.out.println(computerHand);
        for (int i = 0; i < player.GetHand().length; i++) {
            playerHand += (i + 1) + ".card:" + player.GetHand()[i].getName() + " | ";
        }
        playerHand += "score:  " + player.GetScore();
        System.out.println(playerHand);
    }

    public void InitGame() {
        String ranks[] = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jacks", "Queen", "King"};
        String suits[] = {"♠", "♣", "♥", "♦"};
        deck = new Card[totalCards];

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 4; j++) {
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

    public void CreateLeaderboard() throws IOException {
        Scanner sc = new Scanner(System.in);
        ReadLeaderboard(true);
        int position = -1;
        for (int i = 0; i < playerScores.length; i++) {
            if (playerScores[i][1] == null || player.GetScore() > Integer.parseInt(playerScores[i][1])) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            System.out.println("CONGRATULATIONS\n---high score---\nPlease enter your name:");
            name = sc.nextLine();
            for (int i = playerScores.length - 1; i > position; i--) {
                playerScores[i][0] = playerScores[i - 1][0];
                playerScores[i][1] = playerScores[i - 1][1];
            }
            playerScores[position][0] = name;
            playerScores[position][1] = Integer.toString(player.GetScore());
        }
        UpdateLeaderboard();
    }

    public void UpdateLeaderboard() {
        Formatter f = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter("leaderboard.txt", false);
            f = new Formatter(fw);
            for (int i = 0; i < playerScores.length; i++) {
                f.format("%s,%s\n", playerScores[i][0], playerScores[i][1]);
            }
            fw.close();
        } catch (Exception e) {
            System.err.println("Something went wrong.");
        } finally {
            if (f != null) {
                f.close();
            }
        }
    }

    public void PrintLeaderboard() {
        ReadLeaderboard(false);
        System.out.println("Leaderboard:");
        for (int i = 0; i < playerScores.length; i++) {
            if (playerScores[i][1] != null) {
                System.out.println(playerScores[i][0] + ": " + playerScores[i][1]);
            }
        }
    }

    public void ReadLeaderboard(boolean isNewHighScore) {
        FindPlayerCount(isNewHighScore);
        Scanner reader = null;
        String line = "";
        int playerCount = 0;
        try {
            reader = new Scanner(Paths.get("C:\\Users\\Okan Özyürekli\\IdeaProjects\\Card Game Project\\leaderboard.txt"));
            while (reader.hasNextLine()) {
                line = reader.nextLine();
                String[] info = line.split(",");
                playerScores[playerCount][0] = info[0];
                playerScores[playerCount][1] = info[1];
                playerCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    public void FindPlayerCount(boolean isNewHighScore) {
        Scanner reader = null;
        String line = "";
        int playerCount = 0;
        try {
            reader = new Scanner(Paths.get("C:\\Users\\Okan Özyürekli\\IdeaProjects\\Card Game Project\\leaderboard.txt"));
            while (reader.hasNextLine()) {
                playerCount++;
                reader.nextLine();
            }
            if (playerCount >= 10) {
                playerCount = 10;
            } else if (isNewHighScore) {
                playerCount++;
            }
            playerScores = new String[playerCount][2];
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}