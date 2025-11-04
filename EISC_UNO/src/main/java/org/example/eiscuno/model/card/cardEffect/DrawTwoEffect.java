package org.example.eiscuno.model.card.cardEffect;


import org.example.eiscuno.model.card.Card;

import java.io.Serializable;

/**
 * Represents the effect of a "Draw Two" card in the UNO game.
 * <p>
 * When this card is played, the targeted opponent must draw two cards
 * from the deck, losing their turn.
 * </p>
 *
 * <p>Behavior:</p>
 * <ul>
 *     <li>The target player is forced to draw two cards from the deck.</li>
 *     <li>No color change occurs, as this card retains its color.</li>
 *     <li>The target player misses their next turn.</li>
 * </ul>
 *
 * @see ICardEffect
 * @see Card
 */
public class DrawTwoEffect  implements ICardEffect, Serializable {

    /**
     * Applies the "Draw Two" effect to the targeted player.
     * <p>
     * This method forces the target player to draw two cards from the deck.
     * </p>
     *
     * @param context the context of the played card, containing:
     *                <ul>
     *                    <li>The {@link Card} on which the effect is applied.</li>
     *                    <li>The target player forced to draw two cards.</li>
     *                    <li>The {@link org.example.eiscuno.model.game.GameUno} instance
     *                        where the effect takes place.</li>
     *                </ul>
     */
    @Override
    public void applyEffect(Card.CardEffectContext context) {
        try {
            context.getGame().eatCard(context.getTargetPlayer(), 2);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(context.getTargetPlayer().getTypePlayer() + " roba 2 cartas.");
    }

}
