use std::io;
use std::collections::HashMap;
use std::collections::hash_map::Entry::{Occupied, Vacant};

// palindrome of odd length
// BAAAB
// Must have only one character with odd count, all other must be even
// If more than one character has odd count, then it's impossible to create a palindrom.

// palindrome of even length
// ABBA
// Can have as many even character counts.
// If any are odd, then there are an even number of them.
// Then,Impossible to create a palindrome.

// In summary, character with odd count must be 0 or 1.

fn can_become_palindrome_inner(
    input_str: String,
) -> bool {
    let mut char_counts = HashMap::new();
    for character in input_str.as_str().chars() { 
        let count = char_counts.entry(character).or_insert(0);
        *count +=1;
    }

    let mut odd_count = 0;
    for (character, count) in &char_counts {
        if count % 2 == 1 {
            odd_count += 1;
        }
    }

    return odd_count <= 1;
}

fn can_become_palindrome(
    lower_bound: i64,
    upper_bound: i64,
    input_str: &str,
) -> bool {
    match input_str.get(((lower_bound-1) as usize)..(upper_bound as usize)) {
        Some(slice) => can_become_palindrome_inner(slice.to_string()),
        None => {
            println!("Err");
            false
        }
    }
}

fn solve(
    num_blocks: i64,
    num_questions: i64,
    input_str: String,
    values: Vec<(i64,i64)>
) -> i64 {
    let mut total_palindromes = 0;
    let mut previous_answers = HashMap::new();
    for index in 0..num_questions {
        let (lower_bound, upper_bound) = values[index as usize];
        
        let can_be = previous_answers.entry((lower_bound, upper_bound))
            .or_insert_with(|| can_become_palindrome(lower_bound, upper_bound, &input_str.as_str()));
        /*
        let can_be = match previous_answers.entry((lower_bound, upper_bound)) {
            Vacant(entry) => entry.insert(can_become_palindrome(lower_bound, upper_bound, input_str)),
            Occupied(entry) => entry.into_mut(),
        };
        */

        if *can_be {
            total_palindromes += 1;
        }
    }
    return total_palindromes;
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

