![tag jdk8](https://img.shields.io/badge/tag-jdk8-orange.svg)
![technology Kotlin](https://img.shields.io/badge/technology-Kotlin-olive.svg)
![technology VertX](https://img.shields.io/badge/technology-VertX-blue.svg)
![technology Gradle](https://img.shields.io/badge/technology-Gradle-green.svg)
# Ruleflow Engine and DSL
This 
## Prerrequisites
1. Docker
2. Java 17

## RuleFlowLanguage Language Reference

### Basic Workflow Syntax
A workflow is a collection of rules that return a risk
```
workflow <workflow name>
    <rule_name> <condition> return <risk> [with <action> [AND <action> ...]]
    ...
    default <risk>
end
```

#### Example
```
 workflow 'my_rule'
    ruleset 'my_ruleset'
        'high risk user' user_id = 15 return block with action('manual_review', {'test': 'me', 'foo': 'bar'})
        'risky fingerprint' payment_method.fingerprint = 'abcdeofgh101' return prevent 
        'risky card bin' card_bin in '046111', '014141' return prevent
    default allow
 end
```


### Basic Types
* String: Represent characters such as `'abc'` with quotes. 
* Numeric: Numbers with 2 decimals precision. 
* Boolean: A boolean can be `true` or `false` 
* List: Used to represent a collection of `String` elements from 1 to n elements. Example: `'foo', 'bar', 'foobar'`. For more information
about stored lists refer to the Stored Lists section below.  
* DateTime: A String representing date and time with ISO Basic Format. Example: `'2019-10-31T08:31:58.129'`
* Null Values: Whenever a null value is present, the condition result will be false.
#### Stored Lists
Rules Engine allows the user to declare and store bigger lists for rules evaluation. They must be activated in order to be 
accessed. Stored Lists can be used by comparing `String` values with the `list(<list name>)` operator. For example: 
``` 
workflow 'test'
    ruleset 'dummy'
        'rule_a' card_bin contains list('card_bins') return block
    default allow
end
```
The list operators: `contains`, `in` and `starts_with` are also available for stored lists. 

<em>Note: Stored lists must be activated before use.</em>

### Basic Operations

Basic operations allow to modify a given field and evaluate it in a logical expression. All mathematical functions 
will be rounded down to 2 decimals and the containing rule will be evaluated to false in the event of an error.

#### Math Operators 
* Addition `+`. Returns the sum of the left value with the right 
* Subtraction `-`. Returns the subtract 
* Multiplication `*`. Multiplies the left value by the right value. 
* Division `/`. Returns the division of the left value with the right value as the divisor. If the divisor is 0, 
the condition will be evaluated as `false`. The result will be scaled down to 2 decimals.

* Absolute value `abs`. Returns the absolute value of a number.

* Day Of Week `dayofweek`. Returns the day of the week value text of a date.

#### Logical Operators
`AND` Evaluates to true if all operands are true and not NULL, to false if one or more operands are false or null. 
     
`OR` When both operands are non null, the result is true if any operand is true, and false otherwise. With a null operand, the result is false if the other operand is false and false otherwise 
If both operands are false or null, the result is false.

#### List Operators
`contains` Returns true if any of the elements of the collection contains the given value

`in` Returns true if a value matches any of the elements of the collection

`startswith` Returns true if a value starts with any of the elements of the collection

##### The `not` Modifier

The `not` modifier can be used  to negate the result of the `contains`, `in`, and `startswith` operators. 

#### Date Operators
`datediff` Returns the difference between two dates in the specified unit: `day` `hour` or `minute`

`currentdate` Returns the current system date time.  

#### Aggregate Operators
`count` Returns the number of elements in a collection

`average` Returns the average value of elemens in the collection of 
numbers

`any` returns true if at least one element matches the given predicate.
`all` returns true if all elements match the given predicate. Note that all() returns true when called with any valid predicate on an empty collection.
`distinct` Returns the count of distinct non null values matching the given predicate

### Returns and Tags
#### Return States
`allow`	The evaluation is not risky

`prevent` ake extra validation steps for this evaluation (e.g. put the customer through Identity Validation)

`block`	Block this order

##### Tags

Tags allow the result to communicate behaviour and information not covered with the risk. For example, a tag `not_risk_user`
can be used in the order creation evaluation.

#### Actions  
Actions allow the result of the risk evaluation to inform expected actions to be executed. For example in the order creation, 
the workflow result might contain the `manual_review` action, to notify the receiver that a manual review case must be created. 
`manual_review`. A rule can return zero, one or more actions using the `AND` connector. 

To identify a returning action use the `action(<action_name>, [<action_params>])`. The `action_name` is a String
identifier and the `<action_params>` parameter is an optional collection in JSON notation that allows storing additional configuration.
For example: 

```
 workflow 'test'
    ruleset 'dummy'
        'rule_a' user_id = 15 return block with action('manual_review', {'test': 'me', 'foo': 'bar'}) and action('logout_user')
    default allow
 end
```