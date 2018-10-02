#!/bin/bash

################
# Constants
################
OUTPUT_FILE="./processed/processed.csv"

################
# Functions
################
usage() {
  echo "usage: ./process-raw-csvs.sh [-e] [-h]"
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
echo "Bestellnummer,Hersteller,k[N/mm] Federrate,Masse,Lr[mm] Relevante Laenge,V[Nmm] Maximale potentielle Energie,Fn[N] Hoechstkraft bei statischer Belastung,De[mm] Ausserer Windungsdurchmesser,d[mm] Drahtdurchmesser" > $OUTPUT_FILE
# Process all sources
echo "Processing Gutekunst ..."
./gutekunst/process-raw-gk-csv.awk ./raw/Federndatenbank2.csv >> $OUTPUT_FILE
echo "Processing Century Springs ..."
./centurysprings/process-raw-cs-csv.awk ./raw/cs.csv >> $OUTPUT_FILE
echo "Done: `cat $OUTPUT_FILE | wc -l` lines processed"

# Open it in excel
if [ "$openInExcel" = "1" ]; then
  echo "Opening out.csv for review in Excel..."
  "C:\Program Files\Microsoft Office\root\Office16\EXCEL.EXE" $OUTPUT_FILE
fi