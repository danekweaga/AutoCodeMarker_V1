\# Scenario: Auto Code Marker for a Pass/Fail Grade Checker Assignment

\## Context

You are a teaching assistant for an introductory programming course
where students are learning basic input/output, conditionals, and simple
logic.

For one of the early assignments, students must implement a
\*\*\"Pass/Fail Grade Checker\"\*\* program:

\> The program reads a single integer grade from standard input (0--100)
and prints \`PASS\` if the grade is \*\*greater than or equal to 50\*\*,
otherwise it prints \`FAIL\`.

Example: - Input: \`75\` → Output: \`PASS\` - Input: \`30\` → Output:
\`FAIL\`

Hundreds of students submit similar programs, and the goal is to
automate checking whether each student\'s program behaves correctly on a
standard set of test inputs.

\-\--

\## Stakeholders

\- \*\*Instructor / Course Coordinator\*\*  - Defines the assignment
specification and marking criteria.  - Chooses the set of test cases
that all submissions must pass.

\- \*\*Teaching Assistants (TAs)\*\*  - Configure test suites in
\*\*Auto Code Marker\*\*.  - Run the tests against all student
submissions.  - Review and interpret the results.

\- \*\*Students\*\*  - Submit their solution programs.  - Expect
consistent, fair, and timely feedback.

\-\--

\## System Role

\*\*Auto Code Marker\*\* is used to:

1\. \*\*Create and manage a test suite\*\* for the Pass/Fail Grade
Checker assignment. 2. \*\*Define individual test cases\*\*, each with:
 - Input (the grade entered by the user)  - Expected output (\`PASS\` or
\`FAIL\`) 3. \*\*Run the test suite\*\* against all student submissions
in a selected folder. 4. \*\*View results\*\* for each submission:  -
Which test cases passed  - Which test cases failed  - Overall behavior
of each program

\-\--

\## Test Data Used

The following logical test cases are used for this assignment:

1\. \`basic_pass\`  - Input: \`75\`  - Expected output: \`PASS\`  -
Purpose: Typical passing grade.

2\. \`basic_fail\`  - Input: \`30\`  - Expected output: \`FAIL\`  -
Purpose: Typical failing grade.

3\. \`boundary_pass\`  - Input: \`50\`  - Expected output: \`PASS\`  -
Purpose: Tests the exact boundary condition for passing.

4\. \`boundary_fail\`  - Input: \`49\`  - Expected output: \`FAIL\`  -
Purpose: Tests just below the passing threshold.

5\. \`zero_fail\`  - Input: \`0\`  - Expected output: \`FAIL\`  -
Purpose: Tests the lowest valid grade.

These test cases are documented in the file \`test_data_pass_fail.txt\`
and can be implemented as actual runnable test cases inside a test suite
folder used by Auto Code Marker.

\-\--

\## How Auto Code Marker Is Used in This Scenario

1\. The TA launches \*\*Auto Code Marker\*\*. 2. The TA configures a
test suite named, for example, \`PassFailSuite\`. 3. Inside
\`PassFailSuite\`, the TA creates one \`.txt\` file per test case:  -
File name corresponds to the test case name (e.g., \`basic_pass.txt\`).
 - Each file contains:  - Line 1: the input grade (e.g., \`75\`)  - Line
2: the expected output (e.g., \`PASS\`) 4. The TA selects the folder
containing all student submissions. 5. The TA selects the
\`PassFailSuite\` test suite from the dropdown. 6. The TA clicks \*\*Run
Test Cases\*\*. 7. After execution:  - The \*\*Output Viewer\*\* shows,
for each submission, which test cases passed or failed.  - TAs use this
information to assign marks or investigate incorrect behavior.

This scenario demonstrates how \*\*Auto Code Marker\*\* supports
repeatable, consistent, and efficient automated marking for a simple,
clearly specified programming task.
