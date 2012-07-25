select count (*)
from (
     select distinct itemID
     from Category
     group by itemID
     having count(*) = 4);
