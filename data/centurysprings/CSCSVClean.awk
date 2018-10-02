#!/bin/awk -f
BEGIN {
	FS=",";
	OFS=",";
	RS="\n";
}
{
  # Possible options for field 8:
  #  16 instead of 1,6
  #  163.2 instead of 1,6,3.2
  #  1632 instead of 1,6,3,2

	if (NF == 16) {
    # Fields 8 and 9 have been concatenated: 16 instead of 1,6
    $8 = gensub(/([0-9])([0-9])/, "\\1,\\2", "g", $8)
    print $0
	} else if (NF == 15 || NF == 14) {
    # Fields 8 and 9 and 10 have been concatenated: 161.1 instead of 1,6,1.1 OR 161 instead of 1,6,1 OR 1611 instead of 1,6,11
    # If 14 then last field is also missing
    $8 = gensub(/([0-9])([0-9])([0-9]*.?[0-9]*)/, "\\1,\\2,\\3", "g", $8)
    print $0
  } else {
    print $0
  }  
}