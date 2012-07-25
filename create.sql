create table Seller (sellerID INT PRIMARY KEY, rating INT);
create table Bidder (bidderID INT PRIMARY KEY, rating INT, location TEXT, country TEXT);
create table Bid (itemID INT, bidderID INT, time INT, amount REAL);
create table Category (itemID INT, category TEXT);
create table Item (itemID INT PRIMARY KEY, name TEXT, currently REAL, buyPrice REAL, firstBid REAL, numberOfBids INT, location TEXT, country TEXT, started INT, ends INT, sellerID INT, description TEXT);