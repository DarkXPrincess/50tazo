package org.example.eiscuno.model.card.cardEffect;

import org.example.eiscuno.model.card.Card;

import java.io.Serializable;

/**
 * Represents a wrapper for a card's effect in the UNO game.
 * <p>
 * Each {@link org.example.eiscuno.model.card.Card} may have an effect
 * that alters the game state when played. This class serves as a
 * container for a specific {@link ICardEffect} implementation and
 * allows applying it dynamically.
 * </p>
 *
 * @see ICardEffect
 * @see Card
 */
public class CardEffect implements Serializable {
    /**
     * The specific effect behavior associated with a card.
     * <p>
     * This can be one of several implementations:
     * <ul>
     *     <li>{@link DrawTwoEffect}</li>
     *     <li>{@link DrawFourEffect}</li>
     *     <li>{@link ColorEffect}</li>
     *     <li>{@link SkipEffect}</li>
     * </ul>
     * If no effect is set, this attribute may remain {@code null},
     * meaning the card is a normal number card with no special effect.
     */
    private ICardEffect cardEffect;

    /**
     * Constructs a {@code CardEffect} with no assigned effect.
     * The {@link #cardEffect} attribute will be {@code null}.
     */
    public CardEffect(){
        cardEffect = null;
    }

    /**
     * Constructs a {@code CardEffect} with the given effect behavior.
     *
     * @param cardEffect the specific effect implementation to be assigned
     */
    public CardEffect(ICardEffect cardEffect) {
        this.cardEffect = cardEffect;
    }

    /**
     * Assigns a specific behavior to this card effect.
     * <p>
     * This allows dynamically changing a card's effect if necessary
     * (e.g., during card initialization or testing).
     * </p>
     *
     * @param cardEffect the {@link ICardEffect} implementation to set
     */
    public void setCardEffect(ICardEffect cardEffect) {
        this.cardEffect = cardEffect;
    }

    /**
     * Applies the effect of this card using the given context.
     * <p>
     * If no effect has been set ({@code cardEffect == null}),
     * this method does nothing.
     * </p>
     *
     * @param context the {@link Card.CardEffectContext} containing the
     *                game state, target player, and any additional parameters
     */
    public void applyEffect(Card.CardEffectContext context) {
        if(cardEffect != null){
            cardEffect.applyEffect(context);
        }
    }

    /**
     * Retrieves the currently assigned effect of this card.
     *
     * @return the current {@link ICardEffect} implementation, or {@code null}
     * if this card has no special behavior
     */
    public ICardEffect getCardEffect(){
        return cardEffect;
    }
}
