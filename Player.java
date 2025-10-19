import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Player {
    private int id;
    private List<Card> hand;

    public Player(int id) {
        this.id = id;
        this.hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getId() {
        return id;
    }

    public boolean hasWinningHand() {
    if (hand.isEmpty()) return false;
    int value = hand.get(0).getValue();
    for (Card card : hand) {
        if (card.getValue() != value) {
            return false;
        }
    }
    return true;
    }


    
        public void playTurn(ArrayList<Deck> decks, int playerCount) throws InterruptedException {
            Deck drawDeck = decks.get(id - 1);
            Deck discardDeck = decks.get(id % playerCount);

            synchronized(this){
                Card drawnCard = drawDeck.drawCard();
                if (drawnCard != null) {
                    hand.add(drawnCard);
                }
                if (!hand.isEmpty()) {
                    Card discardedCard = hand.remove(0); // doesnt have "strategy" yet
                    discardDeck.addCard(discardedCard);
                }
            }
            
        }

    @Override
    public String toString() {
        return "Player " + id + " current hand: " + hand;
    }
}