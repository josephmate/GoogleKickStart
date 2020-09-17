use std::io;

fn solve(
    num_checkpoints: i32,
    mut player_scores: Vec<i32>
) -> i32 {
   return 0;
}

fn parse_int_vector_line() -> Result<Vec<i32>, String> {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            return Ok(buffer.split(' ')
                .map(|score_as_str| score_as_str.trim().parse::<i32>()
                    .expect("Expected all items to be an integer"))
                .collect());
        },
        Err(error) => Err("Unable to parse vector line".to_string())
    }
}

fn parse_int_line() -> Result<i32, String> {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            match buffer.trim().parse::<i32>() {
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


fn parse_pair_int_line() -> Result<(i32, i32), String> {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            let row: Vec<&str> = buffer.split(' ').collect();
            match row[0].trim().parse::<i32>() {
                Ok(cell1) => {
                    match row[1].trim().parse::<i32>() {
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
    match parse_int_line() {
        Ok(num_checkpoints) => {
            match parse_int_vector_line() {
                Ok(checkpoint_heights) => {
                    let result = solve(num_checkpoints, checkpoint_heights);
                    println!("{}", result);
                },
                Err(error) => println!("error: {}", error)
            }
        }
        Err(error) => println!("error: {}", error)
    }
}

fn handle_test_cases(test_cases: i32) {
    for x in 1..(test_cases+1) {
        print!("Case #{}: ", x);
        handle_test_case();
    }
}

fn main() {
    let mut buffer = String::new();
    match parse_int_line() {
        Ok(test_cases) => {
            handle_test_cases(test_cases);
        },
        Err(error) => println!("error: {}", error)
    }
}

