select itemID
from item
where currently = (select max(currently) from item);