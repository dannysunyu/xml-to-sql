select count(*)
from Seller
where sellerID in (select bidderID from Bidder);
