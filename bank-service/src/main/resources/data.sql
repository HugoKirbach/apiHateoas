INSERT INTO loan (id, state,firstname,lastname,adress,birthdate,currentjob,incomelastthreeyears,loanamount,loanduration,lastupdate) 
VALUES (1, 'Début', 'John', 'Doe', 'Paris', '1980-01-01', 'Developer', 3000, 100000, 24, now()),
(2, 'Début', 'Jane', 'Doe', 'Paris', '2000-01-01', 'Developer', 160000, 100000, 26, now());


INSERT INTO finance (id, firstname, lastname, incomelastthreeyears)
VALUES (1, 'Hugo', 'Kirbach', 12345678);