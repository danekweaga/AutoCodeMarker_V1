# Auto Code Marker – Version 1
Programming Assignment Testing Tool  
CS 2043 – Group 1  
Members: Alamin Adeleke, Chukwunonso Ekweaga, Frances Felicidario, Aniekan Ekarika

## Overview
This project is a JavaFX-based tool that automates running student Java submissions against instructor-defined test cases.

Auto Code Marker is a JavaFX desktop application for managing test suites and running automated tests on student programming submissions. It is designed to help teaching assistants and instructors create, edit, and reuse test cases for common introductory programming assignments.

---

## Features

- **Submission folder selection**
  - Choose a folder containing student submissions to be tested.

- **Test suite management**
  - Create, rename, and delete *test suites* (folders of test cases).
  - Test suites are stored under the user's home directory in:
    - `~/Auto Code Marker/Test Suites/` (Windows: `C:\Users\<user>\Auto Code Marker\Test Suites\`)

- **Test case management**
  - For each test suite, manage individual test cases using the **Test Case Manager**.
  - Each test case has:
    - A **name**
    - An **input** string
    - An **expected output** string
    - An **index** (for display order)
  - Test cases are stored as `.txt` files inside the test suite folder.

- **Running tests**
  - The coordinator class (`Coord`) coordinates running tests against submissions using the selected test suite.
  - Results are stored and displayed via the `Output` and `OutputViewer` classes.

- **Results viewer**
  - The **Output Viewer** window displays:
    - The submission name
    - Each test case name
    - The result for each test (e.g., “PASS” / “FAIL” or other status string)
  - Navigation controls (Next/Previous) allow paging through results.

---

## Architecture Overview

Main components:

- `UI.java`  
  JavaFX `Application` class. Sets up the main window ("Auto Code Marker") and the primary controls:
  - Select Submission Folder
  - Add Test Case (copy a test case file into the Test Suites folder)
  - Manage Test Suites
  - Manage Test Cases
  - Run Test Cases

- `Coord.java`  
  Coordinator class connecting the UI to the underlying logic. Handles:
  - Managing test suites (`TestSuiteManager`)
  - Managing test cases (`TestCaseManager`)
  - Running tests and collecting `Output` objects

- `TestSuiteManager.java`  
  JavaFX window for creating, renaming, and deleting test suite folders under:
  - `~/Auto Code Marker/Test Suites/`

- `TestCaseManager.java`  
  JavaFX window for:
  - Selecting a test suite
  - Listing test case files in that suite
  - Creating, updating, deleting test cases
  - Saving test cases back to disk

  **Test case storage format (per file):**

  - File extension: `.txt`
  - Location: `~/Auto Code Marker/Test Suites/<SuiteName>/`
  - Contents:
    - **Line 1:** Input (string) sent to the program
    - **Line 2:** Expected output (string) compared with the program output

- `TestSuite.java`  
  Simple container class holding a list of `TestCase` objects.

- `TestCase.java`  
  Represents one test case:
  - `name` – logical name of test
  - `input` – input string
  - `output` – expected output string
  - `index` – display order

- `Submission.java`  
  Represents a student submission:
  - `name`
  - `path`
  - `fileName`

- `Output.java`  
  Holds:
  - `submissionName`
  - A list of `Result` objects for that submission.

- `Result.java`  
  Represents one test result:
  - `testCaseName`
  - `result` – status string (e.g. “PASS” / “FAIL”).

- `OutputViewer.java`  
  JavaFX window for paging through a list of `Output` objects and displaying them in a user-friendly way.
