--[[
作者: 应卓
功能: 分布式锁 - 锁止

参数:
    1 -> 锁键
    2 -> UUID
    3 -> 锁键TTL (秒)

返回值:
    true -> 成功
    false -> 失败
]]

local lockKey = ARGV[1]
local uuid = ARGV[2]
local ttlInSeconds = ARGV[3]

local res = redis.call("SET", lockKey, uuid, "EX", ttlInSeconds, "NX")

if res == nil then
    return false
else
    return true
end
