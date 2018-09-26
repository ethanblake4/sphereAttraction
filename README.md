# sphereAttraction
This programmatically finds the answer to the Physics problem:
 `What is the electric field inside a charged hollow sphere`?

The answer is 0 by Gauss's Law, but it's not very intuitive.
A good explanation is given here: (2nd answer)
https://physics.stackexchange.com/questions/277929/why-is-the-electric-field-inside-a-hollow-sphere-zero-but-not-for-a-ring
 
This program finds it as such:
1. Generate a (nearly) uniform distribution of points on a sphere
    using the Fibonacci sphere algorithm
2. Calculate the electric field magnitude to each of those points
    from a test charge
3. Sum the components of the E field vectors in X, Y, and Z
 
The result should be very close to zero. Slight deviation
results because of the imperfect nature of the Fibonacci sphere
algorithm.

This is closely equivalent to doing an integral of the electric
field over the entire sphere, but possibly easier to understand.
