package processor

fun main() {
    while(true) {
        println("1. Add Matrices\n2. Multiply matrix by a constant\n3. Multiply matrices\n0. Exit")
        println("Your choice: ")
        var choice = readLine()!!.toInt()
        when(choice) {
            1 -> firstFunction()
            2 -> secondFunction()
            3 -> thirdFunction()
            else -> break
        }
    }
}

fun thirdFunction() {
    println("Enter size of first matrix: ")
    var firstDim = readLine()!!.split(" ").map { it.toInt() }
    var firstMatrix = buildMatrix(firstDim[0], firstDim[1])

    println("Enter size of second matrix: ")
    var secondDim = readLine()!!.split(" ").map { it.toInt() }
    var secondMatrix = buildMatrix(secondDim[0], secondDim[1])

    var result = multiplyMatrices(firstMatrix, secondMatrix)
    printMatrix(result)
}

fun extractColumnAsList(matrix: List<List<Int>>, column: Int): List<Int> {
    var flatened = mutableListOf<Int>()
    for (i in 0 until matrix.size) {
        flatened.add(matrix[i][column])
    }
    return flatened
}

fun multiplyMatrices(a: List<List<Int>>, b: List<List<Int>>): List<List<Int>> {
    var a_i = a.size
    var a_j = a.first().size
    var b_i = b.size
    var b_j = b.first().size

    if (a_j != b_i) throw Exception("Incorrect Matrix Size")

    var resultMatrix = MutableList(a_i) { MutableList(b_j) { 0 } }

    a.forEachIndexed { row, intA ->
        b[row].forEachIndexed { column, value ->
            var flatB = extractColumnAsList(b, column)
            resultMatrix[row][column] = calculateNorm(intA, flatB)
        }
    }
    return resultMatrix
}

fun calculateNorm(a: List<Int>,b: List<Int>): Int {
    var norm = 0
    if (a.size != b.size) throw Exception("Incorrect Matrix Size")

    for (i in 0 until a.size) {
        norm += a[i]*b[i]
    }
    return norm
}

fun secondFunction() {
    var aMeasures = readLine()!!.split(" ").map { it.toInt() }
    var A = buildMatrix(aMeasures.first(), aMeasures.last())
    var scalar = readLine()!!.toInt()
    var result = scalarMultiplication(scalar, A)
    printMatrix(result)
}

fun scalarMultiplication(scalar: Int, a: List<List<Int>>): List<List<Int>> {
    val rows = a.size
    val columns = a.first().size
    var resultMatrix = MutableList(rows) { MutableList(columns) { 0 } }

    for(m in 0 until rows) {
        for (n in 0 until columns) {
            resultMatrix[m][n] = scalar * a[m][n]
        }
    }
    return resultMatrix
}

fun firstFunction() {
    var aMeasures = readLine()!!.split(" ").map { it.toInt() }
    var A = buildMatrix(aMeasures.first(), aMeasures.last())

    var bMeasures = readLine()!!.split(" ").map { it.toInt() }
    var B = buildMatrix(bMeasures.first(), bMeasures.last())

    if (aMeasures.first() != bMeasures.first() || aMeasures.last() != bMeasures.last()) println("ERROR")
    else printMatrix(sumMatrix(A, B))
}

fun buildMatrix(rows: Int, column: Int): List<List<Int>> {
    val matrix = List(rows) { readLine()!!.split(" ").map { it.toInt() } }
    return matrix
}

fun printMatrix(matrix: List<List<Int>>) {
    matrix.forEach {
        println(it.joinToString(" "))
    }
    println("")
}

fun sumMatrix(a: List<List<Int>>, b: List<List<Int>>): List<List<Int>> {
    val rows = a.size
    val columns = a.first().size
    var resultMatrix = MutableList(rows) { MutableList(columns) { 0 } }

    for(m in 0 until rows) {
        for (n in 0 until columns) {
            resultMatrix[m][n] = a[m][n] + b[m][n]
        }
    }
    return resultMatrix
}
