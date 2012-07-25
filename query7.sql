select count(distinct category)
from Category NATURAL JOIN Item NATURAL JOIN Bid 
where amount > 100;