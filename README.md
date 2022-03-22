# Cassandra

# Cassandra 是什麼？

Apache Cassandra是高度可延伸的，高效能的分散式NoSQL資料庫。

Cassandra旨在處理多個節點之間的巨量資料工作負載，而無需擔心單點故障。 它在其節點之間具有對等分散式系統，資料分布在叢集中的所有節點上。

- 在Cassandra中，每個節點是獨立的，同時與其他節點互連。 叢集中的所有節點都扮演著相同的角色。
- 叢集中的每個節點都可以接受讀取和寫入請求，而不管資料實際位於叢集中的位置。
- 在一個節點發生故障的情況下，可以從網路中的其他節點提供讀/寫請求。

# 實作

## 安裝

1. 拉Docker image

```bash
docker pull cassandra:latest
```

1. 啟動

```bash
docker run --restart=always --name cassandra-1 -d -e CASSANDRA_BROADCAST_ADDRESS=192.168.0.1 -p 7000:7000 cassandra:latest
```

1. 啟動CQL shell

```bash
docker exec -it cass_cluster cqlsh
```

## 設定(略過則使用預設)

因為使用docker安裝，所以設定檔的位置會在`/etc/cassandra`

有`cassandra.yaml`就可以設定一個單節點的cluster，若需要更多節點有一些相關的檔案：

- `cassandra.yaml`: Cassandra主要的設定檔
- `cassandra-env.sh`: 環境變數放的地方
- `cassandra-rackdc.properties` OR `cassandra-topology.properties`: 設定cluster的rack跟datacenter的相關資訊
- `logback.xml`: loggings設定檔
- `jvm-*`: server跟client的jvm設定檔
- `commitlog_archiving.properties`:  `commitlog`模板設定

<aside>
📌 A rack server, also known as a rack mount server, rack-mounted server or rack mount computer, is a computer designed to be situated in a rectangular structure called a server rack.

</aside>

另外在`./conf`中可以找到範例設定檔：

- `metrics-reporter-config-sample.yaml`: 設定metrics-report要蒐集什麼
- `cqlshrc.sample`: CQL shell(cqlsh)的設定

## 插入資料以及查詢

1. 建立`docker-compose.yml`

```yaml
version: "3.7"
 
services:
 
  cassandra-1:
    image: "cassandra"
    container_name: "cassandra-1"
    ports:
      - "9042:9042"
    environment:
      - "MAX_HEAP_SIZE=256M"
      - "HEAP_NEWSIZE=128M"
      - "CASSANDRA_SEEDS=cassandra-1,cassandra-2"
 
  cassandra-2:
    image: "cassandra"
    container_name: "cassandra-2"
    environment:
      - "MAX_HEAP_SIZE=256M"
      - "HEAP_NEWSIZE=128M"
      - "CASSANDRA_SEEDS=cassandra-1,cassandra-2"
    depends_on:
      - "cassandra-1"
 
  cassandra-3:
    image: "cassandra"
    container_name: "cassandra-3"
    environment:
      - "MAX_HEAP_SIZE=256M"
      - "HEAP_NEWSIZE=128M"
      - "CASSANDRA_SEEDS=cassandra-1,cassandra-2"
    depends_on:
      - "cassandra-2"
```

1. 查看node狀態

```bash
docker exec -it cassandra-1 nodetool status
docker exec -it cassandra-2 nodetool status
docker exec -it cassandra-3 nodetool status
--------------------------------------------
Datacenter: datacenter1
=======================
Status=Up/Down
|/ State=Normal/Leaving/Joining/Moving
--  Address     Load       Tokens  Owns (effective)  Host ID                               Rack
UN  172.20.0.3  69.06 KiB  16      67.6%             9038360c-e365-4468-847b-219117bc92aa  rack1
UN  172.20.0.4  83.23 KiB  16      60.9%             bc342395-633a-407c-b758-c14b6d324d0e  rack1
UN  172.20.0.2  74.11 KiB  16      71.5%             df400a86-9fbe-43a8-aaaa-f7b6be9b3f2b  rack1
```

參考：[http://www.inanzzz.com/index.php/post/ecgh/docker-compose-for-single-and-multi-node-cassandra-example](http://www.inanzzz.com/index.php/post/ecgh/docker-compose-for-single-and-multi-node-cassandra-example)

Docker Desktop也可查看

![Untitled](Cassandra%200b832/Untitled.png)

1. 分別進到不同node對同一個資料庫做操作
    1. 進到cassandra-1建立keyspace
        
        ```bash
        docker exec -it cassandra-1 cqlsh
        Connected to Test Cluster at 127.0.0.1:9042
        [cqlsh 6.0.0 | Cassandra 4.0.3 | CQL spec 3.4.5 | Native protocol v5]
        Use HELP for help.
        cqlsh> CREATE KEYSPACE test_keyspace WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 2 };
        cqlsh> exit
        ```
        
    2. 進到cassandra-2建立table
        
        ```bash
        docker exec -it cassandra-2 cqlsh
        Connected to Test Cluster at 127.0.0.1:9042
        [cqlsh 6.0.0 | Cassandra 4.0.3 | CQL spec 3.4.5 | Native protocol v5]
        Use HELP for help.
        cqlsh> use test_keyspace;
        cqlsh:test_keyspace> CREATE TABLE users(
                         ... id int PRIMARY KEY,
                         ... name varchar,
                         ... email varchar);
        cqlsh:test_keyspace> exit
        ```
        
    3. 進到cassandra-2插入資料
        
        ```bash
        docker exec -it cassandra-3 cqlsh
        Connected to Test Cluster at 127.0.0.1:9042
        [cqlsh 6.0.0 | Cassandra 4.0.3 | CQL spec 3.4.5 | Native protocol v5]
        Use HELP for help.
        cqlsh> use test_keyspace;
        cqlsh:test_keyspace> INSERT INTO users (id,name,email) VALUES (1,'Jane','jane@mail.com');
        cqlsh:test_keyspace> select * from users ;
        
         id | email         | name
        ----+---------------+------
          1 | jane@mail.com | Jane
        
        (1 rows)
        cqlsh:test_keyspace> exit
        ```
        
2. 用GUI介面查看 (TablePlus: [https://tableplus.com/](https://tableplus.com/))
    1. 打開tableplus建立新的connection
        
        ![Untitled](Cassandra%200b832/Untitled%201.png)
        
    2. 只需輸入上方紅框部分(User, Password預設都是`cassandra`)
        
        ![Untitled](Cassandra%200b832/Untitled%202.png)
        
    3. 連線完成後可看到剛剛新增的內容
        
        ![Untitled](Cassandra%200b832/Untitled%203.png)
