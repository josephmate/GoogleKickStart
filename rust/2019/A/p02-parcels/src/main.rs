use std::io;
use std::collections::VecDeque;

// naive solution that attempts all potentials post offices
// for each potential post office compute the score
// to compute the score, calculate the distance to the nearest post office using bfs
// complexity:
// N * N * N * N * N * N
// \   /   \   /   \   /
//  \ /     \ /     BFS to find nearest post office (could start from one corner and post office in opposite corner)
//   |       Finds the manning distance for each position 
//   Trying every possible post office position
// This solution will be too slow for the large data set, but I'm using it as a starting point.

// scoreing
// 1 0 1       1 0 0 0 1
// 0 0 0       0 0 0 0 0
// 1 0 1       0 0 0 0 0
//             0 0 0 0 0
//             1 0 0 0 1
// start with the postal offices, put a 0 there
// 0 ? 0       0 ? ? ? 0
// ? ? ?       ? ? ? ? ?
// 0 ? 0       ? ? ? ? ?
//             ? ? ? ? ?
//             0 ? ? ? 0
// move up down left right and place score incremented by 1
// 0 1 0       0 1 ? 1 0
// 1 ? 1       1 ? ? ? 1
// 0 1 0       ? ? ? ? ?
//             1 ? ? ? 1
//             0 1 ? 1 0
// repeat until filled
// 0 1 0       0 1 2 1 0
// 1 2 1       1 2 3 2 1
// 0 1 0       2 3 4 3 2
//             1 2 3 2 1
//             0 1 2 1 0



// Will placing the post office at the max give min overall delivery time?
// For instance, with these scores, best thing we can do it place it at 2,3, on the right side.
// 0 1 2 3 2 1 0 1 2 3 4 3 2 1 0
// Proof by contradication attempt
// Assume there is some configuration where the max does not provide the min overall delivery time.
// Let M be the max point
// Let B be the best point
// Let X be the point that that is minimized by placing a B, but wasn't minimized when placing at M, reducing the overall by r over placing it at M
// Let D(M,m) be the distance from M to the closest post office, m, to M
// Let D(M,B) be the distance between M and B
// Let D(X,x) be the distance between X and closest postal before placing B
// m           
//     M    B        x 
//              X
// then
// D(M,m) > D(X,x)
// D(M,m) > D(M,B)
// D(X,x) > D(X,B)
// since placing a postal M did not give us the overall min for X
// D(M,X) - D(X,B) = r by assumption
// D(M,X) > D(X,B)

// D(M,X) ? D(X,x)

// Summary
//               D(M,X) > D(X,B)
//      D(M,m) > D(X,x) > D(X,B)
//      D(M,m) > D(M,B)

// Maybe it's not true. Let me try finding a counter example
//    434
//    323
//    212
//    101
// Placing at max does not give the best solution.
//    012
//    123
//    212
//    101
// This one is the best solution.
//    101
//    212
//    212
//    101
// Maybe we have to try every position?
// With the score calculating optimization, getting rid of the BFS results in
//
// N * N + N * N * N * N
// |   |   |   |   \   /
// |   |   \   /     Brute forcing each possible square
// \   /    Calculating the score, from the inital score map when adding a post office.
// Calculating the initial score map
// 250^4 ~ 10 * 10^8 = 10^9 = 1,000,000,000

// That wasn't good enough. Maybe if we
// start from the intial score map
// try each square
// change each cell until score is larger than initial map

// What if we look at all the maximums and take the mid point, and place the post office there.
// We can try a 3x3 square incase we're off by one.
// What if there are another square, one less than the max?
// Maybe weight the midpoint so that it's 1 closer to the max.
// What if there's one two less than the max?
// Weight the max 2 more than the one that is two less.
// Maybe keep recursively doing this?
// Maybe a weighted average of the coords depending on the score.
// Maybe try a 3x3 around the weighted average coord just in case I'm off by a bit.
// this would be O(N*N) algorithm because
//       N * N for intial score
// +     N * N calucating the weighted average
// + 9 * N * N for calculating the scores for 9 potential configurations
// Unfortunately this doesn't provide the correct solution.

// put together this webpage to explore the problem
// https://josephmate.github.io/GoogleKickStart/rust/2019/A/p02-parcels/explore_problem.html
// Still couldn't figure it. as a result, I took a look at the analysis for how do do it.

// It turns out to be a divide any conquer problem with two key pieces:
// 1) You binary search for the minimumn max delivery time.
// 2) You need an efficient way to determine if you can achieve a specific delivery time.
// I'm glad I looked it up, because I would have never come up with 2).
// You look at all the squares that have above K delivery time.
// Will they fit in a K by K square? Kind of like using the radius for circle detection in 2D.
// Since we're doing manhattan distance, we can do it efficiently in O(1) time.
// So the pseudocode looks something like:
// 1) compute delivery times
// 2) binary search if min max delivery time on the K in the range [0, max delivery time from 1)]
//    2.1) find all squares with distance greater than K
//    2.2) determine if all the squares can fit in a K sized 'manning distance shape'
//         (this is where my solution differs from the kickstarter analysis. I found this
//          way to be easier to understand)
//        2.2.1) using / diagonal lines, search for the first and last occurrence of square from (2.1)
//             2.2.1.1) if that distance is bigger than K, it does not fit in a K sized 'manning distance shape'
//                      otherwise it does



fn bfs_score(
    rows: usize,
    columns: usize,
    grid: & Vec<Vec<i32>>,
) -> Vec<Vec<i32>> {
    // create an empty grid of scores
    // -1 indicates no score yet
    let mut scores = Vec::new();
    for _r in 0..rows {
        let mut row = Vec::new();
        for _c in 0..columns {
            row.push(std::i32::MAX);
        }
        scores.push(row);
    }

    // get all the post offices in the queue
    let mut bfs_queue: VecDeque<(usize, usize, usize)> = VecDeque::new();
    for r in 0..rows {
        for c in 0..columns {
            if grid[r][c] == 1 {
                bfs_queue.push_back((r,c,0));
            }
        }
    }

    loop {
        match bfs_queue.pop_front() {
            Some((current_row, current_column, score)) => {
                if (score as i32) < scores[current_row][current_column] {
                    scores[current_row][current_column] = score as i32;
                    if current_row + 1 < rows {
                        bfs_queue.push_back((current_row + 1, current_column, score + 1));
                    }
                    if current_row as i32 - 1 >= 0 {
                        bfs_queue.push_back((current_row - 1, current_column, score + 1));
                    }
                    if current_column + 1 < columns {
                        bfs_queue.push_back((current_row, current_column + 1, score + 1));
                    }
                    if current_column as i32 - 1 >= 0 {
                        bfs_queue.push_back((current_row, current_column - 1, score + 1));
                    }
                }
            },
            None => {
                break;
            }
        }
    }

    return scores;
}

fn max_grid(
    rows: usize,
    columns: usize,
    scores: &Vec<Vec<i32>>
) -> i32 {
    let mut max_so_far = 0;
    for r in 0..rows {
        for c in 0..columns {
            if scores[r][c] > max_so_far {
                max_so_far = scores[r][c];
            }
        }
    }
    return max_so_far;
}

fn find_too_far_grid(
    rows: usize,
    columns: usize,
    scores: &Vec<Vec<i32>>,
    max_allowable_distance: i32
) -> Vec<Vec<bool>> {
    let mut too_far_grid = Vec::new();
    for r in 0..rows {
        let mut row = Vec::new();
        for c in 0..columns {
            row.push(scores[r][c] > max_allowable_distance);
        }
        too_far_grid.push(row);
    }
    return too_far_grid;
}

#[derive(Copy, Clone)]
enum DiagonalDirection {
    TopLeftToBottomRight,
    BottomLeftToBottomRight
}

fn diagonal_distance(
    rows: usize,
    columns: usize,
    too_far_grid: &Vec<Vec<bool>>,
    direction: DiagonalDirection
) -> i32 { 
    let square_bound =
        if rows > columns {
            rows
        } else {
            columns
        };

    let mut found = false;
    let mut diagonal_distance = 0;
    for diagonal_length in 0..square_bound {
        if found {
            diagonal_distance += 1;
        }
        for diagonal_position in 0..square_bound {
            let row_posn = match direction {
                DiagonalDirection::TopLeftToBottomRight => diagonal_length - diagonal_position,
                DiagonalDirection::BottomLeftToBottomRight => (square_bound-1) - diagonal_length + diagonal_position
            };
            let col_posn = match direction {
                DiagonalDirection::TopLeftToBottomRight => diagonal_position,
                DiagonalDirection::BottomLeftToBottomRight => diagonal_position
            };
            
            if row_posn < rows  && col_posn < columns {
                if too_far_grid[row_posn][col_posn] {
                    if found {
                        return diagonal_distance;
                    } else {
                        found = true;
                        break;
                    }
                }
            }
        }
    }

    return 0;
}

fn man_dist_fits(
    rows: usize,
    columns: usize,
    scores: &Vec<Vec<i32>>,
    max_allowable_distance: i32
) -> bool {
    let too_far_grid = find_too_far_grid(rows, columns, scores, max_allowable_distance);

    if diagonal_distance(rows, columns, &too_far_grid, DiagonalDirection::TopLeftToBottomRight) > max_allowable_distance {
        return false;
    }

    return diagonal_distance(rows, columns, &too_far_grid, DiagonalDirection::BottomLeftToBottomRight) <= max_allowable_distance;
}

fn binary_search_problem(
    rows: usize,
    columns: usize,
    scores: &Vec<Vec<i32>>,
    min_range: i32,
    max_range: i32
) -> i32 {
    let expected_max_score = (min_range + max_range)/2;

    if man_dist_fits(rows, columns, scores, expected_max_score) {
        if expected_max_score - 1 < min_range {
            return expected_max_score;
        }
        return binary_search_problem(rows, columns, scores, min_range, expected_max_score - 1);
    } else {
        if expected_max_score + 1 > max_range {
            return expected_max_score - 1;
        }
        return binary_search_problem(rows, columns, scores, expected_max_score + 1, max_range);
    }
}

fn solve(
    rows: usize,
    columns: usize,
    grid: &mut Vec<Vec<i32>>
) {
    let scores = bfs_score(rows, columns, grid);
    let max_score = max_grid(rows, columns, &scores);
    if max_score == 0 {
        println!("{}", 0);
        return;
    }

    let min_max_score = binary_search_problem(rows, columns, &scores, 0, max_score);
    println!("{}", min_max_score);
}

// The first line of the input gives the number of test cases, T. T test cases follow.
// The first line of each test case contains the number of rows R and number of columns C of the grid.
// Each of the next R lines contains a string of C characters chosen from the set {0, 1},
// where 0 denotes the absence of a delivery office
// and 1 denotes the presence of a delivery office in the square.

fn parse_row() -> Result<Vec<i32>, io::Error> {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        // trim is needed to get rid of the newline
        Ok(_n) => Ok(buffer.trim().chars()
                    .map(|binary_char| binary_char.to_digit(10)
                        .expect("Expected all student scores to be binary numbers") as i32)
                    .collect()),
        Err(error) => Err(error)
    }
}

fn handle_rows_and_columns(rows: usize, columns: usize) {
    let mut grid: Vec<Vec<i32>> = Vec::new();
    for _i in 0..rows {
        match parse_row() {
            Ok(row) => {
                grid.push(row);
            }
            Err(io_error) => {
                print!("io error: {}", io_error);
                return;
            }
        }
    }
    solve(rows, columns, &mut grid);
}

fn handle_test_case() {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            let row_col: Vec<&str> = buffer.split(' ').collect();
            
            match row_col[0].trim().parse::<usize>() {
                Ok(rows) => {
                    match row_col[1].trim().parse::<usize>() {
                        Ok(columns) => {
                            handle_rows_and_columns(rows, columns);
                        },
                        Err(io_error) => {
                            print!("2nd argument should be a number but got {} {}",
                                buffer.as_str(), io_error);
                        }
                    }
                },
                Err(io_error) => {
                    print!("1st argument should be a number but got {} {}",
                        buffer.as_str(), io_error);
                }
            }
        },
        Err(error) => println!("error: {}", error)
    }
}

fn handle_test_cases(num_test_cases: i32) {
    for i in 1..(num_test_cases+1) {
        print!("Case #{}: ", i);
        handle_test_case();
    }
}

fn main() {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            match buffer.trim().parse::<i32>() {
                Ok(num_test_cases) => {
                    buffer.clear();
                    handle_test_cases(num_test_cases);
                },
                Err(io_error) => {
                    print!("First line shoudld be a number but got {} {}",
                    buffer.as_str(), io_error);
                }
            }
            
        },
        Err(error) => println!("error: {}", error)
    }
}

