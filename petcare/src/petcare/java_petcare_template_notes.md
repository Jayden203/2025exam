
# Java 面向对象考试模板 & 讲解（宠物寄养系统示例）

> 说明：这是一份可在开卷考试中快速查阅、直接“改名复用”的 Java 面向对象模板，示例题是“宠物寄养服务系统”，你可以把 Animal/Enclosure/PetService 换成 Student/Room/Library 等。

---

## 一、枚举：AnimalSize

### 1.1 代码

```java
package petcare;

public enum AnimalSize {
    SMALL,
    MEDIUM,
    LARGE
}
```

### 1.2 要点说明

- `package petcare;`
  - 声明这个文件属于 `petcare` 包。
  - 要求文件路径为 `src/petcare/AnimalSize.java`。
- `public enum AnimalSize { ... }`
  - `enum` 表示“固定的一组常量”，适合用来表示：大小、状态、等级等。
  - 这里是三个体型：`SMALL`, `MEDIUM`, `LARGE`。
- 使用方式：
  ```java
  AnimalSize s = AnimalSize.SMALL;
  ```

### 1.3 套路总结

只要题目出现“有几种固定取值”，比如：
- small / medium / large
- NEW / IN_PROGRESS / DONE
- MALE / FEMALE

就可以写一个 `enum` 来表示。

---

## 二、实体类：Animal

### 2.1 完整代码

```java
package petcare;

import java.util.Objects;

public class Animal {

    private String name;
    private AnimalSize size;
    private int comfortTempLower;
    private int comfortTempUpper;

    public Animal(String name, AnimalSize size, int comfortTempLower, int comfortTempUpper) {
        setName(name);
        setSize(size);
        setComfortTempRange(comfortTempLower, comfortTempUpper);
    }

    public String getName() {
        return name;
    }

    public AnimalSize getSize() {
        return size;
    }

    public int getComfortTempLower() {
        return comfortTempLower;
    }

    public int getComfortTempUpper() {
        return comfortTempUpper;
    }

    public void setName(String name) {
        if (name == null || name.trim().length() < 3) {
            throw new IllegalArgumentException("Name must be at least 3 characters.");
        }
        this.name = name.trim();
    }

    public void setSize(AnimalSize size) {
        if (size == null) {
            throw new IllegalArgumentException("Size cannot be null.");
        }
        this.size = size;
    }

    public void setComfortTempRange(int lower, int upper) {
        if (lower < 0 || lower > 50 || upper < 0 || upper > 50) {
            throw new IllegalArgumentException("Temperature bounds must be between 0 and 50.");
        }
        if (lower > upper) {
            throw new IllegalArgumentException("Lower bound cannot be greater than upper bound.");
        }
        this.comfortTempLower = lower;
        this.comfortTempUpper = upper;
    }

    public void setComfortTempLower(int lower) {
        setComfortTempRange(lower, this.comfortTempUpper);
    }

    public void setComfortTempUpper(int upper) {
        setComfortTempRange(this.comfortTempLower, upper);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", comfortTempLower=" + comfortTempLower +
                ", comfortTempUpper=" + comfortTempUpper +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Animal)) return false;
        Animal animal = (Animal) o;
        return Objects.equals(name, animal.name) && size == animal.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size);
    }
}
```

### 2.2 逐段说明

#### package / import

- `package petcare;`：包声明，和文件夹名对应。
- `import java.util.Objects;`：导入 `Objects` 工具类，用于安全实现 `equals`、`hashCode`。

#### 字段（成员变量）

```java
private String name;
private AnimalSize size;
private int comfortTempLower;
private int comfortTempUpper;
```

- `private`：封装，外部不能直接访问，只能通过 getter/setter。
- `String name`：动物名字。
- `AnimalSize size`：动物体型，使用枚举。
- `int comfortTempLower/Upper`：舒适温度上下界。

**通用套路：**

> 对象的属性 = 类中的 `private` 字段。

#### 构造方法

```java
public Animal(String name, AnimalSize size, int comfortTempLower, int comfortTempUpper) {
    setName(name);
    setSize(size);
    setComfortTempRange(comfortTempLower, comfortTempUpper);
}
```

- 构造器方法名 = 类名。
- `new Animal(...)` 时会调用。
- 通过调用 setter，复用验证逻辑。

**通用套路：**

- 实体类通常有一个“设置所有属性”的构造方法。
- 如果有校验逻辑，构造器尽量调用 setter。

#### Getter（读取属性）

```java
public String getName() {
    return name;
}
```

- 命名：`get + 字段名首字母大写`。
- 返回字段值。

**通用套路：**

- 几乎所有字段都要写 getter 方法，尤其是考试题常要求“添加访问器方法”。

#### Setter + 校验（重点）

示例：名字校验

```java
public void setName(String name) {
    if (name == null || name.trim().length() < 3) {
        throw new IllegalArgumentException("Name must be at least 3 characters.");
    }
    this.name = name.trim();
}
```

- 检查参数是否合法，不合法就 `throw new IllegalArgumentException(...)`。
- 使用 `this.name` 区分字段和参数。

温度区间校验：

```java
public void setComfortTempRange(int lower, int upper) {
    if (lower < 0 || lower > 50 || upper < 0 || upper > 50) {
        throw new IllegalArgumentException("Temperature bounds must be between 0 and 50.");
    }
    if (lower > upper) {
        throw new IllegalArgumentException("Lower bound cannot be greater than upper bound.");
    }
    this.comfortTempLower = lower;
    this.comfortTempUpper = upper;
}
```

单独修改上下界时复用范围方法：

```java
public void setComfortTempLower(int lower) {
    setComfortTempRange(lower, this.comfortTempUpper);
}
```

**通用套路：**

- 所有“有约束”的字段（如长度、范围）都在 setter 中进行检查。
- 不合法时抛 `IllegalArgumentException`，非常常见。
- 复杂属性（比如上下界）可以写一个综合 setter，然后其他 setter 调用它。

#### toString

```java
@Override
public String toString() {
    return "Animal{" +
            "name='" + name + '\'' +
            ", size=" + size +
            ", comfortTempLower=" + comfortTempLower +
            ", comfortTempUpper=" + comfortTempUpper +
            '}';
}
```

- 方便调试和打印对象信息。
- `System.out.println(animal);` 时会自动调用。

**通用套路：**

- 题目要求“打印对象信息”时，重写 `toString()` 是最便捷办法。

#### equals / hashCode

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Animal)) return false;
    Animal animal = (Animal) o;
    return Objects.equals(name, animal.name) && size == animal.size;
}
```

- 题目规定：两个 Animal“相等”的条件是：名字相同 & 体型相同。
- 使用 `Objects.equals(a, b)` 安全比较字符串。

```java
@Override
public int hashCode() {
    return Objects.hash(name, size);
}
```

- 和 equals 使用相同字段（name, size）生成 hash 值。

**通用套路：**

- 如果题目提到“相等的定义”，就重写 `equals` 和 `hashCode`。
- 一般只选择“关键字段”作为比较依据，例如 id、name+birthday 等。

---

## 三、Enclosure（笼子）

### 3.1 完整代码

```java
package petcare;

public class Enclosure {

    private final AnimalSize size;
    private final int temperature;
    private final int runningCosts;
    private Animal occupant;

    public Enclosure(AnimalSize size, int temperature, int runningCosts) {
        if (size == null) {
            throw new IllegalArgumentException("Enclosure size cannot be null.");
        }
        this.size = size;
        this.temperature = temperature;
        this.runningCosts = runningCosts;
        this.occupant = null;
    }

    public AnimalSize getSize() {
        return size;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getRunningCosts() {
        return runningCosts;
    }

    public Animal getOccupant() {
        return occupant;
    }

    public boolean checkCompatibility(Animal animal) {
        if (animal == null) {
            return false;
        }
        boolean sizeOk = animal.getSize().ordinal() <= this.size.ordinal();
        boolean tempOk = this.temperature >= animal.getComfortTempLower()
                && this.temperature <= animal.getComfortTempUpper();
        return sizeOk && tempOk;
    }

    public void addAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Animal cannot be null.");
        }
        if (this.occupant != null) {
            throw new IllegalArgumentException("Enclosure already has an occupant.");
        }
        if (!checkCompatibility(animal)) {
            throw new IllegalArgumentException("Animal is not compatible with this enclosure.");
        }
        this.occupant = animal;
    }

    public void removeAnimal() {
        this.occupant = null;
    }

    @Override
    public String toString() {
        return "Enclosure{" +
                "size=" + size +
                ", temperature=" + temperature +
                ", runningCosts=" + runningCosts +
                ", occupant=" + occupant +
                '}';
    }
}
```

### 3.2 字段说明

```java
private final AnimalSize size;
private final int temperature;
private final int runningCosts;
private Animal occupant;
```

- `final`：只能在构造器中赋值一次，以后不能改（只读属性）。
- `occupant` 没有 `final`，因为住在里面的动物是可变的（添加/移除）。

**通用套路：**

- 不希望外部或后续修改的属性，可以设为 `final`。
- “关联关系”（一个对象中包含另一个对象）直接用类型字段表示，比如 `private Animal occupant;`

### 3.3 构造器要点

```java
public Enclosure(AnimalSize size, int temperature, int runningCosts) {
    if (size == null) {
        throw new IllegalArgumentException("Enclosure size cannot be null.");
    }
    this.size = size;
    this.temperature = temperature;
    this.runningCosts = runningCosts;
    this.occupant = null;
}
```

- 按题目要求初始化所有属性。
- occupant 一开始为 `null` 表示空笼子。

### 3.4 兼容检查方法

```java
public boolean checkCompatibility(Animal animal) {
    if (animal == null) {
        return false;
    }

    boolean sizeOk = animal.getSize().ordinal() <= this.size.ordinal();

    boolean tempOk = this.temperature >= animal.getComfortTempLower()
            && this.temperature <= animal.getComfortTempUpper();

    return sizeOk && tempOk;
}
```

- `ordinal()`：枚举值在声明中的顺序，如 SMALL=0, MEDIUM=1, LARGE=2。
- 体型条件：动物体型序号 ≤ 笼子体型序号 → 动物不会比笼子大。
- 温度条件：`动物舒适温度下限 <= 笼子温度 <= 动物舒适温度上限`。

**通用套路：**

- 多条件判断时，可以拆成多个 `boolean` 变量，最后返回 `&&` 组合，代码更清晰。
- “能不能放进去”类问题：用 `boolean canXXX(Type x)` 这种方法形式。

### 3.5 添加和移除动物

```java
public void addAnimal(Animal animal) {
    if (animal == null) {
        throw new IllegalArgumentException("Animal cannot be null.");
    }
    if (this.occupant != null) {
        throw new IllegalArgumentException("Enclosure already has an occupant.");
    }
    if (!checkCompatibility(animal)) {
        throw new IllegalArgumentException("Animal is not compatible with this enclosure.");
    }
    this.occupant = animal;
}
```

- 按顺序检查：
  1. 参数不能为空；
  2. 笼子必须是空的；
  3. 兼容性必须通过；
  4. 全部通过才真正赋值。

```java
public void removeAnimal() {
    this.occupant = null;
}
```

- 不管有还是没有，设成 null 就相当于“空”。

**通用套路：**

- 修改内部状态的前后，通常要先做合法性检查，不合法就抛异常。
- “删除一个引用”就是把它设成 `null`。

---

## 四、PetService（管理类）

### 4.1 完整代码

```java
package petcare;

import java.util.ArrayList;
import java.util.List;

public class PetService {

    private final List<Enclosure> enclosures;

    public PetService() {
        this.enclosures = new ArrayList<>();
    }

    public void addEnclosure(Enclosure enclosure) {
        if (enclosure == null) {
            throw new IllegalArgumentException("Enclosure cannot be null.");
        }
        enclosures.add(enclosure);
    }

    public void printAllEnclosures() {
        for (Enclosure e : enclosures) {
            System.out.println(e);
        }
    }

    public boolean allocateAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Animal cannot be null.");
        }

        Enclosure best = null;

        for (Enclosure e : enclosures) {
            if (e.getOccupant() != null) {
                continue;
            }
            if (!e.checkCompatibility(animal)) {
                continue;
            }

            if (best == null || e.getRunningCosts() < best.getRunningCosts()) {
                best = e;
            }
        }

        if (best != null) {
            best.addAnimal(animal);
            return true;
        }

        return false;
    }

    public void removeAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Animal cannot be null.");
        }

        for (Enclosure e : enclosures) {
            Animal occ = e.getOccupant();
            if (occ != null && occ.equals(animal)) {
                e.removeAnimal();
                return;
            }
        }

        throw new IllegalArgumentException("Animal not found in any enclosure.");
    }
}
```

### 4.2 字段与构造

```java
private final List<Enclosure> enclosures;

public PetService() {
    this.enclosures = new ArrayList<>();
}
```

- 使用 `List<Enclosure>` 存放多个笼子实例。
- `new ArrayList<>()` 创建一个空集合。

**通用套路：**

- 管理多个对象：`private List<Type> list = new ArrayList<>();`

### 4.3 添加和打印笼子

```java
public void addEnclosure(Enclosure enclosure) {
    if (enclosure == null) {
        throw new IllegalArgumentException("Enclosure cannot be null.");
    }
    enclosures.add(enclosure);
}
```

- null 检查 + `list.add(...)`。

```java
public void printAllEnclosures() {
    for (Enclosure e : enclosures) {
        System.out.println(e);
    }
}
```

- 增强 for 循环遍历：`for (Type x : collection)`。
- 利用 `toString()` 打印对象内容。

### 4.4 分配动物（寻找最优笼子）

```java
public boolean allocateAnimal(Animal animal) {
    if (animal == null) {
        throw new IllegalArgumentException("Animal cannot be null.");
    }

    Enclosure best = null;

    for (Enclosure e : enclosures) {
        if (e.getOccupant() != null) {
            continue;
        }
        if (!e.checkCompatibility(animal)) {
            continue;
        }

        if (best == null || e.getRunningCosts() < best.getRunningCosts()) {
            best = e;
        }
    }

    if (best != null) {
        best.addAnimal(animal);
        return true;
    }

    return false;
}
```

- 目标：在所有笼子里找一个满足条件且“运行成本最低”的笼子。
- 典型“找最优”的循环写法：
  1. 用 `best` 暂存当前最优对象，初始为 null。
  2. 遍历集合，对每个元素：
     - 若不满足条件（已占用 / 不兼容），继续下一个。
     - 若满足条件：
       - 如果 `best == null`（第一次找到），直接赋值；
       - 或者当前 `e` 比 `best` 更好（更便宜），则更新 `best`。
  3. 遍历结束后：
     - 若 `best != null`，表示有合适的，执行操作并返回 true；
     - 否则返回 false。

**通用套路（重要）：**

在一堆对象中找“最小/最大/最优”的对象，可以使用通用模板：

```java
Type best = null;
for (Type x : list) {
    if (!条件) continue;
    if (best == null || x 更优于 best) {
        best = x;
    }
}
if (best != null) {
    // 找到了
} else {
    // 没找到
}
```

### 4.5 按动物移除（按条件查找）

```java
public void removeAnimal(Animal animal) {
    if (animal == null) {
        throw new IllegalArgumentException("Animal cannot be null.");
    }

    for (Enclosure e : enclosures) {
        Animal occ = e.getOccupant();
        if (occ != null && occ.equals(animal)) {
            e.removeAnimal();
            return;
        }
    }

    throw new IllegalArgumentException("Animal not found in any enclosure.");
}
```

- 遍历所有笼子：
  - 取出当前 occupant；
  - 如果 occupant 不为 null 且等于目标 animal（使用 `equals`）：
    - 移除并 `return` 结束方法。
- 如果循环结束还没找到，抛 “找不到” 异常。

**通用套路：**

- 在集合中查找某个满足条件的元素并操作，通常写法：

```java
for (Type x : list) {
    if (满足条件) {
        // 做操作
        return;
    }
}
throw new IllegalArgumentException("not found");
```

---

## 五、Main 测试入口

### 5.1 代码

```java
package petcare;

public class Main {
    public static void main(String[] args) {
        PetService service = new PetService();

        Enclosure e1 = new Enclosure(AnimalSize.SMALL, 20, 50);
        Enclosure e2 = new Enclosure(AnimalSize.LARGE, 25, 30);
        service.addEnclosure(e1);
        service.addEnclosure(e2);

        Animal cat = new Animal("Mimi", AnimalSize.SMALL, 18, 26);

        System.out.println("Allocate cat: " + service.allocateAnimal(cat));
        service.printAllEnclosures();

        service.removeAnimal(cat);
        service.printAllEnclosures();
    }
}
```

### 5.2 要点

- `public static void main(String[] args)`：Java 程序入口。
- 在 main 里：
  1. 创建 `PetService`（管理类）。
  2. 创建几个 `Enclosure`，加到 `PetService`。
  3. 创建 `Animal`，调用 `allocateAnimal` 分配。
  4. 打印所有笼子，检查结果。
  5. 调用 `removeAnimal` 再打印，验证删除。

**通用套路：**

- 考试中，如果题目没特别要求 UI 或交互，用一个简单的 `main` 方法做“样例运行”就行。

---

## 六、考试实战“改名字复用”指南

### 6.1 常见替换模式

你可以把当前方案看成：

- `Animal` → 被管理的对象（Student、Book、Car、Room…）
- `Enclosure` → 容器/位置（Classroom、Shelf、ParkingSpot、HotelRoom…）
- `PetService` → 管理系统（School, Library, ParkingLot, Hotel…）

### 6.2 操作步骤

1. 阅读题目，找出：
   - 有哪些“实体”（需要一个类）
   - 每个实体有哪些属性（字段）
   - 有没“固定取值”（枚举）
   - 有没有“管理一堆东西的类”（List + 增删查改）

2. 把模板复制后：
   - 改类名 `Animal`、`Enclosure`、`PetService`；
   - 改字段名和类型（比如温度换成价格、容量等）；
   - 根据新题目改校验条件（范围、字符串长度等）；
   - 保留结构：构造器 + getter/setter + 校验 + `toString` + `equals/hashCode` + 管理类里的 `List` 操作。

3. “找最便宜/最合适”的逻辑几乎不变，只是条件不同：
   - 比较运行成本 → 比较价格、年龄、距离、空位等；
   - 兼容检查 `checkCompatibility` → 条件检查方法 `canAccept`, `canRegister`, `isAvailable` 等。

---

## 七、通用模板（可直接搬用）

### 7.1 实体类通用结构

```java
public class Entity {
    // 字段
    private 类型1 字段1;
    private 类型2 字段2;

    // 构造器（带所有字段）
    public Entity(类型1 字段1, 类型2 字段2) {
        set字段1(字段1);
        set字段2(字段2);
    }

    // getter
    public 类型1 get字段1() { return 字段1; }

    // setter + 校验
    public void set字段1(类型1 字段1) {
        // 校验逻辑
        this.字段1 = 字段1;
    }

    // toString
    @Override
    public String toString() {
        return "Entity{" +
                "字段1=" + 字段1 +
                ", 字段2=" + 字段2 +
                '}';
    }

    // equals & hashCode
}
```

### 7.2 管理类通用结构

```java
public class Manager {

    private final List<Entity> list;

    public Manager() {
        this.list = new ArrayList<>();
    }

    public void addEntity(Entity e) {
        if (e == null) throw new IllegalArgumentException("Entity cannot be null.");
        list.add(e);
    }

    public void printAll() {
        for (Entity e : list) {
            System.out.println(e);
        }
    }

    public boolean allocate(Entity e) {
        // 像 allocateAnimal 一样寻找最优的对象
        return false;
    }

    public void remove(Entity e) {
        // 像 removeAnimal 一样遍历查找并移除
    }
}
```

---

这份文件你可以：

- 在 VS Code 或任意编辑器打开当“秘籍”；  
- 考试时照着对应结构写自己的类，只改名字和部分条件逻辑即可。
