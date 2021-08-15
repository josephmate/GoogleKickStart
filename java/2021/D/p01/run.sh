rm src/*.class
javac src/Solution.java
java -cp src Solution < input_01.txt > actual_output.txt
echo "Actual"
cat actual_output.txt
echo "Expected"
cat expected_output_01.txt
