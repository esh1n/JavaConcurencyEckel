package com.esh1n;

public class AngleTest {
    // Function to find the line given two points
    static Line lineFromPoints(Point one, Point two) {
        int part = two.x - one.x;
        int k = (two.y - one.y) / part;
        int b = (two.x * one.y - one.x * two.y) / part;
        System.out.println("The line passing through points one and two is: " +
                "k " + k + " b = " + b);
        return new Line(k, b);
    }

    private static double calcAngleBetweenTwoLines(Line one, Line second) {
        double tan = (second.k - one.k) / (1 + second.k * one.k);
        return Math.toDegrees(Math.atan(tan));
    }

    // Driver code
    public static void main(String[] args) {
        Point a = new Point(0, 3);
        Point b = new Point(2, 3);
        Point c = new Point(3, 1);
        Line first = lineFromPoints(a, b);
        Line second = lineFromPoints(c, b);
        double angle = calcAngleBetweenTwoLines(first, second);
        System.out.println("Angle: " + angle);
    }

    static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = -y;
        }


    }

    static class Line {
        int k, b;

        public Line(int k, int b) {
            this.k = k;
            this.b = b;
        }


    }

}
