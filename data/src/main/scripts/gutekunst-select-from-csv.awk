#!/bin/awk -f
BEGIN {
	FS=",";
	OFS=",";
	RS="\n";
}
FNR > 1 {
  # Note: FNR > 1 => We're not processing the header line (which is therefore required to be there!)

  # Gutekunst Columns:
  #  1 Bestellnummer,
  #  2 Hersteller,
  #  3 Werkstoff,
  #  4 Oesenform,
  #  5 d[mm] Drahtdurchmesser,
  #  6 De[mm] Ausserer Windungsdurchmesser,
  #  7 Detol[mm] (+-) Tolereanz fuer ausseren Windungsdurchmesser,
  #  8 Dh[mm] Kleinster Huelsendurchmesser,
  #  9 Oesenstellung,
  #  10 Oesensttol,
  #  11 L0[mm] Laenge unbelastete Feder,
  #  12 L0tol[mm] (+-) Toleranz fuer ungespannte Laenge,
  #  13 Lk[mm] Laenge der unbelasteten Feder,
  #  14 Lh[mm] Abstand der Oeseninnenkante vom Federkoerper,
  #  15 F0[N] Innere Vorspannkraft,
  #  16 Fn[N] Hoechstkraft bei statischer Belastung,
  #  17 Fntol[N] (+-) Toleranz bei statischer Hoechstkraft,
  #  18 k[N/mm] Federrate,
  #  19 Sn[mm] Groesster Federweg bei statischer Belastung,
  #  20 Masse,
  #  21 Preisgruppe,
  #  22 W[Nmm] Federungsarbeit,
  #  23 Ln[mm] Maximal zulaessige Laenge,
  #  24 Lr[mm] Relevante Laenge,
  #  25 Lf[mm] Freie Laenge,
  #  26 V[Nmm] Maximale potentielle Energie

  # Required Output Columns:
  #  1 Bestellnummer,
  #  2 Hersteller,
  #  3 k[N/mm] Federrate,
  #  4 Masse,
  #  5 Lr[mm] Relevante Laenge,
  #  6 V[Nmm] Maximale potentielle Energie,
  #  7 Fn[N] Hoechstkraft bei statischer Belastung,
  #  8 De[mm] Ausserer Windungsdurchmesser,
  #  9 d[mm] Drahtdurchmesser

  # All data as raw input 
  print $1, $2, $18, $20, $24, $26, $16, $6, $5
}