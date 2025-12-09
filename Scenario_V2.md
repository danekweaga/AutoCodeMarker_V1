# ✅ Scenario – Version 2 Execution and Comparison Workflow

This scenario describes how to use Auto Code Marker – Version 2 to:
- Execute a test suite on a first set of submissions
- Execute the same test suite on a second (resubmission) set
- Store results to disk
- Reload stored results
- Compare success rates between two runs

## 1. Assignment Context
This scenario uses the Pass/Fail Grade Checker assignment:
The program reads a single integer grade (0–100) and prints:
- PASS if the grade is greater than or equal to 50
- FAIL otherwise

## 2. Required Test Data
### ✅ Test Suite Location
Test suites must be placed under:
> <UserHome>/Auto Code Marker/Test Suites/

> Example:
> C:\Users\<username>\Auto Code Marker\Test Suites\PassFailSuite\

### ✅ Test Case File Format
Each test case file is a .txt file with the following structure:
> <test case name>
> <input>
> <expected output>

> Example (boundary_pass.txt):
> boundary_pass
> 50
> PASS

### ✅ Test Cases Used

> Test Case Name	Input	Expected Output
> basic_pass	75	PASS
> basic_fail	30	FAIL
> boundary_pass	50	PASS
> boundary_fail	49	FAIL
> zero_fail	0	FAIL

## 3. Submissions Folder Structure
### ✅ First Submission Folder
Each submission folder contains one subfolder per student.
Each student subfolder:
- May contain multiple .java files
- Contains exactly one file with
> public static void main(String[] args)

All files belong to the same application

> Example:
> Submissions_Run1/
> Alice/
> GradeChecker.java
> Utils.java
> Bob/
> MainProgram.java

### ✅ Second Submission Folder (Resubmission)
The second folder has the same structure, but may:
- Be missing some students
- Contain modified or fixed code
- Contain code that still does not compile

> Example:
> Submissions_Run2/
> Alice/
> GradeChecker.java
> Utils.java
> Charlie/
> Main.java


## 4. Launching the Application
Run the JavaFX application using the UI class.

The main window appears with controls for:
- Submission folder selection
- Test suite selection
- Running tests
- Loading / comparing results


## 5. Running the Test Suite on the First Submission Folder
Click Browse next to Select Submission Folder.

Select Submissions_Run1.

Open the Choose Test Suite dropdown.

Select PassFailSuite.

Click Run Test Cases.

### ✅ System Behavior
Each student folder is processed independently
The system:
- Scans .java files to detect the file containing main
- Compiles the detected main file
- Executes the program once per test case
- Records PASS / FAIL results
- Compilation failures are recorded as COMPILE_ERROR

### ✅ Storing Results
Click Save Results.

Choose filename:
> PassFail_Run1.txt


## 6. Running the Same Test Suite on the Second Submission Folder
Click Browse next to Select Submission Folder.

Select Submissions_Run2.

Ensure PassFailSuite is still selected.

Click Run Test Cases.

After execution, click Save Results.

Save as:
> PassFail_Run2.txt


## 7. Reloading and Viewing Stored Results
Click Import Result File.

Select PassFail_Run1.txt.

### ✅ Output Viewer Displays
One entry per student

For each student:
- Number of passed test cases
- Total number of test cases
- Success rate displayed as a fraction (e.g., 3/5)

The text view is read-only


## 8. Comparing Two Result Files
Click Compare Result Files.

Select:
> First file: PassFail_Run1.txt
> Second file: PassFail_Run2.txt

### ✅ Comparison Output Logic
Students are matched by folder name

For each student:

Two success rates are shown side-by-side

Format:

<studentName>: <run1 result> | <run2 result>

✅ Example Output
Alice: 3/5 | 5/5
Bob: 2/5 | NO_RESUB
Charlie: NO_PRIOR_RUN | 4/5
David: COMPILE_ERROR | COMPILE_ERROR

✅ Special Codes Used
Code	Meaning
NO_RESUB	Student did not submit a second version
NO_PRIOR_RUN	Student only exists in second run
COMPILE_ERROR	Program failed to compile in that run
9. Ordering and Assumptions

Recommended action order:

Prepare test suite

Run test suite on first submissions

Save results

Run test suite on second submissions

Save results

Reload results as needed

Compare two result files

The UI does not enforce strict ordering, but this scenario defines correct usage.
