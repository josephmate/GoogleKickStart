use std::io;
use std::collections::BTreeMap;

// input format:
// T
// N_1 P_1
// S_1 S_2 S_3 ... S_N_1
// ...
// N_T P_T
// S_1 S_2 S_3 ... S_N_T

struct TrainingTracker <'a> {
    min_so_far: i32,
    players_so_far: i32,
    training_so_far: i32,
    num_players_at_level: BTreeMap<i32, i32>,
    lowest_level_so_far_level: i32, 
    lowest_level_so_far_count: i32, 
    highest_level_so_far_level: i32, 
    highest_level_so_far_count: i32, 
    lowest_level_so_far_itr: &'a mut dyn std::iter::Iterator<Item =  i32>,
    highest_level_so_far_itr: &'a mut dyn std::iter::Iterator<Item =  i32>
}

fn group_players_by_level<'a>(player_scores: Vec<i32>) -> BTreeMap<i32, i32> {
    let mut num_players_at_level = BTreeMap::new();
    for player_score in player_scores {
        match num_players_at_level.get(&player_score) {
            Some(player_count) => num_players_at_level.insert(player_score, player_count + 1),
            None => num_players_at_level.insert(player_score, 1)
         };
    }
    return num_players_at_level;
}

fn add_next_level(num_to_pick: i32, training_tracker: &mut TrainingTracker) -> bool {
    let even_higher_player_level: i32;
     match (*training_tracker).highest_level_so_far_itr.next() {
        Some(v) => {even_higher_player_level = v},
        None => {return false}
    };
    let even_higher_player_count = *training_tracker.num_players_at_level.get(&even_higher_player_level)
        .expect(std::format!("even_higher_player_level should be in the map {}", even_higher_player_level).as_str());


    let mut new_players_so_far = training_tracker.players_so_far + even_higher_player_count;
    // need to remove from lowest levels
    // while loop because the even_higher_player_level_count could include multiple levels
    while new_players_so_far > num_to_pick {
        // figure out how many of the lowest level we need to remove
        let num_overfilled_by = new_players_so_far - num_to_pick;
        let num_overfilled_by = if num_overfilled_by > training_tracker.lowest_level_so_far_count {
            training_tracker.lowest_level_so_far_count
        } else {
            num_overfilled_by
        };

        // remove the training from some of the lowest level
        training_tracker.players_so_far = training_tracker.players_so_far - num_overfilled_by;
        let difference_in_training = training_tracker.highest_level_so_far_level - training_tracker.lowest_level_so_far_level;
        training_tracker.training_so_far = training_tracker.training_so_far - difference_in_training*training_tracker.lowest_level_so_far_level;
        training_tracker.lowest_level_so_far_count = training_tracker.lowest_level_so_far_count - num_overfilled_by;

        // no more left, move the iterator forward
        if training_tracker.lowest_level_so_far_count == 0 {
            training_tracker.lowest_level_so_far_level = training_tracker.highest_level_so_far_itr.next()
                .expect(std::format!("lowest_level_so_far_level should not be the last level available: {}",  training_tracker.lowest_level_so_far_level).as_str());
            training_tracker.lowest_level_so_far_level = *training_tracker.num_players_at_level.get(&training_tracker.lowest_level_so_far_level)
                .expect(std::format!("lowest_level_so_far_level should be in the map {}", training_tracker.lowest_level_so_far_level).as_str());
        }
    }

    // add the even higher level
    let even_higher_level_difference = even_higher_player_level - training_tracker.highest_level_so_far_level;
    training_tracker.training_so_far = training_tracker.training_so_far + even_higher_level_difference*training_tracker.players_so_far;
    training_tracker.players_so_far = training_tracker.players_so_far + even_higher_player_count;
    training_tracker.highest_level_so_far_level = even_higher_player_level;
    training_tracker.highest_level_so_far_count = even_higher_player_count;
    if training_tracker.training_so_far < training_tracker.min_so_far {
        training_tracker.min_so_far = training_tracker.training_so_far;
    }

    return true;
}

fn accumulate_until_enough_students(num_to_pick: i32, training_tracker: &mut TrainingTracker) {
    while training_tracker.players_so_far < num_to_pick {
        add_next_level(num_to_pick, training_tracker);
    }
    training_tracker.min_so_far = training_tracker.training_so_far;
}

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
fn solve_scores(
    num_to_pick: i32,
    player_scores: Vec<i32>
) {
    // setup data structure to track the calculation
    let num_players_at_level = group_players_by_level(player_scores);
    let mut lowest_level_so_far_itr = num_players_at_level.keys().cloned();
    let mut highest_level_so_far_itr = num_players_at_level.keys().cloned();
    let mut training_tracker = TrainingTracker {
        min_so_far: i32::max_value(),
        players_so_far: 0,
        training_so_far: 0,
        num_players_at_level: num_players_at_level.clone(),
        lowest_level_so_far_level: 0,
        lowest_level_so_far_count: 0,
        lowest_level_so_far_itr: &mut lowest_level_so_far_itr,
        highest_level_so_far_level: 0,
        highest_level_so_far_count: 0,
        highest_level_so_far_itr: &mut highest_level_so_far_itr,
    };

    // move the lowest and highest pointers until there are enough students
    accumulate_until_enough_students(num_to_pick, &mut training_tracker);
    let mut working = true;
    while working {
        working = add_next_level(num_to_pick, &mut training_tracker);
        while working && training_tracker.players_so_far < num_to_pick {
            add_next_level(num_to_pick, &mut training_tracker);
        }
        if working && training_tracker.training_so_far <  training_tracker.min_so_far {
            training_tracker.min_so_far = training_tracker.training_so_far;
        }
    }
    println!("{}", training_tracker.min_so_far)
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

