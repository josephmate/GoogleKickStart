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
//
// Looks like you cannot determine the mandistance size of a square using diagonals.
// I'm going to fall back on the approach given by kickstart.
// The binary search part remains the same.
// Instead of determining the man distance size of the square,
// you go through each square that is too far, and try to place the post there.
// You can verify if you can place the post there and cover all the squares in O(1).
// Kick start give us this hint:
// dist((x1, y1), (x2, y2)) = max(abs(x1 + y1 - (x2 + y2)), abs(x1 - y1 - (x2 - y2)))
// Fix (x2, y2), to the post office
// then distance will be maximized when
// x1 + y1 and x1 - y1 are either maximized or minimized.
// So that algorithm becomes:
// 1) binary search on K in range [0, max man distance]
// 2) K is valid if for each position greater than K distance
//    part it as true
// 3) find min and max x1 + y1 from the positions that are true
// 4) find min and max x1 - y1 from teh positions that are true
// 5) for each position that is true
//   5.1) is max(abs(x1 + y1 - (x2 + y2)), abs(x1 - y1 - (x2 - y2))) <= K ?
//        if yes return true
//        if not, continue


fn bfs_score(
    rows: i32,
    columns: i32,
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
    let mut bfs_queue: VecDeque<(i32, i32, i32)> = VecDeque::new();
    for r in 0..rows {
        for c in 0..columns {
            if grid[r as usize][c as usize] == 1 {
                bfs_queue.push_back((r,c,0));
            }
        }
    }

    loop {
        match bfs_queue.pop_front() {
            Some((current_row, current_column, score)) => {
                if (score) < scores[current_row as usize][current_column as usize] {
                    scores[current_row as usize][current_column as usize] = score;
                    if current_row + 1 < rows {
                        bfs_queue.push_back((current_row + 1, current_column, score + 1));
                    }
                    if current_row - 1 >= 0 {
                        bfs_queue.push_back((current_row - 1, current_column, score + 1));
                    }
                    if current_column + 1 < columns {
                        bfs_queue.push_back((current_row, current_column + 1, score + 1));
                    }
                    if current_column - 1 >= 0 {
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
    rows: i32,
    columns: i32,
    scores: &Vec<Vec<i32>>
) -> i32 {
    let mut max_so_far = 0;
    for r in 0..rows {
        for c in 0..columns {
            if scores[r as usize][c as usize] > max_so_far {
                max_so_far = scores[r as usize][c as usize];
            }
        }
    }
    return max_so_far;
}

fn find_too_far_grid(
    rows: i32,
    columns: i32,
    scores: &Vec<Vec<i32>>,
    max_allowable_distance: i32
) -> Vec<Vec<bool>> {
    let mut too_far_grid = Vec::new();
    for r in 0..rows {
        let mut row = Vec::new();
        for c in 0..columns {
            row.push(scores[r as usize][c as usize] > max_allowable_distance);
        }
        too_far_grid.push(row);
    }
    return too_far_grid;
}

// 3) find max x1 + y1 from the positions that are true
// 4) find min x1 - y1 from the positions that are true
// 5) for each position that is true
//   5.1) is max(abs(x1 + y1 - (x2 + y2)), abs(x1 - y1 - (x2 - y2))) <= K ?
//        if yes return true
//        if not, continue
fn man_dist_fits(
    rows: i32,
    columns: i32,
    scores: &Vec<Vec<i32>>,
    max_allowable_distance: i32
) -> bool {
    let too_far_grid = find_too_far_grid(rows, columns, scores, max_allowable_distance);
    let mut min_x1_plus_y1 = 2_i32.pow(30);
    let mut min_x1_minus_y1 = 2_i32.pow(30);
    let mut max_x1_plus_y1 = -2_i32.pow(30);
    let mut max_x1_minus_y1 = -2_i32.pow(30);
    for r in 0..rows {
        for c in 0..columns {
            if too_far_grid[r as usize][c as usize] {
                if r + c < min_x1_plus_y1 {
                    min_x1_plus_y1 = r + c;
                }
                if r - c < min_x1_minus_y1 {
                    min_x1_minus_y1 = r - c;
                }
                if r + c > max_x1_plus_y1 {
                    max_x1_plus_y1 = r + c;
                }
                if r - c > max_x1_minus_y1 {
                    max_x1_minus_y1 = r - c;
                }
            }
        }
    }
    
    for r in 0..rows {
        for c in 0..columns {
            if (max_x1_plus_y1 - (r + c)).abs() <= max_allowable_distance 
                    && (max_x1_minus_y1 - (r - c)).abs() <= max_allowable_distance 
                    && (min_x1_plus_y1 - (r + c)).abs() <= max_allowable_distance
                    && (min_x1_minus_y1 - (r - c)).abs() <= max_allowable_distance  {
                return true;
            }
        }
    }
    return false;
}

// 0 1 0
// 1 2 1
// 0 1 0
// [0, 2]
// 1 OK
// [0, 1]
// 0 NOT OK
// [1, 1]
// 1 OK
// return 1
// 0 1 2 1 0
// 1 2 3 2 1
// 2 3 4 3 2
// 1 2 3 2 1
// 0 1 2 1 0
// [0, 4]
// 2 OK
// [0, 2]
// 1 NOT OK
// [2, 2]
// 2 OK RETURN
// 4 3 4
// 3 2 3
// 2 1 2
// 1 0 1
// [0, 4]
// 2 OK
// [0, 2]
fn binary_search_problem(
    rows: i32,
    columns: i32,
    scores: &Vec<Vec<i32>>,
    min_range: i32,
    max_range: i32
) -> i32 {
    if min_range == max_range {
        return min_range;
    }

    let expected_max_score = (min_range + max_range)/2;
    if man_dist_fits(rows, columns, scores, expected_max_score) {
        return binary_search_problem(rows, columns, scores, min_range, expected_max_score);
    } else {
        return binary_search_problem(rows, columns, scores, expected_max_score + 1, max_range);
    }
}

fn solve(
    rows: i32,
    columns: i32,
    grid: &Vec<Vec<i32>>
) -> i32 {
    let scores = bfs_score(rows, columns, grid);
    let max_score = max_grid(rows, columns, &scores);
    if max_score == 0 {
        return 0;
    }

    return binary_search_problem(rows, columns, &scores, 0, max_score);
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

fn handle_rows_and_columns(rows: i32, columns: i32) {
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
    let result = solve(rows, columns, &mut grid);
    println!("{}", result);
}

fn handle_test_case() {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            let row_col: Vec<&str> = buffer.split(' ').collect();
            
            match row_col[0].trim().parse::<i32>() {
                Ok(rows) => {
                    match row_col[1].trim().parse::<i32>() {
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

#[cfg(test)]
mod tests {
    // Note this useful idiom: importing names from outer (for mod tests) scope.
    use super::*;


    #[test]
    fn test_man_dist_fits_4_by_2(){
        assert_eq!( man_dist_fits(4,2,
                &vec!(
                    vec!(0, 0),
                    vec!(0, 0),
                    vec!(0, 0),
                    vec!(1, 1),
                ),
                1),
            false);
    }

    #[test]
    fn solve_one_row() {
        assert_eq!(solve(1, 6,
                &vec!(
                    vec!(1, 0, 0, 0, 0, 0)
                )),
            2);
        assert_eq!(solve(1, 5,
                &vec!(
                    vec!(1, 0, 0, 0, 0)
                )),
            1);
        assert_eq!(solve(1, 4,
                &vec!(
                    vec!(1, 0, 0, 0)
                )),
            1);
    }

    #[test]
    fn solve_one_column() {
        assert_eq!(solve(6, 1,
                &vec!(
                    vec!(1),
                    vec!(0),
                    vec!(0),
                    vec!(0),
                    vec!(0),
                    vec!(0)
                )),
            2);
        assert_eq!(solve(5, 1,
                &vec!(
                    vec!(1),
                    vec!(0),
                    vec!(0),
                    vec!(0),
                    vec!(0)
                )),
            1);
        assert_eq!(solve(4, 1,
                &vec!(
                    vec!(1),
                    vec!(0),
                    vec!(0),
                    vec!(0),
                    vec!(0)
                )),
            1);
    }

    #[test]
    fn solve_no_good_spot() {
        assert_eq!(solve(4, 3,
                &vec!(
                    vec!(0,1,0),
                    vec!(0,1,0),
                    vec!(0,0,0),
                    vec!(1,0,1),
                )),
            1);
        assert_eq!(solve(5, 5,
                &vec!(
                    vec!(0,0,0,0,0),
                    vec!(0,0,0,0,0),
                    vec!(0,0,1,0,0),
                    vec!(0,0,0,0,0),
                    vec!(0,0,0,0,0),
                )),
            4);
    }
    
    #[test]
    fn solve_3_corners() {
        assert_eq!(solve(5, 5,
                &vec!(
                    vec!(1,0,0,0,0),
                    vec!(0,0,0,0,0),
                    vec!(0,0,0,0,0),
                    vec!(0,0,0,0,0),
                    vec!(1,0,0,0,1),
                )),
            3);
        assert_eq!(solve(5, 5,
                &vec!(
                    vec!(1,0,0,0,1),
                    vec!(0,0,0,0,0),
                    vec!(0,0,0,0,0),
                    vec!(0,0,0,0,0),
                    vec!(1,0,0,0,0),
                )),
            3);
        assert_eq!(solve(5, 5,
                &vec!(
                    vec!(1,0,0,0,1),
                    vec!(0,0,0,0,0),
                    vec!(0,0,0,0,0),
                    vec!(0,0,0,0,0),
                    vec!(0,0,0,0,1),
                )),
            3);
        assert_eq!(solve(5, 5,
            &vec!(
                vec!(0,0,0,0,1),
                vec!(0,0,0,0,0),
                vec!(0,0,0,0,0),
                vec!(0,0,0,0,0),
                vec!(1,0,0,0,1),
            )),
        3);
    }

    #[test]
    fn solve_not_max() {
        assert_eq!(solve(5, 5,
                &vec!(
                    vec!(0,0,0,0,0),
                    vec!(0,0,0,0,0),
                    vec!(0,0,0,0,0),
                    vec!(0,0,0,0,0),
                    vec!(1,0,1,0,0),
                )),
            3);
    }
    
    #[test]
    fn solve_broken_4_by_2_case() {
        assert_eq!(solve(4, 2,
                &vec!(
                    vec!(0,0),
                    vec!(0,0),
                    vec!(0,0),
                    vec!(1,1),
                )),
            2);
    }

    fn score_position(
        rows: i32,
        columns: i32,
        grid: &mut Vec<Vec<i32>>
    ) -> i32 {
        let scores = bfs_score(rows, columns, grid);
        let mut max_so_far = 0;
        for r in 0..rows {
            for c in 0..columns {
                if scores[r as usize][c as usize] > max_so_far {
                    max_so_far = scores[r as usize][c as usize];
                }
            }
        }
        return max_so_far;
    }
    
    fn solve_slowly(
        rows: i32,
        columns: i32,
        grid: &mut Vec<Vec<i32>>
    ) -> i32 {
    
        let mut min_so_far = None;
        for r in 0..rows {
            for c in 0..columns {
                if grid[r as usize][c as usize] == 0 {
                    // change it to 1
                    grid[r as usize][c as usize] = 1;
                    // score it
                    let score = score_position(rows, columns, grid);
                    // change it back
                    grid[r as usize][c as usize] = 0;
    
                    match min_so_far {
                        Some(min) => {
                            if score < min {
                                min_so_far = Some(score);
                            }
                        },
                        None => {
                            min_so_far = Some(score)
                        }
                    }
                    
                }
            }
        }
    
        match min_so_far {
            Some(min) => {
                return min;
            },
            None => {
                return 0;
            }
        }
    }

    fn generate_grid_str(
        rows: i32,
        columns: i32,
        grid: &mut Vec<Vec<i32>>
    ) -> String {
        let mut result = String::new();
        for r in 0..rows {
            for c in 0..columns {
                result.push_str(grid[r as usize][c as usize].to_string().as_str());
                result.push_str(" ");
            }
            result.push_str("\n");
        }
        return result;
    }

    fn compare_to_slow(
        rows: i32,
        columns: i32,
        grid: &mut Vec<Vec<i32>>
    ) {
        assert_eq!(
            solve(rows, columns, grid),
            solve_slowly(rows, columns, grid),
            "rows {} columns {}\ngrid\n{}",
            rows,
            columns,
            generate_grid_str(rows, columns, grid)
        );
    }

    #[test]
    fn test_compare_to_slow() {
        for cell_0 in 0..2 {
            for cell_1 in 0..2 {
                for cell_2 in 0..2 {
                    for cell_3 in 0..2 {
                        for cell_4 in 0..2 {
                            for cell_5 in 0..2 {
                                for cell_6 in 0..2 {
                                    for cell_7 in 0..2 {
                                        if cell_0 > 0 || cell_1 > 0 || cell_2 > 0 || cell_3 > 0 || cell_4 > 0 || cell_5 > 0 || cell_6 > 0 || cell_7 > 0 {
                                            compare_to_slow(1, 8, &mut vec!(
                                                vec!(cell_0, cell_1, cell_2, cell_3, cell_4, cell_5, cell_6, cell_7),
                                            ));
                                            compare_to_slow(8, 1, &mut vec!(
                                                vec!(cell_0),
                                                vec!(cell_1),
                                                vec!(cell_2),
                                                vec!(cell_3),
                                                vec!(cell_4),
                                                vec!(cell_5),
                                                vec!(cell_6),
                                                vec!(cell_7),
                                            ));
                                            compare_to_slow(2, 4, &mut vec!(
                                                vec!(cell_0, cell_1, cell_2, cell_3),
                                                vec!(cell_4, cell_5, cell_6, cell_7),
                                            ));
                                            compare_to_slow(4, 2, &mut vec!(
                                                vec!(cell_0, cell_1),
                                                vec!(cell_2, cell_3),
                                                vec!(cell_4, cell_5),
                                                vec!(cell_6, cell_7),
                                            ));
                                        }
                                    }
                                    if cell_0 > 0 || cell_1 > 0 || cell_2 > 0 || cell_3 > 0 || cell_4 > 0 || cell_5 > 0 || cell_6 > 0 {
                                        compare_to_slow(1, 7, &mut vec!(
                                            vec!(cell_0, cell_1, cell_2, cell_3, cell_4, cell_5, cell_6),
                                        ));
                                        compare_to_slow(7, 1, &mut vec!(
                                            vec!(cell_0),
                                            vec!(cell_1),
                                            vec!(cell_2),
                                            vec!(cell_3),
                                            vec!(cell_4),
                                            vec!(cell_5),
                                            vec!(cell_6),
                                        ));
                                    }
                                }
                                if cell_0 > 0 || cell_1 > 0 || cell_2 > 0 || cell_3 > 0 || cell_4 > 0 || cell_5 > 0 {
                                    compare_to_slow(1, 6, &mut vec!(
                                        vec!(cell_0, cell_1, cell_2, cell_3, cell_4, cell_5),
                                    ));
                                    compare_to_slow(6, 1, &mut vec!(
                                        vec!(cell_0),
                                        vec!(cell_1),
                                        vec!(cell_2),
                                        vec!(cell_3),
                                        vec!(cell_4),
                                        vec!(cell_5),
                                    ));
                                    compare_to_slow(2, 3, &mut vec!(
                                        vec!(cell_0, cell_1, cell_2),
                                        vec!(cell_3, cell_4, cell_5),
                                    ));
                                    compare_to_slow(3, 2, &mut vec!(
                                        vec!(cell_0, cell_1),
                                        vec!(cell_2, cell_3),
                                        vec!(cell_4, cell_5),
                                    ));
                                }
                            }
                            if cell_0 > 0 || cell_1 > 0 || cell_2 > 0 || cell_3 > 0 || cell_4 > 0 {
                                compare_to_slow(1, 5, &mut vec!(
                                    vec!(cell_0, cell_1, cell_2, cell_3, cell_4),
                                ));
                                compare_to_slow(5, 1, &mut vec!(
                                    vec!(cell_0),
                                    vec!(cell_1),
                                    vec!(cell_2),
                                    vec!(cell_3),
                                    vec!(cell_4),
                                ));
                            }
                        }
                        if cell_0 > 0 || cell_1 > 0 || cell_2 > 0 || cell_3 > 0 {
                            compare_to_slow(1, 4, &mut vec!(
                                vec!(cell_0, cell_1, cell_2, cell_3),
                            ));
                            compare_to_slow(4, 1, &mut vec!(
                                vec!(cell_0),
                                vec!(cell_1),
                                vec!(cell_2),
                                vec!(cell_3),
                            ));
                            compare_to_slow(2, 2, &mut vec!(
                                vec!(cell_0, cell_1),
                                vec!(cell_2, cell_3),
                            ));
                        }
                    }
                    if cell_0 > 0 || cell_1 > 0 || cell_2 > 0 {
                        compare_to_slow(1, 3, &mut vec!(
                            vec!(cell_0, cell_1, cell_2),
                        ));
                        compare_to_slow(3, 1, &mut vec!(
                            vec!(cell_0),
                            vec!(cell_1),
                            vec!(cell_2),
                        ));
                    }
                }
                if cell_0 > 0 || cell_1 > 0 {
                    compare_to_slow(1, 2, &mut vec!(
                        vec!(cell_0, cell_1),
                    ));
                    compare_to_slow(2, 1, &mut vec!(
                        vec!(cell_0),
                        vec!(cell_1),
                    ));
                }
            }
            if cell_0 > 0 {
                compare_to_slow(1, 1, &mut vec!(
                    vec!(cell_0),
                ));
            }
        }
    }
}