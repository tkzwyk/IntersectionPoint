package com.github.zawataki;

import org.junit.Test;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link IntersectionPoint}
 */
public class IntersectionPointTest {

    final private Comparator<Point2D> point2DComparator =
            Comparator.comparing(Point2D::getX).thenComparing(Point2D::getY);

    @Test
    public void getIntersectionPointFromIntersectedLines() {
        final Line2D line1 = new Line2D.Double(0, 0, 1, 1);
        final Line2D line2 = new Line2D.Double(1, 0, 0, 1);

        final Optional<Point2D> intersectionPointWhenExcludesEndpoint =
                IntersectionPoint.getIntersectionPoint(line1, line2);

        assertThat("Cannot get intersection point when excludes endpoint",
                intersectionPointWhenExcludesEndpoint.isPresent(), is(true));

        final Point2D.Double expectedIntersectionPoint =
                new Point2D.Double(0.5, 0.5);
        assertThat("Returned intersection point when excludes endpoint, " +
                        "is NOT equal to " + expectedIntersectionPoint,
                intersectionPointWhenExcludesEndpoint.get(),
                is(expectedIntersectionPoint));


        final boolean includesEndpoint = true;
        final Optional<Point2D> intersectionPointWhenIncludesEndpoint =
                IntersectionPoint.getIntersectionPoint(line1, line2,
                        includesEndpoint);

        assertThat("Cannot get intersection point when includes endpoint",
                intersectionPointWhenIncludesEndpoint.isPresent(), is(true));

        assertThat("Returned intersection point when includes endpoint, " +
                        "is NOT equal to " + expectedIntersectionPoint,
                intersectionPointWhenIncludesEndpoint.get(),
                is(expectedIntersectionPoint));
    }

    @Test
    public void getIntersectionPointFromNotIntersectedLines() {
        final Line2D line1 = new Line2D.Double(0, 0, 1, 1);
        final Line2D line2 = new Line2D.Double(1, 1.0001, 2, 2);

        final Optional<Point2D> intersectionPointWhenExcludesEndpoint =
                IntersectionPoint.getIntersectionPoint(line1, line2);

        assertThat("Got intersection point when excludes endpoint",
                intersectionPointWhenExcludesEndpoint.isPresent(), is(false));


        final boolean includesEndpoint = true;
        final Optional<Point2D> intersectionPointWhenIncludesEndpoint =
                IntersectionPoint.getIntersectionPoint(line1, line2,
                        includesEndpoint);

        assertThat("Got intersection point when includes endpoint",
                intersectionPointWhenIncludesEndpoint.isPresent(), is(false));
    }

    @Test
    public void getIntersectionPointFromLinesIntersectedAtEndpoint() {
        final Line2D line1 = new Line2D.Double(0, 0, 1, 1);
        final Line2D line2 = new Line2D.Double(2, 0, 0, 2);

        final Optional<Point2D> intersectionPointWhenExcludesEndpoint =
                IntersectionPoint.getIntersectionPoint(line1, line2);

        assertThat("Got intersection point when excludes endpoint",
                intersectionPointWhenExcludesEndpoint.isPresent(), is(false));


        final boolean includesEndpoint = true;
        final Optional<Point2D> intersectionPointWhenIncludesEndpoint =
                IntersectionPoint.getIntersectionPoint(line1, line2,
                        includesEndpoint);

        assertThat("Cannot get intersection point when includes endpoint",
                intersectionPointWhenIncludesEndpoint.isPresent(), is(true));

        final Point2D.Double expectedIntersectionPoint =
                new Point2D.Double(1, 1);
        assertThat("Returned intersection point when includes endpoint," +
                        " is NOT equal to " + expectedIntersectionPoint,
                intersectionPointWhenIncludesEndpoint.get(),
                is(expectedIntersectionPoint));
    }

    @Test
    public void pointIsOnLine() {
        final Line2D line = new Line2D.Double(0, 0, 1, 1);
        final Point2D point = new Point2D.Double(0.2, 0.2);
        final boolean includesEndpoint = true;

        final boolean pointIsOnLineWhenIncludesEndpoint =
                IntersectionPoint.pointIsOnLine(point, line, includesEndpoint);

        assertThat(point + " is NOT located on " + toStringFrom(line) +
                        " when includes endpoint", pointIsOnLineWhenIncludesEndpoint,
                is(true));


        final boolean pointIsOnLineWhenExcludesEndpoint =
                IntersectionPoint.pointIsOnLine(point, line, !includesEndpoint);

        assertThat(point + " is NOT located on " + toStringFrom(line) +
                        " when excludes endpoint", pointIsOnLineWhenExcludesEndpoint,
                is(true));
    }

    @Test
    public void pointIsOnLineEndpoint() {
        final Line2D line = new Line2D.Double(0, 0, 1, 1);
        final Point2D point = new Point2D.Double(1, 1);
        final boolean includesEndpoint = true;

        final boolean pointIsOnLineWhenIncludesEndpoint =
                IntersectionPoint.pointIsOnLine(point, line, includesEndpoint);

        assertThat(point + " is NOT located on " + toStringFrom(line) +
                        " when includes endpoint", pointIsOnLineWhenIncludesEndpoint,
                is(true));


        final boolean pointIsOnLineWhenExcludesEndpoint =
                IntersectionPoint.pointIsOnLine(point, line, !includesEndpoint);

        assertThat(point + " is located on " + toStringFrom(line) +
                        " when excludes endpoint", pointIsOnLineWhenExcludesEndpoint,
                is(false));
    }

    @Test
    public void pointIsNotOnLine() {
        final Line2D line = new Line2D.Double(0, 0, 1, 1);
        final Point2D point = new Point2D.Double(1, 1.00001);
        final boolean includesEndpoint = true;

        final boolean pointIsOnLineWhenIncludesEndpoint =
                IntersectionPoint.pointIsOnLine(point, line, includesEndpoint);

        assertThat(point + " is located on " + toStringFrom(line) +
                        " when includes endpoint", pointIsOnLineWhenIncludesEndpoint,
                is(false));


        final boolean pointIsOnLineWhenExcludesEndpoint =
                IntersectionPoint.pointIsOnLine(point, line, !includesEndpoint);

        assertThat(point + " is located on " + toStringFrom(line) +
                        " when excludes endpoint", pointIsOnLineWhenExcludesEndpoint,
                is(false));
    }

    private String toStringFrom(Line2D line) {
        return String.format("Line2D[ [%s, %s] -> [%s, %s] ]", line.getX1(),
                line.getY1(), line.getX2(), line.getY2());
    }

    @Test
    public void getIntersectionPointsFromIntersectedRectangleAndLine() {
        final Rectangle2D rectangle = new Rectangle2D.Double(0, 1, 1, 1);
        final Line2D line = new Line2D.Double(-1, -1, 2, 2);
        final boolean includesEndpoint = true;

        final List<Point2D> pointsWhenIncludesEndpoint =
                IntersectionPoint.getIntersectionPoints(rectangle, line,
                        includesEndpoint);

        final List<Point2D> expectedPoints =
                Stream.of(new Point2D.Double(0, 0), new Point2D.Double(1, 1))
                        .sorted(point2DComparator)
                        .collect(Collectors.toList());
        assertThat("Intersection points are NOT equal to " + expectedPoints +
                " when includes endpoint", pointsWhenIncludesEndpoint.stream()
                .sorted(point2DComparator)
                .collect(Collectors.toList()), is(expectedPoints));

        final List<Point2D> pointsWhenExcludesEndpoint =
                IntersectionPoint.getIntersectionPoints(rectangle, line,
                        !includesEndpoint);
        assertThat("Intersection points are NOT equal to " + expectedPoints +
                " when excludes endpoint", pointsWhenExcludesEndpoint.stream()
                .sorted(point2DComparator)
                .collect(Collectors.toList()), is(expectedPoints));
    }

    @Test
    public void getIntersectionPointsFromNotIntersectedRectangleAndLine() {
        final Rectangle2D rectangle = new Rectangle2D.Double(0, 1, 1, 1);
        final Line2D line = new Line2D.Double(1, 1.00001, 2, 2);
        final boolean includesEndpoint = true;

        final List<Point2D> pointsWhenIncludesEndpoint =
                IntersectionPoint.getIntersectionPoints(rectangle, line,
                        includesEndpoint);

        assertThat("Intersection points NOT found when includes endpoint",
                pointsWhenIncludesEndpoint.isEmpty(), is(true));

        final List<Point2D> pointsWhenExcludesEndpoint =
                IntersectionPoint.getIntersectionPoints(rectangle, line,
                        !includesEndpoint);
        assertThat("Intersection points NOT found when excludes endpoint",
                pointsWhenExcludesEndpoint.isEmpty(), is(true));
    }

    @Test
    public void getIntersectionPointsFromRectangleAndLineIntersectedAtEndpoint() {
        final Rectangle2D rectangle = new Rectangle2D.Double(0, 1, 1, 1);
        final Line2D line = new Line2D.Double(1, 1, 2, 0);
        final boolean includesEndpoint = true;

        final List<Point2D> pointsWhenIncludesEndpoint =
                IntersectionPoint.getIntersectionPoints(rectangle, line,
                        includesEndpoint);

        final List<Point2D> expectedPoints =
                Arrays.asList(new Point2D.Double(1, 1));
        assertThat("Intersection points are NOT equal to " + expectedPoints +
                " when includes endpoint", pointsWhenIncludesEndpoint.stream()
                .sorted(point2DComparator)
                .collect(Collectors.toList()), is(expectedPoints));

        final List<Point2D> pointsWhenExcludesEndpoint =
                IntersectionPoint.getIntersectionPoints(rectangle, line,
                        !includesEndpoint);
        assertThat("Intersection points NOT found when excludes endpoint",
                pointsWhenExcludesEndpoint.isEmpty(), is(true));
    }

    @Test
    public void getIntersectionPointsFromRectangleAndLineIntersectedAtVertex() {
        final Rectangle2D rectangle = new Rectangle2D.Double(0, 1, 1, 1);
        final Line2D line = new Line2D.Double(0, 2, 2, 0);
        final boolean includesEndpoint = true;

        final List<Point2D> pointsWhenIncludesEndpoint =
                IntersectionPoint.getIntersectionPoints(rectangle, line,
                        includesEndpoint);

        final List<Point2D> expectedPoints =
                Arrays.asList(new Point2D.Double(1, 1));
        assertThat("Intersection points are NOT equal to " + expectedPoints +
                " when includes endpoint", pointsWhenIncludesEndpoint.stream()
                .sorted(point2DComparator)
                .collect(Collectors.toList()), is(expectedPoints));

        final List<Point2D> pointsWhenExcludesEndpoint =
                IntersectionPoint.getIntersectionPoints(rectangle, line,
                        !includesEndpoint);
        assertThat("Intersection points NOT found when excludes endpoint",
                pointsWhenExcludesEndpoint.isEmpty(), is(true));
    }

    @Test
    public void getIntersectionPointsFromRectangleAndLineIntersectedAtBothEndpoint() {
        final Rectangle2D rectangle = new Rectangle2D.Double(0, 1, 1, 1);
        final Line2D line = new Line2D.Double(0.0, 0.5, 1, 0.5);
        final boolean includesEndpoint = true;

        final List<Point2D> pointsWhenIncludesEndpoint =
                IntersectionPoint.getIntersectionPoints(rectangle, line,
                        includesEndpoint);

        final List<Point2D> expectedPoints =
                Stream.of(line.getP1(), line.getP2())
                        .sorted(point2DComparator)
                        .collect(Collectors.toList());
        assertThat("Intersection points are NOT equal to " + expectedPoints +
                " when includes endpoint", pointsWhenIncludesEndpoint.stream()
                .sorted(point2DComparator)
                .collect(Collectors.toList()), is(expectedPoints));

        final List<Point2D> pointsWhenExcludesEndpoint =
                IntersectionPoint.getIntersectionPoints(rectangle, line,
                        !includesEndpoint);
        assertThat("Intersection points are NOT equal to " + expectedPoints +
                " when excludes endpoint", pointsWhenExcludesEndpoint.stream()
                .sorted(point2DComparator)
                .collect(Collectors.toList()), is(expectedPoints));
    }
}
