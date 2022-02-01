# data_parsing_app

### HTTPie
http GET localhost:8080/text/1

HTTP/1.1 200 OK \
Content-Length: 1\
Content-Type: text/plain; charset=UTF-8\
Date: Mon, 24 Jan 2022 20:29:54 GMT\
Server: akka-http/10.2.6\
1

### test json
price by date: \
http GET localhost:8080/api/price_by_date <<< '{ "date":"2013-04-15" }'

average price by dates: \
http GET localhost:8080/api/average_price_by_dates <<< '{ "start":"2013-04-15", "end":"2015-04-15" }'

max and min prices by dates: \
http GET localhost:8080/api/max_min_prices_by_dates <<< '{ "start":"2013-03-15", "end":"2013-09-15" }'

get cells: \
http GET localhost:8080/api/cells

get cells by dates: \
http GET localhost:8080/api/cells_by_dates <<< '{ "start":"2013-03-15", "end":"2013-09-15" }'