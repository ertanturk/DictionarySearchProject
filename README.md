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

## 📊 Test Results

The `Test.java` class compares the performance of all three search algorithms against a set of 50 test words (both existing dictionary words and non-existent words to test edge cases).

### Sample Output

```text
Key: ocean
Linear Search: 3987520.000 ns (339.42x slower)
Binary Search: 11748.000 ns (FASTEST)
Hash Search: 13431.000 ns (1.14x slower)

Key: whispint
Linear Search: 5351093.000 ns (1593.54x slower)
Binary Search: 4962.000 ns (1.48x slower)
Hash Search: 3358.000 ns (FASTEST)

Key: friendship
Linear Search: 883127.000 ns (178.99x slower)
Binary Search: 4934.000 ns (FASTEST)
Hash Search: 9563.000 ns (1.94x slower)

Key: beautiful
Linear Search: 2148806.000 ns (261.83x slower)
Binary Search: 8207.000 ns (FASTEST)
Hash Search: 11385.000 ns (1.39x slower)

Key: krivalla
Linear Search: 1822947.000 ns (701.94x slower)
Binary Search: 5617.000 ns (2.16x slower)
Hash Search: 2597.000 ns (FASTEST)

Key: success
Linear Search: 1783707.000 ns (285.71x slower)
Binary Search: 6243.000 ns (FASTEST)
Hash Search: 13600.000 ns (2.18x slower)

Key: triangle
Linear Search: 896120.000 ns (156.86x slower)
Binary Search: 5713.000 ns (FASTEST)
Hash Search: 58804.000 ns (10.29x slower)

Key: computer
Linear Search: 320253.000 ns (29.82x slower)
Binary Search: 21157.000 ns (1.97x slower)
Hash Search: 10738.000 ns (FASTEST)

Key: bicycle
Linear Search: 270160.000 ns (35.32x slower)
Binary Search: 17604.000 ns (2.30x slower)
Hash Search: 7649.000 ns (FASTEST)

... (41 more test cases)

Overall Average Times for Searches: (50 test words) (ns)
Linear Search: 861082.000 ns (143.75x slower)
Binary Search: 5990.000 ns (FASTEST)
Hash Search: 12294.000 ns (2.05x slower)
```

### Performance Analysis

From the test results across 50 test words (mix of dictionary words and non-existent words), we observe:

- **Linear Search**: Consistently the slowest algorithm

  - Average time: **861,082 ns** (~0.86 ms) per search
  - Ranges from **30x to 1594x slower** than the fastest algorithm
  - Performance degrades significantly with dataset size
  - Impractical for real-world dictionary lookups

- **Binary Search**: Best overall performance

  - Average time: **5,990 ns** (~6 μs) per search
  - Most consistent and fastest for the majority of test cases
  - Requires pre-sorted data (one-time sorting cost)
  - Optimal for sorted dictionary datasets

- **Hash Search**: Strong performance with occasional outliers
  - Average time: **12,294 ns** (~12 μs) per search
  - **Fastest** for non-existent words (krivalla, whispint, zintar, drevica, etc.)
  - Occasionally slower due to hash collisions (e.g., triangle: 58,804 ns, spuddler: 108,458 ns)
  - Excels at constant-time lookups for most cases

**Key Findings:**

- Binary search is the **overall winner** with an average of **5,990 ns**, making it **2.05x faster** than hash search and **143.75x faster** than linear search
- Hash search excels at detecting non-existent words quickly (constant-time failure)
- Linear search is completely impractical for datasets of this size (40,000+ words)
- For a sorted dictionary dataset, binary search provides the most reliable and consistently fast performance

---

## 🗂️ Project Structure

```text
DictionarySearch/
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
