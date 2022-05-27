#Welcome to Mobile.de scraper

###Scope
- Retrieve all car announcement
- Responsible scraping by:
  - log each http request to mobile.de website
  - limit/enforce only one full run per day
  - limit to 1 request per 10-15 seconds


- Seems like /sitemap.xml was removed. TODO:
  - use wayback machine to get back all the main category links:
    - https://suchen.mobile.de/fahrzeuge/sitemap-pls-carspecification-0.xml
    - https://suchen.mobile.de/fahrzeuge/sitemap-pls-carspecification-1.xml
    - https://suchen.mobile.de/fahrzeuge/sitemap-pls-carspecification-2.xml
    - ...
  - Extract all sub links from those websites. Save them!
  - Continue working