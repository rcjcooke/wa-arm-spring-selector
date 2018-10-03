#!/bin/bash

################
# Functions
################
usage() {
  echo "usage: ./process-raw-csvs.sh -s scriptInputPath -c csvInputPath -o outputFile [-e] [-h]"
  echo ""
  echo -e "-s, --scriptinputpath\t\tThe directory containing the selector scripts, e.g. src/main/scripts"
  echo -e "-c, --csvinputpath\t\tThe directory containing the CSVs to be processed, e.g. build/csvs"
  echo -e "-o, --outputfile\t\tOutput the result to the specified file, e.g. build/processed.csv"
  echo -e "-e, --excel\t\tOpen the resulting output file (out.csv) in Excel"
  echo -e "-h, --help\t\tShow this usage text"
}

################
# Main
################

# Setup control variables
openInExcel=0
scriptInputPath=""
csvInputPath=""
outputFile=""

# Process arguments
while [ "$1" != "" ]; do
    case $1 in
        -s | --scriptinputpath )  shift
                                  scriptInputPath=$1
                                  ;;
        -c | --csvinputpath )     shift
                                  csvInputPath=$1
                                  ;;
        -o | --outputfile )       shift
                                  outputFile=$1
                                  ;;
        -e | --excel )            openInExcel=1
                                  ;;
        -h | --help )             usage
                                  exit
                                  ;;
        * )                       usage
                                  exit 1
    esac
    shift
done
if [ "$scriptInputPath" = "" ]; then
  echo "No script input directory specified"
  usage
  exit 2
fi
if [ "$csvInputPath" = "" ]; then
  echo "No CSV input directory specified"
  usage
  exit 3
fi
if [ "$outputFile" = "" ]; then
  echo "No output file specified"
  usage
  exit 4
fi

# Put the headers in
echo "Bestellnummer,Hersteller,k[N/mm] Federrate,Masse,Lr[mm] Relevante Laenge,V[Nmm] Maximale potentielle Energie,Fn[N] Hoechstkraft bei statischer Belastung,De[mm] Ausserer Windungsdurchmesser,d[mm] Drahtdurchmesser" > $outputFile
# Process all sources
for scriptFile in $scriptInputPath/*.awk; do
  # Skip over if there's no match
  [ -e "$scriptFile" ] || continue
  # Get the file name by stripping the path away
  filename=${scriptFile##*/}
  # Get the base by stripping away everything after and including the first '-' 
  base=${filename%%-*}
  # Execute the script against the CSV file and append it to the output file
  ./$scriptFile $csvInputPath/$base.csv >> $outputFile
done


# echo "Processing Gutekunst ..."
# ./gutekunst/process-raw-gk-csv.awk ./raw/Federndatenbank2.csv >> $outputFile
# echo "Processing Century Springs ..."
# ./centurysprings/process-raw-cs-csv.awk ./raw/cs.csv >> $outputFile
# echo "Done: `cat $outputFile | wc -l` lines processed"

# Open it in excel
if [ "$openInExcel" = "1" ]; then
  echo "Opening out.csv for review in Excel..."
  "C:\Program Files\Microsoft Office\root\Office16\EXCEL.EXE" $outputFile
fi