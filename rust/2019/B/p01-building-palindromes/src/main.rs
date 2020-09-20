use std::io;

fn solve(
    num_blocks: i64,
    num_questions: i64,
    input_str: String,
    values: Vec<(i64,i64)>
) -> i64 {
    return 0;
}

fn parse_line() -> Result<String, String> {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            return Ok(buffer.trim().to_string());
        },
        Err(error) => Err("Unable to parse vector line".to_string())
    }
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

fn parse_many_pairs(
    num_pairs: i64
) -> Result<Vec<(i64,i64)>, String> {
    let mut result = Vec::new();

    for line_num in 0..num_pairs {
        match parse_pair_int_line() {
            Ok(pair) => {
                result.push(pair);
            },
            Err(error) => return Err("could not parse pair from line".to_string())
        }
    }

    return Ok(result);
}

fn handle_test_case() {
    match parse_pair_int_line() {
        Ok((first_value, second_value)) => {
            match parse_line() {
                Ok(input_string) => {
                    match parse_many_pairs(second_value) {
                        Ok(values) => {
                            let result = solve(first_value, second_value, input_string, values);
                            println!("{}", result);
                        },
                        Err(error) => println!("error: {}", error)
                    }
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

