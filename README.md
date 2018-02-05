# weather

Simulation of weather

Assumptions to follow in the simulation:

 - Temperature and pressure have a direct relationship.
 - Temperature and altitude have an inverse relationship of an average decrease of 6 degrees for each 1Km altitude change.
 - Temperature May vary up to ±5°C between days.
 - Humid air and pressure have an inverse relationship, but humid depends on other factors like rain and wind so it can 
 be more randomised than the others, plus it is a percentile value.
 - Sea level temperature average will be assume to have following ranges:
    *   From the equatorial latitude to 30°Lat => temperature goes from average 30°C to 20°C
    *   From 30°Lat to 60°Lat => temperature goes from average 20°C to 0°C
    *   From 60°Lat to 90°Lat => temperature goes from average 0°C to -20°C
 - Depending of the season from above of the 30°Lat the temperature average may have an increase/decrease of max ±10°C
 - Depending of the time of the day the temperature may vary ±5°C 
 - Atmospheric pressure at sea level is in average 1010 millibars with ±30 of deviation.
 - Atmospheric pressure decrease at most in the highest land point by 256 millibars(Mount Everest)                     
 - Random generated 10 positions around the planet earth
 - Each position follow 10 hours of simulation
 - Open sea elevation changed as 0 (weather is calculated in the atmosphere)
 - Seasons (Winter and Summer)affect the simulation 


**Configurations**
All the assumptions are configurable trough the application.conf file.
Refer to the references for explanations on the values.

Logging is set to Error level to avoid extra output but it is open to be change in the logback.xml file if required  

 **References:**

http://littleshop.physics.colostate.edu/activities/atmos2/TempPressureRelated.pdf
https://www.enotes.com/homework-help/what-relationship-between-altitude-temperature-556362
https://www.quora.com/How-does-air-pressure-relate-to-humidity
http://www-das.uwyo.edu/~geerts/cwx/notes/chap16/geo_clim.html
https://www.windows2universe.org/earth/Atmosphere/pressure_vs_altitude.html


TODO

Add extra test units, time from work didn't let me add more, but they are in important stuff. 