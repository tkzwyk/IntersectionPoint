package com.github.zawataki;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

/**
 * @see <a href="https://stackoverflow.com/a/15594751/9246253">java - Line
 * crosses Rectangle - how to find the cross points? - Stack Overflow</a>
 */
public class IntersectPoint {

    static public Optional<Point2D> getIntersectionPoint(Line2D l1, Line2D l2) {

        // Line AB represented as a1x + b1y = c1
        final double a1 = l1.getP2().getY() - l1.getP1().getY();
        final double b1 = l1.getP1().getX() - l1.getP2().getX();

        // Line CD represented as a2x + b2y = c2
        final double a2 = l2.getP2().getY() - l2.getP1().getY();
        final double b2 = l2.getP1().getX() - l2.getP2().getX();

        final double determinant = a1 * b2 - a2 * b1;
        if (determinant == 0) {
            return Optional.empty();
        }

        final double c1 = a1 * (l1.getP1().getX()) + b1 * (l1.getP1().getY());
        final double c2 = a2 * (l2.getP1().getX()) + b2 * (l2.getP1().getY());

        final double x = (b2 * c1 - b1 * c2) / determinant + 0.0;
        final double y = (a1 * c2 - a2 * c1) / determinant + 0.0;

        final Point2D crossPoint =
                new Point2D.Double(doubleToBigDecimal(x).doubleValue(),
                        doubleToBigDecimal(y).doubleValue());

        if (!pointIsOnLineExcludesEndpoint(crossPoint, l1) ||
                !pointIsOnLineExcludesEndpoint(crossPoint, l2)) {
            return Optional.empty();
        }

        return Optional.of(crossPoint);
    }

    static private boolean pointIsOnLineExcludesEndpoint(Point2D point,
            Line2D line) {
        return pointIsOnLine(point, line, false);
    }

    static private boolean pointIsOnLine(Point2D point, Line2D line,
            boolean includesEndpoint) {

        if (!includesEndpoint &&
                (point.equals(line.getP1()) || point.equals(line.getP2()))) {
            return false;
        }

        final BigDecimal distanceBetweenPointAndLineP1 =
                doubleToBigDecimal(point.distance(line.getP1()));
        final BigDecimal distanceBetweenPointAndLineP2 =
                doubleToBigDecimal(point.distance(line.getP2()));
        final BigDecimal distanceBetweenPointAndLineEndpoint =
                distanceBetweenPointAndLineP1.add(distanceBetweenPointAndLineP2)
                        .stripTrailingZeros();
        final BigDecimal lineLength =
                doubleToBigDecimal(line.getP1().distance(line.getP2()));

        return distanceBetweenPointAndLineEndpoint.equals(lineLength) ||
                (distanceBetweenPointAndLineEndpoint.subtract(lineLength)
                        .abs()
                        .doubleValue() == 0.00001);
    }

    static private BigDecimal doubleToBigDecimal(double value) {
        return BigDecimal.valueOf(value)
                .setScale(5, RoundingMode.DOWN)
                .stripTrailingZeros();
    }
}
