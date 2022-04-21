--[[
作者: 应卓
功能: 分布式锁 - 释放

参数:
    1 -> 锁键
    2 -> UUID

返回值:
    true -> 成功
    false -> 失败
]]

local lockKey = ARGV[1]
local uuid = ARGV[2]

local uuidCheck = redis.call("GET", lockKey)

if uuidCheck == uuid then
    redis.call("DEL", lockKey)
    return true
else
    return false
end
