package processor

fun main() {
    while(true) {
        println("1. Add Matrices\n2. Multiply matrix by a constant\n3. Multiply matrices\n0. Exit")
        println("Your choice: ")
        var choice = readLine()!!.toInt()
        when(choice) {
            1 -> addMatrices()
            2 -> scalarMultiplication()
            3 -> matrixMultiplication()
            4 -> matrixTranspose()
            else -> break
        }
    }
}

fun matrixTranspose() {
    println("1. Main diagonal\n2. Side diagonal\n3. Vertical line\n4. Horizontal line")
    println("Your choice: ")
    var choice = readLine()!!.toInt()
    println("Enter matrix size: ")
    var measures = readLine()!!.split(" ").map { it.toInt() }
    var (matrix, isInt) = buildMatrix(measures.first(), measures.last())

    var transposed = when(choice) {
        1 -> mainDiagonalTranspose(matrix)
        2 -> sideDiagonalTranspose(matrix)
        3 -> verticalLineTranspose(matrix)
        4 -> horizontaLineTranspose(matrix)
        else -> return
    }
}

fun mainDiagonalTranspose(matrix: List<List<Double>>): List<List<Double>> {

}

fun sideDiagonalTranspose(matrix: List<List<Double>>): List<List<Double>> {

}

fun verticalLineTranspose(matrix: List<List<Double>>): List<List<Double>> {
    var cpyMatrix = matrix.map { it.reversed() }
    return cpyMatrix
}

fun horizontaLineTranspose(matrix: List<List<Double>>): List<List<Double>> {

}

fun matrixMultiplication() {
    println("Enter size of first matrix: ")
    var firstDim = readLine()!!.split(" ").map { it.toInt() }
    var (firstMatrix, isInt) = buildMatrix(firstDim[0], firstDim[1])

    println("Enter size of second matrix: ")
    var secondDim = readLine()!!.split(" ").map { it.toInt() }
    var (secondMatrix, isIntTwo) = buildMatrix(secondDim[0], secondDim[1])

    var result = multiplyMatrices(firstMatrix, secondMatrix)
    printMatrix(result, isIntTwo)
}

fun extractColumnAsList(matrix: List<List<Double>>, column: Int): List<Double> {
    var flatened = mutableListOf<Double>()
    for (i in 0 until matrix.size) {
        flatened.add(matrix[i][column])
    }
    return flatened
}

fun multiplyMatrices(a: List<List<Double>>, b: List<List<Double>>): List<List<Double>> {
    var a_i = a.size
    var a_j = a.first().size
    var b_i = b.size
    var b_j = b.first().size

    if (a_j != b_i) throw Exception("Incorrect Matrix Size")

    var resultMatrix = MutableList(a_i) { MutableList(b_j) { 0.0 } }

    a.forEachIndexed { row, intA ->
        b[row].forEachIndexed { column, value ->
            var flatB = extractColumnAsList(b, column)
            resultMatrix[row][column] = calculateNorm(intA, flatB)
        }
    }
    return resultMatrix
}

fun calculateNorm(a: List<Double>,b: List<Double>): Double {
    var norm = 0.0
    if (a.size != b.size) throw Exception("Incorrect Matrix Size")

    for (i in 0 until a.size) {
        norm += a[i]*b[i]
    }
    return norm
}

fun scalarMultiplication() {
    println("Enter size of matrix: ")
    var aMeasures = readLine()!!.split(" ").map { it.toInt() }
    println("Enter matrix: ")
    var (A, isInt) = buildMatrix(aMeasures.first(), aMeasures.last())
    println("Enter constant: ")
    var scalar = readLine()!!.toDouble()
    var result = scalarMultiplication(scalar, A)
    printMatrix(result, isInt)
}

fun scalarMultiplication(scalar: Double, a: List<List<Double>>): List<List<Double>> {
    val rows = a.size
    val columns = a.first().size
    var resultMatrix = MutableList(rows) { MutableList(columns) { 0.0 } }

    for(m in 0 until rows) {
        for (n in 0 until columns) {
            resultMatrix[m][n] = scalar * a[m][n]
        }
    }
    return resultMatrix
}

fun addMatrices() {
    println("Enter size of first matrix: ")
    var aMeasures = readLine()!!.split(" ").map { it.toInt() }
    println("Enter first matrix: ")
    var (A, isInt) = buildMatrix(aMeasures.first(), aMeasures.last())
    println("Enter size of second matrix: ")
    var bMeasures = readLine()!!.split(" ").map { it.toInt() }
    println("Enter size of second matrix: ")
    var (B, isIntTwo) = buildMatrix(bMeasures.first(), bMeasures.last())

    if (aMeasures.first() != bMeasures.first() || aMeasures.last() != bMeasures.last()) println("ERROR")
    else printMatrix(sumMatrix(A, B), isInt && isIntTwo)
}

fun buildMatrix(rows: Int, column: Int): Pair<List<List<Double>>, Boolean> {
    var isInt = false
    val matrix = List(rows) { readLine()!!.split(" ").map { isInt = !it.contains('.')
        it.toDouble() } }
    return matrix to isInt
}

fun printMatrix(matrix: List<List<Double>>, isInt: Boolean) {
    var cpyMatrix: MutableList<List<Number>>

    if(isInt) cpyMatrix = matrix.map { list -> list.map { number -> number.toInt() } }.toMutableList()
    else cpyMatrix = matrix.toMutableList()

    println("The result is: ")
    cpyMatrix.forEach {
        println(it.joinToString(" "))
    }
    println("")
}

fun sumMatrix(a: List<List<Double>>, b: List<List<Double>>): List<List<Double>> {
    val rows = a.size
    val columns = a.first().size
    var resultMatrix = MutableList(rows) { MutableList(columns) { 0.0 } }

    for(m in 0 until rows) {
        for (n in 0 until columns) {
            resultMatrix[m][n] = a[m][n] + b[m][n]
        }
    }
    return resultMatrix
}
