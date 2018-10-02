# wa-arm-spring-selector: data project

Directory structure:
* centurysprings - Catalogues, scraped pages, cleanup script and processing script for Century Springs
* gutekunst - Web scraped and processed CSVs, Excel spreadsheets and processing script for Gutekunst
* raw - The CSVs for each supplier after cleanup
* processed - The supplier CSVs consolidated into a single CSV with the required columns

`process-raw-csvs.sh` takes the CSVs in the `raw` directory and combines them in to the processed CSV in the `processed` directory for use by the spring selector app.
