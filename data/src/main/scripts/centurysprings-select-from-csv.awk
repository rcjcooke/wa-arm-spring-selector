#!/bin/awk -f
BEGIN {
	FS=",";
	OFS=",";
	RS="\n";
}
FNR > 1 {
  # Note: FNR > 1 => We're not processing the header line (which is therefore required to be there!)

  # Century Springs Columns:
  #  1 OD / in,
  #  2 OD / mm,
  #  3 Order Num,
  #  4 Length / in,
  #  5 Length / mm,
  #  6 Rate / lbs per in,
  #  7 Rate / N per mm,
  #  8 Initial Tension / lbs,
  #  9 Initial Tension / N,
  #  10 Sugg. Max. Defl. / in,
  #  11 Sugg. Max. Defl. / mm,
  #  12 Sugg. Max. Load / lbs,
  #  13 Sugg. Max. Load / N,
  #  14 Wire Dia. / in,
  #  15 Wire Dia. / mm,
  #  16 MAT'L,
  #  17 FNSH

  # Required Output Columns:
  #  1 Order Number,
  #  2 Manufacturer,
  #  3 Rate / N per mm,
  #  4 Mass / g,
  #  5 Maximum length under max load / mm,
  #  6 Max potential energy / Nmm,
  #  7 Max static load / N,
  #  8 Outer Diameter / mm,
  #  9 Wire Diameter / mm

  # All data as raw input except: 
  #  * The max potential energy is calculated
  #  * The catalogue only prints to 2 decimal places, so if the rate is 0 then 
  #    reverse-engineer the rate in Newtons per mm based on the rate in imperial units
  #  * Mass of springs is not provided so defaults to 0
  
  rate = $7
  if (rate == 0) {
    rate = $6 * 0.175126835;
  }
  print $3, "Century Springs", rate, 0, $11, ($13 * $13 / rate / 2), $13, $2, $15
}