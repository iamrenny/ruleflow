# RuleFlow Language Documentation

## Overview

RuleFlow is a rule and workflow definition language designed for evaluating conditions and executing actions based on those conditions. It is structured to define workflows, rulesets, rules, and actions in a clear and maintainable manner.

## Syntax

### Workflow

A workflow starts with the keyword `WORKFLOW`, followed by a workflow name, rulesets, an optional default clause, and ends with the keyword `END`.

```bash 
WORKFLOW 'workflow_name'
rulesets*
default_clause
````

### Ruleset

A ruleset starts with the keyword `RULESET`, followed by a ruleset name, and a collection of rules.

```bash 
RULESET 'ruleset_name'
```


### Rule

A rule consists of a name and a rule body. The rule body contains an expression and actions to be executed if the expression evaluates to true.

```'rule_name'
expr THEN actions
```

### Actions

Actions specify what should be done if a rule's condition is satisfied. Actions can be defined using the `action` keyword followed by parameters.

```action('action_name', { 'param1': 'value1', 'param2': 'value2' })```


### Expressions

Expressions define the conditions that need to be evaluated. They can include logical, mathematical, comparison, and aggregation operations.

#### Operators

- **Mathematical**: `+`, `-`, `*`, `/`, `%`
- **Comparison**: `<`, `<=`, `>`, `>=`, `==`, `=`, `<>`
- **Logical**: `AND`, `OR`, `NOT`
- **List**: `CONTAINS`, `IN`, `STARTS_WITH`
- **Aggregation**: `COUNT`, `AVERAGE`, `ANY`, `ALL`, `DISTINCT`, `NONE`

### Example Workflow

Below is an example of a RuleFlow workflow:
```
WORKFLOW 'ExampleWorkflow'
RULESET 'Ruleset1'
'Rule1' (a > 5) AND (b < 10) THEN action('send_alert', {'type': 'email', 'priority': 'high'})
'Rule2' c == 'active' THEN action('log_event', {'event': 'user_active'})
RULESET 'Ruleset2'
'Rule3' (d == 0) OR (e != 'inactive') RETURN 'Approved'
'Rule4' f CONTAINS 'keyword' THEN
        action('notify_admin', {'message': 'keyword found'})
    )

DEFAULT RETURN 'Rejected' WITH action('update_status', {'status': 'rejected'})
END
```

### Detailed Elements

#### WORKFLOW

The `WORKFLOW` keyword begins a workflow definition. It must be followed by a unique workflow name.
```
WORKFLOW 'ExampleWorkflow'
...
END
```


#### RULESET

A `RULESET` groups related rules. It begins with the `RULESET` keyword and a name.
```
RULESET 'Ruleset1'
...
```

#### RULE

A `RULE` defines a condition and the actions to take when that condition is met.
```
'Rule1'
(
(a > 5) AND (b < 10)
THEN
action('send_alert', {'type': 'email', 'priority': 'high'})
)
```

#### ACTION

Actions are defined using the `action` keyword, followed by the action name and parameters.
```
action('send_alert', {'type': 'email', 'priority': 'high'})
```


#### DEFAULT

The `DEFAULT` clause specifies what happens if no rules are matched.

``` 
DEFAULT RETURN 'Rejected' WITH action('update_status', {'status': 'rejected'})
```

### Expressions and Operators

Expressions use operators to define conditions.

- **Mathematical**: `+`, `-`, `*`, `/`, `%`
- **Comparison**: `<`, `<=`, `>`, `>=`, `==`, `=`, `<>`
- **Logical**: `AND`, `OR`, `NOT`
- **List**: `CONTAINS`, `IN`, `STARTS_WITH`
- **Aggregation**: `COUNT`, `AVERAGE`, `ANY`, `ALL`, `DISTINCT`, `NONE`

### Built-in Functions

#### Mathematical Functions

- `ABS(value)`: Returns the absolute value of a number.
- `DATE_DIFF(unit, date1, date2)`: Returns the difference between two dates in the specified unit (minute, hour, day).

#### String Functions

- `REGEX_STRIP(property, regex)`: Removes parts of a string that match a given regular expression.

#### Date Functions

- `DAY_OF_WEEK(date)`: Returns the day of the week for a given date.
### Example Usage

1. **Define a Workflow**:
```bash
WORKFLOW 'PaymentWorkflow'
RULESET 'AmountCheck'
'HighValue' amount > 1000 THEN action('flag_transaction', {'priority': 'high'})
'LowValue' amount <= 1000 THEN action('approve_transaction', {'priority': 'low'})
DEFAULT RETURN 'ManualReview' WITH action('send_to_review', {'department': 'compliance'})
END
```

2. **Add Conditions and Actions**:

```bash 
RULESET 'RiskCheck' 
'HighRisk' risk_score > 80 THEN block_transaction({'reason': 'high risk'})
'LowRisk' risk_score <= 80 THEN allow_transaction({'risk_level': 'acceptable'})
DEFAULT 'allow_transaction'
END
```

### Conclusion

The RuleFlow language provides a structured way to define workflows, rules, and actions. By following the syntax and examples provided, users can create complex workflows to evaluate conditions and execute corresponding actions efficiently.

## String Similarity Scoring

The RuleFlow engine provides advanced string similarity functions. These are useful for comparing names, usernames, or any text fields where exact matches are rare but close matches are meaningful.

### Methods

| Method                    | Description                                                        | Range   |
|---------------------------|--------------------------------------------------------------------|---------|
| string_distance           | Normalized Levenshtein similarity (0–100)                          | 0–100   |
| partial_ratio             | Best substring match similarity (0–100)                            | 0–100   |
| token_sort_ratio          | Ignores word order, compares sorted tokens (0–100)                 | 0–100   |
| token_set_ratio           | Ignores word order and duplicates, set-based (0–100)               | 0–100   |
| string_similarity_score   | **Best overall score (max of all above, 0–100)**                   | 0–100   |

- All methods return 100 for a perfect match, 0 for completely different strings.
- `string_similarity_score` is recommended for most use cases: it returns the highest similarity from all available algorithms, making it robust to formatting, order, and partial matches.

### Example Usage

```java
String s1 = "ganchozojose572";
String s2 = "JOSEGANCHOZO";
int score = StringUtils.string_similarity_score(s1, s2);
System.out.println("Similarity score: " + score); // e.g., 75
```

### How It Works

- **string_distance**: Uses normalized Levenshtein distance.
- **partial_ratio**: Finds the best matching substring.
- **token_sort_ratio**: Sorts words before comparing.
- **token_set_ratio**: Compares unique words, ignoring order and duplicates.
- **string_similarity_score**: Returns the maximum score from all the above, so it is robust to extra digits, word order, and partial matches.

**Recommended:** Use `string_similarity_score` for most fuzzy matching needs in rules, risk, or deduplication logic.