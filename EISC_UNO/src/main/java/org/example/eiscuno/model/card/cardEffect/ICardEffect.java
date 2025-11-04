package org.example.eiscuno.model.card.cardEffect;

import org.example.eiscuno.model.card.Card;

/**
 * Represents the behavior of a special card effect in the UNO game.
 * <p>
 * This interface defines the contract that all card effects must follow,
 * allowing each special card (such as "Draw Two", "Draw Four", "Skip", or "Change Color")
 * to implement its unique behavior when played.
 * </p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     ICardEffect effect = new DrawTwoEffect();
 *     effect.applyEffect(context);
 * </pre>
 *
 * @see Card.CardEffectContext
 * @see Card
 */
public interface ICardEffect {
    /**
     * Applies the effect of a specific UNO card.
     * <p>
     * Implementations of this method define the unique behavior of each special card,
     * such as forcing a player to draw cards, skipping a turn, or changing the color in play.
     * </p>
     *
     * @param context the context in which the effect is applied, containing:
     *                <ul>
     *                    <li>The {@link Card} triggering the effect.</li>
     *                    <li>The target player affected by the effect (if applicable).</li>
     *                    <li>The game state in which the effect is executed.</li>
     *                </ul>
     */
    void applyEffect(Card.CardEffectContext context);
}
