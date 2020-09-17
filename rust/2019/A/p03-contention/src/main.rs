use std::io;

// N : number of seats
// Q : number of bookings
// k : each booking assigned at least k seats 
// Brainstorming
// - naive: try all order permutations
//     - O( Q! * Q * N)
// - do something similar to problem 2 where we binary search on k
// - 
// - represent the problem as a graph
//      - each booking is a node, edge between if they overlap
//      - each booking,seat pair is a node, there is an edge if they


fn solve(
    bookings: Vec<(i32,i32)>)
    -> i32
{
    return 0;
}

fn parse_row() -> Result<(i32,i32), String> {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        // trim is needed to get rid of the newline
        Ok(_n) => {
            let booking: Vec<&str> = buffer.split(' ').collect();

            match booking[0].trim().parse::<i32>() {
                Ok(lower) => {
                    match booking[1].trim().parse::<i32>() {
                        Ok(upper) => {
                            Result::Ok((lower, upper))
                        },
                        Err(parse_error) => Result::Err(String::from("Second param did not parse to int"))
                    }
                },
                Err(parse_error) => Result::Err(String::from("First param did not parse to int"))
            }
        },
        Err(io_error) => Result::Err(String::from("Could not read line"))
    }
}

fn handle_seats_and_bookings(num_seats: i32, num_bookings: i32) {
    let mut bookings: Vec<(i32,i32)> = Vec::new();
    for _i in 0..num_bookings {
        match parse_row() {
            Ok(row) => {
                bookings.push(row);
            }
            Err(io_error) => {
                print!("io error: {}", io_error);
                return;
            }
        }
    }
    let result = solve(bookings);
    println!("{}", result);
}

fn handle_test_case() {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            let problem_sizes: Vec<&str> = buffer.split(' ').collect();
            
            match problem_sizes[0].trim().parse::<i32>() {
                Ok(num_seats) => {
                    match problem_sizes[1].trim().parse::<i32>() {
                        Ok(num_bookings) => {
                            handle_seats_and_bookings(num_seats, num_bookings);
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

}