public class Card {
    private String suit;
    private String rank;
    private int valueOfCard;

    
    public String getRank() { return rank; }
    public String getSuit() { return suit; }
    public String getName() { return suit + " " + rank; }
    public int getValueOfCard() {
        return valueOfCard;
    }
    public void setRank(String a)
    {
        rank = a;
        if(suit != "")
        {
            setValueOfCard();
        }
    }
    public void setSuit(String b) {
        suit = b;
        if(rank != "")
        {
            setValueOfCard();
        }
    }
    public void setValueOfCard()
    {
        switch (rank) {
            case "10":
                valueOfCard = suit == "♦" ? 3:1;
                break;
            case "2":
                valueOfCard = suit == "♣" ? 2:1;
                break;
            default:
                valueOfCard = 1;
                break;
        }
    }



}
