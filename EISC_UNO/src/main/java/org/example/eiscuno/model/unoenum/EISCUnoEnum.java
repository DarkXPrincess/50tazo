package org.example.eiscuno.model.unoenum;

/**
 * Enum EISCUnoEnum
 *
 * This enum represents the various file paths for the images used in the EISC Uno game.
 */
public enum EISCUnoEnum {
    FAVICON("images/icono_poker.png"),
    UNO("images/uno.png"),
    BACKGROUND_UNO("images/background_uno.png"),
    BUTTON_UNO("images/button_uno.png"),
    RECURSO_10("cartas-poker/Recurso 10.png"),
    RECURSO_11("cartas-poker/Recurso 11.png"),
    RECURSO_12("cartas-poker/Recurso 12.png"),
    RECURSO_13("cartas-poker/Recurso 13.png"),
    RECURSO_14("cartas-poker/Recurso 14.png"),
    RECURSO_15("cartas-poker/Recurso 15.png"),
    RECURSO_16("cartas-poker/Recurso 16.png"),
    RECURSO_17("cartas-poker/Recurso 17.png"),
    RECURSO_18("cartas-poker/Recurso 18.png"),
    RECURSO_19("cartas-poker/Recurso 19.png"),
    RECURSO_20("cartas-poker/Recurso 20.png"),
    RECURSO_21("cartas-poker/Recurso 21.png"),
    RECURSO_22("cartas-poker/Recurso 22.png"),
    RECURSO_23("cartas-poker/Recurso 23.png"),
    RECURSO_24("cartas-poker/Recurso 24.png"),
    RECURSO_25("cartas-poker/Recurso 25.png"),
    RECURSO_26("cartas-poker/Recurso 26.png"),
    RECURSO_27("cartas-poker/Recurso 27.png"),
    RECURSO_28("cartas-poker/Recurso 28.png"),
    RECURSO_29("cartas-poker/Recurso 29.png"),
    RECURSO_3("cartas-poker/Recurso 3.png"),
    RECURSO_30("cartas-poker/Recurso 30.png"),
    RECURSO_31("cartas-poker/Recurso 31.png"),
    RECURSO_32("cartas-poker/Recurso 32.png"),
    RECURSO_33("cartas-poker/Recurso 33.png"),
    RECURSO_34("cartas-poker/Recurso 34.png"),
    RECURSO_35("cartas-poker/Recurso 35.png"),
    RECURSO_36("cartas-poker/Recurso 36.png"),
    RECURSO_37("cartas-poker/Recurso 37.png"),
    RECURSO_38("cartas-poker/Recurso 38.png"),
    RECURSO_39("cartas-poker/Recurso 39.png"),
    RECURSO_4("cartas-poker/Recurso 4.png"),
    RECURSO_40("cartas-poker/Recurso 40.png"),
    RECURSO_41("cartas-poker/Recurso 41.png"),
    RECURSO_42("cartas-poker/Recurso 42.png"),
    RECURSO_43("cartas-poker/Recurso 43.png"),
    RECURSO_44("cartas-poker/Recurso 44.png"),
    RECURSO_45("cartas-poker/Recurso 45.png"),
    RECURSO_46("cartas-poker/Recurso 46.png"),
    RECURSO_47("cartas-poker/Recurso 47.png"),
    RECURSO_48("cartas-poker/Recurso 48.png"),
    RECURSO_49("cartas-poker/Recurso 49.png"),
    RECURSO_5("cartas-poker/Recurso 5.png"),
    RECURSO_50("cartas-poker/Recurso 50.png"),
    RECURSO_51("cartas-poker/Recurso 51.png"),
    RECURSO_52("cartas-poker/Recurso 52.png"),
    RECURSO_53("cartas-poker/Recurso 53.png"),
    RECURSO_54("cartas-poker/Recurso 54.png"),
    RECURSO_55("cartas-poker/Recurso 55.png"),
    RECURSO_6("cartas-poker/Recurso 6.png"),
    RECURSO_7("cartas-poker/Recurso 7.png"),
    RECURSO_8("cartas-poker/Recurso 8.png"),
    RECURSO_9("cartas-poker/Recurso 9.png"),
;
   
    /**
     * The resolved path to the image file, prefixed by the base resource path.
     */
    private final String filePath;
    /**
     * Base path for all image resources in the game.
     */
    private static final String PATH = "/org/example/eiscuno/";

    /**
     * Constructor for the EISCUnoEnum enum.
     *
     * @param filePath the file path of the image relative to the base directory
     */
    EISCUnoEnum(String filePath) {
        this.filePath = PATH + filePath;
    }

    /**
     * Gets the full file path of the image.
     *
     * @return the full file path of the image
     */
    public String getFilePath() {
        return filePath;
    }
}
