package day15

import readToList
import java.util.*

private val input = readToList("day15.txt")

fun main() {
    val weightMatrix = input.map { line ->
        line.chars().map { char ->
            Character.getNumericValue(char)
        }.toArray()
    }.toTypedArray()


    val distance = dijkstra(weightMatrix)
    println(distance[weightMatrix.size - 1][weightMatrix[0].size - 1])
}

fun dijkstra(graph: Array<IntArray>): Array<IntArray> {
    val dist = Array(graph.size) { _ -> IntArray(graph[0].size) { Int.MAX_VALUE } }
    dist[0][0] = 0

    val vis = Array(graph.size) { _ -> BooleanArray(graph[0].size) }
    val pq = PriorityQueue(Comparator.comparing(NodeDistance::dist))
    pq.add(NodeDistance(Node(0, 0, 0), 0))

    while (pq.isNotEmpty()) {
        val (index, minValue) = pq.poll()
        vis[index.i][index.j] = true
        if (dist[index.i][index.j] < minValue) continue

        for (edge in adjacent(graph, index.i, index.j)) {
            if (vis[edge.i][edge.j]) continue;

            val newDist = dist[index.i][index.j] + edge.cost
            if (newDist < dist[edge.i][edge.j]) {
                dist[edge.i][edge.j] = newDist
                pq.add(NodeDistance(edge, newDist))
            }
        }
    }

    return dist
}

private data class NodeDistance(val node: Node, val dist: Int)
private data class Node(val i: Int, val j: Int, val cost: Int)

private fun adjacent(matrix: Array<IntArray>, rowIndex: Int, columnIndex: Int): List<Node> {
    return listOfNotNull(
        get(matrix, rowIndex + 1, columnIndex),
        get(matrix, rowIndex - 1, columnIndex),
        get(matrix, rowIndex, columnIndex + 1),
        get(matrix, rowIndex, columnIndex - 1),
    )
}

private fun get(matrix: Array<IntArray>, rowIndex: Int, columnIndex: Int): Node? {
    if (rowIndex >= 0 && rowIndex < matrix.size) {
        val row = matrix[rowIndex]
        if (columnIndex >= 0 && columnIndex < row.size) {
            return Node(rowIndex, columnIndex, row[columnIndex])
        }
    }
    return null
}
