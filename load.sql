.separator <>
.import sql_data/seller.dat Seller
.import sql_data/bidder.dat Bidder
.import sql_data/bid.dat Bid
.import sql_data/category.dat Category
.import sql_data/item.dat Item
update Item set buyPrice = null where buyPrice = 'NULL';
update Bidder set location = null where location = 'NULL';
update Bidder set country = null where location = 'NULL';