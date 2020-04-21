package edu.brown.cs.ilayzer.stars;

import edu.brown.cs.ilayzer.distance.Axis;
import edu.brown.cs.ilayzer.distance.Euclidean;
import edu.brown.cs.ilayzer.comparators.DistanceComparator;
import edu.brown.cs.ilayzer.kdtree.KDNode;
import edu.brown.cs.ilayzer.kdtree.KDTree;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class KDTreeTest {

    private KDTree<Cartesian> tree;
    private static Euclidean<Cartesian> euclidean;
    private static Axis<Cartesian> axis;

    /**
     * Sets up the kd tree.
     */
    @Before
    public void setUp() {
        // create kd tree
        tree = new KDTree<>(makeCartesianArray1(), 3);
        euclidean = new Euclidean<>();
        axis = new Axis<>(0);
    }

    /**
     * Resets the kd tree.
     */
    @After
    public void tearDown() {
        tree = null;
    }

    /**
     ** Tests generating a tree.
     */
    @Test
    public void testGenerateTree() {
        setUp();
        // coordinates array
        Cartesian[] coords = new Cartesian[4];
        coords[0] = (new Cartesian(new double[]{-1, 4, 5}));
        coords[1] = (new Cartesian(new double[]{0, 0, 0}));
        coords[2] = (new Cartesian(new double[]{1, 2, 1}));
        coords[3] = (new Cartesian(new double[]{2, -5, 9}));
        // actual
        KDTree<Cartesian> tree = new KDTree<>(coords, 3);
        KDNode<Cartesian> generated = tree.getRoot();
        // expected
        KDNode<Cartesian> handMade = makeByHand1();
        // compare string representations
        assertEquals(handMade.toString(), generated.toString());
        // generating an empty tree
        KDTree<Cartesian> tree2 = new KDTree<>(new Cartesian[0], 2);
        assertNull(tree2.getRoot());

        tearDown();
    }

    /**
     * Tests the k nearest neighbors method.
     */
    @Test
    public void testNearestNeighbors() {
        setUp();
        double[] target = new double[]{0, 0, 0};
        Cartesian wrappedTarget = new Cartesian(target);
        int k = 4;
        // use kd tree to find k nearest neighbors
        List<Cartesian> nearestNeighbors1 =
            tree.nearestNeighbors(wrappedTarget, k);
        // sort list and take k nearest neighbors (naive)
        List<Cartesian> naive1 = makeCartesianList1();
        naive1.sort(new DistanceComparator<>(wrappedTarget, euclidean));
        naive1 = naive1.subList(0, 4);
        // compare
        for (int i = 0; i < naive1.size(); i++) {
            doubleArrayEquals(naive1.get(i).getCoordinates(),
                nearestNeighbors1.get(i).getCoordinates());
        }
        // 0 neighbors
        List<Cartesian> neighbors = tree.nearestNeighbors(wrappedTarget, 0);
        assertArrayEquals(new Cartesian[0], neighbors.toArray());
        tearDown();
    }

    /**
     * Method that asserts the equality of double arrays.
     * @param a1 a double array
     * @param a2 another double array
     */
    private static void doubleArrayEquals(double[] a1, double[] a2) {
        assertEquals(a1.length, a2.length);
        for (int i = 0; i < a1.length; i++) {
            assertEquals(a1[i], a2[i], 0.00005);
        }
    }

    /**
     * Method that asserts the quality of two lists of coordinates
     * @param l1 coordinate list
     * @param l2 second coordinate list
     */
    private static void coordinatesListEquals(List<Cartesian> l1, List<Cartesian> l2) {
        assertEquals(l1.size(), l2.size());
        // compare
        for (int i = 0; i < l1.size(); i++) {
            doubleArrayEquals(l1.get(i).getCoordinates(),
                l2.get(i).getCoordinates());
        }
    }

    /**
     * Provides a naive implementation of radius search.
     * @param points a list of points
     * @param target a target point
     * @param radius the radius to search around the target
     * @return the list of points within a radius from a point (naive)
     */
    private static List<Cartesian> naiveRadiusSearch(List<Cartesian> points,
                                                       double[] target, double radius) {
        Cartesian targetWrapped = new Cartesian(target);
        List<Cartesian> withinRadius = new ArrayList<>();
        for (Cartesian c: points) {
            if (euclidean.distance(c, targetWrapped) <= radius) {
                withinRadius.add(c);
            }
        }
        withinRadius.sort(new DistanceComparator<>(targetWrapped, euclidean));
        return withinRadius;
    }

    /**
     * Tests the radius search method.
     */
    @Test
    public void testRadiusSearch() {
        setUp();
        double[] target = new double[]{0, 0, 0};
        Cartesian targetWrapped = new Cartesian(target);
        double radius = 5;
        // solve problem naively
        List<Cartesian> rawCartesian = makeCartesianList1();
        List<Cartesian> withinRadiusNaive = naiveRadiusSearch(rawCartesian,
            target, radius);
        // solve problem with kd tree
        List<Cartesian> withinRadiusKd = tree.radiusSearch(targetWrapped, radius);
        // check equality
        coordinatesListEquals(withinRadiusNaive, withinRadiusKd);
        // no stars within radius
        coordinatesListEquals(new ArrayList<>(),
            tree.radiusSearch(new Cartesian(new double[] {100, 0, 0}), 5));
        tearDown();
    }

    /**
     * Tests the euclidean distance method.
     */
    @Test
    public void testEuclideanDistance() {
        double delta = 0.00005;
        Cartesian point1 = new Cartesian(new double[] {0, 0, 0});
        Cartesian point2 = new Cartesian(new double[] {3, -4, 3});
        Cartesian point3 = new Cartesian(new double[] {-5, 0, 0});
        Cartesian point4 = new Cartesian(new double[] {1});
        Cartesian point5 = new Cartesian(new double[] {-9});

        assertEquals(euclidean.distance(point1, point2), 5.83095, delta);
        assertEquals(euclidean.distance(point1, point3), 5, delta);
        assertEquals(euclidean.distance(point3, point3), 0, delta);
        assertEquals(euclidean.distance(point4, point5), 10, delta);
    }

    /**
     * Tests the axis distance method.
     */
    @Test
    public void testAxisDistance() {
        double delta = 0.00005;
        Cartesian point1 = new Cartesian(new double[] {0, 0, 0});
        Cartesian point2 = new Cartesian(new double[] {3, -4, 3});
        Cartesian point3 = new Cartesian(new double[] {-5, 0, 0});
        Cartesian point4 = new Cartesian(new double[] {1});
        Cartesian point5 = new Cartesian(new double[] {-9});

        axis.setAxis(0);
        assertEquals(3, axis.distance(point1, point2), delta);
        axis.setAxis(1);
        assertEquals(4, axis.distance(point1, point2), delta);
        axis.setAxis(0);
        assertEquals(8, axis.distance(point2, point3), delta);
        axis.setAxis(2);
        assertEquals(0, axis.distance(point1, point3), delta);
        axis.setAxis(0);
        assertEquals(10, axis.distance(point4, point5), delta);
    }

    /**
     * Makes a coordinates array
     * @return a coordinates array
     */
    private Cartesian[] makeCartesianArray1() {
        Cartesian c1 = new Cartesian(new double[]{1, 0, 0});
        Cartesian c2 = new Cartesian(new double[]{2, 0, 0});
        Cartesian c3 = new Cartesian(new double[]{3, 0, 0});
        Cartesian c4 = new Cartesian(new double[]{0, 4, -4});
        Cartesian c5 = new Cartesian(new double[]{1, 3, 0});
        return new Cartesian[] {c1, c2, c3, c4, c5};
    }


    /**
     * Makes a coordinates list same as coordinates array.
     * @return a coordinates list.
     */
    private List<Cartesian> makeCartesianList1() {
        return Arrays.asList(makeCartesianArray1());
    }

    /**
     * Makes a tree by hand.
     * @return a kd tree
     */
    private KDNode<Cartesian> makeByHand1() {
        // the nodes
        KDNode<Cartesian> node1 = new KDNode<>(new Cartesian(new double[]{-1, 4, 5}));
        KDNode<Cartesian> node2 = new KDNode<>(new Cartesian(new double[]{0, 0, 0}));
        KDNode<Cartesian> node3 = new KDNode<>(new Cartesian(new double[]{1, 2, 1}));
        KDNode<Cartesian> node4 = new KDNode<>(new Cartesian(new double[]{2, -5, 9}));
        // build connections
        node3.setLeft(node1);
        node3.setRight(node4);
        node1.setLeft(node2);

        return node3;
        /*
        diagram:
                            Node3
                        Node1    Node4
                    Node2
         */
    }
}
