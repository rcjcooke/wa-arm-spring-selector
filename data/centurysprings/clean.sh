#!/bin/bash

################
# Functions
################
usage() {
  echo "usage: ./clean.sh -o outputFile [-i inputPath] [-e] [-h]"
  echo ""
  echo -e "-o, --output\t\tOutput the result to the specified file, e.g. build/out.csv"
  echo -e "-i, --input\t\tThe path to the CS.P* input files, e.g. src/main/resources/ - default: ./"
  echo -e "-e, --excel\t\tOpen the resulting output file in Excel"
  echo -e "-h, --help\t\tShow this usage text"
}

################
# Main
################

# Setup control variables
openInExcel=0
outputFile=""
inputPath="./"

# Process arguments
while [ "$1" != "" ]; do
    case $1 in
        -i | --input )  shift
                        inputPath=$1
                        ;;
        -o | --output ) shift
                        outputFile=$1
                        ;;
        -e | --excel )  openInExcel=1
                        ;;
        -h | --help )   usage
                        exit
                        ;;
        * )             echo "Unexpected argument: $1"
                        usage
                        exit 1
    esac
    shift
done
if [ "$outputFile" = "" ]; then
  echo "No output file specified"
  usage
  exit 2
fi

# Make sure the directory exists for the file
mkdir -p -- "${outputFile%/*}" 

# Put the headers in
echo "OD / in,OD / mm,Order Num,Length / in,Length / mm,Rate / lbs per in,Rate / N per mm,Initial Tension / lbs,Initial Tension / N,Sugg. Max. Defl. / in,Sugg. Max. Defl. / mm,Sugg. Max. Load / lbs,Sugg. Max. Load / N,Wire Dia. / in,Wire Dia. / mm,MAT'L,FNSH" > $outputFile
# Process all 55 pages
for ((i=1;i<55;i++)); do
  echo "`cat $outputFile | wc -l`: Processing $inputPath/CS.P$i ..."
  cat "$inputPath/CS.P$i" | sed 's/  */,/g' | sed 's/,\(MW\|SPR\|HD\|OT\|SST\|BC\|PB\|B\)\([A-Z,]*\),\([0-9]*\)/,\1\2\n\3/g' | ./CSCSVClean.awk >> $outputFile
done
echo "Done: `cat $outputFile | wc -l` lines processed"
echo "Output file: $outputFile"

# Open it in excel if required
if [ "$openInExcel" = "1" ]; then
  echo "Opening $outputFile for review in Excel..."
  "C:\Program Files\Microsoft Office\root\Office16\EXCEL.EXE" $outputFile
fi