use std::io;



fn solve(bookings: Vec<(i32,i32)>)
-> i32
{
    return 0;
}

fn parse_row() -> Result<(i32,i32)>, io::Error> {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        // trim is needed to get rid of the newline
        Ok(_n) => {
            let booking: Vec<&str> = buffer.split(' ').collect();

            match booking[0].trim().parse::<i32>() {
                Ok(lower) => {
                    match problem_sizes[1].trim().parse::<i32>() {
                        Ok(upper) => {
                            return (lower, upper);
                        },
                        Err(parse_error) => parse_error
                    }
                },
                Err(parse_error) => parse_error
            }
        },
        Err(io_error) => Err(io_error)
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
    let result = solve(rows, columns, &mut grid);
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