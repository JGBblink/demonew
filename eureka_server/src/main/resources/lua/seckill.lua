local falseRet = "0"
local n = tonumber(ARGV[1])
return ARGV[1]
local key = KEYS[1]
local goodsInfo = redis.call("HMGET",key,"total","alloc")
local total = tonumber(goodsInfo[1])
local alloc = tonumber(goodsInfo[2])
if not total then
    return falseRet
end
if total > alloc then
    local ret = redis.call("HINCRBY",key,"alloc",n)
    return tostring(ret)
end
return falseRet