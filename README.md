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
Still warming up, though I'm already having trouble with Clojure, and progressing way too slow.

## [Day 3](https://adventofcode.com/2021/day/3)
Clojure is still feeling like a straight jacket, and I'm taking too long to solve these problems.

## [Day 4](https://adventofcode.com/2021/day/4)
This was a long one, guess it's the weekend effect where it's assumed we have more time to spend on each problem. 
Once again, i struggled, and kept falling into an imperative way of thinking, which to be honest, is a more natural style to solve some aspects of this problem. Nevertheless, in the end I'm happy with the solution and feel it is "obvious". 
The main part is the `play-bingo-rounds`, where consecutive rounds are played, until there are no remaining numbers to call. It returns a list of results from each round: nil if there are no winners on the round or a list of the called-nums and winner boards on that round. From there we can calculate the output of the 2 parts.

## [Day 5](https://adventofcode.com/2021/day/5)
Fairly straightforward, after yesterday's problem. The solution just fills the elements between the start and end positions for each line given on the input and then uses the quite useful `frequencies` function of Clojure to count the number of repeated positions.

## [Day 6](https://adventofcode.com/2021/day/6)
An exponential growth problem, for which i tried to find a closed form solution (like n0*2^gens). But given that the first generation has a time to reproduce different than the others, i couldn't find a closed form solution (and i don't think one exists). So, for part 1 i implemented a straightforward simulation of the problem, even though i suspected that part 2 would make that approach infeasible, which, surely enough was the case.
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

## [Day 12](https://adventofcode.com/2021/day/12)

## [Day 13](https://adventofcode.com/2021/day/13)

## [Day 14](https://adventofcode.com/2021/day/14)

## [Day 15](https://adventofcode.com/2021/day/15)

## [Day 16](https://adventofcode.com/2021/day/16)

## [Day 17](https://adventofcode.com/2021/day/17)

## [Day 18](https://adventofcode.com/2021/day/18)

## [Day 19](https://adventofcode.com/2021/day/19)

## [Day 20](https://adventofcode.com/2021/day/20)

## [Day 21](https://adventofcode.com/2021/day/21)

## [Day 22](https://adventofcode.com/2021/day/22)

## [Day 23](https://adventofcode.com/2021/day/23)

## [Day 24](https://adventofcode.com/2021/day/24)

## [Day 25](https://adventofcode.com/2021/day/25)
