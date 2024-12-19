alter table interest
add foreign key (deposit)
references transactionshistory(balance);bankdetails