# ✅ Scenario – Version 1 Execution Workflow

This scenario describes how to use the **Auto Code Marker – Version 1** application to execute a test suite on a set of program submissions using the provided sample test data.

---

## 1. Required Test Data

The sample test data included in the repository is located at:

scenario_test_data/
TestSuites/
SimpleEchoSuite/
tc1.txt
tc2.txt
Submissions/
EchoProgram/
EchoProgram.java


### ✅ Test Suite Format

Each test case file follows the required structure:

- **Line 1:** test case name  
- **Line 2:** input value  
- **Line 3:** expected output

Example (`tc1.txt`):
Echo 5
5
5

These files must be placed under the following directory on the user’s machine before running the application:
<UserHome>/Auto Code Marker/Test Suites/SimpleEchoSuite/


Example final path:
C:\Users<username>\Auto Code Marker\Test Suites\SimpleEchoSuite\



### ✅ Submissions Folder

The submissions root folder must contain one subfolder per program, where:

- The **subfolder name = program name**
- The **.java file inside matches that name**
- The class contains a `main` method

Example structure:
Submissions/
EchoProgram/
EchoProgram.java


---

## 2. Launching the Application

1. Run the JavaFX application with `UI` as the main class.
3. Select the **Submissions** folder.
4. The full path now appears in the submissions text field.
5. The application will treat each subfolder under this directory as a separate program.

---

## 3. Selecting the Submission Folder

1. Click **Browse** next to *Select Submission Folder*.
2. Navigate to: scenario_test_data/Submissions
3. Select the **Submissions** folder.
4. The full path now appears in the submissions text field.
5. The application will treat each subfolder under this directory as a separate program.

---

## 4. Choosing the Test Suite

1. Open the **Choose Test Suite** dropdown.
2. Select: SimpleEchoSuite
3. The UI now associates this suite as the active `TestSuite`.

> **Ordering note:**  
> This step should occur **before running test cases**, even though the UI does not enforce it.

---

## 5. (Optional) Adding a Test Case File

1. Confirm that **SimpleEchoSuite** is selected.
2. Click **Browse File** under *Add Test Case File to Selected Suite*.
3. Choose any `.txt` test case file.
4. The file will be automatically copied into: <UserHome>/Auto Code Marker/Test Suites/SimpleEchoSuite/

5. Opening **Manage TestCases** will display the newly added file.

---

## 6. Running the Test Suite

1. Ensure BOTH conditions are true:
   - A **submission folder** is selected
   - A **test suite** is selected

2. Click **Run Test Cases**.

3. The application performs the following:

   - Loads the single `TestSuite` from the selected suite folder
   - Generates a list of programs from the submissions root
   - For each program:
     - Compiles the `<ProgramName>.java` file
     - Runs the program once per test case
     - Captures output and compares it to the expected value
     - Records the result

4. For each program (e.g., `EchoProgram`), an **Output Viewer** window opens displaying:
   - The submission name
   - Up to four results per page
   - PASS/FAIL status for each test case

---

## 7. Viewing Test Case Details

1. In the Output Viewer, click on any displayed result.
2. A dialog opens showing:
   - Status (PASS or FAIL)
   - Input used
   - Expected output
   - Actual output

3. Close the dialog to return to the results view.

---

## 8. Ordering and Assumptions

- The **intended sequence** of actions is:

  1. Prepare or choose a Test Suite  
  2. Place it in `<UserHome>/Auto Code Marker/Test Suites/`  
  3. Select the Submissions folder in the UI  
  4. Choose the Test Suite from the dropdown  
  5. (Optional) Add additional test case files  
  6. Run Test Cases  

- The UI does **not** prevent running without test cases or before selection, but this scenario defines the correct order.

- The same workflow applies to any conforming test suite and submission set.

---

## ✅ End of Scenario – Version 1

This scenario, along with the provided test data, allows the marker to execute and verify all Version-1 features.











