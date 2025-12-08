# 📘 DictionarySearch

**DictionarySearch** is a **Data Structures and Algorithms (DSA) term project** implemented in **Java**.

The project explores how different **data structures** and **search algorithms** impact performance when searching for words in a large dictionary dataset.
All core data structures are implemented **from scratch**, without using Java's built-in collection framework (`java.util`), as required by the course.

---

## 🎯 Project Objectives

This project aims to:

- ✅ Implement classic search algorithms manually
- ✅ Compare performance across different data structures
- ✅ Analyze experimental results using **time complexity theory**
- ✅ Gain practical experience implementing data structures internally
- ✅ Build an interactive GUI application for dictionary lookup

---

## 🔍 Implemented Search Methods

### ▶️ Linear Search _(Array-Based)_

```java
LinearSearch.search(array, target)
```

- Stores words in a basic array
- Sequentially scans elements from beginning to end
- **Time Complexity:**
  - Average case: `O(n)`
  - Worst case: `O(n)`

---

### ▶️ Binary Search _(Sorted Array)_

```java
BinarySearch.search(sortedArray, target)
```

- Operates on a **sorted array**
- Splits search space in half each step
- Implemented manually (no library calls)
- **Time Complexity:**
  - Worst case: `O(log n)`

---

### ▶️ Hash Table _(Custom Implementation)_

```java
hashTable.put(key, value)
hashTable.get(key)
```

- Custom generic implementation:
  ```java
  HashTable<Key, Value>
  ```
- Collision handling via **separate chaining**
- Dynamically resizes when load factor increases

**Time Complexity:**

- Average case: `O(1)`
- Worst case: `O(n)`

#### 🔑 Hash Function

You compute the hash for a given word `w` as:

$$
h(w) = \left(\sum_{i=0}^{|w|-1} w_i \times 31^{|w|-1-i}\right) \bmod M
$$

Where:

- \(w_i\) is the ASCII / Unicode value of the \(i\)-th character of `w`
- \(M\) is a large prime number (e.g. `100003`)

```java
private int hash(Key key) {
    final long M = 100_003L;
    String keyStr = key.toString();
    long hash = 0L;

    for (int i = 0; i < keyStr.length(); i++) {
      int wi = keyStr.charAt(i);
      hash = (hash * 31 + wi) % M;
    }

    return (int) (hash % this.capacity);
}
```

---

## 🧱 Custom Data Structures

All structures below are implemented **without using `java.util` classes**:

- `HashTable<Key, Value>` — main dictionary storage
- `ArrayList<T>` — dynamic array with manual resizing
- `LinkedList<T>` — used for chaining in hash table buckets
- `Entry<Key, Value>` — key–value node representation

All implementations use **Java Generics** to ensure type safety.

---

## ✨ Features

### 🎨 Interactive GUI Application

- Built with **Java Swing**
- Modern, user-friendly interface with custom **Geist Mono** font
- Real-time word search with instant results

### 🔍 Smart Word Search

- User can select between **Binary Search** or **Linear Search** algorithms
- Displays word definitions from the dictionary
- Shows **execution time** for performance comparison

### 💡 Word Suggestions

- Suggests similar words when search fails
- Uses intelligent matching algorithm:
  - Words with **1 character difference** (edit distance = 1)
  - Words that **start with** the search query
- Displays up to **8 suggestions**
- Click on suggestions to instantly search for them

### ⏱️ Performance Analysis

- **ExecutionTimeAnalyzer** — measures search performance in nanoseconds
- **ExecutionTimeFormatter** — formats timing results with configurable precision
- Real-time display of algorithm execution time

---

## 📂 Dataset

- **File:** `dict.csv`
- **Size:** 40,000+ English words
- **Format:**

```csv
word,definition
```

---

## 📄 CSV Parsing

CSV parsing is implemented **character-by-character** to correctly support:

- ✅ Quoted fields
- ✅ Commas inside definitions
- ✅ Escaped quotes (`""`)
- ✅ Multi-line definitions

This avoids incorrect splitting and data loss, a common issue with naïve CSV parsing.

---

## 📥 File Loading

The `Loader` class is responsible for loading dictionary data:

```java
Loader<String, String> loader = new Loader<>("data/dict.csv");
HashTable<String, String> dictionary = loader.load();
```

### Supported Formats

- **TXT**
  - One word per line
- **CSV**
  - Word–definition pairs

File type detection is automatic.

---

## 🧪 Experimental Setup

To evaluate performance:

- 🔢 At least **50 test words** were selected
  - Included both **existing** and **non-existing** words
- 🔁 The same word set was searched using:
  - Linear Search
  - Binary Search
  - Hash Table Lookup

### Measured Metrics

- ⏱️ Total search time
- 📊 Average lookup time per word

---

## 🗂️ Project Structure

```text
DictionarySearch/
├── data/
│   ├── dict.csv                    # 40,000+ word-definition pairs
│   └── testWords.txt               # Test words for experiments
├── fonts/
│   ├── GeistMono-Regular.ttf       # Custom UI font
│   └── GeistMono-Bold.ttf          # Bold variant
└── src/
    └── main/
        └── java/
            ├── app/
            │   └── DictionaryApp.java           # Main GUI application
            ├── loader/
            │   └── Loader.java                  # CSV/TXT file loader
            ├── search/
            │   ├── Search.java                  # Search interface
            │   ├── LinearSearch.java            # O(n) linear search
            │   └── BinarySearch.java            # O(log n) binary search
            └── utils/
                ├── Entry.java                   # Key-value pair node
                ├── HashTable.java               # Custom hash table
                ├── ArrayList.java               # Dynamic array
                ├── LinkedList.java              # Linked list for chaining
                ├── analysis/
                │   ├── ExecutionTimeAnalyzer.java    # Performance measurement
                │   └── ExecutionTimeFormatter.java   # Result formatting
                └── features/
                    └── WordSuggester.java       # Smart word suggestions
```

---

## 🚀 How to Run

1. **Clone the repository:**

   ```bash
   git clone <repository-url>
   cd DictionarySearchProject/DictionarySearch
   ```

2. **Compile the project:**

   ```bash
   javac -d bin src/main/java/**/*.java
   ```

3. **Run the application:**
   ```bash
   java -cp bin main.java.app.DictionaryApp
   ```

---

## 💡 Key Observations

- 🔴 Linear search does not scale well with large datasets
- 🟡 Binary search is efficient but requires sorted data
- 🟢 Hash tables offer the best average-case lookup performance

Implementing these structures manually revealed real-world challenges such as:

- Hash collisions
- Load factor tuning
- Rehashing overhead
- Edge cases in CSV parsing
- GUI responsiveness with large datasets

---

## 🛠️ Technologies Used

- **Language:** Java (JDK 11+)
- **GUI Framework:** Java Swing
- **Font:** Geist Mono (custom embedded font)
- **Build Tool:** Manual compilation (can be migrated to Maven/Gradle)

---

## 👥 Team Contribution

This project was developed collaboratively as a **team effort**.
Team members contributed to:

- Data structure design and implementation
- Algorithm development
- File parsing and loading logic
- GUI design and implementation
- Performance testing and analysis
- Debugging and validation

---

## 📜 License

This project is developed for **educational purposes** as part of a university DSA course.
See the `LICENSE` file for details.
