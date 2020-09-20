use std::io;

fn solve(
    num_bus_rides: i64,
    final_day: i64,
    mut bus_schedules: Vec<i64>
) -> i64 {
    let mut current_bus = num_bus_rides - 1;
    let mut current_day = final_day;

    while current_day > 0  && current_bus >= 0 {
        let current_bus_schedule = bus_schedules[current_bus as usize];
        if current_day % current_bus_schedule == 0 {
            current_bus -= 1;
        } else {
            // integer division. since a % b > 0 then a / b * b < a
            current_day = (current_day/current_bus_schedule) * current_bus_schedule;
        }
    }

    return current_day;
}

fn parse_int_vector_line() -> Result<Vec<i64>, String> {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            return Ok(buffer.split(' ')
                .map(|score_as_str| score_as_str.trim().parse::<i64>()
                    .expect("Expected all items to be an integer"))
                .collect());
        },
        Err(error) => Err("Unable to parse vector line".to_string())
    }
}

fn parse_int_line() -> Result<i64, String> {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            match buffer.trim().parse::<i64>() {
                Ok(result) => {
                    return Ok(result);
                },
                Err(_error) => {
                    return Err("1st argument should be a number".to_string());
                }
            }
        },
        Err(error) => Err(error.to_string())
    }
}

fn parse_pair_int_line() -> Result<(i64, i64), String> {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            let row: Vec<&str> = buffer.split(' ').collect();
            match row[0].trim().parse::<i64>() {
                Ok(cell1) => {
                    match row[1].trim().parse::<i64>() {
                        Ok(cell2) => {
                            return Ok((cell1, cell2));
                        },
                        Err(_error) => {
                            return Err("2nd argument should be a number".to_string());
                        }
                    }
                },
                Err(_error) => {
                    return Err("1st argument should be a number".to_string());
                }
            }
        },
        Err(error) => Err(error.to_string())
    }
}

fn handle_test_case() {
    match parse_pair_int_line() {
        Ok((first_value, second_value)) => {
            match parse_int_vector_line() {
                Ok(values) => {
                    let result = solve(first_value, second_value, values);
                    println!("{}", result);
                },
                Err(error) => println!("error: {}", error)
            }
        }
        Err(error) => println!("error: {}", error)
    }
}

fn handle_test_cases(test_cases: i64) {
    for x in 1..(test_cases+1) {
        print!("Case #{}: ", x);
        handle_test_case();
    }
}

fn main() {
    match parse_int_line() {
        Ok(test_cases) => {
            handle_test_cases(test_cases);
        },
        Err(error) => println!("error: {}", error)
    }
}

