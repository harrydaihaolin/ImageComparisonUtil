# ImageComparisonUtil

## Development Guide

Please change the absolute path of the sample csv file first.

use `mvn clean install` to install the program

Please refer to the java-doc and the test cases to understand
how the program works

## Design Considerations

1. The code has a good test coverage to ensure that the code works
2. The documentation and test cases are designed for developers to understand
how the program works so that if other developers wanted to make contributions 
to the program, the first thing they should do is to understand the documentation
and the test cases.
3. The program is powered with version control and the release version is in the
github branch so that the developers are getting the latest version of the program

## Function Design

1. Firstly, we use Apache common-csv as the tool to read and write to the excel file.
2. Secondly, in order to satisfy the compare only visual appearance of the image, we
used the Match and Finder Class of SikuliX api, which is powered by OpenCV SIFT. In terms
of Image A and B, it is looking for a match from B to A and when it finds the match, it
will return a similarity point as the result. That makes a difference when we compare A 
with B and when we compare B with A. So we did both, see which finds the matches first.
Since one image is similar to another image isreversible in literals.
3. Thirdly, between each comparison, we use `ElapsedUtil` to calculate the elapsed time.






