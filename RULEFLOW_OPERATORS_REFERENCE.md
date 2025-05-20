# RuleFlow Operators Reference

This document explains every operator available in the RuleFlow Language, grouped by category, with examples for each.

---

## 1. Boolean Operators

Boolean operators are used to combine or negate conditions in rule expressions.

| Operator | Description           | Example |
|----------|----------------------|---------|
| AND      | Logical AND          | `x AND y` |
| OR       | Logical OR           | `x OR y`  |
| NOT      | Logical negation     | `NOT x` or `x NOT IN list('a', 'b')` |

**Example:**
```text
'rule1' age > 18 AND country = 'BR' return allow
'rule2' NOT is_blacklisted return allow
```

---

## 2. Comparison Operators

Used to compare values, numbers, strings, or fields.

| Operator | Description         | Example |
|----------|--------------------|---------|
| =, ==    | Equal to           | `x = 10`, `status == 'OK'` |
| <>       | Not equal to       | `country <> 'BR'` |
| <        | Less than          | `score < 100` |
| <=       | Less than or equal | `score <= 99` |
| >        | Greater than       | `score > 0` |
| >=       | Greater or equal   | `score >= 1` |

**Example:**
```text
'rule3' amount >= 1000 return block
'rule4' status <> 'APPROVED' return review
```

---

## 3. Mathematical Operators

Perform arithmetic calculations in expressions.

| Operator | Description     | Example |
|----------|----------------|---------|
| +        | Addition       | `x + y` |
| -        | Subtraction    | `x - y` |
| *        | Multiplication | `x * y` |
| /        | Division       | `x / y` |
| % / mod  | Modulo         | `x % y`, `x mod y` |

**Example:**
```text
'rule5' (price * quantity) > 1000 return block
'rule6' x % 2 = 0 return even
```

---

## 4. List and Collection Operators

Used to work with lists, sets, and collections.

| Operator      | Description                       | Example |
|---------------|-----------------------------------|---------|
| IN            | Value is in list/tuple            | `country IN list('BR', 'AR')` |
| NOT IN        | Value is not in list/tuple        | `status NOT IN list('OK', 'PENDING')` |
| CONTAINS      | List contains value               | `tags CONTAINS 'urgent'` |
| ANY           | Any element matches predicate     | `items.ANY { type = 'A' }` |
| ALL           | All elements match predicate      | `items.ALL { score > 10 }` |
| NONE          | No element matches predicate      | `items.NONE { status = 'BLOCKED' }` |
| STARTS_WITH   | String starts with prefix         | `email STARTS_WITH 'test@'` |

**Examples:**
```text
'rule7' user.role IN list('admin', 'manager') return allow
'rule8' items.ANY { price > 100 } return review
'rule9' email STARTS_WITH 'test@' return block
```

---

## 5. Special/Built-in Functions

These are functions or keywords that add advanced logic.

| Function        | Description                              | Example |
|-----------------|------------------------------------------|---------|
| dateDiff        | Difference between dates                 | `dateDiff(day, start_date, end_date) > 7` |
| abs             | Absolute value                           | `abs(balance) > 100` |
| regex_strip     | Remove matching regex from string        | `regex_strip(name, '[_0-9]+') = 'John'` |
| currentDate()   | Current date/time                        | `created_at < currentDate()` |
| COUNT, AVERAGE  | Aggregation on collections               | `items.COUNT() > 10` |
| DISTINCT        | Unique elements in collection            | `items.DISTINCT().COUNT() = 3` |

**Examples:**
```text
'rule10' dateDiff(day, signup_date, currentDate()) > 30 return review
'rule11' abs(balance) > 100 return alert
'rule12' items.COUNT() >= 5 return block
```

---

## 6. Field and Property Access

Use dot notation to access nested fields or properties.

**Example:**
```text
'rule13' user.profile.age >= 18 return allow
'rule14' order.items.ANY { type = 'gift' } return block
```

---

## 7. Combining Operators

Operators can be combined with parentheses to control precedence.

**Example:**
```text
'rule15' (x > 10 AND y < 5) OR z = true return block
```

---

*For more advanced usage and edge cases, see the main RuleFlow language reference and test suite.*
