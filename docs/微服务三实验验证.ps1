# ==================================================================
# 微服务三实验验证脚本
# 作用：一次跑完「配置热更新 / 限流 / 熔断」三个验证
# 用法：PowerShell 里执行   & "C:\Users\jin\Doubao\chats\2026-10-11\new-chat\微服务三实验验证.ps1"
# 前提：Nacos(8848)、demo5(8081)、demo6(8082) 都已经启动
# ==================================================================

# 所有结果先收集到数组，最后一次性写文件。
# 原因：这台机器的 PowerShell 5.1 直接打印中文时输出偶尔会被吞，写文件再读最稳。
$out = @()

# ==================================================================
# 实验 1：Nacos 配置热更新
# 验证：改了 Nacos 里的配置，服务不重启也能读到新值
# ==================================================================
$out += "===== 实验1：Nacos 配置热更新 ====="

# 生成一个带时间戳的新值 —— 带时间戳才能证明"确实是刚改的"，不是旧值
$newMsg = "热更新测试成功 " + (Get-Date -Format "HH:mm:ss")

# 组装成 properties 格式的内容，先写到临时文件（Nacos 的接口要读文件）
$configContent = "custom.message=" + $newMsg
[System.IO.File]::WriteAllText("D:\nacos-hotupdate.txt", $configContent, (New-Object System.Text.UTF8Encoding($false)))

# 调用 Nacos 接口，把这份内容覆盖写入 dataId=demo5.properties
# dataId 就是"配置的名字"，必须和 demo5 里 spring.config.import 写的那个一致
$null = curl.exe -s -X POST "http://localhost:8848/nacos/v1/cs/configs" -d "dataId=demo5.properties" -d "group=DEFAULT_GROUP" --data-urlencode "content@D:\nacos-hotupdate.txt"

# 等 3 秒，让 Nacos 把变更推给 demo5（@RefreshScope 会自动重新读）
Start-Sleep -Seconds 3

# 访问 /config 接口，看它读到的值
$r = Invoke-WebRequest -Uri "http://localhost:8081/config" -UseBasicParsing -TimeoutSec 5
$out += "我改成的值： " + $newMsg
$out += "接口返回： " + $r.Content
$out += ""

# ==================================================================
# 实验 2：Sentinel 限流
# 验证：/hello 配了 QPS=2，连续请求会有一部分被拦成 429
# ==================================================================
$out += "===== 实验2：Sentinel 限流（/hello，QPS=2）====="

for ($i = 1; $i -le 10; $i++) {
    try {
        # 正常返回：200
        $r = Invoke-WebRequest -Uri "http://localhost:8081/hello" -UseBasicParsing -TimeoutSec 5
        $out += "第$i 次 | " + $r.StatusCode + " | " + $r.Content
    } catch {
        # 被限流时返回 429，Invoke-WebRequest 会当成异常抛出，需要从异常里挖状态码和响应体
        $status = ""
        $body = ""
        if ($_.Exception.Response) {
            $status = $_.Exception.Response.StatusCode.value__
            try {
                $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
                $body = $reader.ReadToEnd()
                $reader.Close()
            } catch {
                $body = "(响应体读不到)"
            }
        } else {
            # 连不上服务器（服务没启动）
            $status = "ERROR"
            $body = $_.Exception.Message
        }
        $out += "第$i 次 | " + $status + " | " + $body
    }
}
$out += ""

# ==================================================================
# 实验 3：Sentinel 熔断
# 验证：/callBslow 每次 1 秒，连续请求会触发熔断，之后直接走兜底
# ==================================================================
$out += "===== 实验3：Sentinel 熔断（/callBslow）====="

# 关键：先等 12 秒。
# 熔断的 timeWindow=10 秒，如果上次已经熔断，要等它过期、状态回到"关闭"，
# 否则一上来就直接是兜底，看不到"正常 → 熔断"的转折过程。
Start-Sleep -Seconds 12

for ($i = 1; $i -le 8; $i++) {
    # 用秒表量每次耗时：慢接口约 1013ms，兜底约 10~20ms，两者一眼能区分
    $sw = [System.Diagnostics.Stopwatch]::StartNew()
    try {
        $r = Invoke-WebRequest -Uri "http://localhost:8081/callBslow" -UseBasicParsing -TimeoutSec 5
        $sw.Stop()
        $out += "第$i 次 | " + $r.StatusCode + " | " + $sw.ElapsedMilliseconds + "ms | " + $r.Content
    } catch {
        $sw.Stop()
        $out += "第$i 次 | ERROR | " + $sw.ElapsedMilliseconds + "ms | " + $_.Exception.Message
    }
}

# 全部结果写盘（UTF-8 无 BOM，中文不会乱码）
[System.IO.File]::WriteAllLines("D:\三实验验证结果.txt", $out, (New-Object System.Text.UTF8Encoding($false)))
"完成，结果见 D:\三实验验证结果.txt"
