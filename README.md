# CrossAI - Hybrid Movie Recommender System

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![C++](https://img.shields.io/badge/C++-17+-blue.svg)](https://isocpp.org/)
[![Python](https://img.shields.io/badge/Python-3.8+-green.svg)](https://www.python.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A sophisticated hybrid movie recommendation system that demonstrates cross-language integration using **Java**, **C++**, and **Python**. Built as an Object-Oriented Programming laboratory project with emphasis on clean architecture, design patterns, and machine learning integration.

## 🎯 Project Overview

CrossAI is a three-tier application that showcases:
- **Java UI Client**: Interactive user interface for movie recommendations
- **C++ Engine**: High-performance middleware handling communication and rule-based filtering
- **Python ML Module**: Custom machine learning model for personalized recommendations

### Architecture

```
┌─────────────────┐
│   Java Client   │  (UI Layer)
│   - User Input  │
│   - Display     │
└────────┬────────┘
         │ JSON
         ▼
┌─────────────────┐
│   C++ Engine    │  (Logic Layer)
│   - Filtering   │
│   - Processing  │
└────────┬────────┘
         │ JSON
         ▼
┌─────────────────┐
│  Python ML      │  (AI Layer)
│   - ML Model    │
│   - Training    │
└─────────────────┘
```

## ✨ Features

- 🎬 **Personalized Recommendations**: ML-based movie suggestions
- 🔍 **Genre Filtering**: Rule-based genre preferences
- 💾 **User Profiles**: Persistent user data and preferences
- 🚀 **High Performance**: Multi-threaded C++ engine
- 📊 **Custom ML Model**: Trained on real movie datasets

## 🛠️ Tech Stack

| Component | Technologies |
|-----------|-------------|
| **Frontend** | Java 17+, Maven, JavaFX/Swing |
| **Engine** | C++17, CMake, JSON for Modern C++ |
| **ML Backend** | Python 3.8+, scikit-learn, pandas, NumPy |
| **Build Tools** | Maven, CMake, pip |
| **Testing** | JUnit, Google Test, pytest |

## 📋 Prerequisites

- **Java Development Kit (JDK)** 17 or higher
- **C++ Compiler** (GCC 9+, Clang 10+, or MSVC 2019+)
- **CMake** 3.15 or higher
- **Python** 3.8 or higher
- **Maven** 3.6 or higher
- **Git**

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/CrossAI.git
cd CrossAI
```

### 2. Setup Python Environment

```bash
cd python
pip install -r requirements.txt
cd ..
```

### 3. Build C++ Engine

```bash
cd cpp
mkdir build && cd build
cmake ..
cmake --build .
cd ../..
```

### 4. Build Java Client

```bash
cd java
mvn clean install
cd ..
```

### 5. Run the Application

```bash
# Start C++ Engine (in one terminal)
cd cpp/build
./crossai-engine

# Start Java Client (in another terminal)
cd java
mvn exec:java
```

## 📁 Project Structure

```
CrossAI/
├── java/                  # Java UI Client
│   ├── src/crossai/      # Source code
│   ├── test/             # Unit tests
│   └── pom.xml           # Maven configuration
├── cpp/                   # C++ Engine
│   ├── include/          # Header files
│   ├── src/              # Implementation
│   ├── tests/            # Unit tests
│   └── CMakeLists.txt    # CMake configuration
├── python/                # Python ML Module
│   ├── recommender/      # ML model code
│   ├── data/             # Dataset
│   └── requirements.txt  # Dependencies
├── shared/                # Inter-process communication
├── docs/                  # Documentation
└── README.md
```

## 🧪 Running Tests

### Java Tests
```bash
cd java
mvn test
```

### C++ Tests
```bash
cd cpp/build
ctest
```

### Python Tests
```bash
cd python
pytest tests/
```

## 📊 Dataset

The project uses a movie dataset from Kaggle containing:
- Movie titles, genres, ratings
- User preferences and viewing history
- Metadata for recommendation training

*Dataset source will be added after selection*

## 🎓 Learning Objectives

This project demonstrates:
- **OOP Principles**: Inheritance, polymorphism, encapsulation
- **Design Patterns**: Factory, Strategy, Observer patterns
- **Multi-language Integration**: JSON-based IPC
- **Testing**: Unit tests across all components
- **Version Control**: Git workflow and best practices
- **Build Systems**: Maven, CMake automation
- **ML Integration**: Custom recommendation algorithms

## 👥 Team

- **Mateusz Wilk** - Architecture & C++ Engine
- **Paweł Siurek** - Java Client & Integration

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- OOP Laboratory Course - [University Name]
- Kaggle for movie datasets
- Open-source community for tools and libraries

## 📧 Contact

For questions or feedback, please open an issue on GitHub.

---

**Note**: This is an educational project developed as part of an Object-Oriented Programming laboratory course.
