# Number Guessing Game 🎮

A progressive multi-level number guessing game with adaptive difficulty.

## Features
- **Progressive Rounds:**
  - Round 1: 10 levels (no time limit)
  - Round 2: 15 levels (levels 1-10 no timer, 11-15 with 30s timer)
  - Round 3: 20 levels (levels 1-10 no timer, 11-20 with 30s timer)
  - And so on...

- **Dynamic Difficulty:**
  - Range increases per level
  - Chances vary per level (3→15)
  - Time pressure on advanced levels

- **Scoring System:**
  - +10 points for correct guess
  - -5 penalty for loss
  - Independent score per round

- **Smart Hints:**
  - Distance-based feedback
  - "Too low" / "Too high"
  - Proximity indicators

- **Visual Progress:**
  - ASCII maze showing advancement
  - Real-time level/round display
  - Score tracking

## How to Run
```bash
javac number_guessing.java LevelDetails.java
java number_guessing
```

## Game Rules
- Guess the secret number in given range
- Limited chances decrease on wrong guess
- Levels 1-10: No time pressure
- Levels 11+: 30-second time limit
- Complete all levels to advance round
- Each round resets score, increases total levels
