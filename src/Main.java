import java.util.Random;
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
                System.out.println("Atmak istediğiniz kartın numarasını giriniz. (9:Exit)");
                String line = sc.nextLine();
                int input = Integer.parseInt(line);
                if(input>0 && input<5)
                {
                    game.ThrowCardForPlayer(input);
                }
                else {
                    switch (input) {
                        case 9:
                            System.out.println("Oyun bitti");
                            gameGoingOn = false;
                            break;
                        default:
                            System.out.println("Lütfen geçerli bir değer giriniz.");
                    }
                }
            }
            catch (Exception e)
            {
                System.out.println("Lütfen geçerli bir değer giriniz.");
            }
        }
    }

    public static void main(String[] args) throws Exception{
        game.PrintLeaderboard();
        Scanner sc = new Scanner(System.in);
        boolean isStartGame = false;
        while(gameGoingOn)
        {
           try {
               System.out.println("5: Desteyi karıştır. | 6: Desteyi kes. | 7: Oyunu başlat. | 9: Çıkış");
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
                   case 9:
                       System.out.println("Oyun bitti");
                       gameGoingOn = false;
                       break;
                   default:
                       System.out.println("Lütfen geçerli bir değer giriniz.");
               }
           }
           catch (Exception e)
           {
               System.out.println("Lütfen geçerli bir değer giriniz.");
           }
        }
    }
}