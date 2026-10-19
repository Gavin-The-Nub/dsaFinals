# Canteen Order Queue Management System

A pure Java application developed for the **Final Project in Data Structures and Algorithms (Datastrc)** at **De La Salle Lipa (CITE)**.

---

## 📋 Features & Applied DSA

| # | Feature | Real-World Application | DSA Applied |
|---|---|---|---|
| 1 | **Place Order** | Registers a customer, selects food item and quantity, calculates total, assigns Order ID. | **Queue — Enqueue** ($O(1)$) |
| 2 | **Serve Next Order** | Selects first pending order and marks it as served. | **Queue — Dequeue** ($O(1)$) |
| 3 | **Search Order** | Locates an order by ID and displays customer, item, quantity, amount, and status. | **Linear Search** ($O(n)$) |
| 4 | **View Orders** | View pending orders in FIFO order, or completed orders sorted by total price. | **Queue Traversal** + **Insertion Sort** ($O(n^2)$) |
| 5 | **Canteen Menu** | Displays available food items and beverages with pricing. | Catalog traversal |

---

## 📂 Project Structure

```
.
├── src/
│   ├── MenuItem.java          # Food item model
│   ├── Order.java             # Order model with pricing & status
│   ├── OrderNode.java         # Pure Java node for custom Queue
│   ├── OrderQueue.java        # Custom FIFO Queue implementation
│   ├── OrderManager.java      # Controller hosting Linear Search & Insertion Sort
│   └── Main.java              # Interactive Console UI & CLI event loop
├── test/
│   ├── ModelsTest.java        # Unit test for domain models
│   ├── QueueTest.java         # Unit test for custom OrderQueue
│   └── AlgorithmsTest.java    # Unit test for Linear Search & Insertion Sort
└── docs/
    ├── finals_section_lastNames.md  # Complete final documentation for GT6B
    ├── superpowers/specs/           # Design specifications
    └── superpowers/plans/           # Implementation plans
```

---

## 🚀 How to Compile and Run

### Prerequisites
- JDK 17 or higher (tested with OpenJDK 25 LTS).
- Zero external dependencies or Maven/Gradle required.

### 1. Compile the Project
```bash
javac -d out src/*.java
```

### 2. Run the Main Application
```bash
java -cp out Main
```

### 3. Run the Unit Test Suite
```bash
javac -d out -cp out src/*.java test/*.java
java -cp out ModelsTest
java -cp out QueueTest
java -cp out AlgorithmsTest
```

---

## 📄 Final Project Documentation (`GT6B`)
The full project documentation required for **GT6B** is located in:
👉 [`docs/finals_section_lastNames.md`](docs/finals_section_lastNames.md)

It contains all required sections:
1. **Introduction**
2. **Program Design** (Architecture Flowchart and UML Class Diagram)
3. **Code Inventory** (Control structures, libraries, variables, functions, classes, OOP principles)
4. **Concepts of Data Structures and Algorithms** (Queue, Linear Search, Insertion Sort)

To submit as a Word Document (`finals_section_lastNames.docx`):
- Open `docs/finals_section_lastNames.md` in Microsoft Word, Google Docs, or Pages.
- Fill in your group members' names and section.
- Save As / Download as **`finals_section_lastNames.docx`**.
