package com.github.zawataki;

import org.junit.Test;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link IntersectionPoint}
 */
public class IntersectionPointTest {

    @Test
    public void getIntersectionPoint_from_intersectedLines() {
        final Line2D line1 = new Line2D.Double(0, 0, 1, 1);
        final Line2D line2 = new Line2D.Double(1, 0, 0, 1);

        final Optional<Point2D> intersectionPoint =
                IntersectionPoint.getIntersectionPoint(line1, line2);

        assertThat("Cannot get intersection point",
                intersectionPoint.isPresent(), is(true));

        final Point2D.Double expectedIntersectionPoint =
                new Point2D.Double(0.5, 0.5);
        assertThat("Returned intersection point is NOT equal to " +
                        expectedIntersectionPoint, intersectionPoint.get(),
                is(expectedIntersectionPoint));
    }
}
