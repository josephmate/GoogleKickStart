use std::io;
use std::collections::HashSet;

// naive solution that attempts all potentials post offices
// for each potential post office compute the score
// to compute the score, calculate the distance to the nearest post office using bfs
// complexity:
// N * N * N * N * N * N
// \   /   \   /   \   /
//  \ /     \ /     BFS to find nearest post office (could start from one corner and post office in opposite corner)
//   |       Finds the manning distance for each position 
//   Trying every possible post office position

fn bfs_post_office_impl(
    current_row: usize,
    current_column: usize,
    rows: usize,
    columns: usize,
    grid: & Vec<Vec<i32>>,
    visited: &mut HashSet<(i32, i32)>
) -> Option<(usize, usize)> {
    if grid[current_row][current_col] == 1 {
        return Some(current_row, current_col);
    }
}

fn bfs_post_office(
    start_row: usize,
    start_column: usize,
    rows: usize,
    columns: usize,
    grid: &mut Vec<Vec<i32>>
) -> (usize, usize) {
    bfs_post_office_impl(start_row, start_column, rows, columns, &grid, &mut HashSet::new())
}

// The Manhattan distance between two squares (r1,c1) and (r2,c2) is defined as |r1 - r2| + |c1 - c2|, where |*| operator denotes the absolute value.
fn manhattan_distance(
    start_row: usize,
    start_column: usize,
    end_row: usize,
    end_column: usize,
) -> i32 {
    (start_row as i32 - end_row as i32).abs()
    + (start_column as i32 - end_column as i32).abs()
}

fn score_position(
    rows: usize,
    columns: usize,
    grid: &mut Vec<Vec<i32>>
) -> i32 {
    let mut score_so_far = 0;

    for r in 0..rows {
        for c in 0..columns {
            let (nearest_row, nearest_col) = bfs_post_office(r, c, rows, columns, grid);
            score_so_far += manhattan_distance(r, c, nearest_row, nearest_col);
        }
    }

    return score_so_far;
}

fn solve(
    rows: usize,
    columns: usize,
    grid: &mut Vec<Vec<i32>>
) {
    let mut min_so_far = std::i32::MAX;
    for r in 0..rows {
        for c in 0..columns {
            if grid[r][c] == 0 {
                // change it to 1
                grid[r][c] = 1;
                // score it
                let score = score_position(rows, columns, grid);
                // change it back
                grid[r][c] = 0;

                if score < min_so_far {
                    min_so_far = score;
                }
            }
        }
    }
    println!("{}", min_so_far)
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
                print!("{}", io_error);
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

