create database cadastroclientesjava;

use cadastroclientesjava;

create table tbusuarios(
	iduser int primary key,
    usuario varchar(15) not null,
    telefone varchar(15),
    login varchar(15) not null unique,
    senha varchar(250) not null,
    perfil varchar(20) not null
);

create table tbclientes(
	idcli int primary key auto_increment,
    nomecli varchar(50) not null,
    endereçocli varchar(100),
    telefonecli varchar(15) not null,
    emailcli varchar(50) unique,
    planocli varchar(50) unique
);

SELECT * FROM cadastroclientesjava.tbclientes;