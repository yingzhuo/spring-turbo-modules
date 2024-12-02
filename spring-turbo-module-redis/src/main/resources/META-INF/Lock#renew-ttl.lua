--[[
可重入分布式锁 - 刷新TTL
作者: 应卓

KEYS   : 未使用
ARGV[1]: 作为键的字符串
ARGV[2]: HASH的 field
ARGV[3]: 新的TTL

return:
    true : 续期成功
    false: 续期失败
--]]

local key = ARGV[1]
local field = ARGV[2]
local newTtl = ARGV[3]

-- 如果键已存在并且已存在保存重入数的字段，则表示锁不属于当前线程
if redis.call('EXISTS', key) == 1 and redis.call('HEXISTS', key, field) == 0 then
    return false
else
    return redis.call('EXPIRE', key, newTtl) == 1
end
