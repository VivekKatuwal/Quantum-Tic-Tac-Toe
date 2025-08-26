# 🌀 Quantum Tic Tac Toe (Java Console)

A **console-based Quantum Tic Tac Toe** game implemented in **Java**.  
Unlike classic Tic Tac Toe, players can place moves in **superposition**, and a **cycle collapse** determines the classical board outcome.

---

## ✨ Features
- 🌀 **Quantum Superposition** – Multiple tentative moves can exist in a single cell  
- 🔗 **Cycle Detection (Union-Find / DSU)** – Detects loops in the move graph  
- ⚡ **Collapse Mechanism** – Opponent chooses which move collapses when a cycle occurs  
- ✅ **Classical Win Check** – Standard Tic Tac Toe win conditions after collapse  
- 🎮 **Console Gameplay** – Simple positions `0–8` for intuitive input

---

## 🛠️ Tech Stack
- **Language:** Java (JDK 8+)  
- **Data Structures:** 2D ArrayList board, Union-Find (Disjoint Set Union)  
- **Programming Paradigm:** Object-Oriented Programming (OOP)

---

## 🚀 How to Run

1. **Clone the repository:** git clone https://github.com/VivekKatuwal/Quantum-Tic-Tac-Toe.git
2. **Navigate to the project folder:** cd Quantum-Tic-Tac-Toe
3. **Compile the Java file:**javac QuantumTicTacToe.java
4. **Run the program:** java QuantumTicTacToe


---

## 🎮 Gameplay

Quantum Tic Tac Toe introduces **quantum mechanics principles** into the classic game:

1. **Superposed Moves:**  
   - Each player, on their turn, inputs **two positions (0–8)** where they want to place their move.  
   - Both positions are considered **tentative moves** simultaneously, existing in **superposition** until the board collapses.  

2. **Cycle Detection:**  
   - The game continuously checks for **cycles** formed by overlapping moves between X and O.  
   - A cycle occurs when moves link back to each other, creating a **loop of superposed states**.  

3. **Collapse Mechanism:**  
   - When a cycle is detected, the **opponent** gets to decide which move in the cycle **collapses** into a classical state (X or O).  
   - This collapse converts the quantum board into a **classical configuration**, resolving overlaps.  

4. **Winning Conditions:**  
   - After each collapse, the board is checked for a **winner** in rows, columns, or diagonals.  
   - The game continues until either **X or O wins** or the board is **completely filled** with no available moves.  

This gameplay makes Quantum Tic Tac Toe **strategically deeper** than the classic version, combining **logic, anticipation, and a bit of quantum chaos**!


---

## 🧑‍💻 Author
**Vivek Nath Katuwal**  
- B.Tech CSE Student (4th Year)  
- GITAM University, India  

