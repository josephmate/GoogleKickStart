use std::io;

fn diff(
    length: i32,
    mut values: Vec<i32>
) -> Vec<i32> {
    let mut result = Vec::new();
    for index in 1..length as usize {
        result.push(values[index] - values[index - 1]);
    }
    return result;
}

fn solve(
    length: i32,
    mut values: Vec<i32>
) -> i32 {
    if length == 0 {
        return 0;
    }
    if length == 1 {
        return 1;
    }
    let diffs = diff(length, values);
    let mut same_diff_count = 2;
    let mut max_diff_count = 2;
    for index in 1..(length-1) as usize {
        if (diffs[index] == diffs[index-1]) {
            same_diff_count += 1;
            if same_diff_count > max_diff_count {
                max_diff_count = same_diff_count;
            }
        } else {
            same_diff_count = 2;
        }
    }
    return max_diff_count;
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
        Ok(length) => {
            match parse_int_vector_line() {
                Ok(values) => {
                    let result = solve(length, values);
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
    match parse_int_line() {
        Ok(test_cases) => {
            handle_test_cases(test_cases);
        },
        Err(error) => println!("error: {}", error)
    }
}

