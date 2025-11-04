package org.example.eiscuno.model.card;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.cardEffect.CardEffect;
import org.example.eiscuno.model.card.cardEffect.ICardEffect;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;

import java.io.Serializable;

/**
 * Represents a single card in the UNO game.
 * <p>
 * A card has:
 * <ul>
 *   <li>A value (number or special action)</li>
 *   <li>A color (RED, BLUE, GREEN, YELLOW, or neutral for wild cards)</li>
 *   <li>An optional {@link ICardEffect effect} that triggers specific game actions</li>
 *   <li>An image and {@link ImageView} representation for the UI</li>
 * </ul>
 *
 * @see GameUno
 * @see Player
 * @see ICardEffect
 */
public class Card implements Serializable {
    /**
     * Resource path for this card's image file.
     */
    private String url;
    /**
     * Value of the card (0–9 or special keywords such as SKIP, REVERSE, EAT2, EAT4, NEWCOLOR).
     */
    private String value;
    /**
     * Color of the card (RED, BLUE, GREEN, YELLOW, UNKNOWN).
     */
    private String color;
    /**
     * Visual image of the card. Marked as {@code transient} to avoid serialization.
     */
    private transient Image image;
    /**
     * ImageView used to display this card in the JavaFX UI.
     * Reconstructed if lost during serialization.
     */
    private transient ImageView cardImageView;
    /**
     * The effect assigned to this card, defining its special behavior when played.
     */
    private CardEffect effect;

    /**
     * Inner context class that carries all necessary information for applying a card's effect.
     * <p>
     * This class is passed as a parameter to {@link #applyEffect(CardEffectContext)}
     * and contains references to:
     * <ul>
     *     <li>The current {@link GameUno} instance</li>
     *     <li>The target {@link Player} affected by the effect</li>
     *     <li>The color to be set (used for wild cards)</li>
     * </ul>
     */
    public class CardEffectContext implements Serializable {
        /**
         * Reference to the active UNO game instance.
         */
        private GameUno game;
        /**
         * The player affected by this card's effect.
         */
        private Player targetPlayer;
        /**
         * The chosen color to be applied (mainly for wild cards).
         */
        private String color;

        /**
         * Creates a context with game and target player information.
         *
         * @param game         the current game instance
         * @param targetPlayer the player affected by this card
         */
        public CardEffectContext(GameUno game, Player targetPlayer) {

            this.game = game;
            this.targetPlayer = targetPlayer;
            this.color = null;
        }

        /**
         * Creates a context with only a chosen color (for wild cards).
         *
         * @param color the chosen color
         */
        public CardEffectContext(String color) {
            this.color = color;
            this.game = null;
            this.targetPlayer = null;
        }

        /**
         * Creates a context with game, target player, and color.
         *
         * @param game         the game instance
         * @param targetPlayer the player affected by this card
         * @param color        the chosen color
         */
        public  CardEffectContext(GameUno game, Player targetPlayer, String color) {
            this.game = game;
            this.targetPlayer = targetPlayer;
            this.color = color;
        }

        /**
         * @return the current {@link GameUno} instance.
         */
        public GameUno getGame() {
            return game;
        }

        /**
         * Sets the game instance for this context.
         *
         * @param game the {@link GameUno} to assign
         */
        public void setGame(GameUno game) {
            this.game = game;
        }

        /**
         * @return the player affected by this effect.
         */
        public Player getTargetPlayer() {
            return targetPlayer;
        }

        /**
         * Sets the player affected by this effect.
         *
         * @param targetPlayer the {@link Player} to assign
         */
        public void setTargetPlayer(Player targetPlayer) {
            this.targetPlayer = targetPlayer;
        }

        /**
         * @return a reference to the parent {@link Card}.
         */
        public Card getCard() {
            return Card.this;
        }

        /**
         * @return the color to be set by this effect.
         */
        public String getColor() {
            return color;
        }

        /**
         * Sets the color to be used by this effect.
         *
         * @param color the color to assign
         */
        public void setColor(String color) {
            this.color = color;
        }
    }

    /**
     * Constructs a new Card.
     *
     * @param url   the resource path to the card's image
     * @param value the card's value (number or action: SKIP, REVERSE, EAT2, EAT4, NEWCOLOR)
     * @param color the card's color (RED, BLUE, GREEN, YELLOW, or UNKNOWN for wild cards)
     */
    public Card(String url, String value, String color) {
        this.url = url;
        this.value = value;
        this.color = color;
        this.image = new Image(String.valueOf(getClass().getResource(url)));
        this.cardImageView = createCardImageView();
        this.effect = new CardEffect();
    }

    /**
     * Assigns an effect to this card.
     *
     * @param effect the card effect implementation
     */
    public void setEffect(ICardEffect effect) {
        this.effect.setCardEffect(effect);
    }

    /**
     * Retrieves the effect associated with this card, if any.
     *
     * @return the card's effect
     */
    public ICardEffect getEffect(){
        return this.effect.getCardEffect();
    }

    /**
     * Creates and configures the ImageView for the card.
     *
     * @return the configured ImageView of the card
     */
    private ImageView createCardImageView() {
        ImageView card = new ImageView(this.image);
        card.setY(16);
        card.setFitHeight(90);
        card.setFitWidth(60);
        return card;
    }

    /**
     * Returns the image view representation of this card.
     * <p>
     * If the image was marked as {@code transient} and lost during serialization,
     * it will be reloaded from the resource path.
     * </p>
     *
     * @return the image view of the card
     */
    public ImageView getCard() {
        if (cardImageView == null || image == null) {
            this.image = new Image(String.valueOf(getClass().getResource(url)));
            this.cardImageView = createCardImageView();
        }
        return cardImageView;
    }

    /**
     * Returns the {@link Image} associated with this card.
     * <p>
     * If it was previously null (e.g., after deserialization), it will be reloaded.
     * </p>
     *
     * @return the image of the card
     */
    public Image getImage() {
        if (image == null) {
            this.image = new Image(String.valueOf(getClass().getResource(url)));
        }
        return image;
    }

    /**
     * Retrieves the value or figure of the card.
     * @return the value of this card (number or action)
     */
    public String getValue() {
        return value;
    }

    /**
     * Retrieves the color of the card, returns "UNKOWN" for wild cards.
     * @return the color of this card
     */
    public String getColor() {
        return color;
    }

    /**
     * Changes the color of this card (used for wild cards).
     *
     * @param color the new color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Applies the effect of this card using the given context.
     * <p>
     * If no effect is assigned, nothing happens.
     * </p>
     *
     * @param context a {@link CardEffectContext} containing the game, target player, and/or chosen color
     */
    public void applyEffect(Card.CardEffectContext context) {
        if (effect != null) {
            effect.applyEffect(context);
        }
    }

}
