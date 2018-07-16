create table char_test(char_col char(10));
insert into char_test(char_col) values('string1'),(' string2'),('string3 ');
select concat("'",char_col,"'") from char_test;--末尾空格自动去了

create table enum_test(e enum('fish','apple','dog') not null);
insert into enum_test(e) values('fish'),('dog'),('apple');
select e + 0 from enum_test;
select e from enum_test order by e;--排序是按枚举顺序排的!!

create table bittest(a bit(8));
insert into bittest(a) values(b'00111001');
select a,a+0 from bittest;

create table my_char_test like char_test;--只复制表结构
 
 