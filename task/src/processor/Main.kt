package processor

import kotlin.math.pow
import kotlin.math.round

fun main() {
    while(true) {
        println("1. Add Matrices\n2. Multiply matrix by a constant\n3. Multiply matrices\n4. Transpose matrix\n5. Calculate a determinant\n6. Inverse matrix\n0. Exit")
        println("Your choice: ")
        when(readLine()!!.toInt()) {
            1 -> addMatrices()
            2 -> scalarMultiplication()
            3 -> matrixMultiplication()
            4 -> matrixTranspose()
            5 -> calculateDeterimant()
            6 -> calculateInverse()
            else -> break
        }
    }
}

fun calculateInverse() {
    println("\nEnter matrix size")
    var size = readLine()!!.split(' ').map { it.toInt() }
    println("Enter matrix:")
    var (matrix, _) = buildMatrix(size[0])

    var determinant = calculateDeterminant(matrix)
    if (determinant == 0.0) {
        println("This matrix doesn't have an inverse.")
        return
    }

    var cofactorMatrix = calculateCofactorMatrix(matrix)
    var transposedCofactor = mainDiagonalTranspose(cofactorMatrix)
    var inverseDeterminant = 1.0 / determinant

    var inverseMatrix = scalarMultiplication(inverseDeterminant, transposedCofactor)
    printMatrix(inverseMatrix, false, round = true)
}

fun calculateCofactorMatrix(matrix: List<List<Double>>): List<List<Double>> {
    var cofactorMatrix = MutableList(matrix.size) { MutableList(matrix.size) { 0.0 } }
    for (i in 0 until cofactorMatrix.size) {
        for (j in 0 until cofactorMatrix.size) {
            cofactorMatrix[i][j] = (-1.0).pow(i + j + 2) * calculateCofactor(createSubmatrix(matrix, i, j))
        }
    }
    return cofactorMatrix
}

fun calculateCofactor(matrix: List<List<Double>>): Double {
    var size = matrix.size

    var sign = 1.0

    var determinant = 0.0

    if(size == 2) return getCofactor(matrix)

    matrix.forEachIndexed { i, list ->
        list.forEachIndexed { j, _ ->
            var subMatrix = createSubmatrix(matrix, 0, j)
            determinant += matrix[i][j] * calculateCofactor(subMatrix) * sign
            //determinant += calculateCofactor(subMatrix) * sign
            sign *= -1.0
        }
    }
    return determinant
}

fun calculateDeterimant() {
    println("Enter matrix size")
    var size = readLine()!!.split(' ').map { it.toInt() }
    println("Enter matrix:")
    var (matrix, isInt) = buildMatrix(size[0])
    var determinant = calculateDeterminant(matrix)

    println("The result is:")
    println(
        if (isInt) determinant.toInt()
        else determinant
    )
    println("")
}

fun getCofactor(matrix: List<List<Double>>): Double {
    return (matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1])
}

fun createSubmatrix(matrix: List<List<Double>>, row: Int, column: Int): List<List<Double>> {
    var temp = MutableList(matrix.size - 1) { MutableList(matrix.size - 1) { 0.0 } }
    var x = 0
    var y = 0
    for (i in 0 until matrix.size) {
        for (j in 0 until matrix.first().size) {
            if (row != i && column != j) {
                temp[y][x] = matrix[i][j]
                x++
                if (x == matrix.size - 1) {
                    x = 0
                    y++
                }
            }
        }
    }
    return temp
}

fun calculateDeterminant(matrix: List<List<Double>>): Double {
    var size = matrix.size

    var sign = 1.0

    var determinant = 0.0

    if(size == 2) return getCofactor(matrix)

    matrix.first().forEachIndexed { index, d ->
        var subMatrix = createSubmatrix(matrix, 0, index)
        determinant += matrix[0][index] * calculateDeterminant(subMatrix) * sign
        sign *= -1.0
    }

    return determinant
}




fun matrixTranspose() {
    println("\n1. Main diagonal\n2. Side diagonal\n3. Vertical line\n4. Horizontal line")
    println("Your choice: ")
    val choice = readLine()!!.toInt()
    println("Enter matrix size: ")
    val measures = readLine()!!.split(" ").map { it.toInt() }
    println("Enter matrix: ")
    val (matrix, isInt) = buildMatrix(measures.first())

    val transposed = when(choice) {
        1 -> mainDiagonalTranspose(matrix)
        2 -> sideDiagonalTranspose(matrix)
        3 -> verticalLineTranspose(matrix)
        4 -> horizontalLineTranspose(matrix)
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

fun horizontalLineTranspose(matrix: List<List<Double>>): List<List<Double>> {
    return matrix.reversed()
}

fun matrixMultiplication() {
    println("Enter size of first matrix: ")
    val firstDim = readLine()!!.split(" ").map { it.toInt() }
    val (firstMatrix, isInt) = buildMatrix(firstDim[0])

    println("Enter size of second matrix: ")
    val secondDim = readLine()!!.split(" ").map { it.toInt() }
    val (secondMatrix, isIntTwo) = buildMatrix(secondDim[0])

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

    for (i in a.indices) {
        norm += a[i]*b[i]
    }
    return norm
}

fun scalarMultiplication() {
    println("Enter size of matrix: ")
    val aMeasures = readLine()!!.split(" ").map { it.toInt() }
    println("Enter matrix: ")
    val (A, isInt) = buildMatrix(aMeasures.first())
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
    val (A, isInt) = buildMatrix(aMeasures.first())
    println("Enter size of second matrix: ")
    val bMeasures = readLine()!!.split(" ").map { it.toInt() }
    println("Enter size of second matrix: ")
    val (B, isIntTwo) = buildMatrix(bMeasures.first())

    if (aMeasures.first() != bMeasures.first() || aMeasures.last() != bMeasures.last()) println("ERROR")
    else printMatrix(sumMatrix(A, B), isInt && isIntTwo)
}

fun buildMatrix(rows: Int): Pair<List<List<Double>>, Boolean> {
    var isInt = false
    val matrix = List(rows) { readLine()!!.split(" ").map { isInt = !it.contains('.')
        it.toDouble() } }
    return matrix to isInt
}

fun printMatrix(matrix: List<List<Double>>, isInt: Boolean, round: Boolean = false) {
    val cpyMatrix: MutableList<List<Number>>

    if(isInt) cpyMatrix = matrix.map { list -> list.map { number -> number.toInt() } }.toMutableList()
    else if (round) cpyMatrix = matrix.map { it.map { it.truncate(2) }  }.toMutableList()
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


fun Double.truncate(decimals: Int): Double {
    var doubleStr = this.toString()
    var pointIndex = doubleStr.indexOf('.')
    if (doubleStr.slice(pointIndex..doubleStr.lastIndex).length <= 2) return doubleStr.toDouble()
    return doubleStr.slice(0.. pointIndex + decimals).toDouble()
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}