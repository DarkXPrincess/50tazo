package org.example.eiscuno.model.card.cardEffect;

import org.example.eiscuno.model.card.Card;

import java.io.Serializable;

/**
 * Represents the effect of a "Draw Four" card in the UNO game.
 * <p>
 * When this card is played, the targeted opponent must draw four cards
 * from the deck, and the player who played this card chooses the next color
 * for the game to continue with.
 * </p>
 *
 * <p>Behavior:</p>
 * <ul>
 *     <li>The target player draws four cards.</li>
 *     <li>The played card's color is changed to the chosen color.</li>
 * </ul>
 *
 * @see ICardEffect
 * @see Card
 */
public class DrawFourEffect implements ICardEffect, Serializable {

    /**
     * Applies the "Draw Four" effect to the targeted player.
     * <p>
     * This method forces the target player to draw four cards and then changes
     * the color of the played card based on the chosen color in the
     * {@link Card.CardEffectContext}.
     * </p>
     *
     * @param context the context of the played card, containing:
     *                <ul>
     *                    <li>The {@link Card} on which the effect is applied.</li>
     *                    <li>The target player forced to draw four cards.</li>
     *                    <li>The selected color for the next turn.</li>
     *                    <li>The {@link org.example.eiscuno.model.game.GameUno} instance
     *                        where the effect takes place.</li>
     *                </ul>
     */
    @Override
    public void applyEffect(Card.CardEffectContext context) {
        try {
            context.getGame().eatCard(context.getTargetPlayer(), 4);
        }catch (Exception e){
            e.printStackTrace();
        }

        context.getCard().setColor(context.getColor());
        System.out.println(context.getTargetPlayer().getTypePlayer() + " roba 4 cartas.");
    }
}

