import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private class Node {
        private Point2D point;
        private Node rightTop;
        private Node leftBottom;
        private boolean isVert;
        private RectHV rect;

        public Node(Point2D point, boolean isVert, RectHV rect) {
            this.point = point;
            this.isVert = isVert;
            this.rect = rect;
        }
    }

    public KdTree() {
    }

    Node root;
    int size = 0;
    RectHV rect = new RectHV(0, 0, 1, 1);

    private int size() {
        return size;
    }

    /*
     * Add the point p to the set (if it is not already in the set)
     */
    public void insert(Point2D point) {
        if (contains(point)) {
            return;
        }
        root = insert(root, point, rect, true);
    }

    private Node insert(Node node, Point2D point, RectHV rect, boolean isVert) {
        if (node == null) {
            size++;
            return new Node(point, isVert, rect);
        }
        if (node.point.x() <= point.x() && node.isVert
                || node.point.y() <= point.y() && !node.isVert) {
            node.rightTop = insert(node.rightTop, point,
                    makeRect(node, point, rect), !node.isVert);
        }
        if (node.point.x() > point.x() && node.isVert
                || node.point.y() > point.y() && !node.isVert) {
            node.leftBottom = insert(node.leftBottom, point,
                    makeRect(node, point, rect), !node.isVert);
        }
        return node;
    }

    private RectHV makeRect(Node node, Point2D point, RectHV rect) {
        if (root == null) {
            return rect;
        }
        if (node.isVert && node.point.x() <= point.x()) {
            return new RectHV(node.point.x(), rect.ymin(), rect.xmax(),
                    rect.ymax());
        }
        if (node.isVert && node.point.x() > point.x()) {
            return new RectHV(rect.xmin(), rect.ymin(), node.point.x(),
                    rect.ymax());
        }
        if (!node.isVert && node.point.y() <= point.y()) {
            return new RectHV(rect.xmin(), node.point.y(), rect.xmax(),
                    rect.ymax());
        }
        if (!node.isVert && node.point.y() > point.y()) {
            return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(),
                    node.point.y());
        }
        return null;
    }

    /*
     * Does the set contain the point p?
     */
    public boolean contains(Point2D p) {
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node == null) {
            return false;
        }
        if (node.point.equals(p)) {
            return true;
        }
        if (node.isVert && p.x() >= node.point.x() || !node.isVert
                && p.y() >= node.point.y()) {
            return contains(node.rightTop, p);
        } else {
            return contains(node.leftBottom, p);
        }
    }

    /*
     * Draw all of the points to standard draw
     */
    public void draw() {
        StdDraw.setPenRadius(.004);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.rectangle(.5, .5, .5, .5);
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) {
            return;
        }
        StdDraw.setPenRadius(.02);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(node.point.x(), node.point.y());
        StdDraw.setPenRadius(.004);
        if (node.isVert) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.rect.ymin(), node.point.x(),
                    node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.point.y(), node.rect.xmax(),
                    node.point.y());
        }
        draw(node.leftBottom);
        draw(node.rightTop);
    }

    /*
     * All points in the set that are inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> range = new ArrayList<Point2D>();
        range = range(rect, root, range, false);
        return range;
    }

    private List<Point2D> range(RectHV rect, Node node, List<Point2D> list,
            boolean intersects) {
        if (node == null) {
            return list;
        }
        if (rect.intersects(node.rect)) {
            if (rect.contains(node.point)) {
                list.add(node.point);
            }
            range(rect, node.leftBottom, list, true);
            range(rect, node.rightTop, list, true);
        }
        if (!rect.intersects(node.rect)) {
            if (!intersects) {
                return list;
            } else {
                range(rect, node.leftBottom, list, false);
                range(rect, node.rightTop, list, false);
            }
        }
        return list;
    }

    /*
     * A nearest neighbor in the set to p; null if set is empty
     */
    public Point2D nearest(Point2D point) {
        if (root == null) {
            return null;
        }
        Point2D candidate = root.point;
        Point2D nearest = nearest(root, point, candidate);
        return nearest;
    }

    private Point2D nearest(Node node, Point2D point, Point2D candidate) {
        if (node == null) {
            return candidate;
        }
        if (node.isVert && point.x() >= node.point.x() || !node.isVert
                && point.y() >= node.point.y()) {
            if (point.distanceTo(node.point) < point.distanceTo(candidate)) {
                candidate = node.point;
            }
            return nearest(node.rightTop, point, candidate);
        } else {
            if (point.distanceTo(node.point) < point.distanceTo(candidate)) {
                candidate = node.point;
            }
            return nearest(node.leftBottom, point, candidate);
        }
    }

    public static void main(String[] args) {
        KdTree kd = new KdTree();
        Point2D p1 = new Point2D(.5, .5);
        Point2D p2 = new Point2D(.2, .6);
        kd.insert(p1);
        kd.insert(p2);
        kd.draw();
    }
}