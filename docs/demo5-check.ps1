# demo5 自检脚本
# 用法：在 PowerShell 里执行  C:\Users\jin\Doubao\chats\2026-10-11\new-chat\demo5-check.ps1

$ErrorActionPreference = "Continue"

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  demo5 self-check (port 8081)" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# ---- 第1步：端口是否在监听 ----
Write-Host ""
Write-Host "[1/3] Check port 8081 ..." -ForegroundColor Yellow
$conn = Get-NetTCPConnection -LocalPort 8081 -State Listen -ErrorAction SilentlyContinue
if ($conn) {
    Write-Host "    OK  : demo5 is running" -ForegroundColor Green
} else {
    Write-Host "    FAIL: demo5 is NOT running (start it in IDEA first)" -ForegroundColor Red
    Write-Host ""
    Write-Host "Done. Nothing to test." -ForegroundColor Gray
    return
}

# ---- 第2步：单次访问 ----
Write-Host ""
Write-Host "[2/3] Call /hello once ..." -ForegroundColor Yellow
$code = curl.exe -s -o NUL -w "%{http_code}" "http://localhost:8081/hello"
Write-Host ("    HTTP status = " + $code) -ForegroundColor Green
if ($code -eq "200") {
    $body = curl.exe -s "http://localhost:8081/hello"
    Write-Host ("    Body = " + $body) -ForegroundColor Gray
}

# ---- 第3步：连续压测 10 次 ----
Write-Host ""
Write-Host "[3/3] Send 10 requests quickly ..." -ForegroundColor Yellow
$codes = @()
for ($i = 1; $i -le 10; $i++) {
    $codes += curl.exe -s -o NUL -w "%{http_code}" "http://localhost:8081/hello"
}
Write-Host ("    Codes : " + ($codes -join " | ")) -ForegroundColor Gray

$ok    = ($codes | Where-Object { $_ -eq "200" }).Count
$block = ($codes | Where-Object { $_ -eq "429" }).Count
$other = $codes.Count - $ok - $block

Write-Host ""
Write-Host ("    Passed   (200) = " + $ok) -ForegroundColor Green
Write-Host ("    Blocked  (429) = " + $block) -ForegroundColor Red
if ($other -gt 0) {
    Write-Host ("    Other    (" + $other + ") = check IDEA console for errors") -ForegroundColor Magenta
}

# ---- 结论 ----
Write-Host ""
if ($block -gt 0 -and $ok -gt 0) {
    Write-Host "RESULT: rate limiting IS working." -ForegroundColor Green
} elseif ($ok -eq 10) {
    Write-Host "RESULT: all passed. Either rule is off, or QPS is high enough." -ForegroundColor Yellow
} else {
    Write-Host "RESULT: unexpected, check the codes above." -ForegroundColor Red
}
Write-Host ""
