# RuleFlow Language Reference

## Introduction

The RuleFlow Language is a domain-specific language (DSL) designed to define and evaluate business rules and workflows in a readable, maintainable format. This reference is intended for analysts and technical users who need to author, review, or debug rules in the RuleFlow system.

---

## Workflow Structure

A RuleFlow file is organized into **workflows**, each containing one or more **rulesets**. Each ruleset contains one or more **rules**. Rules are evaluated in order, and actions or return values are triggered when a rule matches.

### Basic Structure

```text
workflow 'workflow_name'
    ruleset 'ruleset_name'
        'rule_name' <expression> return <result> [with <action>(<params>)]
    default <result>
end
```

- **workflow**: Starts a workflow block.
- **ruleset**: Groups related rules.
- **'rule_name'**: Each rule has a unique name within a ruleset.
- **default**: Specifies the default outcome if no rule matches.
- **end**: Marks the end of the workflow.

---

## Expressions

Expressions are used to define the logic for each rule. They support:
- Boolean logic (`AND`, `OR`, `NOT`)
- Comparisons (`=`, `==`, `<`, `<=`, `>`, `>=`, `<>`)
- Mathematical operations (`+`, `-`, `*`, `/`, `%`)
- List and tuple operations (`IN`, `CONTAINS`, `ANY`, `ALL`, `NONE`)
- Functions (e.g., `dateDiff`, `abs`, `regex_strip`)

### Example
```text
'user_is_prime' order.custom.user_is_prime = true and features.is_card_bin_in_anomaly_detector <> true return block
```

### Operator Reference
| Operator         | Description                |
|------------------|---------------------------|
| `AND`, `OR`      | Boolean logic             |
| `NOT`            | Negation                  |
| `=` or `==`      | Equality                  |
| `<>`             | Not equal                 |
| `<`, `<=`, `>`, `>=` | Comparisons           |
| `IN`             | Membership in list/tuple  |
| `CONTAINS`       | List contains value       |
| `ANY`, `ALL`, `NONE` | List quantifiers      |
| `+`, `-`, `*`, `/`, `%` | Math operations    |

## Date and DateTime Functions

The following functions are available for date and datetime operations:

* `date('YYYY-MM-DD')` — Casts a string or property to a date value.
* `datetime('YYYY-MM-DDTHH:MM[:SS][Z|±hh:mm]')` — Casts a string or property to a datetime value.
* `now()` — Returns the current system date and time as a datetime value.
* `date_add(date, amount, unit)` — Adds the specified amount of the given unit (`day`, `hour`, `minute`) to the date or datetime value.
* `date_subtract(date, amount, unit)` — Subtracts the specified amount of the given unit from the date or datetime value.
* `dateDiff(unit, date1, date2)` — Returns the difference between two dates or datetimes in the specified unit (`day`, `hour`, `minute`).

**Examples:**
```text
'future_date' date_add(date('2024-06-01'), 5, day) = date('2024-06-06') return block
'past_date' date_subtract(datetime('2024-06-01T12:00Z'), 2, hour) = datetime('2024-06-01T10:00Z') return block
'now_check' now() > date('2024-06-01') return block
'diff_check' dateDiff(day, date('2024-06-01'), date('2024-06-10')) = 9 return block
```

---

## Actions and Results

- **return <result>**: Specifies the result when the rule matches (e.g., `block`, `allow`).
- **with <action>(<params>)**: Triggers an action, optionally with parameters.
    - Actions can be chained using `and`.
    - Actions can be specified as `action('name', {'param': 'value'})` or directly as `action_name({'param': 'value'})`.

### Example
```text
'rule_a' user_id = 15 return block with action('manual_review', {'test': 'me', 'foo': 'bar'}) and action('logout_user')
```

---

## Value Types

- **String**: `'value'` or quoted
- **Number**: `15`, `3.14`
- **Boolean**: `true`, `false`
- **Null**: `null`
- **Lists**: `list('a', 'b', 'c')` or tuples
- **Date/Time**: `currentDate()`

---

## Examples from Test Suite

### 1. Simple Rule with Action
```text
workflow 'test'
    ruleset 'dummy'
        'rule_a' user_id = 15 return block with action('manual_review')
    default allow
end
```

### 2. Boolean Logic
```text
workflow 'test'
    ruleset 'dummy'
        'item_a' x AND y OR z return block
    default allow
end
```

### 3. Parenthesis and Precedence
```text
workflow 'test'
    ruleset 'dummy'
        'item_a' (x OR y) AND z return block
    default allow
end
```

### 4. List Operations
```text
workflow 'test'
    ruleset 'dummy'
        'item_a' order.items.any { type = 'a' } return block
    default allow
end
```

### 5. Math Operations
```text
workflow 'test'
    ruleset 'dummy'
        'item_a' x + y * z = 15 return block
    default allow
end
```

### 6. String Comparison
```text
workflow 'test'
    ruleset 'dummy'
        'x_is_lesser' x < '7.53' return block
    default allow
end
```

### 7. Multiple Actions
```text
workflow 'test'
    ruleset 'dummy'
        'rule_a' user_id = 15 return block with action('manual_review', {'test': 'me', 'foo': 'bar'}) and action('logout_user')
    default allow
end
```

---

## Keywords and Special Functions

| Keyword/Function   | Description                                 |
|--------------------|---------------------------------------------|
| `workflow`         | Start a workflow block                      |
| `ruleset`          | Group of rules                              |
| `default`          | Default result if no rule matches           |
| `end`              | End of workflow                             |
| `return`           | Specifies return value                      |
| `with`, `and`      | Combine actions                             |
| `action`           | Triggers an action                          |
| `list`             | List literal                                |
| `dateDiff`, `abs`, `regex_strip` | Built-in functions             |
| `currentDate()`    | Current date/time                           |
| `any`, `all`, `none` | List quantifiers                          |

---

## Best Practices & Gotchas

- **Parentheses**: Use parentheses to clarify precedence in complex boolean or math expressions.
- **Field Existence**: If a referenced field does not exist in the input, a warning is generated and the default result is returned.
- **Division/Modulo by Zero**: Results in a warning and default result.
- **Action Parameters**: Use curly braces `{}` for key-value pairs in action parameters.
- **Chaining Actions**: Use `and` or `with` to chain multiple actions after a rule.
- **String Literals**: Use single quotes for string values.

---

## Example: Realistic Rule

```text
workflow 'fraud_detection'
    ruleset 'risk_checks'
        'prime_user' order.custom.user_is_prime = true
            and features.is_card_bin_in_anomaly_detector <> true
            and features.fake_users_user_email_score <= 1
            and user.email_verification_status in 'VERIFIED'
            and (features.mk_payer_distinct_cc_fingerprint_90d + features.mk_payer_distinct_device_id_90d) <= 8
            and features.crosses_login_device_qty_users_7d <= 3
            and features.crosses_registration_device_qty_users_7d < 1
            and (features.user_compensations_amount_90d/features.mk_payer_approved_amount_90d) < 0.05
            and features.mk_payer_approved_qty_7d >= 1
            and features.mk_payer_approved_qty_30d >= 6
            and (features.mk_payer_approved_qty_60d >= features.mk_payer_approved_qty_30d + 6)
            and (features.mk_payer_approved_qty_90d >= features.mk_payer_approved_qty_60d + 6)
            and features.mk_payer_approved_amount_30d >= 50
            and (features.mk_payer_approved_amount_90d >= features.mk_payer_approved_amount_30d + 100) return block
    default allow
end
```

---

## Further Reading
- See the test suite for more usage patterns and edge cases.
- Refer to the grammar file (`RuleFlowLanguage.g4`) for advanced syntax details.

---

*This document is auto-generated from the language grammar and test cases. For feedback or updates, contact the RuleFlow maintainers.*
