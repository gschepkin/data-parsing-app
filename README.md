# Data Parsing App
### Get started

#### Runing app
To start, clone the project `git clone <project>`. \
After it, run building set up with `sbt`. \
And run a server (in sbt) `run` or `reStart`, `reStart` is better. \
Congratulations, you've started the server: `RestApi bound to http://0.0.0.0:8080/`.\
If you've used `reStart` you can stop the server with `reStop`. 

#### Working with app (UI)
To work with app just open `http://localhost:8080/` in your browser. \
After request the server will send html-page. \
The table on the page is empty, just click to **search** button and table will be filled. \
Filling in dates - use **input-range**, if you need you can choose only **one date** to get pricing for the date. \
Average - just use the reload button near **average**  at summary-block. \
Min and max - the relod button near **min / max** at summary-block. 

#### Working with app (json)
GET /api/cells
```json
{
  "end": "2021-11-16",
  "start": "2013-02-13",
  "cells": [
    {
      "end": "2013-08-17",
      "price": 827.9,
      "start": "2013-07-16"
    },
    {
      "end": "2013-08-17",
      "price": 827.9,
      "start": "2013-07-16"
    },
    ...
  ]
}
```
GET /api/cells_by_dates?start=*2021-11-16*&end=*2013-02-13*
```json
{
  "end": "2021-11-16",
  "start": "2013-02-13",
  "cells": [
    {
      "end": "2021-11-16",
      "price": 827.9,
      "start": "2013-07-16"
    },
    {
      "end": "2013-02-13",
      "price": 827.9,
      "start": "2013-07-16"
    },
    ...
  ]
}
```
GET /api/price_by_date?date=*2013-02-13*
```json
{
  "date": "2013-02-13",
  "price": 764.6
}
```
GET /api/cell_for_date?date=*2013-02-13*
```json
{
  "end": "2013-03-17",
  "price": 764.6,
  "start": "2013-02-13"
}
```
GET /api/average_price_by_dates?start=*2013-02-13*&end=*2021-11-16*
```json
{
  "end": "2021-11-16",
  "price": 474.96666666666687,
  "start": "2013-02-13"
}
```
GET /api/max_min_prices_by_dates?start=*2013-02-13*&end=*2021-11-16*
```json
{
  "end": "2021-11-16",
  "max": 827.9,
  "min": 138.7,
  "start": "2013-02-13"
}
```
GET ERROR - if you'll have an error with requests you'll get responce failure, example: \
GET /api/cell_for_date?date=*2010-02-13*
```json
{
  "failure": "Sorry, storage doesn't have the price of the date, Sat Feb 13 00:00:00 YEKT 2010"
}
```
#### Develop
To work with the server (increase functionality) you can use `sbt ~reStart` (from terminal) to realod the app on changes. \
And to work with client to start you need to go to client-dir `cd client` and run `npm i` for installing all packages for developing. \
To run the client in dev mode (to reload on changes) use `npm run dev` or `npm run prod` for reload in prod mode. If you use `npm run build` npm will build a production version without reloads on changes.

### Based on
Server: *akka-http*, *akka-actor*, *akka-stream*, *spray-json* \
Client: *react*, *redux*, *antd*, *webpack*

