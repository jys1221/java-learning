# ==================================================================
# 一键更新到 GitHub
# 作用：把本地工程代码 + 学习资料同步到仓库目录，然后自动提交并推送
# 用法：右键 →「使用 PowerShell 运行」，或者在 PowerShell 里执行：
#       & "C:\Users\jin\IdeaProjects\java-learning\一键更新到GitHub.ps1"
# ==================================================================

# ---------------- 配置区（路径变了只改这里）----------------
$RepoRoot   = "C:\Users\jin\IdeaProjects\java-learning"            # 仓库目录（推 GitHub 的就是这里）
$DevRoot    = "C:\Users\jin\IdeaProjects"                          # 开发目录的父目录
$Projects   = @("demo4", "demo5", "demo6", "demo7")                # 要同步的工程
$DocsSource = "C:\Users\jin\Doubao\chats\2026-10-11\new-chat"      # 学习资料源目录
$DocsFiles  = @("Java后端学习总纲.md", "Java错题本.md", "微服务三实验验证.ps1", "demo5-check.ps1", "java路线图进度.html")
$GhExe      = "C:\Program Files\GitHub CLI\gh.exe"                 # GitHub CLI 路径

# ---------------- 输出收集（最后一次性写日志）----------------
$log = @()
$log += "===== 开始同步 $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') ====="

# ==================================================================
# 第 1 步：同步工程代码
#   robocopy 参数说明：
#     /E        复制所有子目录（含空目录）
#     /XD       排除这些目录：target（编译产物）、.idea（IDE 配置）、.git
#     /XF       排除这些文件：*.iml
#     /NFL /NDL /NJH /NJS   不打印文件/目录清单，减少刷屏
# ==================================================================
foreach ($p in $Projects) {
    $src = Join-Path $DevRoot $p
    if (-not (Test-Path $src)) {
        $log += "跳过 $p（源目录不存在）"
        continue
    }
    $dest = Join-Path $RepoRoot $p
    robocopy $src $dest /E /XD target .idea .git /XF *.iml /NFL /NDL /NJH /NJS | Out-Null
    $log += "同步工程 $p 完成"
}

# ==================================================================
# 第 2 步：同步学习资料到 docs 目录
# ==================================================================
$docsDest = Join-Path $RepoRoot "docs"
if (-not (Test-Path $docsDest)) {
    New-Item -ItemType Directory -Path $docsDest -Force | Out-Null
}
foreach ($f in $DocsFiles) {
    $srcFile = Join-Path $DocsSource $f
    if (Test-Path $srcFile) {
        Copy-Item $srcFile -Destination $docsDest -Force
        $log += "同步资料 $f 完成"
    } else {
        $log += "跳过资料 $f（源文件不存在）"
    }
}

# ==================================================================
# 第 3 步：抹掉 demo4 里的真实密码（最重要的一步）
#   每次复制都会把本地的真实密码带过来，如果不替换就直接提交，密码就会上公开仓库
#   这里用正则把任何 password=xxx 强制替换成占位符
# ==================================================================
$propsPath = Join-Path $RepoRoot "demo4\src\main\resources\application.properties"
if (Test-Path $propsPath) {
    $c = [System.IO.File]::ReadAllText($propsPath, [System.Text.Encoding]::UTF8)

    # (spring\.datasource\.password)=.*  匹配到行尾，$1 表示括号里那段
    # 单引号里的 $1 不会被 PowerShell 展开，会原样交给 .NET 的正则引擎
    $c = [System.Text.RegularExpressions.Regex]::Replace($c, '(spring\.datasource\.password)=.*', '$1=CHANGE_ME_你的数据库密码')
    $c = [System.Text.RegularExpressions.Regex]::Replace($c, '(spring\.security\.user\.password)=.*', '$1=CHANGE_ME_你的登录密码')

    # 确保文件开头有提示说明（如果没有就补上）
    if ($c -notmatch "^# 注意") {
        $c = "# 注意：本文件上传到公开仓库，密码已替换为占位符`r`n# 本地运行时请把 CHANGE_ME_... 换成你自己的真实密码`r`n" + $c
    }

    [System.IO.File]::WriteAllText($propsPath, $c, (New-Object System.Text.UTF8Encoding($false)))
    $log += "已检查并抹掉 demo4 密码"
}

# ==================================================================
# 第 4 步：确保 git 能通过 gh 的凭据推送
#   gh auth setup-git 是幂等操作：已经配置过也不会出问题
# ==================================================================
try {
    & $GhExe auth setup-git 2>&1 | Out-Null
} catch {
    $log += "gh 认证配置跳过（不影响已登录状态）"
}

# ==================================================================
# 第 5 步：提交并推送
# ==================================================================
Push-Location $RepoRoot

# git status --porcelain 有输出 = 有改动；没输出 = 什么都没变
$status = (& git status --porcelain 2>&1 | Out-String).Trim()

if ($status -eq "") {
    $log += "没有任何改动，跳过提交和推送"
} else {
    & git add -A 2>&1 | Out-Null

    # 提交信息带上时间，以后在 GitHub 上能看出每次什么时候更新的
    $msg = "update: $(Get-Date -Format 'yyyy-MM-dd HH:mm')"
    & git commit -m $msg 2>&1 | Out-Null
    $log += "已提交：$msg"

    # 推送并检查结果
    $pushOut = (& git push 2>&1 | Out-String)
    if ($LASTEXITCODE -eq 0) {
        $log += "推送成功 → https://github.com/jys1221/java-learning"
    } else {
        $log += "推送失败，详细信息如下："
        $log += $pushOut
    }
}

Pop-Location

# ==================================================================
# 第 6 步：把本次结果写到日志文件（加在 .gitignore 里，不会被提交）
# ==================================================================
$logPath = Join-Path $RepoRoot "sync-log.txt"
[System.IO.File]::WriteAllLines($logPath, $log, (New-Object System.Text.UTF8Encoding($false)))

"同步结束，结果见仓库目录下的 sync-log.txt"
