--[[
可重入分布式锁 - 解锁
作者: 应卓

KEYS   : 未使用
ARGV[1]: 作为键的字符串
ARGV[2]: HASH的 field

return:
    true : 解锁成功
    false: 解锁失败
--]]

local key = ARGV[1]
local field = ARGV[2]

-- 如果键已存在并且已存在保存重入数的字段，则表示锁不属于当前线程
if redis.call('EXISTS', key) == 1 and redis.call('HEXISTS', key, field) == 0 then
    return false
end

-- 重入计数器减去1
local n = redis.call('HINCRBY', key, field, -1)

-- 如果计数器减到0了，则说明所有的重入线程都已解锁。需要删除key
if n <= 0 then
    redis.call('DEL', key)
end

return true
