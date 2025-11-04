package org.example.eiscuno.model.card.cardEffect;

import org.example.eiscuno.model.card.Card;

import java.io.Serializable;

/**
 * Represents the effect of a "Skip" card in the UNO game.
 * <p>
 * When a "Skip" card is played, the next player in turn order
 * loses their turn and cannot play any cards during that round.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     ICardEffect skipEffect = new SkipEffect();
 *     skipEffect.applyEffect(context);
 * </pre>
 *
 * @see ICardEffect
 * @see Card.CardEffectContext
 */
public class SkipEffect implements ICardEffect, Serializable {

    /**
     * Applies the "Skip" effect to the target player.
     * <p>
     * The player whose turn is skipped cannot play any card in this round.
     * </p>
     *
     * @param context the context containing:
     *                <ul>
     *                    <li>The {@link Card} that triggered the effect.</li>
     *                    <li>The target player whose turn will be skipped.</li>
     *                    <li>The game state where the effect occurs.</li>
     *                </ul>
     */
    @Override
    public void applyEffect(Card.CardEffectContext context) {
        System.out.println("¡Turno saltado para " + context.getTargetPlayer().getTypePlayer() + "!");
    }

}
