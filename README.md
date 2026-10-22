# shop-api-demo

一个**故意做小**的演示仓库，用来给「多 Agent 研发协作助手」提供**真实 GitHub 数据源**。

它不是产品，只是一块靶场：让 Agent 能真的去查 Issue、PR、CI、代码和文档。

## 为什么要有这个仓库

Agent 需要被查询的对象。用假数据能跑通流程，但没法演示「真实调用 GitHub API」。
这个仓库把四类信息摆好，让 Agent 能：

| 仓库里的东西 | Agent 拿它做什么 |
|---|---|
| 3 个 Issue（1 个 P1 bug、1 个 feature、1 个 P1 auth bug） | Issue 分诊：分类、优先级、行动项 |
| 1 个 PR（**描述说只改舍入，实际删了鉴权**） | PR 审查：**描述与改动不一致的冲突检测** |
| 1 次**故意失败**的 CI | CI 排障：从日志提取关键错误块 |
| 2 篇文档 | 知识问答：金额规则、鉴权规范 |

## 目录

```
shop-api-demo/
├── .github/workflows/ci.yml        # 故意失败的 CI（供 Agent 读日志）
├── docs/
│   ├── 订单金额计算规则.md
│   └── 鉴权中间件说明.md
├── src/main/java/
│   ├── order/PriceService.java     # 主分支上是 buggy 版本（DOWN）
│   └── auth/AuthFilter.java        # 主分支上是正确的鉴权
├── src/test/java/order/PriceServiceTest.java
├── ISSUES.md                       # 3 个 Issue 的文案（复制到 GitHub）
├── PR_DESCRIPTION.md               # PR 的误导性描述（复制到 PR 里）
└── README.md
```

## 分支说明

| 分支 | 内容 |
|---|---|
| `main` | 初始状态：`PriceService` 用 `RoundingMode.DOWN`（有 bug），`AuthFilter` 有 token 校验 |
| `fix/price-rounding` | **PR 用**：把 `DOWN` 改成 `HALF_UP`，同时**删掉鉴权校验**（制造冲突） |

## 建完怎么验证

```powershell
# Issue
curl.exe -s -H "Authorization: Bearer $env:GITHUB_TOKEN" `
  "https://api.github.com/repos/<user>/shop-api-demo/issues?state=open"

# PR 改动文件（能看到 AuthFilter 被删行）
curl.exe -s -H "Authorization: Bearer $env:GITHUB_TOKEN" `
  "https://api.github.com/repos/<user>/shop-api-demo/pulls/1/files"

# CI run（应该是 failure）
curl.exe -s -H "Authorization: Bearer $env:GITHUB_TOKEN" `
  "https://api.github.com/repos/<user>/shop-api-demo/actions/runs?per_page=3"
```

> ⚠️ 仓库里的 Java 代码**不需要能编译**，CI 也不会真的跑测试——它只是 echo 一段失败日志然后 `exit 1`。
