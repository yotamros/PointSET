import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    
    TreeSet<Point2D> points = new TreeSet<Point2D>();

    /*
     * Construct an empty set of points
     */
    public PointSET() {
    }
    
    /*
     * Is the set empty?
     */
    public boolean isEmpty() {
        return points.isEmpty();
    }
    
    /*
     * Number of points in the set
     */
    public int size() {
        return points.size();
    }
    
    /*
     * Add the point p to the set (if it is not already in the set) 
     */
    public void insert(Point2D p) {
        points.add(p);
    }
    
    /*
     * Does the set contain the point p?
     */
    public boolean contains(Point2D p) {
        return points.contains(p);
    }
    
    /*
     * Draw all of the points to standard draw
     */
    public void draw() {
        for (Point2D point: points) {
            point.draw();
        }
    }
    
    /*
     * All points in the set that are inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> rangePoints = new ArrayList<Point2D>();
        for (Point2D p: points) {
            if (rect.contains(p)) {
                rangePoints.add(p);
            }
        }
        return rangePoints;
    }
    
    /*
     * A nearest neighbor in the set to p; null if set is empty
     */
    public Point2D nearest(Point2D p) {
        Point2D nearest = null; 
        Iterator<Point2D> iterator = points.iterator();
        while (iterator.hasNext()) {
            Point2D point = iterator.next();
            if (nearest == null || nearest.distanceTo(p) > point.distanceTo(p)) {
                if (!point.equals(p)) {
                    nearest = point;
                }
            }
        }
        return nearest;
    }
}

