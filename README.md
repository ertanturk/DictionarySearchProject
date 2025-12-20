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

#### Key-Value Export

```java
public Object[][] getKeyValuePairs()
```

- Returns an `Object[][]` where each row is a 2-element array: `[keyLowerCase, value]`.
- Implemented as `Object[][]` because Java does not allow creating arrays of generic parameterized types.
- Common use: populating Swing tables or other UI components that expect `Object[][]` data.

Example:

```java
HashTable<String, String> ht = loader.load();
Object[][] pairs = ht.getKeyValuePairs();
// pairs[i][0] -> String (lowercased key)
// pairs[i][1] -> String (value)
```

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
- **Size:** 41,307 English words
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
Loader loader = new Loader("data/dict.csv");
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

- ⏱️ Total search time (measured as average per-iteration)
- 📊 Average lookup time per word (averaged across test words)

---

## 📊 Test Results

The `Test.java` class compares the performance of all three search algorithms against a set of 50 test words (both existing dictionary words and non-existent words to test edge cases).

### Sample Output (representative)

The `Test.java` harness now uses a warmup of `10` runs and `10000` iterations; reported numbers are the average per-iteration time (nanoseconds converted to milliseconds in the report). Below are a few representative outputs captured from a run:

```text
----------------------------------------------------------------
Execution Time Comparison (Word: 'ocean' exists in the dictionary):
- Linear Search: 0.0938730000 ms (1020.359x Times Slower)
- Binary Search: 0.0001560000 ms (1.696x Times Slower)
- Hash Search: 0.0000920000 ms (Fastest)
----------------------------------------------------------------

----------------------------------------------------------------
Execution Time Comparison (Word: 'whispint' not exists in the dictionary):
- Linear Search: 0.1739500000 ms (2174.375x Times Slower)
- Binary Search: 0.0001690000 ms (2.113x Times Slower)
- Hash Search: 0.0000800000 ms (Fastest)
----------------------------------------------------------------

... (additional per-word comparisons)

----------------------------------------------------------------
Average Execution Time Comparison for 50 words over 10000 iterations:
- Linear Search: 0.1269870000 ms (4703.222x Times Slower)
- Binary Search: 0.0000520000 ms (1.926x Times Slower)
- Hash Search: 0.0000270000 ms (Fastest)
----------------------------------------------------------------
```

### Performance Analysis (updated)

From the updated test harness and measured runs we observe:

- **Hash Search**: fastest on average in this runset

  - Average time: **0.0000270000 ms** per lookup (averaged across test words and analyzer iterations)
  - Fastest for both existent and many non-existent lookups in the measurement above

- **Binary Search**: very close to hash performance

  - Average time: **0.0000520000 ms** per lookup (≈1.93× slower than hash on average)
  - Reliable and consistent due to deterministic comparisons on a sorted array

- **Linear Search**: much slower
  - Average time: **0.1269870000 ms** per lookup (≈4703× slower than hash on average)
  - Unsuitable for interactive lookups at this dataset size

**Key Findings (revised):**

- With the current measurement harness (warmup `10`, iterations `10000`) and the dataset of `41,307` words, the measured averages show `Hash Search` as the fastest on average, with `Binary Search` very close.
- `Linear Search` remains impractical for production interactive use on datasets of this size.

---

## 🗂️ Project Structure

```text
DictionarySearch/
├── README.md                               # Project documentation
├── LICENSE                                 # License information
├── .gitignore                              # Git ignore rules
├── report/
│   └── report.pdf         # Project report
├── data/
│   ├── dict.csv                              # 40,000+ word-definition pairs
│   └── testWords.txt                         # Test words for experiments
├── fonts/
│   ├── GeistMono-Regular.ttf                 # Custom UI font
│   └── GeistMono-Bold.ttf                    # Bold variant
└── src/
    └── main/
        └── java/
            ├── app/
            │   ├── icon.png                  # Application icon
            │   └── DictionaryApp.java        # Main GUI application
            ├── loader/
            │   └── Loader.java               # CSV/TXT file loader
            ├── search/
            │   ├── Search.java               # Search interface
            │   ├── HashSearch.java           # O(1) hash table search
            │   ├── LinearSearch.java         # O(n) linear search
            │   └── BinarySearch.java         # O(log n) binary search
            ├── tests/
            │   └── Test.java                 # Performance testing suite
            └── utils/
                ├── Entry.java                # Key-value pair node
                ├── HashTable.java            # Custom hash table
                ├── ArrayList.java            # Dynamic array
                ├── LinkedList.java           # Linked list for chaining
                ├── analysis/
                │   ├── ExecutionTimeAnalyzer.java    # Performance measurement
                │   └── ExecutionTimeFormatter.java   # Result formatting
                └── features/
                    └── WordSuggester.java    # Smart word suggestions
```

---

## 🚀 How to Run

1. **Clone the repository:**

   ```bash
   git clone <https://github.com/ertanturk/DictionarySearchProject>
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
