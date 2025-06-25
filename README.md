# WildLoop

**WildLoop** is a simple ecosystem simulation where animals move, eat, reproduce, and die based on environmental conditions.  

This project was developed as part of coursework at Wrocław University of Science and Technology.

## Project Overview

The world is represented as a two-dimensional grid populated by two types of animals:
- **Predators** – hunt prey to survive and regain energy.
- **Prey** – search for food and try to avoid predators.

Each animal has a life energy level that decreases over time and can be replenished by eating. If energy drops to zero, the animal dies. Animals can reproduce if they meet certain criteria. Every turn, all animals make decisions based on their state and surroundings (e.g., predators search for the nearest prey, prey try to escape predators).

## Key Features

- 2D grid world with configurable size
- Distinct behaviours for predators and prey
- Simple decision-making algorithms (hunt, escape, random movement)
- Life energy and reproduction mechanisms
- Population dynamics observable over simulation turns
- Integrated logging system for tracking simulation events and debugging
- Clean object-oriented design using Java

## System Requirements

- Java 21 or newer
- Graphical environment supporting Swing

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/szycic/wild-loop.git
   cd wild-loop
   ```

2. **Build the project with Maven:**
   ```bash
   mvn compile
   ```

3. **Run the simulation:**
   ```bash
   mvn exec:java
   ```

4. **Customising simulation settings:**  
   You can modify the default simulation parameters by editing the `simulation.properties` file generated after compiling the project.  
   **Path:** `target/classes/simulation.properties`

## Project Structure

- `src/main/java/`
  - `Animal.java` – Base class for all animals
  - `Predator.java` – Predator logic
  - `Prey.java` – Prey logic
  - `World.java` – Grid and environment logic
  - `Main.java` – Simulation entry point
  - (plus utility classes as needed)
 
Full documentation can be found [here](https://szycic.com/wild-loop/). Apart from that you can also find various project diagrams [here](diagrams).

## Educational Goals

This project was designed to help practice:
- Object-oriented design and inheritance
- Interactions between objects
- Simple agent-based algorithms
- Collections and iteration in Java

## Example

Below is a schematic example of the simulation grid (actual visualisation may vary):

```
. . P . . R .
. R . . . . .
. . . P . . .
```
Legend:  
- `P` – Prey  
- `R` – Predator  
- `.` – Empty cell

## Authors

- [Szymon Cichy](https://github.com/szycic)
- [Tomasz Druszcz](https://github.com/tomaszdruszcz)
- [Jan Osmęda](https://github.com/Dzonyyyyy)

---

Enjoy experimenting with the wild ecosystem simulation! 🐺🐇🌱
