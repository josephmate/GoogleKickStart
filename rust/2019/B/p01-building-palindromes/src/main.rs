use std::io;
use std::collections::HashMap;

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

// can pre compute prefixes and suffixes
// Ex:
// A
// A B
// A B C D
// A B C D E
//         E
//       D E
//     C D E
//   B C D E
// Now to make any count, we can do
// 1 to 3 : already precomputed
// 2 to 4 : 1 to 4 minus 1 to 1
// 3 to 5 : 1 to 5 minus 1 to 2
// in general X Y can be calculated with:
// 1 to Y minus 1 to (X - 1)

fn clone_map(
    char_counts: HashMap<char, i64>
) -> HashMap<char, i64> {
    let mut cloned_map = HashMap::new();
    for (key, value) in char_counts {
        cloned_map.insert(key, value);
    }
    return cloned_map;
}

fn pre_compute_prefix_char_counts(
    num_blocks: i64,
    input_str: String
) -> HashMap<i64, HashMap<char, i64>> {
    let mut accumulated_char_counts = HashMap::new();
    let mut pre_computed_prefix_char_counts = HashMap::new();

    let mut position = 1;
    for character in input_str.chars() { 
        let count = accumulated_char_counts.entry(character).or_insert(0);
        *count +=1;
        pre_computed_prefix_char_counts.insert(position, clone_map(accumulated_char_counts));
        position += 1;
    }

    return pre_computed_prefix_char_counts;
}

///fn subtract_map(

//)

fn can_become_palindrome(
    lower_bound: i64,
    upper_bound: i64,
    pre_computed_prefix_char_counts: &HashMap<i64, HashMap<char, i64>>

) -> bool {
    let empty_map = HashMap::new();
    let big_map = &pre_computed_prefix_char_counts[&upper_bound];
    let small_map = if lower_bound > 1 {
        &pre_computed_prefix_char_counts[&(lower_bound - 1)]
    } else {
        &empty_map
    };
    return false;
}

fn solve(
    num_blocks: i64,
    num_questions: i64,
    input_str: String,
    values: Vec<(i64,i64)>
) -> i64 {
    let pre_computed_prefix_char_counts = pre_compute_prefix_char_counts(num_blocks, input_str);
    let mut total_palindromes = 0;
    for index in 0..num_questions {
        let (lower_bound, upper_bound) = values[index as usize];
        if can_become_palindrome(lower_bound, upper_bound, &pre_computed_prefix_char_counts) {
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

