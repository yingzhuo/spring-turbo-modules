--[[
可重入分布式锁 - 加锁
作者: 应卓

KEYS   : 未使用
ARGV[1]: 作为键的字符串
ARGV[2]: HASH的 field
ARGV[3]: 键TTL (秒)

return:
    true : 加锁成功
    false: 加锁失败
--]]

local key = ARGV[1]
local field = ARGV[2]
local ttl = ARGV[3]

-- 如果键已存在并且已存在保存重入数的字段，则加锁失败
if redis.call('EXISTS', key) == 1 and redis.call('HEXISTS', key, field) == 0 then
    return false
end

-- 重入计数器增加1
local count = redis.call('HINCRBY', key, field, 1)

-- 设置TTL
redis.call('EXPIRE', key, ttl)

return count > 0
