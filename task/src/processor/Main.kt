package processor

fun main() {
    while(true) {
        println("1. Add Matrices\n2. Multiply matrix by a constant\n3. Multiply matrices\n4. Transpose matrix\n0. Exit")
        println("Your choice: ")
        when(readLine()!!.toInt()) {
            1 -> addMatrices()
            2 -> scalarMultiplication()
            3 -> matrixMultiplication()
            4 -> matrixTranspose()
            else -> break
        }
    }
}

fun matrixTranspose() {
    println("\n1. Main diagonal\n2. Side diagonal\n3. Vertical line\n4. Horizontal line")
    println("Your choice: ")
    val choice = readLine()!!.toInt()
    println("Enter matrix size: ")
    val measures = readLine()!!.split(" ").map { it.toInt() }
    println("Enter matrix: ")
    val (matrix, isInt) = buildMatrix(measures.first(), measures.last())

    val transposed = when(choice) {
        1 -> mainDiagonalTranspose(matrix)
        2 -> sideDiagonalTranspose(matrix)
        3 -> verticalLineTranspose(matrix)
        4 -> horizontaLineTranspose(matrix)
        else -> return
    }

    printMatrix(transposed, isInt)
}

fun mainDiagonalTranspose(matrix: List<List<Double>>): List<List<Double>> {
    var i = -1 //Initialize at -1 because it is increased before calling the function
    return List(matrix.size) {
        i++
        extractColumnAsList(matrix, i)
    }
}

fun sideDiagonalTranspose(matrix: List<List<Double>>): List<List<Double>> {
    var i = matrix.size
    return List(matrix.size) {
        i--
        extractColumnAsList(matrix, i).reversed()
    }
}

fun verticalLineTranspose(matrix: List<List<Double>>): List<List<Double>> {
    return matrix.map { it.reversed() }
}

fun horizontaLineTranspose(matrix: List<List<Double>>): List<List<Double>> {
    return matrix.reversed()
}

fun matrixMultiplication() {
    println("Enter size of first matrix: ")
    val firstDim = readLine()!!.split(" ").map { it.toInt() }
    val (firstMatrix, isInt) = buildMatrix(firstDim[0], firstDim[1])

    println("Enter size of second matrix: ")
    val secondDim = readLine()!!.split(" ").map { it.toInt() }
    val (secondMatrix, isIntTwo) = buildMatrix(secondDim[0], secondDim[1])

    val result = multiplyMatrices(firstMatrix, secondMatrix)
    printMatrix(result, isInt && isIntTwo)
}

fun extractColumnAsList(matrix: List<List<Double>>, column: Int): List<Double> {
    val flatened = mutableListOf<Double>()
    for (i in 0 until matrix.size) {
        flatened.add(matrix[i][column])
    }
    return flatened
}

fun multiplyMatrices(a: List<List<Double>>, b: List<List<Double>>): List<List<Double>> {
    val aI = a.size
    val aJ = a.first().size
    val bI = b.size
    val bJ = b.first().size

    if (aJ != bI) throw Exception("Incorrect Matrix Size")

    val resultMatrix = MutableList(aI) { MutableList(bJ) { 0.0 } }

    a.forEachIndexed { row, intA ->
        b[row].forEachIndexed { column, value ->
            val flatB = extractColumnAsList(b, column)
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
    val aMeasures = readLine()!!.split(" ").map { it.toInt() }
    println("Enter matrix: ")
    val (A, isInt) = buildMatrix(aMeasures.first(), aMeasures.last())
    println("Enter constant: ")
    val scalar = readLine()!!.toDouble()
    val result = scalarMultiplication(scalar, A)
    printMatrix(result, isInt)
}

fun scalarMultiplication(scalar: Double, a: List<List<Double>>): List<List<Double>> {
    val rows = a.size
    val columns = a.first().size
    val resultMatrix = MutableList(rows) { MutableList(columns) { 0.0 } }

    for(m in 0 until rows) {
        for (n in 0 until columns) {
            resultMatrix[m][n] = scalar * a[m][n]
        }
    }
    return resultMatrix
}

fun addMatrices() {
    println("Enter size of first matrix: ")
    val aMeasures = readLine()!!.split(" ").map { it.toInt() }
    println("Enter first matrix: ")
    val (A, isInt) = buildMatrix(aMeasures.first(), aMeasures.last())
    println("Enter size of second matrix: ")
    val bMeasures = readLine()!!.split(" ").map { it.toInt() }
    println("Enter size of second matrix: ")
    val (B, isIntTwo) = buildMatrix(bMeasures.first(), bMeasures.last())

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
    val cpyMatrix: MutableList<List<Number>>

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
    val resultMatrix = MutableList(rows) { MutableList(columns) { 0.0 } }

    for(m in 0 until rows) {
        for (n in 0 until columns) {
            resultMatrix[m][n] = a[m][n] + b[m][n]
        }
    }
    return resultMatrix
}
