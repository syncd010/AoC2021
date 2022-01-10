# [Advent of Code 2021](https://adventofcode.com/2021) in Clojure

This year i chose to go back in time and use the most popular modern dialect of Lisp, Clojure. I fully expect this decision to come back and haunt me because 1) i haven't touched Lisp in a long time, 2) i didn't have time to properly look into Clojure, so this will be "on-the-job" training and 3) I'll probably struggle not having an escape hatch to a fully imperative style - though i love functional programming and try to use it wherever it feels right, i recognized that my initial problem approach is based on an imperative paradigm.

Clojure version is 1.10, and i used [Leiningen](https://leiningen.org/) for managing the project. All code is on `./src`, my input files as well as test inputs are in `./resources`.

## Post-event impressions


## Usage
To run:
> lein run [-f FILE] [-t] -d day

This will run the 2 parts of the specified `day` using `FILE` as input. If no file is specified `./resources/input{day}` is used as input. If no file is specified and `-t` is used, `./resources/input{day}Test` is used instead. 

---

## [Day 1](https://adventofcode.com/2021/day/1)
A simple warm up, though more complex than other years i believe.

## [Day 2](https://adventofcode.com/2021/day/2)
Still warming up, but I'm already having trouble with Clojure and progressing way too slow.

## [Day 3](https://adventofcode.com/2021/day/3)
Clojure is still feeling like a straight jacket, and I'm taking too long to solve these problems.

## [Day 4](https://adventofcode.com/2021/day/4)
This was a long one, guess it's the weekend effect where it's assumed we have more time to spend on each problem. 
Once again, i struggled, and kept falling into an imperative way of thinking, which to be honest, is a more natural style to solve some aspects of this problem. Nevertheless, in the end I'm happy with the solution and feel it is "obvious". 
The main part is the `play-bingo-rounds`, where consecutive rounds are played, until there are no remaining numbers to call. It returns a list of results from each round: nil if there are no winners on the round or a list of the called numbers and winner boards on that round. From there we can calculate the output of the 2 parts.

## [Day 5](https://adventofcode.com/2021/day/5)
Fairly straightforward, after yesterday's problem. The solution just fills the elements between the start and end positions for each line given on the input and then uses the quite useful `frequencies` function of Clojure to count the number of repeated positions.

## [Day 6](https://adventofcode.com/2021/day/6)
An exponential growth problem, for which i tried to find a closed form solution (like n0*2^gens). But given that the first generation has a time to reproduce different than the others, i couldn't find one (and i don't think one exists). So, for part 1 i implemented a straightforward simulation of the problem, even though i suspected that part 2 would make that approach infeasible, which, surely enough was the case.
The insight for part to is to recognize that all elements with the same age will produce the same result, so we can group them and evolve based on the age group. This can be done by having a vector where each position represents the number of elements with that age, and on each step rotate the vector (simulating decreasing the age) and adding the previous 0 age group to the 6 age group (the new 8 age group is the same size as the precious 0 age group, which gets counted automatically on the vector rotation).

## [Day 7](https://adventofcode.com/2021/day/7)
For part one, there's a closed form solution, and a google search for "minimize sum of absolute differences" tells that the median is the right answer.
For part two i suspected that the answer would be something along the mean, but i didn't find a proof for that, so i just implemented a simple search. Recognizing that the function is convex, and the answer should lie close to the mean, the solution searches in both directions for the minimum value and returns it. In fact the solution is the mean +-0.5 but i find that the search is good enough.

## [Day 8](https://adventofcode.com/2021/day/8)
This was a fun problem, though it took some time as as been the norm this year. The description of the problem is long and somewhat confusing, but the core of it amounts to a decoding problem. Part of the solution is given in the first part - the digits 1, 4, 7 and 8 can be found by the length of their encoding, respectively 2, 4, 3 and 7 -, and from there we can derive the encoding of the other digits:
- 3 has length 5, and its segments completely mask 7;
- 5 has length 5, and its segments completely mask the difference between the segments of 4 and 1;
- 2 has length 5, and is whatever is left after applying the previous rules;
- 6 has length 6, and its segments don't completely mask 1;
- 9 has length 6, and its segments completely mask 4;
- 0 has length 6, and its segments don't completely mask the difference between the segments of 4 and 1;
With that decoding map built, it's just a matter of applying it to the input and deriving the asked result.

## [Day 9](https://adventofcode.com/2021/day/9)
Yet another day where my go to solution would be completely imperative, at least for part one. Nevertheless, it doesn't look bad in Clojure, and is quite readable.
For part 2 i did a convoluted depth-first-search using `reduce` and recursion, which returns the visited nodes. The implementation isn't canonical nor particularly readable, but it was what came out the first try, so i kept it.

## [Day 10](https://adventofcode.com/2021/day/10)
This was a simple one. The main function is `parse-line` which gathers the input tokens on a stack and when a closer token is found checks if it is at the top of the stack, stopping if it is not. Returns the token that broke the parsing (or nil if it the parsing completes until the end) and the remaining tokens on the stack (which are the ones that weren't closed). Part 2 uses the remaining stack to construct the corresponding sequence of `closers` and calculating the scores from there.

## [Day 11](https://adventofcode.com/2021/day/11)
This would be so much easier with a matrix and some nested for's... Anyway, i'm in a hurry today, so the solution isn't simple, readable nor efficient, but it seems to work, so i'll leave it as is. Just a note: i chose to represent the board using a 1D vector instead of 2D matrix to simplify some manipulations.

## [Day 12](https://adventofcode.com/2021/day/12)
I was wondering when search would appear, and this was the day. It's not a search problem, but the solution structure is similar to a Depth-First Search, just visiting all nodes instead of stopping when finding a goal. It only counts the number of explored paths, it doesn't store them, as that can lead to problems with stack overflows and/or memory depletion.
As usual, solving this took too much time, partly because i had to stop aoc for a week, and on returning half of the gained proficiency in Clojure was gone, but the final solution is simple and elegant IMO.

## [Day 13](https://adventofcode.com/2021/day/13)
The solution for this one is straightforward to see, though it took some time to implement, particularly the second part, the representation of the board. To make it manageable i had to represent it as a 1D array, manipulate it and then partition it to 2D. Once again this would be a straightforward, mindless job in a imperative language, but took some thinking in a functional one.

## [Day 14](https://adventofcode.com/2021/day/14)
First part is a straightforward implementation, expanding the initial template according to the rules, and manipulating the result to obtain what is being asked.
For part 2 a full expansion wouldn't work, as the result would be on the order of 2^40*(initial size). Looking at the structure of the problem, each pair of letters generates two new pairs according to a rule, so we just need to keep track of the number of pairs that are generated on each step and present on the output. Given that - the count of each pair that is on the result - to get the individual letters we should only consider the second letter, given that they are all overlapped, and add the first letter of the initial template.
I feel that this is a somewhat confusing solution, and the code is less than readable (could be better if i had partitioned it in smaller functions), but enough for today. Anyway, it's one day where i feel that the solution isn't very elegant.

## [Day 15](https://adventofcode.com/2021/day/15)
The first search problem, this time a uniform-cost/Dijkstra search to find the shortest path between the beginning and the end. I decided to over-engineer the solution and make it general enough to support breadth-first, depth-first, uniform-cost, greedy and A* search methods. So i dusted off my copy of Norvig's AI book, took a look at the search algorithms chapter and just went with it. This took some time and lead to long debugging sessions but it is done, and will probably be used in later days...The code is in search.clj and is fairly well documented, so additional info should be looked there.
As for the specific problem of the day, part one is simple, and part two is solvable by brute-forcing it, which takes approximately 16s on my computer. Curiously, adding more fields to the *search-nodes* (like for instance, pre-computing the total cost as `path-cost + estimated-cost`) makes this run a lot slower (doubling the time), which implies that Clojure's implementation of vectors is susceptible to  the size of its elements. 
I also learned a lot (and got frustrated a lot) about Clojure's lists and vectors, `conjs` and `concats`, `first`, `rest` and `subvec`, how it pays to be aware about the type of collection that is being manipulated, how a FIFO queue isn't native, and how simple and seemingly innocuous changes to those constructs can lead the runtimes exponentially higher or hairy exceptions (like for instance [this](https://stuartsierra.com/2015/04/26/clojure-donts-concat)).

## [Day 16](https://adventofcode.com/2021/day/16)
This was a fun one, though it took some work/time getting all the indexes right. The whole problem is recursive, which plays to Clojure's strengths. Part two was a breeze, quick to implement and a clean solution. There were issues with large numbers in the problem - the value on literals can be quite large so a direct parsing with `Integer/parseInt` isn't feasible, it has to be done in the same chunks it is represented. Given the magnitude of the literals it is best to handle all the values as floating-point from there on to minimize the risk of overflows.

## [Day 17](https://adventofcode.com/2021/day/17)
This was a pure classical mechanics problem, which can be solved by calculating the projectile trajectory. The x and y axes are independent, and the movement on the y axis can be described by a direct application of motion equations with constant acceleration (which need to be adapted for discrete time): `y(t) = y0 + v0y*t + 1/2*a*t^2`, while on the x axis we need to take into account that v cannot be bellow 0, or equivalently, it only moves up until time `t-max = v0x / a`.
To check whether the projectile hits a bounding box defined by `[xm, xM] [ym, yM]`, it's easier to start with the y axis and calculate the interval of time it travels between ym and yM by solving the motion equation `y(t) = ym` (or yM). With that interval the positions of x can be calculated (bearing in mind that x travels at most for `v0x/a` time) using `x(t)` and then check whether any of the resulting positions is within the interval `[xm, xM]`. Some care must be taken because we're dealing with discrete time steps in the checking. 
To get the final solutions we need to define maximum values for the starting velocities, which are defined as xM and yM positions, given that with that velocities the bounding box wouldn't even be hit on t=1. Stricter conditions could be deduced, but these suffice for the scale of the problem. After getting the initial velocities that hit the box, the solutions are straightforward.

## [Day 18](https://adventofcode.com/2021/day/18)
Another one that took some work and time, but was fun. The description is deliberately obfuscated to hide the obvious way to describe the problem, which is to apply some operations on trees. Working with trees and recursion always needs care, so i took some time validating the code before testing the reduce algorithm, which, thankfully, almost worked the first time. Almost is the key here, as i then spent a lot of time making sure that the explode and split operations were applied exactly in the order specified in the description - simple changes in this order gives different results, and working out why isn't straightforward.
Not much more to say, the code isn't exactly simple, but i guess the problem isn't either. Just a note regarding the initial parsing: i opted to do it in one single pass, building the results partially and saving them in a stack. This approach is more suited to when we have mutable state, so that the partial results are built upon. In Clojure this approach creates a lot of temporary lists, and probably an approach based on recursion on the left and right side of each expression would be more suitable (even tough it couldn't be done in one single pass of the initial string).

## [Day 19](https://adventofcode.com/2021/day/19)
Goodness, i'm not even sure i have the stamina to write any coherent thoughts about this - i might have missed some direct solution because it felt way too complex. It started with figuring out how to align 2 sets of points on different coordinate systems, then going off to (re)learn how to make rotations in 3D space, then untangling a mess of loops inside loops inside loops, and finally having to optimize the solution with heuristics and parallelism because it was taking too much time...
Anyway, here goes some unstructured thoughts:
- To check whether two scanners overlap, we need to:
    1. For each beacon of the first scanner, "rebase" all the other beacons to that one (which is done by calculating the difference between them). Them for each one:
    2. For each beacon of the second scanner, also "rebase" the other to that one. Them for each pair of "rebased" beacons from the first and second scanner:
    3. Apply each rotation to the rebased beacons of the second set, and check wether there are at least 12 beacons with the same coordinates in the first and second set.
- If successful, we get the beacon of the first scanner that served as the base for that scanner (and its location), the beacon of the second scanner that served as the base for that scanner and the rotation that overlapped them. From this we can calculate the location of the second scanner relative to the first.
- Overlapping the scanners one by one, and getting the transformations that overlap them, allows to deduce the location of each one relative to the first, as well as the rotation that it is in.
- So in the end, we get a map that for each beacon gives its location and rotation relative to the first one, which can be applied to the initial beacons to get their "absolute" positions, remove the duplicates and get the answer for part one. Part two is direct from the locations of the scanners.
- For reference, there are 30 scanners in the input, each with 26 beacons and 24 different rotations. To check overlaps for each pair of beacons we would do 26 passes over the first scanner, 26 over the second scanner and try all 24 rotations, and for each of these we are checking how many beacons overlap on the 2 sets, which is again `O(26*26)`. So, a direct solution would be approximately `O(s*b*b*r*b*b) (s=30, b=26, r=24)` or 300M. This was a problem, so some optimizations were made:
    1. To fail fast, before the rotations are applied, we check whether it is even possible for the 2 set of beacons to overlap. Recognizing that all the rotations are multiples of 90º, this implies that each 3D coordinate will only be multiplied by +1 or -1, or they will be swapped (x with y, etc). Their absolute value won't change, so a quick check is to sum the absolute values of the coordinates of each and see if at least 12 of them are identical between the two sets of beacons. Only if this check is passed do we go on and apply all the 24 rotations to really check if they overlap;
    2. Noticing that most of the scanners/beacons don't overlap, and that we only need to overlap on one beacon, we can cut the number of beacons to check by disregarding 11 beacons of the first scanner. This guarantees that, if they overlap, at least one beacon that does so remains in the set to be checked, and if they don't overlap, we just cut the work in about 1/3.
    3. Finally i tried to parallelize the computation, which in Clojure was a breeze, just substituting one `map` by one `pmap` (and calling `shutdown-agents` so that we don't wait 1 min. after the result is calculated).
- All in all, the time come down from +10 min (not sure how long, as i didn't wait for it to finish) to 13 sec (all cores at max). Nice.
A fun one, but hopefully the last that takes so much work.

## [Day 20](https://adventofcode.com/2021/day/20)
This was a lot softer, even though it had the curve ball of having the input from the test and the real one give totally different results. Position 0 of the real input is 1 (and position 511 is 0), so all the "outer" positions on the image alternatively switch between a 0 and a 1, which didn't happen on the test input, where position 0 is 0, so they all stayed fixed at 0. Clever twist.
Generally this problem didn't play to Clojure's strengths, or at least to my knowledge of Clojure. An imperative style with mutable state would IMO be cleaner and quicker. My implementation isn't very quick (takes about 7s), i even tried to use transient vectors, but that didn't help at all. In the end i just brute-forced it and added a `pmap` to parallelize each row processing, which helped a bit. Once again, making an algorithm parallel in Clojure is a breeze.

## [Day 21](https://adventofcode.com/2021/day/21)
This year is definitely harder than the previous ones... 
First part is straightforward. I understand that, with some thought, a generic "formula" could be devised for the game, but i implemented a direct description of the game, which wasn't hard.
The second part scared me, right from the moment i read the result numbers. It's an explosive combinatorics problem, that required some iterations to get right - Clojure's REPL was a huge help here. The main insights to solve it are:
- On each round of 3 dice rolls, the number of possible universes expands by 27;
- `round-results` contains the parameters by which a round of 3 rolls change a universe: a map of "positions to advance" to "number of universes" that have that result.
- The state of universes is represented by a map, whose key is the state of the players and the value is the number of universes for that state. The state/key is a vector with 2 positions, one for each player, and each one is a vector with that player's position and total points, ie `[ [ p1-position p1-points ] [ p2-position p2-points ] ]`. Thank god that nested vectors can be used as keys in Clojure...
- Evolving a universe is a matter of "applying" the position/counts in `round-results` to each entry of a universe (thereby expanding it by 27), taking care to apply that to the correct player.
There might be a (much) simpler solution, but this is the best i could find.

## [Day 22](https://adventofcode.com/2021/day/22)
Yet another time-sink. I suppose that either i missed an obvious solution, or this is a common CS problem which i never encountered. I recognize it as some kind of algorithm useful in 3D, but i never heard of it so i had to reinvent the wheel (and probably i ended up with a squared one).
Anyway, the straightforward implementation of storing the state of each position in a multi-dimensional array wouldn't work, given the size of the problem. For part 1 we already had to consider 1M positions, which was manageable, but it was obvious that in part 2 that number would explode. The cubes would need to be the main structure, and the solution would have to join each cube with the already processed ones, partitioning them into new cubes that didn't intersect with the one being processed, and adding it if it was on. Pretty simple to visualize, but a pain to implement.
The intersection process could be a lot smarter, as it currently just produces all possible intersections, and there are obvious ways to reduce the number of cubes produced (join the cubes that have similar size at 2 of the dimensions and are at the same coordinates), but i let it rest. Maybe because of that i feel it runs kind of slowly - about 3 seconds. I tried using transients but it only got me a 5% improvement, so i decided to keep the simpler version.

## [Day 23](https://adventofcode.com/2021/day/23)

## [Day 24](https://adventofcode.com/2021/day/24)

## [Day 25](https://adventofcode.com/2021/day/25)
