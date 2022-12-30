public class Player {
    private Card[] hand = new Card[4];
    int score =0;
    public void SetCard(Card a,int i)
    {
        hand[i] =a;
    }
    public Card[] GetHand()
    {
        return hand;
    }
    public void SetScore(int score)
    {
        this.score+=score;
    }
    public int GetScore()
    {
        return score;
    }
    public void SetHand(Card[] c)
    {
        hand = c;
    }
}
