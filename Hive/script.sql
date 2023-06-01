create temporary table if not exists ad.dim_ads_info_full
(
    ad_id         string comment '??id',
    ad_name       string comment '????',
    product_id    string comment '????id',
    product_name  string comment '??????',
    product_price decimal(16, 2) comment '??????',
    material_id   string comment '??id',
    material_url  string comment '????',
    group_id      string comment '???id',
    dt            string
);

create temporary table if not exists ad.dim_crawler_user_agent
(
    pattern       string comment '?????',
    addition_date string comment '????',
    url           string comment '????url',
    instances     array<string> comment 'UA??'
);

create temporary table if not exists ad.dim_platform_info_full
(
    id               string comment '??id',
    platform_name_en string comment '????(??)',
    platform_name_zh string comment '????(??)',
    dt               string
);

create temporary table if not exists ad.dwd_ad_event_inc
(
    event_time             bigint comment '????',
    event_type             string comment '????',
    ad_id                  string comment '??id',
    ad_name                string comment '????',
    ad_product_id          string comment '????id',
    ad_product_name        string comment '??????',
    ad_product_price       decimal(16, 2) comment '??????',
    ad_material_id         string comment '????id',
    ad_material_url        string comment '??????',
    ad_group_id            string comment '???id',
    platform_id            string comment '????id',
    platform_name_en       string comment '??????(??)',
    platform_name_zh       string comment '??????(??)',
    client_country         string comment '???????',
    client_area            string comment '???????',
    client_province        string comment '???????',
    client_city            string comment '???????',
    client_ip              string comment '???ip??',
    client_device_id       string comment '?????id',
    client_os_type         string comment '?????????',
    client_os_version      string comment '?????????',
    client_browser_type    string comment '????????',
    client_browser_version string comment '????????',
    client_user_agent      string comment '???UA',
    is_invalid_traffic     boolean comment '???????',
    dt                     string
);

create external table if not exists ad.ods_ad_log_inc
(
    time_local     string comment '?????????????',
    request_method string comment 'HTTP????',
    request_uri    string comment '????',
    status         string comment '?????????',
    server_addr    string comment '???????ip'
)
    partitioned by (dt string) row format serde 'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'
    with serdeproperties ('field.delim' = '\u0001') stored as
    inputformat 'org.apache.hadoop.mapred.TextInputFormat'
    outputformat 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
    location 'hdfs://192.168.137.128:9820/warehouse/ad/ods/ods_ad_log_inc'
    tblproperties ('bucketing_version' = '2');

create external table if not exists ad.ods_ads_info_full
(
    id           string comment '????',
    product_id   string comment '??id',
    material_id  string comment '??id',
    group_id     string comment '???id',
    ad_name      string comment '????',
    material_url string comment '????'
)
    partitioned by (dt string) row format serde 'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'
    with serdeproperties ('field.delim' = '\t') stored as
    inputformat 'org.apache.hadoop.mapred.TextInputFormat'
    outputformat 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
    location 'hdfs://192.168.137.128:9820/warehouse/ad/ods/ods_ads_info_full'
    tblproperties ('bucketing_version' = '2');

create external table if not exists ad.ods_ads_platform_full
(
    id          string comment '??',
    ad_id       string comment '??id',
    platform_id string comment '??id',
    create_time string comment '????',
    cancel_time string comment '????'
)
    partitioned by (dt string) row format serde 'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'
    with serdeproperties ('field.delim' = '\t') stored as
    inputformat 'org.apache.hadoop.mapred.TextInputFormat'
    outputformat 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
    location 'hdfs://192.168.137.128:9820/warehouse/ad/ods/ods_ads_platform_full'
    tblproperties ('bucketing_version' = '2');

create external table if not exists ad.ods_platform_info_full
(
    id               string comment '??id',
    platform_name_en string comment '????(??)',
    platform_name_zh string comment '????(??)'
)
    partitioned by (dt string) row format serde 'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'
    with serdeproperties ('field.delim' = '\t') stored as
    inputformat 'org.apache.hadoop.mapred.TextInputFormat'
    outputformat 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
    location 'hdfs://192.168.137.128:9820/warehouse/ad/ods/ods_platform_info_full'
    tblproperties ('bucketing_version' = '2');

create external table if not exists ad.ods_product_info_full
(
    id    string comment '??id',
    name  string comment '????',
    price decimal(16, 2) comment '????'
)
    partitioned by (dt string) row format serde 'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'
    with serdeproperties ('field.delim' = '\t') stored as
    inputformat 'org.apache.hadoop.mapred.TextInputFormat'
    outputformat 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
    location 'hdfs://192.168.137.128:9820/warehouse/ad/ods/ods_product_info_full'
    tblproperties ('bucketing_version' = '2');

create external table if not exists ad.ods_server_host_full
(
    id   string comment '??',
    ipv4 string comment 'ipv4??'
)
    partitioned by (dt string) row format serde 'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe'
    with serdeproperties ('field.delim' = '\t') stored as
    inputformat 'org.apache.hadoop.mapred.TextInputFormat'
    outputformat 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
    location 'hdfs://192.168.137.128:9820/warehouse/ad/ods/ods_server_host_full'
    tblproperties ('bucketing_version' = '2');


