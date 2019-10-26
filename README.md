# Flights Map-Reduce

## Start node
sh ./node

## Start clients
### Query 1
sh ./query1 -Daddresses=[ADRESSES] -DinPath=[ABSOLUTE PATH] -DoutPath=[ABSOLUTE PATH] 

### Query 2
sh ./query2 -Dn=[LIMIT] -Daddresses=[ADRESSES] -DinPath=[ABSOLUTE PATH] -DoutPath=[ABSOLUTE PATH] 

### Query 3
sh ./query3 -Daddresses=[ADRESSES] -DinPath=[ABSOLUTE PATH] -DoutPath=[ABSOLUTE PATH] 

### Query 4
sh ./query4 -Dn=[LIMIT] -Doaci=[OACI] -Daddresses=[ADRESSES] -DinPath=[ABSOLUTE PATH] -DoutPath=[ABSOLUTE PATH] 

### Query 5
sh ./query5 -Dn=[LIMIT] -Daddresses=[ADRESSES] -DinPath=[ABSOLUTE PATH] -DoutPath=[ABSOLUTE PATH] 

### Query 6
sh ./query6 -Dmin=[MINIMUM] -Daddresses=[ADRESSES] -DinPath=[ABSOLUTE PATH] -DoutPath=[ABSOLUTE PATH] 
