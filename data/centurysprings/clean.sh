#!/bin/bash

################
# Functions
################
usage() {
  echo "usage: ./clean.sh [-e] [-h]"
  echo ""
  echo -e "-e, --excel\t\tOpen the resulting output file (out.csv) in Excel"
  echo -e "-h, --help\t\tShow this usage text"
}

################
# Main
################

# Setup control variables
openInExcel=0

# Process arguments
while [ "$1" != "" ]; do
    case $1 in
        -e | --excel )          openInExcel=1
                                ;;
        -h | --help )           usage
                                exit
                                ;;
        * )                     usage
                                exit 1
    esac
    shift
done

# Put the headers in
echo "OD / in,OD / mm,Order Num,Length / in,Length / mm,Rate / lbs per in,Rate / N per mm,Initial Tension / lbs,Initial Tension / N,Sugg. Max. Defl. / in,Sugg. Max. Defl. / mm,Sugg. Max. Load / lbs,Sugg. Max. Load / N,Wire Dia. / in,Wire Dia. / mm,MAT'L,FNSH" > out.csv
# Process all 55 pages
for ((i=1;i<55;i++)); do
  echo "`cat out.csv | wc -l`: Processing CS.P$i ..."
  cat "CS.P$i" | sed 's/  */,/g' | sed 's/,\(MW\|SPR\|HD\|OT\|SST\|BC\|PB\|B\)\([A-Z,]*\),\([0-9]*\)/,\1\2\n\3/g' | ./CSCSVClean.awk >> out.csv
done
echo "Done: `cat out.csv | wc -l` lines processed"
# Open it in excel
if [ "$openInExcel" = "1" ]; then
  echo "Opening out.csv for review in Excel..."
  "C:\Program Files\Microsoft Office\root\Office16\EXCEL.EXE" out.csv
fi