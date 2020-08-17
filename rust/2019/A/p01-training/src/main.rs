use std::io;

// input format:
// T
// N_1 P_1
// S_1 S_2 S_3 ... S_N_1
// ...
// N_T P_T
// S_1 S_2 S_3 ... S_N_T

/// Old Solution
/// To solve this problem, notice two key properties.
/// Firstly, to consider all possible solutions, you need to go from the least trained to the most trained students.
/// Secondly, you could naively move a pointer from smallest to largest.
///     For each movement of the pointer, create a second pointer starting at the current pointer and keep moving it
///     left until you have enough students, keeping track of the training necessary.
/// This is O(N*N) in the worst case.
/// 
/// To improve this, I noticed that if you start from the left, and keep moving the point to the right until it's
/// 'full', keeping track the amount of training need as you're moving the pointer.
/// Now you keep a second pointer at the least skilled player. Move the first pointer to the right by one, then update
/// the left pointer until you have enough just enough players. Make sure to keep track of the min training needed so far.
/// This is O(2*N) because you point pointers only move to the right, so they cannot return to a previous student.
///
/// Why Old Solution was Poor
/// "Then, another line follows containing N integers S_i; the i-th of these is the skill of the i-th student.
/// I don't need to group together the repeated students. I can just have a constant 'window' or contiguous array.
/// The sorted map over complicates the problem.
///
/// Second attempt
/// sort the student scores (this will place the closest scores together)
/// fill from beginning until left until enough students
/// move left, adding the highest, removing the lowest
/// it only makes sense to consider all continuous sub arrays from the sorted array.
/// any non contiguous sub array will have a small contiguous version.
fn solve_scores(
    num_to_pick: i32,
    mut player_scores: Vec<i32>
) {
    player_scores.sort();

    let mut lowest_index = 0;
    let mut highest_index = num_to_pick - 1;
    // fill window until we have enough players
    let mut training_needed = 0;
    for i in 0..num_to_pick-1 {
        training_needed +=  player_scores[highest_index as usize] - player_scores[i as usize];
    }
    let mut min_so_far = training_needed;

    while ((highest_index + 1) as usize) < player_scores.len() {
        // assume window length 4
        //
        //          v     V
        // idx    0 1 2 3 4  5 
        // scores 1 1 5 5 9 10
        
        // move the lowest index over and get rid of the training needed for the lowest
        training_needed -= player_scores[highest_index as usize] - player_scores[lowest_index as usize];
        lowest_index += 1;

        // move the highest_index index over
        // all the lower indexes have already been trained to highest_index, so we only need
        // ( player_scores[highest_index+1] - player_scores[highest_index] ) * (num_to_pick - 1)
        // to move them over
        // num_to_pick - 1 since we already moved the lowest
        training_needed += (player_scores[(highest_index + 1) as usize]- player_scores[highest_index as usize])
            * (num_to_pick - 1);
        highest_index += 1;

        if training_needed < min_so_far {
            min_so_far = training_needed;
        }
    }
    
    println!("{}", min_so_far)
}

fn handle_test_case_scores(
    num_of_students: i32,
    num_to_pick: i32,
    mut buffer: &mut String
) {
    match io::stdin().read_line(&mut buffer) {
        Ok(_n) => {
            let player_scores: Vec<i32> = buffer.split(' ')
                .map(|score_as_str| score_as_str.trim().parse::<i32>()
                    .expect("Expected all student scores to be integers"))
                .collect();
            buffer.clear();
            if num_of_students > 0 {
                solve_scores(num_to_pick, player_scores);
            } else {
                println!("{}", 0)
            }
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
    for x in 1..=num_test_cases {
        print!("Case #{}: ", x);
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

