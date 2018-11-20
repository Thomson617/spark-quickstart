select iplocation.* ,count(1)
from iplocation
  join tempIp
    on ip_transfer >= longitude and ip_transfer <= latitude
group by longitude,latitude,ip_transfer;

select count(1)
from iplocation
  join tempIp
    on ip_transfer >= longitude and ip_transfer <= latitude
group by longitude,latitude,ip_transfer;


select longitude, latitude,ip_transfer from tempIp left  join iplocation on ip_transfer >= longitude and ip_transfer <= latitude limit 30


select longitude, latitude,city,ip_transfer, count(1) as number from tempIp
left  join iplocation on ip_transfer >= start_ip and ip_transfer <= end_ip
group by longitude, latitude,city,ip_transfer
