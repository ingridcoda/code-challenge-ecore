// The task:
// 1. Read and understand the Hierarchy data structure described in this file.
// 2. Implement filter() function.
// 3. Implement more test cases.
//
// The task should take 30-90 minutes.
//
// When assessing the submission, we will pay attention to:
// - correctness, efficiency, and clarity of the code;
// - the test cases.

/**
 * A `Hierarchy` stores an arbitrary _forest_ (an ordered collection of ordered trees)
 * as an array of node IDs in the order of DFS traversal, combined with a parallel array of node depths.
 *
 * Parent-child relationships are identified by the position in the array and the associated depth.
 * Each tree root has depth 0, its children have depth 1 and follow it in the array, their children have depth 2 and follow them, etc.
 *
 * Example:
 * ```
 * nodeIds: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
 * depths:  0, 1, 2, 3, 1, 0, 1, 0, 1, 1, 2
 * ```
 *
 * the forest can be visualized as follows:
 * ```
 * 1
 * - 2
 * - - 3
 * - - - 4
 * - 5
 * 6
 * - 7
 * 8
 * - 9
 * - 10
 * - - 11
 *```
 * 1 is a parent of 2 and 5, 2 is a parent of 3, etc. Note that depth is equal to the number of hyphens for each node.
 *
 * Invariants on the depths array:
 *  * Depth of the first element is 0.
 *  * If the depth of a node is `D`, the depth of the next node in the array can be:
 *      * `D + 1` if the next node is a child of this node;
 *      * `D` if the next node is a sibling of this node;
 *      * `d < D` - in this case the next node is not related to this node.
 */
interface Hierarchy {
    /** The number of nodes in the hierarchy. */
    val size: Int

    /**
     * Returns the unique ID of the node identified by the hierarchy index. The depth for this node will be `depth(index)`.
     * @param index must be non-negative and less than [size]
     * */
    fun nodeId(index: Int): Int

    /**
     * Returns the depth of the node identified by the hierarchy index. The unique ID for this node will be `nodeId(index)`.
     * @param index must be non-negative and less than [size]
     * */
    fun depth(index: Int): Int

    fun formatString(): String {
        return (0 until size).joinToString(
            separator = ", ",
            prefix = "[",
            postfix = "]"
        ) { i -> "${nodeId(i)}:${depth(i)}" }
    }
}

/**
 * A node is present in the filtered hierarchy iff its node ID passes the predicate and all of its ancestors pass it as well.
 */
fun Hierarchy.filter(nodeIdPredicate: (Int) -> Boolean): Hierarchy {
    var nodeIds = emptyArray<Int>()
    var nodeDepths = emptyArray<Int>()

    for (i in 0 until size) {
        if (checkPredicate(
                predicate = nodeIdPredicate,
                nodeId = nodeId(i),
                nodeDepth = depth(i),
                hierarchy = this as ArrayBasedHierarchy
            )
        ) {
            nodeIds += arrayOf(nodeId(i))
            nodeDepths += arrayOf(depth(i))
        }
    }
    return ArrayBasedHierarchy(myNodeIds = nodeIds.toIntArray(), myDepths = nodeDepths.toIntArray())
}

private fun checkPredicate(
    predicate: (Int) -> Boolean,
    nodeId: Int,
    nodeDepth: Int,
    hierarchy: ArrayBasedHierarchy
): Boolean {
    return if (nodeDepth == 0) {
        predicate(nodeId)
    } else {
        val orderedAncestorsPair = getOrderedAncestors(hierarchy, nodeId)
        val ancestorsIds = orderedAncestorsPair.first
        val ancestorsDepths = orderedAncestorsPair.second
        val ancestors = ArrayBasedHierarchy(myNodeIds = ancestorsIds, myDepths = ancestorsDepths)

        predicate(nodeId) && checkPredicate(predicate, ancestorsIds.first(), ancestorsDepths.first(), ancestors)
    }
}

private fun getOrderedAncestors(hierarchy: ArrayBasedHierarchy, referredNodeId: Int): Pair<IntArray, IntArray> {
    // ordering ancestors descending
    val orderedAncestors = getAncestorsIds(hierarchy, referredNodeId).map {
        Pair(it, hierarchy.myDepths()[hierarchy.myNodeIds().indexOf(it)])
    }.sortedByDescending {
        it.second // where it.second is the depth
    }.sortedByDescending {
        it.first // where it.first is the id
    }

    val ancestorsIds = orderedAncestors.map { it.first }.toIntArray() // where it.first is the id
    val ancestorsDepths = orderedAncestors.map { it.second }.toIntArray() // where it.second is the depth

    return Pair(ancestorsIds, ancestorsDepths)
}

private fun getAncestorsIds(hierarchy: ArrayBasedHierarchy, referredNodeId: Int): IntArray {
    val ids = hierarchy.myNodeIds()
    val depths = hierarchy.myDepths()

    return ids.toList().filter {
        (it < referredNodeId) // referredNodeId > it nodeId
                && depths[ids.indexOf(referredNodeId)] > depths[ids.indexOf(it)] // referredNodeId depth > it depth
    }.toIntArray()
}

class ArrayBasedHierarchy(
    private val myNodeIds: IntArray,
    private val myDepths: IntArray,
) : Hierarchy {
    override val size: Int = myDepths.size

    override fun nodeId(index: Int): Int = myNodeIds[index]

    override fun depth(index: Int): Int = myDepths[index]

    fun myNodeIds() = myNodeIds // created to be able to get and iterate by these values in an easy way
    fun myDepths() = myDepths // created to be able to get and iterate by these values in an easy way
}