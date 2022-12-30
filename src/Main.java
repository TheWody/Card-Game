import java.util.Scanner;

public class Main {
    private static Game game = new Game();
    private static boolean gameGoingOn = true;
    public static void GameLoop()
    {
        Scanner sc = new Scanner(System.in);
        while(gameGoingOn && !game.gameOver)
        {
            try {
                game.WriteInfos();
                System.out.println("Enter number of the card you want to throw. (9:Exit)");
                String line = sc.nextLine();
                int input = Integer.parseInt(line);
                if(input>0 && input<5)
                {
                    game.ThrowCardForPlayer(input);
                }
                else {
                    switch (input) {
                        case 9:
                            System.out.println("Game Over");
                            gameGoingOn = false;
                            break;
                        default:
                            System.out.println("---------------------------------");
                            System.out.println("Please Enter A Valid Number");
                    }
                }
            }
            catch (Exception e)
            {
                System.out.println("---------------------------------");
                System.out.println("Please Enter A Valid Number");
            }
        }
    }

    public static void main(String[] args) throws Exception{
        Scanner sc = new Scanner(System.in);
        boolean isStartGame = false;
        while(gameGoingOn)
        {
           try {
               System.out.println("5: Shuffle The Deck. | 6: Cut The Deck. | 7: Start Game. | 8: Print Leaderboard | 9: Exit");
               String line = sc.nextLine();
               int input = Integer.parseInt(line);
               switch (input) {
                   case 5:
                       game.ShuffleDeck();
                       break;
                   case 6:
                       game.CutDeck();
                       break;
                   case 7:
                       isStartGame = true;
                       game.PullCard(true);
                       GameLoop();
                       game = new Game();
                       break;
                   case 8:
                       game.PrintLeaderboard();
                       break;
                   case 9:
                       System.out.println("Game Over.");
                       gameGoingOn = false;
                       break;
                   default:
                       System.out.println("Please Enter A Valid Number.");
               }
           }
           catch (Exception e)
           {
               System.out.println("Please Enter A Valid Number.");
           }
        }
    }
}