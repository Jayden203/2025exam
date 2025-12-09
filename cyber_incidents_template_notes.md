# Python 数据分析 & SQLite 模板：网络安全事件案例

> 说明：这是一个带详细中文注释的 Python 数据分析脚本模板，包含：
> - 使用 pandas 读取和清洗 CSV 数据
> - 使用 matplotlib 可视化
> - 使用 SQLite 数据库存储和查询
>
> 你可以在开卷考试中将其作为参考，根据题目改文件名、列名和查询逻辑。

---

## 完整示例代码（含注释）

```python
# -*- coding: utf-8 -*-                                 # 指定源码文件的字符编码，避免中文注释在某些环境报错
# Surname_Name_StudentID                                # 交作业时把这里替换成你的姓名/学号

import pandas as pd                                     # 导入 pandas，做数据读取、清洗、汇总
import matplotlib.pyplot as plt                         # 导入 matplotlib，用于画图
import sqlite3                                          # 导入 sqlite3，用于创建和操作本地 SQLite 数据库
import torch                                            # 导入 PyTorch，用于构建和训练神经网络（本任务中可以不用，但模板保留）
from torch import nn                                    # 从 torch 中导入 nn 模块，定义网络结构（如不需要可去掉）
import numpy as np                                      # 导入 numpy，生成合成数据等
import os                                               # 操作系统接口（用于环境变量、文件操作等）

# 临时允许重复 OpenMP 运行时继续执行（长期不推荐，一般是为了解决某些运行报错）
os.environ['KMP_DUPLICATE_LIB_OK'] = 'TRUE'

print("\n======================")                      # 下面三行只是美化控制台输出
print(" TASK 1: DATA ANALYSIS")
print("======================\n")

# ---- Task 1a: Load Data ----
df = pd.read_csv("cyber_incidents.csv")                 # 读取同目录下的 CSV 数据为 DataFrame
print("▶ First 5 Rows:")                                # 打印提示信息
print(df.head(), "\n")                                 # 打印前 5 行，快速预览数据
print(f"▶ DataFrame Shape: {df.shape}\n")              # 打印 DataFrame 形状 (行数, 列数)

# 将 Date 列转换为日期类型
df["Date"] = pd.to_datetime(df["Date"], errors="coerce")# 将 Date 列转为日期类型；无法解析的置为 NaT
print(f"▶ Date dtype after conversion: {df['Date'].dtype}\n")  # 确认转换后的数据类型

# 处理缺失值
df["Count"] = df["Count"].fillna(0)                     # Count 列缺失用 0 填充（计数型）
df = df.dropna(subset=["Date"]).reset_index(drop=True)  # 丢弃 Date 为 NaT 的行；重置索引保证连续

# 清洗 Incident_Type 列
df["Incident_Type"] = (                                 # 清洗事件类型字符串
    df["Incident_Type"].astype(str)                     # 统一转为字符串
                     .str.strip()                       # 去前后空格
                     .str.upper()                       # 转大写，规避 PHISHing/Phishing 等大小写不一
)
print("▶ Unique Incident Types:")                       # 打印提示
print(df["Incident_Type"].unique(), "\n")              # 查看清洗后的去重类型值

print("----- Task 1b: Visualisation Output -----\n")   # 分节标题

# 计算每个事件类型的总次数
totals_by_type = (                                      # 计算每个事件类型的总次数
    df.groupby("Incident_Type")["Count"]
      .sum()
      .sort_values(ascending=False)
)
print("▶ Total incidents per incident type:")           # 打印提示
print(totals_by_type, "\n")                            # 输出各类型总数

# 画柱状图：总次数 vs 事件类型
plt.figure()                                            # 创建一个新图形
totals_by_type.plot(kind="bar", color="steelblue")      # 画柱状图：X=类型，Y=总次数
plt.title("Total Incidents by Type")                    # 设置图标题
plt.xlabel("Incident Type")                             # X 轴标签
plt.ylabel("Total Count")                               # Y 轴标签
plt.tight_layout()                                      # 自适应边距避免标签被遮挡
plt.show()                                              # 显示图形（在脚本里会弹窗/在某些环境内嵌显示）

# ---- Monthly totals for 2025 (MUST show zeros for missing months) ----
df_2025 = df[df["Date"].dt.year == 2025].copy()         # 过滤出 2025 年的数据
df_2025["YearMonth"] = df_2025["Date"].dt.to_period("M").astype(str)  # 转为月份粒度的字符串如 '2025-03'

# 明确列出 2025 年所有 12 个月，用于补齐没有数据的月份
all_months = [f"2025-{m:02d}" for m in range(1, 13)]    # 生成 '2025-01' 到 '2025-12'

# 汇总每个月的总事件数
monthly_totals = (                                      # 汇总每月总数
    df_2025.groupby("YearMonth")["Count"]
          .sum()
          .reindex(all_months, fill_value=0)            # 用完整月份索引重建，缺的月填 0（关键：保证 0 也显示）
)

print("▶ Monthly Totals for 2025 (Including 0 Values):\n")  # 打印提示
print(monthly_totals, "\n")                            # 打印 12 个月每月总数（包含 0）

# 画 2025 年每月总数折线图
plt.figure(figsize=(8, 4))                              # 新建图形并设置尺寸
plt.plot(                                               # 画折线图
    monthly_totals.index,                               # X 轴为月份字符串
    monthly_totals.values,                              # Y 轴为每月总数
    marker="o",                                         # 每个点用圆点标记
    markersize=8,                                       # 标记大小
    markerfacecolor="white",                            # 标记内填充白色，便于看清
    linestyle="-",                                      # 线型为实线
    linewidth=2,                                        # 线宽
    color="darkgreen"                                   # 折线颜色
)
plt.ylim(bottom=0)                                      # 强制 Y 轴从 0 开始，突出 0 值含义
# 在每个点上方标出数值（包括 0）
for x, y in zip(monthly_totals.index, monthly_totals.values):
    plt.text(x, y + 0.5, str(y), ha='center', va='bottom', fontsize=9)

plt.title("Monthly Incident Totals (2025)")             # 图标题
plt.xlabel("Month (YYYY-MM)")                           # X 轴标签
plt.ylabel("Total Incidents")                           # Y 轴标签
plt.grid(alpha=0.4, linestyle="--")                     # 添加虚线网格，增强可读性
plt.tight_layout()                                      # 调整边距
plt.show()                                              # 显示折线图

print("\n======================")                      # 分节装饰
print(" TASK 2: DATABASE WORK")
print("======================\n")

# 连接或创建 SQLite 数据库
con = sqlite3.connect("incidentsDB.sqlite")             # 如果文件不存在会自动创建
cur = con.cursor()                                      # 获取游标对象，执行 SQL 语句

# 建表：包含唯一性约束以避免重复插入
cur.execute("""                                         
CREATE TABLE IF NOT EXISTS incidents(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    Date TEXT,
    System TEXT,
    Incident_Type TEXT,
    Count INTEGER,
    UNIQUE(Date, System, Incident_Type)
);
""")

# 将 DataFrame 数据写入 SQLite 表
for row in df.itertuples(index=False):                  # 遍历 DataFrame 每一行记录
    # 使用参数化插入，避免 SQL 注入/类型错误
    cur.execute("""                                     
    INSERT OR IGNORE INTO incidents (Date, System, Incident_Type, Count)
    VALUES (?, ?, ?, ?)
    """, (row.Date.strftime("%Y-%m-%d"),              # 将 datetime 格式化为 'YYYY-MM-DD'
            row.System,
            row.Incident_Type,
            int(row.Count)))                            # 确保 Count 是整数

con.commit()                                            # 提交事务，将插入写入数据库

print("✅ Data inserted into SQLite database (duplicates skipped).\n")  # 友好提示

print("▶ Total incidents in 2025:")                     # 打印提示
# 用 pandas 执行 SQL 并展示结果
total_2025 = pd.read_sql("""                                  
SELECT SUM(Count) AS Total_2025
FROM incidents
WHERE Date >= '2025-01-01' AND Date < '2026-01-01'
""", con)
print(total_2025, "\n")

print("▶ Top 3 Systems in 2025:")                       # 打印提示
# 查询 2025 年按系统汇总并取前 3 名
top3 = pd.read_sql("""                                  
SELECT System, SUM(Count) AS Total
FROM incidents
WHERE Date >= '2025-01-01' AND Date < '2026-01-01'
GROUP BY System
ORDER BY Total DESC
LIMIT 3
""", con)
print(top3, "\n")                                      # 打印前 3 系统及其总数

# 导出前 3 系统结果为 CSV 文件
top3.to_csv("top3_systems.csv", index=False)            # 导出为 CSV 文件供上交/复查
print("💾 Exported to: top3_systems.csv\n")            # 输出保存路径提示

# 关闭数据库连接，释放资源
con.close()
```

---

## 如何在其他题目中复用这份模板？

你可以把这个脚本看成三个模块：

1. **Task 1：CSV 数据读取与清洗**
   - `pd.read_csv("xxx.csv")`
   - 列类型转换（`pd.to_datetime`）
   - 缺失值处理（`fillna` / `dropna`）
   - 字段清洗（`str.strip()`、`str.upper()`）

2. **Task 1b：可视化**
   - 柱状图：`Series.plot(kind="bar")`
   - 折线图：`plt.plot(x, y, ...)`
   - 补齐缺失月份：`reindex(all_months, fill_value=0)`

3. **Task 2：数据库工作（SQLite）**
   - 建表：`CREATE TABLE IF NOT EXISTS ...`
   - 唯一约束防止重复：`UNIQUE(列1, 列2, ...)`
   - 插入数据：`INSERT OR IGNORE ...`
   - 查询并用 pandas 读取：`pd.read_sql(SQL, con)`
   - 导出 CSV：`DataFrame.to_csv(...)`

你在新题目中主要需要修改的部分：

- CSV 文件名：`cyber_incidents.csv`
- 字段名：`Date`, `System`, `Incident_Type`, `Count`
- 图表标题和标签文字
- SQLite 表名、列名及查询语句中的条件（例如年份范围）

整体结构（读取 → 预处理 → 汇总 → 可视化 → 写入数据库 → SQL 查询）非常通用，可以直接搬到其他数据分析场景中使用。
