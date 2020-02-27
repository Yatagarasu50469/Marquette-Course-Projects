ls */*.java > sources.txt
mkdir classes
javac -d classes @sources.txt
rm sources.txt
