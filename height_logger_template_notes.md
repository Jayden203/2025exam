# Python Practice Task 3 模板：学生身高记录（CSV + pandas + matplotlib）

> 说明：这是一个 **可复用的考试模板**，适用于需要：
> - 读写 CSV
> - 用 pandas / numpy 做统计
> - 用 matplotlib 画直方图  
> 的题目。  
> 你可以把“身高”换成“体重/价格/温度”等，只需改变量名与文案即可。

---

## 一、完整代码（height_logger.py）

```python
"""
学生身高记录脚本 (Practice Task 3 模板版)

功能：
- Task 1: 创建/检查 CSV 文件，用于存储学生姓名和身高
- Task 2: 通过输入循环，追加记录到 CSV
- Task 3: 用 pandas + numpy 做统计摘要，并写入文本文件
- Task 4: 用 matplotlib 画身高直方图

你在考试中：
- 可以把 Name/Height 换成其他字段，比如 Product/Price
- 可以把文件名 heights.csv 换成别的
- 逻辑结构基本不变，照抄+稍微修改即可
"""

import os                  # 用于检查文件是否存在
import csv                 # 用于向 CSV 文件中写入数据（行）
import pandas as pd        # 用于读取 CSV 并做数据分析
import numpy as np         # 用于计算平均值、最大值、最小值
import matplotlib.pyplot as plt  # 用于画直方图


# ===== 全局常量（方便统一改名字） =====
CSV_FILE = "heights.csv"              # 存放明细记录的 CSV 文件
SUMMARY_FILE = "height_summary.txt"   # 存放统计摘要的文本文件


def setup_csv():
    """
    Task 1: CSV 文件准备工作

    - 检查 heights.csv 是否存在：
      - 如果不存在：创建文件并写入表头（Name, Height）
      - 如果已存在：不覆盖，只提示已经存在
    - 打印提示信息：
      - 告诉用户文件是新建的还是已有的
      - 告诉用户文件已经准备好可以追加（append）数据
    """
    file_exists = os.path.exists(CSV_FILE)  # True/False：文件是否已经存在

    if not file_exists:
        # 文件不存在：创建新文件，并写入表头
        with open(CSV_FILE, mode="w", newline="", encoding="utf-8") as f:
            writer = csv.writer(f)
            # 写入表头列名，与题目要求一致：Name, Height
            writer.writerow(["Name", "Height"])
        print(f"{CSV_FILE} created (new file with header).")
    else:
        # 文件已存在：不要覆盖，只提示
        print(f"{CSV_FILE} already exists (will append new data).")

    # 不管是否新建，最终都说明一下：后续会以“追加模式”写入数据
    print(f"{CSV_FILE} is ready for appending new records.\n")


def data_entry():
    """
    Task 2: 数据录入

    - 反复提示用户输入 name 和 height（cm）
    - 输入 name 为 'q' 时退出循环
    - 所有合法记录（name + height 整数）追加写入 heights.csv
    - 不在这里写表头，表头由 setup_csv() 创建

    返回值：
        None（不返回数据），但是会把用户输入写入 CSV 文件
    """
    print("=== Task 2: Data Entry ===")

    # 临时在内存里先存一批要写入的行，最后一次性写入 CSV
    new_rows = []

    while True:
        name = input("Enter name (or 'q' to quit): ")

        # 如果用户输入 q（不区分大小写），结束录入循环
        if name.lower() == "q":
            break

        # 继续问身高
        height_str = input("Enter height (cm): ")

        # 尝试把身高转换成整数
        try:
            height = int(height_str)
        except ValueError:
            # 转换失败：说明用户没输入纯数字
            print("Invalid height. Please enter an integer number (e.g. 170).")
            # 跳过本次循环，重新输入 name
            continue

        # 如果走到这里，说明 name 和 height 都合法
        # 把一条记录保存到列表中，稍后统一写入 CSV
        new_rows.append((name, height))

    # 退出循环后，判断有没有新数据需要写入
    if new_rows:
        # 以“追加模式（a）”打开 CSV 文件，在文件末尾添加新行
        with open(CSV_FILE, mode="a", newline="", encoding="utf-8") as f:
            writer = csv.writer(f)
            for row in new_rows:
                writer.writerow(row)

        print(f"\nData saved to {CSV_FILE}\n")
    else:
        # 用户直接输入 q，没有任何新数据
        print("\nNo new data entered. Existing data (if any) is unchanged.\n")


def summary_stats():
    """
    Task 3: 统计摘要

    - 使用 pandas 读取 heights.csv 到 DataFrame
    - 使用 numpy 计算身高的：
        - 总记录数
        - 平均值
        - 最小值
        - 最大值
    - 把这些信息写入 height_summary.txt
    - 在屏幕上打印这些统计结果

    返回值：
        heights (numpy array) — 用于后续画图。如果没有数据则返回空数组。
    """
    # 用 pandas 读取 CSV 文件到 DataFrame
    df = pd.read_csv(CSV_FILE)

    # 从 DataFrame 中取出 Height 列，并转为 numpy 数组，方便用 numpy 函数
    heights = df["Height"].to_numpy()

    # 记录总数
    count = len(heights)
    print(f"Total records: {count}")

    if count == 0:
        # 极端情况：CSV 只有表头，没有任何行
        print("No height data available. Statistics and plot will be skipped.")
        # 仍然写一个空的 summary 文件，避免文件不存在
        with open(SUMMARY_FILE, mode="w", encoding="utf-8") as f:
            f.write("Height Summary\n")
            f.write("No data available.\n")
        return heights  # 空数组

    # 使用 numpy 计算平均值、最小值、最大值
    avg_height = np.mean(heights)
    min_height = np.min(heights)
    max_height = np.max(heights)

    # 打印统计结果
    print(f"Average height: {avg_height:.2f} cm")
    print(f"Minimum height: {min_height} cm")
    print(f"Maximum height: {max_height} cm")

    # 把统计结果写入文本文件
    with open(SUMMARY_FILE, mode="w", encoding="utf-8") as f:
        f.write("Height Summary\n")
        f.write(f"Total records: {count}\n")
        f.write(f"Average height: {avg_height:.2f} cm\n")
        f.write(f"Minimum height: {min_height} cm\n")
        f.write(f"Maximum height: {max_height} cm\n")

    print(f"\nSummary saved to {SUMMARY_FILE}\n")

    # 返回 heights 数组，后面画图要用
    return heights


def plot_histogram(heights):
    """
    Task 4: 画身高分布直方图

    参数：
        heights: numpy 数组，包含所有身高数据
    """
    # 如果没有数据，就不画图
    if heights is None or len(heights) == 0:
        print("No data to plot. Skipping histogram.\n")
        return

    # 创建直方图
    plt.hist(heights, bins="auto")  # bins="auto" 让 matplotlib 自动选择箱子数
    plt.title("Height Distribution")      # 图标题
    plt.xlabel("Height (cm)")             # x 轴标签
    plt.ylabel("Frequency")               # y 轴标签

    # 显示图形窗口（在有图形界面的环境中会弹出窗口）
    plt.show()


def main():
    """
    主流程函数：

    1. Task 1: 准备 CSV 文件（创建/检查，并打印相应提示）
    2. Task 2: 进行数据录入，把新记录追加到 CSV
    3. Task 3: 读取 CSV，计算统计信息，写入 summary 文本，打印结果
    4. Task 4: 利用统计中的 heights 数据画直方图
    """
    setup_csv()          # Task 1
    data_entry()         # Task 2
    heights = summary_stats()  # Task 3
    plot_histogram(heights)    # Task 4


# 只有当这个脚本直接运行时，才执行 main()
# 如果在别的脚本中 import 这个文件，就不会自动运行 main()
if __name__ == "__main__":
    main()
```

---

## 二、在考试中如何“改名字复用”

上面的代码是 **“学生身高记录”** 版，你在考试可以照着这个结构，改成：

- 学生成绩记录
- 商品价格记录
- 温度日志记录
- ……等等

你主要需要改这几类地方：

### 1. 文件名与列名

在顶部常量处修改：

```python
CSV_FILE = "heights.csv"              # 改成题目要求的 CSV 文件名
SUMMARY_FILE = "height_summary.txt"   # 改成题目要求的摘要文件名
```

以及创建表头的地方：

```python
writer.writerow(["Name", "Height"])
```

例如做“商品价格记录”，可以改成：

```python
writer.writerow(["Product", "Price"])
```

同时，在 `summary_stats()` 中也要把列名 `"Height"` 改成对应的新列名：

```python
values = df["Price"].to_numpy()
```

### 2. 输入提示文案 + 变量名（可选）

原始版本：

```python
name = input("Enter name (or 'q' to quit): ")
height_str = input("Enter height (cm): ")
```

如果改成商品价格，可以写：

```python
product = input("Enter product name (or 'q' to quit): ")
price_str = input("Enter price: ")
```

并相应地调整存入 `new_rows` 的内容。

### 3. 统计结果中的文字和单位

打印和写入 summary 文件时，会出现 “height” 和 “cm”：

```python
print(f"Average height: {avg_height:.2f} cm")
```

改成：

```python
print(f"Average price: {avg_price:.2f} $")
```

或其他单位。

### 4. 图表标题与坐标轴标签

在 `plot_histogram()` 里：

```python
plt.title("Height Distribution")
plt.xlabel("Height (cm)")
plt.ylabel("Frequency")
```

可以随题目要求改成：

```python
plt.title("Price Distribution")
plt.xlabel("Price ($)")
plt.ylabel("Frequency")
```

---

## 三、结构记忆小抄

如果你不想每次都从头看完，可以记住这个“骨架”然后回来查细节：

1. **导入模块**

```python
import os, csv
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
```

2. **两个文件常量**

```python
CSV_FILE = "xxx.csv"
SUMMARY_FILE = "xxx_summary.txt"
```

3. **四个函数 + 一个 main**

- `setup_csv()`：检查/创建 CSV，写表头。
- `data_entry()`：循环输入，'q' 退出，写入 CSV。
- `summary_stats()`：读 CSV，用 pandas+numpy 做统计，写 summary.txt。
- `plot_histogram(heights)`：直方图可视化。
- `main()`：按顺序调用上面四个函数。

4. **脚本入口**

```python
if __name__ == "__main__":
    main()
```

---

有了这份 markdown，你可以在 VS Code 或任何编辑器中打开，对照题目快速修改字段和文本，就能写出一个“看起来非常完整”的数据读取 + 分析 + 可视化脚本。
