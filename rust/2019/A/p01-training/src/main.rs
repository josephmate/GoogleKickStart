use std::io::{self, Read};

// input format:
// T
// N_1 P_1
// S_1 S_2 S_3 ... S_N_1
// ...
// N_T P_T
// S_1 S_2 S_3 ... S_N_T

fn solve_scores(
    num_to_pick: i32,
    player_scores: Vec<i32>
) {
    println!("{}", 
        player_scores
            .iter()
            .map(|score_as_int| score_as_int.to_string())
            .collect::<Vec<String>>()
            .join(" "))
}

fn handle_test_case_scores(
    num_of_students: i32,
    num_to_pick: i32,
    mut buffer: &mut String
) {
    println!("{} {}", num_of_students, num_to_pick);
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            let player_scores: Vec<i32> = buffer.split(' ')
                .map(|score_as_str| score_as_str.trim().parse::<i32>()
                    .expect("Expected all student scores to be integers"))
                .collect();
            buffer.clear();
            solve_scores(num_to_pick, player_scores);
        },
        Err(error) => println!("error: {}", error)
    }
}

fn handle_test_case(
    mut buffer: &mut String
) {
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            let test_case_sizes: Vec<&str> = buffer.split(' ').collect();
            let num_of_students = test_case_sizes[0].trim().parse::<i32>()
                .expect(std::format!("1st argument should be a number but got {}", buffer).as_str());
            let num_to_pick = test_case_sizes[1].trim().parse::<i32>()
                .expect(std::format!("2nd argument should be a number but got {}", buffer).as_str());
            buffer.clear();
            handle_test_case_scores(num_of_students, num_to_pick, &mut buffer);
        },
        Err(error) => println!("error: {}", error)
    }
}

fn handle_test_cases(
        num_test_cases: i32,
        mut buffer: &mut String
) {
    println!("{}", num_test_cases);
    for x in 1..=num_test_cases {
        handle_test_case(&mut buffer);
    }
}

fn main() {
    let mut buffer = String::new();
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            let num_test_cases = buffer.trim().parse::<i32>()
                .expect(std::format!("First line shoudld be a number but got {}", buffer).as_str());
            buffer.clear();
            handle_test_cases(num_test_cases, &mut buffer);
        },
        Err(error) => println!("error: {}", error)
    }
}

