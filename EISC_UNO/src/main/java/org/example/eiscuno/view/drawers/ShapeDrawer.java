package org.example.eiscuno.view.drawers;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 * Utility class responsible for drawing composite shapes used in the UNO game interface.
 * <p>
 * This class programmatically builds complex shapes using {@link javafx.scene.shape.Shape}
 * elements grouped in a {@link Group}, which can then be added to a {@link javafx.scene.Scene}.
 * </p>
 *
 * @see Group
 * @see javafx.scene.shape.Shape
 * @see javafx.scene.Scene
 */
public class ShapeDrawer {
    /**
     * Draws a person icon as a {@link Group} composed of {@link Ellipse}, {@link Circle}, and {@link QuadCurve} shapes.
     * <p>
     * This method creates a stylized person figure that is used in the game UI to represent the human player.
     * </p>
     *
     * @return a {@link Group} containing the person icon shapes.
     */
    public Group drawPerson() {
        // Main group
        Group group = new Group();

        // First ellipse
        Ellipse ellipse1 = new Ellipse(2.0, 8.0, 3.0, 4.0);
        ellipse1.setFill(Color.web("#f9e3b4"));
        ellipse1.setStroke(Color.BLACK);

        // Second ellipse
        Ellipse ellipse2 = new Ellipse(18.0, 8.0, 3.0, 4.0);
        ellipse2.setFill(Color.web("#f9e3b4"));
        ellipse2.setStroke(Color.BLACK);

        // Third ellipse
        Ellipse ellipse3 = new Ellipse(10.0, 8.0, 9.0, 11.0);
        ellipse3.setFill(Color.web("#f9e3b4"));
        ellipse3.setStroke(Color.BLACK);

        // First circle
        Circle circle1 = new Circle(7.0, 5.0, 3.0);
        circle1.setFill(Color.WHITE);
        circle1.setStroke(Color.BLACK);

        // Second circle
        Circle circle2 = new Circle(14.0, 5.0, 3.0);
        circle2.setFill(Color.WHITE);
        circle2.setStroke(Color.BLACK);

        // QuadCurve
        QuadCurve quadCurve = new QuadCurve(-1.989790916442871, -23.560359954833984,
                0.5714216232299805, -15.080877304077148,
                5.353272438049316, -25.301319122314453);
        quadCurve.setFill(Color.TRANSPARENT); // like style="-fx-fill: transparent;"
        quadCurve.setStroke(Color.BLACK);
        quadCurve.setLayoutX(10.0);
        quadCurve.setLayoutY(36.0);

        // Add all shapes to the group
        group.getChildren().addAll(ellipse1, ellipse2, ellipse3, circle1, circle2, quadCurve);

        return group;
    }

    /**
     * Draws a robot icon as a {@link Group} composed of {@link Rectangle}, {@link Circle}, and {@link Line} shapes.
     * <p>
     * This method creates a stylized robot figure that is used in the game UI to represent the machine player
     * </p>
     *
     * @return a {@link Group} containing the robot icon shapes.
     */
    public Group drawRobot() {
        // Main group
        Group group = new Group();

        // Line
        Line line = new Line(-24.500015258789062, -10.357173919677734,
                -24.500015258789062, -16.000030517578125);
        line.setStroke(Color.web("#050d1f"));
        line.setLayoutX(35.0);
        line.setLayoutY(12.0);

        // Base rectangle
        Rectangle rectangle1 = new Rectangle(21.0, 24.0);
        rectangle1.setArcWidth(5.0);
        rectangle1.setArcHeight(5.0);
        rectangle1.setFill(Color.web("#b0bac5"));
        rectangle1.setStroke(Color.BLACK);

        // First red circle
        Circle circle1 = new Circle(6.0, 7.0, 4.0);
        circle1.setFill(Color.web("#ea3002"));
        circle1.setStroke(Color.BLACK);

        // Second red circle
        Circle circle2 = new Circle(15.0, 7.0, 4.0);
        circle2.setFill(Color.web("#ea3002"));
        circle2.setStroke(Color.BLACK);

        // Orange rectangle
        Rectangle rectangle2 = new Rectangle(2.0, 14.0, 17.0, 7.0);
        rectangle2.setArcWidth(5.0);
        rectangle2.setArcHeight(5.0);
        rectangle2.setFill(Color.web("#ffa112"));
        rectangle2.setStroke(Color.BLACK);

        // Blue circle
        Circle circle3 = new Circle(10.0, -7.0, 3.0);
        circle3.setFill(Color.web("#82d1ff"));
        circle3.setStroke(Color.BLACK);

        // Add all shapes
        group.getChildren().addAll(line, rectangle1, circle1, circle2, rectangle2, circle3);

        return group;
    }
}
