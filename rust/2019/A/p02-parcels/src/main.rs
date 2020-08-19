use std::io;
use std::collections::HashSet;
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

fn bfs_post_office_impl(
    start_row: usize,
    start_column: usize,
    rows: usize,
    columns: usize,
    grid: & Vec<Vec<i32>>,
) -> Option<(usize, usize)> {
    let mut visited: HashSet<(usize, usize)> = HashSet::new();
    let mut bfs_queue: VecDeque<(usize, usize)> = VecDeque::new();
    bfs_queue.push_back((start_row, start_column));

    loop {
        match bfs_queue.pop_front() {
            Some((current_row, current_column)) => {
                if grid[current_row][current_column] == 1 {
                    return Some((current_row, current_column));
                }
                visited.insert((current_row, current_column));

                if current_row + 1 < rows {
                    bfs_queue.push_back((current_row + 1, current_column));
                }
                if current_row as i32 - 1 >= 0 {
                    bfs_queue.push_back((current_row - 1, current_column));
                }
                if current_column + 1 < columns {
                    bfs_queue.push_back((current_row, current_column + 1));
                }
                if current_column as i32 - 1 >= 0 {
                    bfs_queue.push_back((current_row, current_column - 1));
                }
            },
            None => {break;}
        }
    }

    return None;
}

fn bfs_post_office(
    start_row: usize,
    start_column: usize,
    rows: usize,
    columns: usize,
    grid: &mut Vec<Vec<i32>>
) -> (usize, usize) {
    bfs_post_office_impl(start_row, start_column, rows, columns, grid)
        .expect("There should be at least one post office in the grid, so something must be found.")
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
            let distance  = manhattan_distance(r, c, nearest_row, nearest_col);
            if distance > score_so_far {
                score_so_far = distance;
            }
        }
    }

    return score_so_far;
}

fn solve(
    rows: usize,
    columns: usize,
    grid: &mut Vec<Vec<i32>>
) {
    let mut min_so_far = None;
    for r in 0..rows {
        for c in 0..columns {
            if grid[r][c] == 0 {
                // change it to 1
                grid[r][c] = 1;
                // score it
                let score = score_position(rows, columns, grid);
                // change it back
                grid[r][c] = 0;

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
            println!("{}", min)
        },
        None => {
            println!("{}", 0)
        }
    }
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

