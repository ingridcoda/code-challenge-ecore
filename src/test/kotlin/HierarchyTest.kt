import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HierarchyTest {
    class FilterTest {
        @Test
        fun testFilter() {
            val unfiltered: Hierarchy = ArrayBasedHierarchy(
                myNodeIds = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
                myDepths = intArrayOf(0, 1, 2, 3, 1, 0, 1, 0, 1, 1, 2)
            )
            val filteredActual: Hierarchy = unfiltered.filter { nodeId -> nodeId % 3 != 0 }
            val filteredExpected: Hierarchy = ArrayBasedHierarchy(
                myNodeIds = intArrayOf(1, 2, 5, 8, 10, 11),
                myDepths = intArrayOf(0, 1, 1, 0, 1, 2)
            )
            Assertions.assertEquals(filteredExpected.formatString(), filteredActual.formatString())
        }

        @Test
        fun testFilterWhenHierarchyIsEmpty() {
            val unfiltered: Hierarchy = ArrayBasedHierarchy(
                myNodeIds = intArrayOf(),
                myDepths = intArrayOf()
            )
            val filteredActual: Hierarchy = unfiltered.filter { nodeId -> nodeId % 3 != 0 }
            val filteredExpected: Hierarchy = ArrayBasedHierarchy(
                myNodeIds = intArrayOf(),
                myDepths = intArrayOf()
            )
            Assertions.assertEquals(filteredExpected.formatString(), filteredActual.formatString())
        }

        @Test
        fun testFilterWhenHierarchyHasNoSiblings() {
            val unfiltered: Hierarchy = ArrayBasedHierarchy(
                myNodeIds = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
                myDepths = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            )
            val filteredActual: Hierarchy = unfiltered.filter { nodeId -> nodeId % 3 != 0 }
            val filteredExpected: Hierarchy = ArrayBasedHierarchy(
                myNodeIds = intArrayOf(1, 2),
                myDepths = intArrayOf(0, 1)
            )
            Assertions.assertEquals(filteredExpected.formatString(), filteredActual.formatString())
        }

        @Test
        fun testFilterWhenHierarchyHasOnlySiblings() {
            val unfiltered: Hierarchy = ArrayBasedHierarchy(
                myNodeIds = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
                myDepths = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            )
            val filteredActual: Hierarchy = unfiltered.filter { nodeId -> nodeId % 3 != 0 }
            val filteredExpected: Hierarchy = ArrayBasedHierarchy(
                myNodeIds = intArrayOf(1, 2, 4, 5, 7, 8, 10, 11),
                myDepths = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
            )
            Assertions.assertEquals(filteredExpected.formatString(), filteredActual.formatString())
        }

    }
}