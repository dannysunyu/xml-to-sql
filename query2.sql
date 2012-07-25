select count(*)
from (
select distinct sellerID id, location from Item) 
where location = 'New York';