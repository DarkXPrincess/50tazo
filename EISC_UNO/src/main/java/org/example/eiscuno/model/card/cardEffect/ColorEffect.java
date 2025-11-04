package org.example.eiscuno.model.card.cardEffect;

import org.example.eiscuno.model.card.Card;

import java.io.Serializable;

/**
 * Represents the effect of a "Color Change" card in the UNO game.
 * <p>
 * This effect allows the player to select a new color when playing
 * a card with a wildcard color change ability (e.g., "WILD").
 * </p>
 *
 * <p>Behavior:</p>
 * <ul>
 *     <li>Sets the color of the played card to the chosen color.</li>
 *     <li>Forces the turn to change to the next player.</li>
 * </ul>
 *
 * @see ICardEffect
 * @see Card
 */
public class ColorEffect implements ICardEffect, Serializable {

    /**
     * Applies the "Change Color" effect to the played card.
     * <p>
     * This method retrieves the selected color from the
     * {@link Card.CardEffectContext} and updates the card accordingly.
     * The game turn is then passed to the next player.
     * </p>
     *
     * @param context the context of the played card, including:
     *                <ul>
     *                    <li>The {@link Card} on which the effect is applied.</li>
     *                    <li>The chosen color specified by the player.</li>
     *                    <li>The {@link org.example.eiscuno.model.game.GameUno} instance
     *                        where the effect is executed.</li>
     *                </ul>
     */
    @Override
    public void applyEffect(Card.CardEffectContext context) {
        Card card = context.getCard();
        String color = context.getColor();
        card.setColor(color);
        context.getGame().changeTurn();
        System.out.println("Se cambio el color a: " + color);
    }
}
