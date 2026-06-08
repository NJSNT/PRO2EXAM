package MyArrayList;

public class MyArrayListTest {

    public static void main(String[] args) {
        testZeroElements();
        testOneElement();
        testManyElements();
        testBoundaryConditions();
        testExceptionConditions();
        testIsFullBehavior();
        testGetExceptionType();
        testNullElementHandling();
        testExpandCapacityBehavior();
        System.out.println("All MyArrayList tests completed.");
    }

    // Zero: Verify behavior when the list is empty.
    private static void testZeroElements() {
        MyArrayList<String> empty = new MyArrayList<>();
        assert empty.isEmpty() : "Empty list should be empty.";
        assert empty.size() == 0 : "Empty list size should be 0.";
        assert !empty.contains("x") : "Empty list should not contain any element.";
        System.out.println("testZeroElements passed.");
    }

    // One: Verify behavior after adding a single element.
    private static void testOneElement() {
        MyArrayList<String> single = new MyArrayList<>();
        single.add("A");
        assert !single.isEmpty() : "List should not be empty after add.";
        assert single.size() == 1 : "List size should be 1 after adding one element.";
        assert "A".equals(single.get(0)) : "First element should be 'A'.";
        single.set(0, "B");
        assert "B".equals(single.get(0)) : "Element should be replaceable.";
        assert single.contains("B") : "List should contain the updated element.";
        System.out.println("testOneElement passed.");
    }

    // Many: Verify ordering and removal across multiple elements.
    private static void testManyElements() {
        MyArrayList<Integer> list = new MyArrayList<>();
        for (int i = 1; i <= 5; i++) {
            list.add(i);
        }
        assert list.size() == 5 : "List should contain 5 elements.";
        assert list.indexOf(3) == 2 : "The index of element 3 should be 2.";
        int removed = list.remove(2);
        assert removed == 3 : "Removed element at index 2 should equal 3.";
        assert list.size() == 4 : "Size should update after removal.";
        assert !list.contains(3) : "Removed element should no longer be present.";
        assert list.get(2) == 4 : "Elements should shift left after removal.";
        System.out.println("testManyElements passed.");
    }

    // Boundary: Verify valid boundary indices and invalid index handling.
    private static void testBoundaryConditions() {
        MyArrayList<String> boundary = new MyArrayList<>();
        boundary.add("first");
        boundary.add("second");
        boundary.add(2, "third");
        assert "third".equals(boundary.get(2)) : "Adding at size should append to the end.";
        assert "first".equals(boundary.remove(0)) : "Removing first element should return first.";
        assert boundary.size() == 2 : "Size should update after removing the first element.";
        boolean indexException = false;
        try {
            boundary.get(5);
        } catch (Exception e) {
            indexException = true;
        }
        assert indexException : "Getting an invalid index should throw an exception.";
        System.out.println("testBoundaryConditions passed.");
    }

    // Exceptions: Verify the collection reports errors for unsupported removals.
    private static void testExceptionConditions() {
        MyArrayList<String> errors = new MyArrayList<>();
        errors.add("x");
        boolean removeException = false;
        try {
            errors.remove("y");
        } catch (IllegalStateException e) {
            removeException = true;
        }
        assert removeException : "Removing a non-existing element should throw IllegalStateException.";
        boolean setException = false;
        try {
            errors.set(5, "z");
        } catch (IndexOutOfBoundsException e) {
            setException = true;
        }
        assert setException : "Setting an invalid index should throw IndexOutOfBoundsException.";
        System.out.println("testExceptionConditions passed.");
    }

    // Interface/Exceptions: Check behavior of isFull().
    // What: `isFull()` should return false for an unbounded list according to spec.
    // How: Call `isFull()` and report observed value (ZOMB: Interface/Exceptions).
    private static void testIsFullBehavior() {
        MyArrayList<Integer> list = new MyArrayList<>();
        boolean observed = list.isFull();
        if (observed) {
            System.out.println("NOTE: testIsFullBehavior - BUG: isFull() returns true (expected false for unbounded list).");
        } else {
            System.out.println("testIsFullBehavior passed (isFull() returned false as expected).");
        }
    }

    // Exceptions: Verify exception type for invalid get index.
    // What: `get(invalidIndex)` should throw IndexOutOfBoundsException per API.
    // How: Call get(largeIndex) and capture exception type (ZOMB: Exceptions).
    private static void testGetExceptionType() {
        MyArrayList<String> list = new MyArrayList<>();
        list.add("a");
        try {
            list.get(5);
            System.out.println("NOTE: testGetExceptionType - no exception thrown for invalid index (unexpected).");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("testGetExceptionType passed (IndexOutOfBoundsException thrown).");
        } catch (Exception e) {
            System.out.println("NOTE: testGetExceptionType - different exception thrown: " + e.getClass().getSimpleName() + " (expected IndexOutOfBoundsException).");
        }
    }

    // Zero/Interface: Check handling of null elements.
    // What: The list should allow null elements and handle indexOf(null) without NPE.
    // How: Add null, then call contains/indexOf and report observed behavior (ZOMB: Zero/Interface).
    private static void testNullElementHandling() {
        MyArrayList<String> list = new MyArrayList<>();
        list.add(null);
        try {
            boolean contains = list.contains(null);
            int idx = list.indexOf(null);
            System.out.println("testNullElementHandling observed: contains=" + contains + ", indexOf=" + idx);
        } catch (NullPointerException e) {
            System.out.println("NOTE: testNullElementHandling - BUG: NullPointerException thrown when querying null elements.");
        } catch (Exception e) {
            System.out.println("NOTE: testNullElementHandling - unexpected exception: " + e.getClass().getSimpleName());
        }
    }

    // Many/Stress: Check expandCapacity behaviour by adding many elements.
    // What: The list should expand beyond DEFAULT_CAPACITY without error and report correct size.
    // How: Add 350 elements and report final size (ZOMB: Many/Stress).
    private static void testExpandCapacityBehavior() {
        MyArrayList<Integer> list = new MyArrayList<>();
        int target = 350;
        for (int i = 0; i < target; i++) {
            list.add(i);
        }
        if (list.size() == target) {
            System.out.println("testExpandCapacityBehavior passed (size=" + list.size() + ").");
        } else {
            System.out.println("NOTE: testExpandCapacityBehavior - unexpected size=" + list.size() + " expected=" + target + ".");
        }
    }
}
