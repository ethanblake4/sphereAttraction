import javafx.geometry.Point3D
import java.text.NumberFormat
import kotlin.math.*

val phi = (sqrt(5.0) + 1) / 2 - 1 // golden ratio
val ga = phi * 2 * PI // golden angle

var nbrPoints = 2000000 // number of points to test. larger values mean more time.

var R = 1.0 // Sphere radius
var q = 1   // "charge" at any point on the sphere's surface

// To add: Give each point a heading and velocity, adjust based on distance/heading to nearby points.
data class SpherePoint(var lat: Double, var lon: Double) {
    fun toXyz(): Point3D {

        // Convert lat/lon to XYZ and create a JavaFX Point3D
        return Point3D(
                R*cos(lat)*cos(lon),
                R*cos(lat)*sin(lon),
                R*sin(lat)
        )
    }
}

var pts = arrayOfNulls<SpherePoint>(nbrPoints)

/**
 * Sphere Attraction
 *
 * This programmatically finds the answer to the Physics problem:
 *  `What is the electric field inside a charged hollow sphere`?
 *
 * The answer is 0 by Gauss's Law, but it's not very intuitive.
 * A good explanation is given here: (2nd answer)
 * https://physics.stackexchange.com/questions/277929/why-is-the-electric-field-inside-a-hollow-sphere-zero-but-not-for-a-ring
 *
 * This program finds it as such:
 *  1. Generate a (nearly) uniform distribution of points on a sphere
 *     using the Fibonacci sphere algorithm
 *  2. Calculate the electric field magnitude to each of those points
 *     from a test charge
 *  3. Sum the components of the E field vectors in X, Y, and Z
 *
 *  The result should be very close to zero. Slight deviation
 *  results because of the imperfect nature of the Fibonacci sphere
 *  algorithm.
 *
 *  This is closely equivalent to doing an integral of the electric
 *  field over the entire sphere, but possibly easier to understand.
 */
fun main(args: Array<String>) {

    println("Sphere Attraction v0.1a")
    println("----------------------")
    println("\nsim with $nbrPoints points, charge=$q, R=$R")

    // Test point [change values to anything in (-1, 1)]
    // To be safe, stay in (-0.95, 0.95) due to float precision
    val mPoint = Point3D(0.8, 0.8, 0.0)

    println("Test point: $mPoint")

    // Check to ensure the point is not outside the sphere
    if(mPoint.magnitude() > R)
        println("\n\u001B[33mThe magnitude of the vector formed" +
                " by the test point is greater than the sphere radius\u001B[0m\n")

    // Generate the sphere points
    initSphere()

    /* Initialize the electric field to zero */
    var Ex = 0.0
    var Ey = 0.0
    var Ez = 0.0

    /* This is just for debugging */
    val t = System.currentTimeMillis()

    print("2. Simulating... ")

    // For each point on the sphere
    pts.map { it!!.toXyz() }.forEach { point ->

        // Calculate the distance to the point
        val rSq = mPoint.distance(point).pow(2)

        // Calculate E field magnitude
        val E = q / rSq

        // Calculate vectors
        val Lvec = mPoint.subtract(point)
        val L = mPoint.distance(point)

        // Sum vector components
        Ex += (E / L) * Lvec.x
        Ey += (E / L) * Lvec.y
        Ez += (E / L) * Lvec.z

    }

    println("done (${System.currentTimeMillis() - t} ms)")

    println()
    println("Simulation Results")
    println("------------------")

    println("E field X: $Ex")
    println("E field Y: $Ey")
    println("E field Z: $Ez")

}


// Creates equidistant point distribution using Fibonacci sphere algorithm
fun initSphere() {
    val fmt = NumberFormat.getIntegerInstance().format(nbrPoints)
    print("1. Creating sphere with $fmt points... ")
    val t = System.currentTimeMillis()
    for (i in 1..nbrPoints) {
        var lon = ga * i
        lon /= 2 * PI
        lon -= floor(lon)
        lon *= 2 * PI
        if (lon > PI) lon -= 2 * PI

        // Convert dome height (which is proportional to surface area) to latitude
        val lat = asin(-1 + 2 * i / nbrPoints.toFloat())

        pts[i-1] = SpherePoint(lat.toDouble(), lon)
    }
    println("done (${System.currentTimeMillis() - t} ms)")
}
