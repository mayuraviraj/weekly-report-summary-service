Weekly Report Summary Service
-----------------------------

This service runs to expose the following api
/getweeklysummary

This API will expose data in weekly summary basis.

Configuration
-------------
issue.tracker.api - This property should be point to third party issue tracking system. For development use default with locally running simulator provided in third-party-api-sim project
redis.server - Redis server
redis.port - port for Redis server
data.cache.service - If redis server is not available service can be run with local cache. Use "local" value for this. To switch to redis, use "redis" value.


Test Coverage
-------------
Test coverage is 44% as per Intellij IDEA. Added test cases are basic to cover happy path

Pending
-------
1. Update Redis with redis cluster connections
2. Integrate sonar, spot bugs
3. Test Coverage to 70%