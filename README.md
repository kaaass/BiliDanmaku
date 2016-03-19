# BiliDanmaku[WIP]
[![Gitter](https://badges.gitter.im/kaaass/BiliDanmaku.svg)](https://gitter.im/kaaass/BiliDanmaku?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
Add user's name of each comment in bilibili.
![Demo version](demo.png)

##Runing Step
To be continued...

##How Data Comes?
"generate_user_data.php" is the way to generate user data.
P.S. SQLite command: 

CREATE TABLE IF NOT EXISTS `user_table` (

  `MID` int(11) NOT NULL,
  
  `UID` varchar(8) NOT NULL
  
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

##Thanks to
Fuckbilibili(http://www.fuckbilibili.com/)

SuperFashi(http://www.superfashi.com/)
