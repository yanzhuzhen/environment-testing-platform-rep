

create table usertable(
    uno int primary key,
    username char(10),
    password char(10),
    email char(20)
);
create table roletable(
    rno int primary key,
    rolename char(10),
    isAdmin boolean
);
create table UR(
    urno int primary key,
    uno int,
    rno int,
    foreign key(uno) references usertable(uno),
    foreign key(rno) references roletable(rno),
    buildTime DATE
);
create table algotable(
    algono int primary key,
    algoname char(20)
);

create table userToAlgo(
    uno int,
    algono int,
    id int primary key,
    foreign key(uno) references usertable(uno),
    foreign key(algono) references algotable(algono)
);

create table menu(
    mno int primary key,
    mname char(20)
);

create table MR(
    id int primary key,
    mno int,
    foreign key(mno) references menu(mno),
    rno int,
    foreign key(rno) references roletable(rno)
)

