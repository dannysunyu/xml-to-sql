select count(*)
from (select sellerID from Seller
union
select bidderID from Bidder) U;